package com.dh.common.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期处理工具 有线程安全问题 可以使用DateTimeFormatter
 * @author caisj
 *
 */
public class DateUtil {
	public final static String DEFAULT_DATE_FROMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String SIMPLE_DATE_FROMAT = "yyyy-MM-dd";
	public final static String YYYYMMDD = "yyyyMMdd";
	//public final static String DATE_FROMAT="yyyyMMddHHmmss";

    /**
     * 格式化日期
     * default format  yyyy-MM-dd HH:mm:ss
     * formatDateTime 
     * @param date
     * @return 
     * String
     * @exception 
     * @since  1.0.0
     */
	public static String formatDateTime(java.util.Date date) {
		return formatDateTime(DEFAULT_DATE_FROMAT, date);
	}
	
	public static String formatDateTime(java.util.Date date,String fromat){
		
		return formatDateTime(fromat, date);
	}

	/**
	 * 以指定格式返回当时时间
	 * 
	 * @param pattern
	 *            - 日期显示格式
	 * @return the formatted date-time string
	 * @see java.text.SimpleDateFormat
	 */
	public static String formatDateTime(String pattern) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
		String now = sdf.format(new java.util.Date());
		return now;
	}

	/**
	 * 以指定格式返回指定日期的字符串
	 * 
	 * @param pattern
	 *            - 日期显示格式
	 * @param date
	 *            - 需要格式 化的时间
	 * @return the formatted date-time string
	 * @see java.text.SimpleDateFormat
	 */
	public static String formatDateTime(String pattern, java.util.Date date) {
		String strDate = null;
		String strFormat = pattern;
		SimpleDateFormat dateFormat = null;

		if (date == null)
			return "";

		dateFormat = new SimpleDateFormat(strFormat);
		strDate = dateFormat.format(date);

		return strDate;
	}

	/**
	 * 以指定格式 和指定的Local返回指定日期的字符串
	 * 
	 * @param pattern
	 *            - 时间显示格式
	 * @param date
	 *            - 指定的时间
	 * @param locale
	 *            - the locale whose date format symbols should be used
	 * @return the formatted date-time string
	 * @see java.text.SimpleDateFormat
	 */
	public static String formatDateTime(String pattern, java.util.Date date,
			Locale locale) {
		String strDate = null;
		String strFormat = pattern;
		SimpleDateFormat dateFormat = null;

		if (date == null)
			return "";

		dateFormat = new SimpleDateFormat(strFormat, locale);
		strDate = dateFormat.format(date);

		return strDate;
	}

	/**
	 * Parses a string to produce a Date.
	 * 
	 * @param pattern
	 *            - the pattern of the string
	 * @param strDateTime
	 *            - the string to be parsed
	 * @return A Date parsed from the string. In case of error, returns null.
	 */
	public static java.util.Date parse(String pattern, String strDateTime) {
		java.util.Date date = null;
		if (strDateTime == null || pattern == null)
			return null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			formatter.setLenient(false);
			date = formatter.parse(strDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 把指定的日期和时间合成一个。
	 * 
	 * @param date
	 *            - the date
	 * @param time
	 *            - the time
	 * @return the composed datetime
	 */
	public static Date composeDate(Date date, Date time) {
		if (date == null || time == null)
			return null;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(time);
		Calendar c3 = Calendar.getInstance();
		c3.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1
				.get(Calendar.DATE), c2.get(Calendar.HOUR_OF_DAY), c2
				.get(Calendar.MINUTE), c2.get(Calendar.SECOND));
		return c3.getTime();
	}

	/**
	 * 忽略掉指定日期的时间，只返回日期信息
	 * 
	 * @param date
	 * @return
	 */
	public static Date getTheDate(Date date) {
		if (date == null)
			return null;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		Calendar c2 = Calendar.getInstance();
		c2.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1
				.get(Calendar.DATE), 0, 0, 0);
		long millis = c2.getTimeInMillis();
		millis = millis - millis % 1000;
		c2.setTimeInMillis(millis);
		return c2.getTime();
	}

	/**
	 * 给指定(d)的日期添加指定(skipDay)的天数
	 * 
	 * @param d
	 * @param skipDay
	 *            需要添加的天数，可以为正数或负数
	 * @return
	 */
	public static Date skipDateTime(Date d, int skipDay) {
		if (d == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, skipDay);
		return calendar.getTime();
	}

	/**
	 * 以字符串形式返回指定日期添加指定的天数后的结果。
	 */
	public static String skipDateTime(String timeStr, int skipDay) {
		String pattern = DEFAULT_DATE_FROMAT;
		Date d = parse(pattern, timeStr);
		Date date = skipDateTime(d, skipDay);
		return formatDateTime(pattern, date);
	}

	/**
	 * 同skipDateTime,但是返回的字符串只有日期部分忽略掉时间的部分
	 */
	public static String skipDate(String dateStr, int skipDay) {
		String pattern = "yyyy-MM-dd";
		Date d = parse(pattern, dateStr);
		Date date = skipDateTime(d, skipDay);
		return formatDateTime(pattern, date);
	}

	/**
	 * 给指定的时间加上指定的天数小时数和分钟数后返回。
	 */
	public static String getTime(String timeStr, int skipDay, int skipHour,
			int skipMinute) {
		if (null == timeStr) {
			return null;
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(parse(DEFAULT_DATE_FROMAT, timeStr));

		cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
		cal.add(GregorianCalendar.HOUR_OF_DAY, skipHour);
		cal.add(GregorianCalendar.MINUTE, skipMinute);
		cal.get(GregorianCalendar.DAY_OF_WEEK_IN_MONTH);

		SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FROMAT);

		return dateFormat.format(cal.getTime());
	}

	/**
	 * 比较指定时间和当前时间，如果早于当前时间就返回True否则返回False 如指定2009-01-01和当前时间比较会返回True。
	 */
	public static boolean dateCompare(String str) {
		boolean bea = false;
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
		String isDate = sdf_d.format(new java.util.Date());
		java.util.Date date1;
		java.util.Date date0;
		try {
			date1 = sdf_d.parse(str);
			date0 = sdf_d.parse(isDate);
			if (date0.after(date1)) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 具体到月的时间比较，如果早于当前时间就返回True，否则返回False
	 */
	public static boolean monthCompare(String str) {
		boolean bea = false;
		SimpleDateFormat sdf_m = new SimpleDateFormat("yyyy-MM");
		String isMonth = sdf_m.format(new java.util.Date());
		java.util.Date date1;
		java.util.Date date0;
		try {
			date1 = sdf_m.parse(str);
			date0 = sdf_m.parse(isMonth);
			if (date0.after(date1)) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 具体到秒的时间比较，如果早于当前时间就返回True，否则返回False
	 */
	public static boolean secondCompare(String str) {
		boolean bea = false;
		SimpleDateFormat sdf_d = new SimpleDateFormat(DEFAULT_DATE_FROMAT);
		String isDate = sdf_d.format(new java.util.Date());
		java.util.Date date1;
		java.util.Date date0;
		try {
			date1 = sdf_d.parse(str);
			date0 = sdf_d.parse(isDate);
			if (date0.after(date1)) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 比较两个具体到秒的时间，如果第一个时间早于第二个时间就返回True，否则返回False
	 * 
	 * @param data1
	 * @param date2
	 * @return
	 */
	public static boolean secondCompare(String data1, String date2) {
		boolean bea = false;
		SimpleDateFormat sdf_d = new SimpleDateFormat(DEFAULT_DATE_FROMAT);
		java.util.Date date1;
		java.util.Date date0;
		try {
			date1 = sdf_d.parse(data1);
			date0 = sdf_d.parse(date2);
			if (date0.after(date1)) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 判断是否为闫年
	 */
	public static boolean isLeapYear(int year) {
		if ((((year % 4) == 0) && ((year % 100) != 0)) || ((year % 4) == 0)
				&& ((year % 400) == 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到指定月份的天数
	 * 
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getMonthsDays(int month, int year) {
		if ((isLeapYear(year) == true) && (month == 2)) {
			return 29;
		} else if ((isLeapYear(year) == false) && (month == 2)) {
			return 28;
		}

		if ((month == 1) || (month == 3) || (month == 5) || (month == 7)
				|| (month == 8) || (month == 10) || (month == 12)) {
			return 31;
		}
		return 30;
	}

	/**
	 * 得到指定日期是周几
	 * 
	 * @return
	 */
	public static String getWeekDay(Date date) {
		DateFormatSymbols symboles = new DateFormatSymbols(Locale.getDefault());
		symboles.setShortWeekdays(new String[] { "", "7", "1", "2", "3", "4",
				"5", "6" });
		SimpleDateFormat df = new SimpleDateFormat("E", symboles);
		return df.format(date);
	}

	/**
	 * 得到当前是周几
	 * 
	 * @return
	 */
	public static String getWeekDay() {
		DateFormatSymbols symboles = new DateFormatSymbols(Locale.getDefault());
		symboles.setShortWeekdays(new String[] { "", "7", "1", "2", "3", "4",
				"5", "6" });
		SimpleDateFormat date = new SimpleDateFormat("E", symboles);
		return date.format(new Date());
	}

	/**
	 * 得到当前年份
	 */
	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	/**
	 * 得到当前年份，格式:yyyy
	 */
	public static String getYearStr() {
		return DateUtil.formatDateTime("yyyy", new Date());
	}

	/**
	 * 得到指定日期的年份，格式:yyyy
	 */
	public static String getTheYearStr(Date date) {
		return DateUtil.formatDateTime("yyyy", date);
	}

	/**
	 * 获取当前年份的最大天数
	 * 
	 * @return
	 */
	public static int getYearDays() {
		Calendar c = Calendar.getInstance();
		// c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取当前年份的最大天数
	 * 
	 * @return
	 */
	public static int getTheYearDays(int year) {
		Calendar c = Calendar.getInstance();
		c.set(year, 1, 1);
		return c.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 得到当前月份
	 * 
	 * @return
	 */
	public static int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH);
	}

	/**
	 * 得到当前月份
	 * 
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}

	/**
	 * 获取指定日期的月份字符串，格式：MM
	 * 
	 * @param date
	 * @return
	 */
	public static String getTheMonthStr(Date date) {
		return DateUtil.formatDateTime("MM", date);
	}

	/**
	 * 获取当前日期的月份字符串，格式：MM
	 * 
	 * @param date
	 * @return
	 */
	public static String getCurrentMonthStr() {
		return DateUtil.formatDateTime("MM", new Date());
	}

	/**
	 * 获取指定日期前一个月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getThePreviousMonthLastDay(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) - 1, 1);
		int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.clear();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) - 1, day);
		return c.getTime();
	}

	/**
	 * 获取指定日期月份的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getCurrentMonthLastDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), day);
		return c.getTime();
	}

	/**
	 * 获取当前时间所在周的开始日期
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 获取当前时间所在周的结束日期
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}

	/**
	 * 获取某年第一天日期
	 * 
	 * @param year
	 * @return Date
	 */
	public static Date getFirstDayOfCurrYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year
	 * @return Date
	 */
	public static Date getLastDayOfCurrYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

//	/**
//	 * 获取某年年中
//	 * 
//	 * @param date
//	 * @return
//	 */
//	public static Date getMiddDayOfYear(Date date) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH) + 1;
//		String dateString = "";
//		if (month <= 6) {
//			dateString = year + "-06-30";
//		} else {
//			dateString = year + "-12-31";
//		}
//		return TimeUtil.parseDate(dateString);
//	}

//	/**
//	 * 获取某一季度第一天
//	 * 
//	 * @param date
//	 * @return
//	 */
//	public static Date getFirstDayOfSeason(Date date) {
//		String dateString = "";
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.clear();
//		calendar.setTime(date);
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH) + 1;
//		if (month >= 1 && month <= 3) {
//			dateString = year + "-" + "01" + "-" + "01";
//		}
//		if (month >= 4 && month <= 6) {
//			dateString = year + "-" + "04" + "-" + "01";
//		}
//		if (month >= 7 && month <= 9) {
//			dateString = year + "-" + "07" + "-" + "01";
//		}
//		if (month >= 10 && month <= 12) {
//			dateString = year + "-" + "10" + "-" + "01";
//		}
//
//		return TimeUtil.parseDate(dateString);
//	}

//	/**
//	 * 获取某一季度最后一天
//	 * 
//	 * @param date
//	 * @return
//	 */
//	public static Date getLastDayOfSeason(Date date) {
//		String dateString = "";
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.clear();
//		calendar.setTime(date);
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH) + 1;
//		if (month >= 1 && month <= 3) {
//			dateString = year + "-" + "03" + "-" + "31";
//		}
//		if (month >= 4 && month <= 6) {
//			dateString = year + "-" + "06" + "-" + "30";
//		}
//		if (month >= 7 && month <= 9) {
//			dateString = year + "-" + "09" + "-" + "30";
//		}
//		if (month >= 10 && month <= 12) {
//			dateString = year + "-" + "12" + "-" + "31";
//		}
//
//		return TimeUtil.parseDate(dateString);
//	}
	
//	/**
//	 * 获取某月的月中或者月末日期
//	 * @param date
//	 * @return
//	 */
//	public static Date getMidOrLastDayOfMonth(Date date){
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		int day = calendar.get(Calendar.DATE);
//		if(day > 15){
//			return DateUtil.getCurrentMonthLastDay(date);
//		}else{
//			String month = DateUtil.formatDateTime("yyyy-MM", date);
//			return TimeUtil.parseDate(month + "-15");
//		}
//	}

	/**
	 * 得到当前日期
	 * 
	 * @return
	 */
	public static int getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取指定年份指定天数的日期
	 * 
	 * @param year
	 * @param day
	 * @return
	 */
	public static Date getDateOfYear(int year, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_YEAR, day);
		c.set(Calendar.AM_PM, 0);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 判断指定的星期几是否假日，默认星期六、星期日为假日
	 * 
	 * @param weekday
	 *            星期几
	 * @return
	 */
	public static boolean isHolidy(String weekday) {
		if ("7".equals(weekday) || "6".equals(weekday)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断指定的日期是否是假日，默认星期六、星期日为假日
	 * 
	 * @param date
	 *            指定日期
	 * @return
	 */
	public static boolean isHolidy(Date date) {
		String weekday = getWeekDay(date);
		return isHolidy(weekday);
	}
	/**
	 * 获取指定日期下一天
	 * */
	public static Date getNextDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date nextDay = new Date(calendar.getTimeInMillis());
		return nextDay;
	}
	
	/**
	 * @功能描述: 计算两个日期之间的时间差 传入的对象为两个日期
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public static int dateBetween(Date dateFrom,Date dateTo){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(dateTo);
		cal2.setTime(dateFrom);
		float a = ((float) (cal1.getTimeInMillis() - cal2.getTimeInMillis()) / 86400) / 1000;
		int b = (int) a;
		return b;
	}
	/**
	 * @功能描述: 获取日期字符串的Date日期
	 * @param dateStr
	 * @return
	 */
	public static Date getDate(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = sdf.parse(dateStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
//	public static void main(String[] args) {
//	}
	
	/**
	 * 给指定(d)的日期添加指定(skipDay)的天数
	 * 
	 * @param d
	 * @param skipDay
	 *            需要添加的月数，可以为正数或负数
	 * @return
	 */
	public static Date skipMonthTime(Date d, int skipMonth) {
		if (d == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.MONTH, skipMonth);
		return calendar.getTime();
	}
	
	/**
	 * 以字符串形式返回指定日期添加指定的月数后的结果。
	 */
	public static String skipMonthTime(String timeStr, int skipMonth) {
		String pattern = DEFAULT_DATE_FROMAT;
		Date d = parse(pattern, timeStr);
		Date date = skipMonthTime(d, skipMonth);
		return formatDateTime(pattern, date);
	}
}
