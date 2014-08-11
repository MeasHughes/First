package maes.infomanagement.bussiness;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import maes.infomanagement.R;
import maes.infomanagement.bussiness.base.BussinessBase;
import maes.infomanagement.model.ModelPayout;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


import android.content.Context;

public class BusinessStatistics extends BussinessBase {

	private BusinessPayout mBusinessPayout;
	private BussinessUser mBusinessUser;
	private BussinessAccount mBusinessAccountBook;

	public BusinessStatistics(Context context) {
		super(context);
		mBusinessPayout = new BusinessPayout(context);
		mBusinessUser = new BussinessUser(context);
		mBusinessAccountBook = new BussinessAccount(context);
	}

	public String getPayoutUserIDByAccountBookID(int accountBookID) {
		String result = "";
		List<ModelStatistics> listModelStatisticsTotal = getPayoutUserID(" And AccountBookID = " + accountBookID);

		// 将得到的信息进行转换，以方便观看
		for (int i = 0; i < listModelStatisticsTotal.size(); i++) {
			ModelStatistics modelStatistics = listModelStatisticsTotal.get(i);
			if (modelStatistics.getPayoutType().equals("个人")) {
				result += modelStatistics.PayerUserID + "个人消费 " + modelStatistics.Cost.toString() + "元\r\n";
			} else if (modelStatistics.getPayoutType().equals("均分")) {
				if (modelStatistics.PayerUserID.equals(modelStatistics.ConsumerUserID)) {
					result += modelStatistics.PayerUserID + "个人消费 " + modelStatistics.Cost.toString() + "元\r\n";
				} else {
					result += modelStatistics.ConsumerUserID + "应支付给" + modelStatistics.PayerUserID
							+ modelStatistics.Cost + "元\r\n";
				}
			}
			// _Result += _ModelStatistics.PayerUserID + "#" +
			// _ModelStatistics.ConsumerUserID + "#" + _ModelStatistics.Cost +
			// "(" + _ModelStatistics.getPayoutType() + ")" + "\r\n";
		}

		return result;
	}

	public List<ModelStatistics> getPayoutUserID(String condition) {
		// 得到拆分好的统计信息
		List<ModelStatistics> listModelStatistics = getModelStatisticsList(condition);
		// 存放按付款人分类的临时统计信息
		List<ModelStatistics> listModelStatisticsTemp = new ArrayList<ModelStatistics>();
		// 存放统计好的汇总
		List<ModelStatistics> listModelStatisticsTotal = new ArrayList<ModelStatistics>();
		String result = "";
		// 遍历拆分好的统计信息
		for (int i = 0; i < listModelStatistics.size(); i++) {
			// 得到拆分好的一条信息
			ModelStatistics modelStatistics = listModelStatistics.get(i);
			result += modelStatistics.PayerUserID + "#" + modelStatistics.ConsumerUserID + "#" + modelStatistics.Cost
					+ "\r\n";
			// 保存当前的付款人ID
			String currentPayerUserID = modelStatistics.PayerUserID;

			// 把当前信息按付款人分类的临时数组
			ModelStatistics modelStatisticsTemp = new ModelStatistics();
			modelStatisticsTemp.PayerUserID = modelStatistics.PayerUserID;
			modelStatisticsTemp.ConsumerUserID = modelStatistics.ConsumerUserID;
			modelStatisticsTemp.Cost = modelStatistics.Cost;
			modelStatisticsTemp.setPayoutType(modelStatistics.getPayoutType());
			listModelStatisticsTemp.add(modelStatisticsTemp);

			// 计算下一行的索引
			int nextIndex;
			// 如果下一行索引小于统计信息索引，则可以加1
			if ((i + 1) < listModelStatistics.size()) {
				nextIndex = i + 1;
			} else {
				// 否则证明已经到尾，则索引赋值为当前行
				nextIndex = i;
			}

			// 如果当前付款人与下一位付款人不同，则证明分类统计已经到尾，或者已经循环到统计数组最后一位，则就开始进入进行统计
			if (!currentPayerUserID.equals(listModelStatistics.get(nextIndex).PayerUserID) || nextIndex == i) {

				// 开始进行遍历进行当前分类统计数组的统计
				for (int j = 0; j < listModelStatisticsTemp.size(); j++) {
					// 取出来一个
					ModelStatistics modelStatisticsTotal = listModelStatisticsTemp.get(j);
					// 判断在总统计数组当中是否已经存在该付款人和消费人的信息
					int index = getPostionByConsumerUserID(listModelStatisticsTotal, modelStatisticsTotal.PayerUserID,
							modelStatisticsTotal.ConsumerUserID);
					// 如果已经存在，则开始在原来的数据上进行累加
					if (index != -1) {
						listModelStatisticsTotal.get(index).Cost = listModelStatisticsTotal.get(index).Cost
								.add(modelStatisticsTotal.Cost);
					} else {
						// 否则就是一条新信息，添加到统计数组当中
						listModelStatisticsTotal.add(modelStatisticsTotal);
					}
				}
				// 全部遍历完后清空当前分类统计数组，进入下一个分类统计数组的计算，直到尾
				listModelStatisticsTemp.clear();
			}

		}

		return listModelStatisticsTotal;
	}

	private int getPostionByConsumerUserID(List<ModelStatistics> listModelStatisticsTotal, String p_PayerUserID,
			String p_ConsumerUserID) {
		int _Index = -1;
		for (int i = 0; i < listModelStatisticsTotal.size(); i++) {
			if (listModelStatisticsTotal.get(i).PayerUserID.equals(p_PayerUserID)
					&& listModelStatisticsTotal.get(i).ConsumerUserID.equals(p_ConsumerUserID)) {
				_Index = i;
			}
		}

		return _Index;
	}

	private List<ModelStatistics> getModelStatisticsList(String p_Condition) {
		// 按支付人ID排序取出消费记录
		List<ModelPayout> listPayout = mBusinessPayout.getPayoutOrderByPayoutUserID(p_Condition);

		// 获取计算方式数组
		String payoutTypeArr[] = getContext().getResources().getStringArray(R.array.PayoutType);

		List<ModelStatistics> _ListModelStatistics = new ArrayList<ModelStatistics>();

		if (listPayout != null) {
			// 遍历消费记录列表
			for (int i = 0; i < listPayout.size(); i++) {
				// 取出一条消费记录
				ModelPayout modelPayout = listPayout.get(i);
				// 将消费人ID转换为真实名称
				String payoutUserName[] = mBusinessUser.getUserNameByUserID(modelPayout.getPayoutUserID()).split(",");
				String payoutUserID[] = modelPayout.getPayoutUserID().split(",");

				// 取出计算方式
				String payoutType = modelPayout.getPayoutType();

				// 存放计算后的消费金额
				BigDecimal cost;

				// 判断本次消费记录的消费类型，如果是均分，则除以本次消费人的个数，算出平均消费金额
				if (payoutType.equals(payoutTypeArr[0])) {
					// 得到消费人数
					int _PayoutTotal = payoutUserName.length;

					/*
					 * 金额的数据类型是BigDecimal
					 * 通过BigDecimal的divide方法进行除法时当不整除，出现无限循环小数时
					 * ，就会抛异常的，异常如下：java.lang.ArithmeticException:
					 * Non-terminating decimal expansion; no exact representable
					 * decimal result. at java.math.BigDecimal.divide(Unknown
					 * Source)
					 * 
					 * 应用场景：一批中供客户的单价是1000元/年，如果按月计算的话1000/12=83.3333333333....
					 * 
					 * 解决之道：就是给divide设置精确的小数点divide(xxxxx,2,
					 * BigDecimal.ROUND_HALF_EVEN)
					 */
					// 得到计算后的平均消费金额
					cost = modelPayout.getAmount().divide(new BigDecimal(_PayoutTotal), 2, BigDecimal.ROUND_HALF_EVEN);
				}
				// 如果是借贷或是个人消费，则直接取出消费金额
				else {
					cost = modelPayout.getAmount();
				}

				// 遍历消费人数组
				for (int j = 0; j < payoutUserID.length; j++) {

					// 如果是借贷则跳过第一个索引，因为第一个人是借贷人自己
					if (payoutType.equals(payoutTypeArr[1]) && j == 0) {
						continue;
					}

					// 声明一个统计类
					ModelStatistics modelStatistics = new ModelStatistics();
					// 将统计类的支付人设置为消费人数组的第一个人
					modelStatistics.PayerUserID = payoutUserName[0];
					// 设置消费人
					modelStatistics.ConsumerUserID = payoutUserName[j];
					// 设置消费类型
					modelStatistics.setPayoutType(payoutType);
					// 设置算好的消费金额
					modelStatistics.Cost = cost;

					_ListModelStatistics.add(modelStatistics);
				}
			}
		}

		return _ListModelStatistics;
	}

	public String ExportStatistics(int accountBookID) throws Exception {
		String result = "";
		String accountBookName = mBusinessAccountBook.getAccountBookNameByAccountId(accountBookID);
		Date date = new Date();
		// String _FileName = _AccountBookName + String.valueOf(_Date.getYear())
		// + String.valueOf(_Date.getMonth()) + String.valueOf(_Date.getDay()) +
		// ".xls";
		String fileName = String.valueOf(date.getYear()) + String.valueOf(date.getMonth())
				+ String.valueOf(date.getDay()) + ".xls";
		File _FileDir = new File("/sdcard/GoDutch/Export/");
		if (!_FileDir.exists()) {
			_FileDir.mkdirs();
		}
		File file = new File("/sdcard/GoDutch/Export/"+ fileName);
		if (!file.exists()) {
			file.createNewFile();
		}

		WritableWorkbook wBookData;
		// 创建工作簿
		wBookData = Workbook.createWorkbook(file);
		// 创建工作表
		WritableSheet wsAccountBook = wBookData.createSheet(accountBookName, 0);

		String[] _Titles = { "编号", "姓名", "金额", "消费信息", "消费类型" };
		Label _Label;
		// 添加标题行
		for (int i = 0; i < _Titles.length; i++) {
			_Label = new Label(i, 0, _Titles[i]);
			wsAccountBook.addCell(_Label);
		}

		/*
		 * 添加行
		 */
		List<ModelStatistics> listModelStatisticsTotal = getPayoutUserID(" And AccountBookID = " + accountBookID);

		for (int i = 0; i < listModelStatisticsTotal.size(); i++) {
			ModelStatistics modelStatistics = listModelStatisticsTotal.get(i);

			// 添加编号列
			jxl.write.Number idCell = new Number(0, i + 1, i + 1);
			wsAccountBook.addCell(idCell);

			// 添加姓名
			Label lblName = new Label(1, i + 1, modelStatistics.PayerUserID);
			wsAccountBook.addCell(lblName);

			// 格式化金额类型显示
			NumberFormat nfMoney = new NumberFormat("#.##");
			WritableCellFormat wcfFormat = new WritableCellFormat(nfMoney);

			// 添加金额
			Number _CostCell = new Number(2, i + 1, modelStatistics.Cost.doubleValue(), wcfFormat);
			wsAccountBook.addCell(_CostCell);

			// 添加消费信息
			String info = "";
			if (modelStatistics.getPayoutType().equals("个人")) {
				info = modelStatistics.PayerUserID + "个人消费 " + modelStatistics.Cost.toString() + "元";
			} else if (modelStatistics.getPayoutType().equals("均分")) {
				if (modelStatistics.PayerUserID.equals(modelStatistics.ConsumerUserID)) {
					info = modelStatistics.PayerUserID + "个人消费 " + modelStatistics.Cost.toString() + "元";
				} else {
					info = modelStatistics.ConsumerUserID + "应支付给" + modelStatistics.PayerUserID + modelStatistics.Cost
							+ "元";
				}
			}
			Label _lblInfo = new Label(3, i + 1, info);
			wsAccountBook.addCell(_lblInfo);

			// 添加消费类型
			Label _lblPayoutType = new Label(4, i + 1, modelStatistics.getPayoutType());
			wsAccountBook.addCell(_lblPayoutType);
		}

		wBookData.write();
		wBookData.close();
		result = "数据已经导出！位置在：/sdcard/GoDutch/Export/" + fileName;
		return result;
	}

	public class ModelStatistics {
		public String PayerUserID;
		public String ConsumerUserID;
		private String mPayoutType;
		public BigDecimal Cost;

		public String getPayoutType() {
			return mPayoutType;
		}

		public void setPayoutType(String value) {
			mPayoutType = value;
		}
	}
}
