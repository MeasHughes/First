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

		// ���õ�����Ϣ����ת�����Է���ۿ�
		for (int i = 0; i < listModelStatisticsTotal.size(); i++) {
			ModelStatistics modelStatistics = listModelStatisticsTotal.get(i);
			if (modelStatistics.getPayoutType().equals("����")) {
				result += modelStatistics.PayerUserID + "�������� " + modelStatistics.Cost.toString() + "Ԫ\r\n";
			} else if (modelStatistics.getPayoutType().equals("����")) {
				if (modelStatistics.PayerUserID.equals(modelStatistics.ConsumerUserID)) {
					result += modelStatistics.PayerUserID + "�������� " + modelStatistics.Cost.toString() + "Ԫ\r\n";
				} else {
					result += modelStatistics.ConsumerUserID + "Ӧ֧����" + modelStatistics.PayerUserID
							+ modelStatistics.Cost + "Ԫ\r\n";
				}
			}
			// _Result += _ModelStatistics.PayerUserID + "#" +
			// _ModelStatistics.ConsumerUserID + "#" + _ModelStatistics.Cost +
			// "(" + _ModelStatistics.getPayoutType() + ")" + "\r\n";
		}

		return result;
	}

	public List<ModelStatistics> getPayoutUserID(String condition) {
		// �õ���ֺõ�ͳ����Ϣ
		List<ModelStatistics> listModelStatistics = getModelStatisticsList(condition);
		// ��Ű������˷������ʱͳ����Ϣ
		List<ModelStatistics> listModelStatisticsTemp = new ArrayList<ModelStatistics>();
		// ���ͳ�ƺõĻ���
		List<ModelStatistics> listModelStatisticsTotal = new ArrayList<ModelStatistics>();
		String result = "";
		// ������ֺõ�ͳ����Ϣ
		for (int i = 0; i < listModelStatistics.size(); i++) {
			// �õ���ֺõ�һ����Ϣ
			ModelStatistics modelStatistics = listModelStatistics.get(i);
			result += modelStatistics.PayerUserID + "#" + modelStatistics.ConsumerUserID + "#" + modelStatistics.Cost
					+ "\r\n";
			// ���浱ǰ�ĸ�����ID
			String currentPayerUserID = modelStatistics.PayerUserID;

			// �ѵ�ǰ��Ϣ�������˷������ʱ����
			ModelStatistics modelStatisticsTemp = new ModelStatistics();
			modelStatisticsTemp.PayerUserID = modelStatistics.PayerUserID;
			modelStatisticsTemp.ConsumerUserID = modelStatistics.ConsumerUserID;
			modelStatisticsTemp.Cost = modelStatistics.Cost;
			modelStatisticsTemp.setPayoutType(modelStatistics.getPayoutType());
			listModelStatisticsTemp.add(modelStatisticsTemp);

			// ������һ�е�����
			int nextIndex;
			// �����һ������С��ͳ����Ϣ����������Լ�1
			if ((i + 1) < listModelStatistics.size()) {
				nextIndex = i + 1;
			} else {
				// ����֤���Ѿ���β����������ֵΪ��ǰ��
				nextIndex = i;
			}

			// �����ǰ����������һλ�����˲�ͬ����֤������ͳ���Ѿ���β�������Ѿ�ѭ����ͳ���������һλ����Ϳ�ʼ�������ͳ��
			if (!currentPayerUserID.equals(listModelStatistics.get(nextIndex).PayerUserID) || nextIndex == i) {

				// ��ʼ���б������е�ǰ����ͳ�������ͳ��
				for (int j = 0; j < listModelStatisticsTemp.size(); j++) {
					// ȡ����һ��
					ModelStatistics modelStatisticsTotal = listModelStatisticsTemp.get(j);
					// �ж�����ͳ�����鵱���Ƿ��Ѿ����ڸø����˺������˵���Ϣ
					int index = getPostionByConsumerUserID(listModelStatisticsTotal, modelStatisticsTotal.PayerUserID,
							modelStatisticsTotal.ConsumerUserID);
					// ����Ѿ����ڣ���ʼ��ԭ���������Ͻ����ۼ�
					if (index != -1) {
						listModelStatisticsTotal.get(index).Cost = listModelStatisticsTotal.get(index).Cost
								.add(modelStatisticsTotal.Cost);
					} else {
						// �������һ������Ϣ����ӵ�ͳ�����鵱��
						listModelStatisticsTotal.add(modelStatisticsTotal);
					}
				}
				// ȫ�����������յ�ǰ����ͳ�����飬������һ������ͳ������ļ��㣬ֱ��β
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
		// ��֧����ID����ȡ�����Ѽ�¼
		List<ModelPayout> listPayout = mBusinessPayout.getPayoutOrderByPayoutUserID(p_Condition);

		// ��ȡ���㷽ʽ����
		String payoutTypeArr[] = getContext().getResources().getStringArray(R.array.PayoutType);

		List<ModelStatistics> _ListModelStatistics = new ArrayList<ModelStatistics>();

		if (listPayout != null) {
			// �������Ѽ�¼�б�
			for (int i = 0; i < listPayout.size(); i++) {
				// ȡ��һ�����Ѽ�¼
				ModelPayout modelPayout = listPayout.get(i);
				// ��������IDת��Ϊ��ʵ����
				String payoutUserName[] = mBusinessUser.getUserNameByUserID(modelPayout.getPayoutUserID()).split(",");
				String payoutUserID[] = modelPayout.getPayoutUserID().split(",");

				// ȡ�����㷽ʽ
				String payoutType = modelPayout.getPayoutType();

				// ��ż��������ѽ��
				BigDecimal cost;

				// �жϱ������Ѽ�¼���������ͣ�����Ǿ��֣�����Ա��������˵ĸ��������ƽ�����ѽ��
				if (payoutType.equals(payoutTypeArr[0])) {
					// �õ���������
					int _PayoutTotal = payoutUserName.length;

					/*
					 * ��������������BigDecimal
					 * ͨ��BigDecimal��divide�������г���ʱ������������������ѭ��С��ʱ
					 * ���ͻ����쳣�ģ��쳣���£�java.lang.ArithmeticException:
					 * Non-terminating decimal expansion; no exact representable
					 * decimal result. at java.math.BigDecimal.divide(Unknown
					 * Source)
					 * 
					 * Ӧ�ó�����һ���й��ͻ��ĵ�����1000Ԫ/�꣬������¼���Ļ�1000/12=83.3333333333....
					 * 
					 * ���֮�������Ǹ�divide���þ�ȷ��С����divide(xxxxx,2,
					 * BigDecimal.ROUND_HALF_EVEN)
					 */
					// �õ�������ƽ�����ѽ��
					cost = modelPayout.getAmount().divide(new BigDecimal(_PayoutTotal), 2, BigDecimal.ROUND_HALF_EVEN);
				}
				// ����ǽ�����Ǹ������ѣ���ֱ��ȡ�����ѽ��
				else {
					cost = modelPayout.getAmount();
				}

				// ��������������
				for (int j = 0; j < payoutUserID.length; j++) {

					// ����ǽ����������һ����������Ϊ��һ�����ǽ�����Լ�
					if (payoutType.equals(payoutTypeArr[1]) && j == 0) {
						continue;
					}

					// ����һ��ͳ����
					ModelStatistics modelStatistics = new ModelStatistics();
					// ��ͳ�����֧��������Ϊ����������ĵ�һ����
					modelStatistics.PayerUserID = payoutUserName[0];
					// ����������
					modelStatistics.ConsumerUserID = payoutUserName[j];
					// ������������
					modelStatistics.setPayoutType(payoutType);
					// ������õ����ѽ��
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
		// ����������
		wBookData = Workbook.createWorkbook(file);
		// ����������
		WritableSheet wsAccountBook = wBookData.createSheet(accountBookName, 0);

		String[] _Titles = { "���", "����", "���", "������Ϣ", "��������" };
		Label _Label;
		// ��ӱ�����
		for (int i = 0; i < _Titles.length; i++) {
			_Label = new Label(i, 0, _Titles[i]);
			wsAccountBook.addCell(_Label);
		}

		/*
		 * �����
		 */
		List<ModelStatistics> listModelStatisticsTotal = getPayoutUserID(" And AccountBookID = " + accountBookID);

		for (int i = 0; i < listModelStatisticsTotal.size(); i++) {
			ModelStatistics modelStatistics = listModelStatisticsTotal.get(i);

			// ��ӱ����
			jxl.write.Number idCell = new Number(0, i + 1, i + 1);
			wsAccountBook.addCell(idCell);

			// �������
			Label lblName = new Label(1, i + 1, modelStatistics.PayerUserID);
			wsAccountBook.addCell(lblName);

			// ��ʽ�����������ʾ
			NumberFormat nfMoney = new NumberFormat("#.##");
			WritableCellFormat wcfFormat = new WritableCellFormat(nfMoney);

			// ��ӽ��
			Number _CostCell = new Number(2, i + 1, modelStatistics.Cost.doubleValue(), wcfFormat);
			wsAccountBook.addCell(_CostCell);

			// ���������Ϣ
			String info = "";
			if (modelStatistics.getPayoutType().equals("����")) {
				info = modelStatistics.PayerUserID + "�������� " + modelStatistics.Cost.toString() + "Ԫ";
			} else if (modelStatistics.getPayoutType().equals("����")) {
				if (modelStatistics.PayerUserID.equals(modelStatistics.ConsumerUserID)) {
					info = modelStatistics.PayerUserID + "�������� " + modelStatistics.Cost.toString() + "Ԫ";
				} else {
					info = modelStatistics.ConsumerUserID + "Ӧ֧����" + modelStatistics.PayerUserID + modelStatistics.Cost
							+ "Ԫ";
				}
			}
			Label _lblInfo = new Label(3, i + 1, info);
			wsAccountBook.addCell(_lblInfo);

			// �����������
			Label _lblPayoutType = new Label(4, i + 1, modelStatistics.getPayoutType());
			wsAccountBook.addCell(_lblPayoutType);
		}

		wBookData.write();
		wBookData.close();
		result = "�����Ѿ�������λ���ڣ�/sdcard/GoDutch/Export/" + fileName;
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
