package maes.infomanagement.util;

/*
 *  Copyright  sunflower
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * ����ʱ�乤����
 * 
 * @author sunflower
 * 
 */
public class DateUtils {
	private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat(
			"HH:mm:ss");

	/**
	 * ��õ�ǰ����ʱ��
	 * <p>
	 * ����ʱ���ʽyyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentDatetime() {
		return datetimeFormat.format(now());
	}

	/**
	 * ��ʽ������ʱ��
	 * <p>
	 * ����ʱ���ʽyyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatDatetime(Date date) {
		return datetimeFormat.format(date);
	}

	/**
	 * ��ʽ������ʱ��
	 * 
	 * @param date
	 * @param pattern
	 *            ��ʽ��ģʽ�����{@link SimpleDateFormat}������
	 *            <code>SimpleDateFormat(String pattern)</code>
	 * @return
	 */
	public static String formatDatetime(Date date, String pattern) {
		SimpleDateFormat customFormat = (SimpleDateFormat) datetimeFormat
				.clone();
		customFormat.applyPattern(pattern);
		return customFormat.format(date);
	}

	/**
	 * ��õ�ǰ����
	 * <p>
	 * ���ڸ�ʽyyyy-MM-dd
	 * 
	 * @return
	 */
	public static String currentDate() {
		return dateFormat.format(now());
	}

	/**
	 * ��ʽ������
	 * <p>
	 * ���ڸ�ʽyyyy-MM-dd
	 * 
	 * @return
	 */
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * ��õ�ǰʱ��
	 * <p>
	 * ʱ���ʽHH:mm:ss
	 * 
	 * @return
	 */
	public static String currentTime() {
		return timeFormat.format(now());
	}

	/**
	 * ��ʽ��ʱ��
	 * <p>
	 * ʱ���ʽHH:mm:ss
	 * 
	 * @return
	 */
	public static String formatTime(Date date) {
		return timeFormat.format(date);
	}

	/**
	 * ��õ�ǰʱ���<code>java.util.Date</code>����
	 * 
	 * @return
	 */
	public static Date now() {
		return new Date();
	}

	public static Calendar calendar() {
		Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		return cal;
	}

	/**
	 * ��õ�ǰʱ��ĺ�����
	 * <p>
	 * ���{@link System#currentTimeMillis()}
	 * 
	 * @return
	 */
	public static long millis() {
		return System.currentTimeMillis();
	}

	/**
	 * 
	 * ��õ�ǰChinese�·�
	 * 
	 * @return
	 */
	public static int month() {
		return calendar().get(Calendar.MONTH) + 1;
	}

	/**
	 * ����·��еĵڼ���
	 * 
	 * @return
	 */
	public static int dayOfMonth() {
		return calendar().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * ���������ڵĵڼ���
	 * 
	 * @return
	 */
	public static int dayOfWeek() {
		return calendar().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * ���������еĵڼ���
	 * 
	 * @return
	 */
	public static int dayOfYear() {
		return calendar().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 *�ж�ԭ�����Ƿ���Ŀ������֮ǰ
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isBefore(Date src, Date dst) {
		return src.before(dst);
	}

	/**
	 *�ж�ԭ�����Ƿ���Ŀ������֮��
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isAfter(Date src, Date dst) {
		return src.after(dst);
	}

	/**
	 *�ж��������Ƿ���ͬ
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isEqual(Date date1, Date date2) {
		return date1.compareTo(date2) == 0;
	}

	/**
	 * �ж�ĳ�������Ƿ���ĳ�����ڷ�Χ
	 * 
	 * @param beginDate
	 *            ���ڷ�Χ��ʼ
	 * @param endDate
	 *            ���ڷ�Χ����
	 * @param src
	 *            ��Ҫ�жϵ�����
	 * @return
	 */
	public static boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}

	/**
	 * ��õ�ǰ�µ����һ��
	 * <p>
	 * HH:mm:ssΪ0������Ϊ999
	 * 
	 * @return
	 */
	public static Date lastDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 0); // M������
		cal.set(Calendar.HOUR_OF_DAY, 0);// H����
		cal.set(Calendar.MINUTE, 0);// m����
		cal.set(Calendar.SECOND, 0);// s����
		cal.set(Calendar.MILLISECOND, 0);// S����
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// �·�+1
		cal.set(Calendar.MILLISECOND, -1);// ����-1
		return cal.getTime();
	}

	/**
	 * ��õ�ǰ�µĵ�һ��
	 * <p>
	 * HH:mm:ss SSΪ��
	 * 
	 * @return
	 */
	public static Date firstDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 1); // M����1
		cal.set(Calendar.HOUR_OF_DAY, 0);// H����
		cal.set(Calendar.MINUTE, 0);// m����
		cal.set(Calendar.SECOND, 0);// s����
		cal.set(Calendar.MILLISECOND, 0);// S����
		return cal.getTime();
	}

	private static Date weekDay(int week) {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_WEEK, week);
		return cal.getTime();
	}

	/**
	 * �����������
	 * <p>
	 * ע��������������{@link #calendar()}������ÿ�����ڵĵ�һ��ΪMonday��US��ÿ���ڵ�һ��Ϊsunday
	 * 
	 * @return
	 */
	public static Date friday() {
		return weekDay(Calendar.FRIDAY);
	}

	/**
	 * �����������
	 * <p>
	 * ע��������������{@link #calendar()}������ÿ�����ڵĵ�һ��ΪMonday��US��ÿ���ڵ�һ��Ϊsunday
	 * 
	 * @return
	 */
	public static Date saturday() {
		return weekDay(Calendar.SATURDAY);
	}

	/**
	 * �����������
	 * <p>
	 * ע��������������{@link #calendar()}������ÿ�����ڵĵ�һ��ΪMonday��US��ÿ���ڵ�һ��Ϊsunday
	 * 
	 * @return
	 */
	public static Date sunday() {
		return weekDay(Calendar.SUNDAY);
	}

	/**
	 * ���ַ�������ʱ��ת����java.util.Date����
	 * <p>
	 * ����ʱ���ʽyyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 * @return
	 */
	public static Date parseDatetime(String datetime) throws ParseException {
		return datetimeFormat.parse(datetime);
	}

	/**
	 * ���ַ�������ת����java.util.Date����
	 *<p>
	 * ����ʱ���ʽyyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date) throws ParseException {
		return dateFormat.parse(date);
	}

	/**
	 * ���ַ�������ת����java.util.Date����
	 *<p>
	 * ʱ���ʽ HH:mm:ss
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String time) throws ParseException {
		return timeFormat.parse(time);
	}

	/**
	 * �����Զ���pattern���ַ�������ת����java.util.Date����
	 * 
	 * @param datetime
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String datetime, String pattern)
			throws ParseException {
		SimpleDateFormat format = (SimpleDateFormat) datetimeFormat.clone();
		format.applyPattern(pattern);
		return format.parse(datetime);
	}
}
