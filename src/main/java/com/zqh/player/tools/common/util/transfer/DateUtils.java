package com.zqh.player.tools.common.util.transfer;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String COMPACT_DATE_FORMAT = "yyyyMMdd";
    public static final String YM = "yyyyMM";
    public static final String NORMAL_DATE_FORMAT = "yyyy-MM-dd";
    public static final String NORMAL_DATE_FORMAT_NEW = "yyyy-MM-dd HH:mm:ss";
    public static final String NORMAL_DATE_FORMAT_NEW_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_FORMAT01 = "yyyy-MM-dd_HH-mm-ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_HOLIDAY_FORMAT = "MM-dd HH:mm:ss";
    public static final String DATE_ALL = "yyyyMMddHHmmssS";
    public static final String DATETIME_Z_FORMAT = "yyyy-MM-dd HH:mm:ss 'GMT'Z";
    public static final String GMT_Z_FORMAT = "d MMM yyyy HH:mm:ss 'GMT'";
    public static final String GMT_T_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /* 时间偏移类型 */
    public static final int directTypeOfYear = 1;
    public static final int directTypeOfMonth = 2;
    public static final int directTypeOfDay = 7;
    public static final int directTypeOfHour = 10;
    public static final int directTypeOfMinute = 12;
    /* 偏移方向 */
    public static final int toBefore = 0;/* 向前 */
    public static final int toAfter = 1;/* 向后 */

    public static final int CPMPARE_DAY = 0;
    public static final int CPMPARE_MONTH = 1;
    public static final int CPMPARE_YEAR = 2;

    /**
     * 根据日期取得相应字符串
     *
     * @param date
     * @return
     */
    public static String getDate(Date date) {
        return getDate(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 从指定的日期获得指定格式的相应字符串
     */
    public static String getDate(Date date, String formater) {
        DateFormat format = new SimpleDateFormat(formater);
        if (date == null) {
            return "";
        }
        return format.format(date);
    }

    /**
     * 通用日期获取 常用于数据解析
     * @param date
     * @return
     */
    public static Date getObjDate(Object date) {
        if(date==null)return null;

        if(date instanceof Long){
            return new Date((Long)date);
        }else if(date instanceof Date){
            return (Date)date;
        }else if(date instanceof String){
            if(((String) date).length()==DEFAULT_DATE_FORMAT.length()){
                return getDate((String) date, DEFAULT_DATE_FORMAT);
            }else{
                return getDate((String) date, DATE_FORMAT);
            }
        }else{
            throw new RuntimeException("date无法解析:"+date);
        }
    }

    public static String getDate(int dataInt) {
        return getDate(dataInt, DEFAULT_DATE_FORMAT);
    }

    /**
     * @param dataInt  long型的日期参数,以秒为单位，不是以毫秒为单位
     * @param formater 希望格式化成的样子
     * @return 从指定的日期获得指定格式的date字串 String
     */
    public static String getDate(int dataInt, String formater) {
        DateFormat dateFormat = new SimpleDateFormat(formater);
        Calendar calendar = Calendar.getInstance();
        long tmp = (long) dataInt * 1000;
        calendar.setTimeInMillis(tmp);
        return dateFormat.format(calendar.getTime());
    }

    public static String getDate(Long dataLong, String formater) {
        DateFormat dateFormat = new SimpleDateFormat(formater);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dataLong);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 根据默认格式，从字符串取得日期 默认时区为系统时区
     *
     * @param sdate a real date String
     * @return
     * @throws ParseException
     */
    public static Date getDate(String sdate) throws RuntimeException {
        return getDate(sdate, DEFAULT_DATE_FORMAT);
    }

    /**
     * 指定格式，从字符串取得日期 默认时区为系统时区
     *
     * @param sdate     a real date String
     * @param formatStr
     * @return
     * @throws ParseException
     */
    public static Date getDate(String sdate, String formatStr) throws RuntimeException {
        DateFormat dateFormat = new SimpleDateFormat(formatStr);
        dateFormat.setLenient(true);
        // dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = dateFormat.parse(sdate);
        } catch (ParseException e) {
            throw new RuntimeException("从字符串取得日期 默认时区为系统时区,解析转换异常:" + sdate + ",格式:" + formatStr);

        }
        return date;
    }



    /**
     * 根据默认格式，从字符串取得日期 时区为gmt时区
     *
     * @param sdate gmt date String
     * @return
     * @throws ParseException
     */
    public static Date getDateByGmt(String sdate) throws ParseException {
        return getDateByGmt(sdate, DEFAULT_DATE_FORMAT);
    }

    /**
     * 指定格式，从字符串取得日期 时区为gmt时区
     *
     * @param sdate
     * @param formatStr
     * @return
     * @throws ParseException
     */
    public static Date getDateByGmt(String sdate, String formatStr)
            throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(formatStr);
        dateFormat.setLenient(true);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = dateFormat.parse(sdate);
        return date;
    }

    /**
     * 得到某天的24点
     *
     * @param date
     * @return
     */
    public static Date getDate24Time(Date date) {
        return getDateTime(date, 24);
    }

    /**
     * 得到某天的24点
     *
     * @param sdate
     * @return
     * @throws ParseException
     */
    public static String getDate24Time(String sdate) {
        return getDate(getDateTime(getDate(sdate), 24));
    }

    /**
     * 得到某天的0点
     *
     * @param date
     * @return
     */
    public static Date getDate0Time(Date date) {
        return getDateTime(date, 0);
    }

    /**
     * 得到某天的0点
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getDate0Time(String date) {

        return getDate(getDateTime(getDate(date), 0));
    }

    /**
     * 得到某天的某点
     *
     * @param date
     * @param hh
     * @return
     */
    public static Date getDateTime(Date date, int hh) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year, month, day, hh, 00, 00);
        return cal.getTime();
    }

    /**
     * @param date       待转化的时间
     * @param direction  偏移方向,toBefore是向前,toAfter是向后
     * @param offset     偏移量
     * @param directType 偏移类型directTypeOfYear按年偏移,directTypeOfMonth是按月,
     *                   directTypeOfDay按天偏移
     * @return 返回偏移后时间
     * @throws ParseException
     */
    public static Date redirectDate(String date, int direction, int offset,
                                    int directType) {
        if (date == null || date.length() < 1) {
            return null;
        }
        return redirectDate(DateUtils.getDate(date), direction, offset,
                directType);
    }

    /**
     * @param date       待转化的时间
     * @param direction  偏移方向,toBefore是向前,toAfter是向后
     * @param offset     偏移量
     * @param directType 偏移类型directTypeOfYear按年偏移,directTypeOfMonth是按月,
     *                   directTypeOfDay按天偏移
     * @return 返回偏移后时间
     */
    public static Date redirectDate(Date date, int direction, int offset,
                                    int directType) {
        if (date == null)
            return null;
        if (direction == toBefore)
            offset = -offset;
        // 对日期进行处理，并返回处理后的日期
        GregorianCalendar worldTour = new GregorianCalendar();
        worldTour.setTime(date);
        worldTour.add(directType, offset);
        return worldTour.getTime();
    }

    /**
     * 昨天
     *
     * @return Date
     */
    public static Date yesterday() {
        Date today = new Date();
        return redirectDate(today, toBefore, 1, directTypeOfDay);
    }

    /**
     * 取得当月第一天的零点
     *
     * @return
     */
    public static Date getFirestDayInCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return getFirestDayInMonth(c.getTime());
    }

    /**
     * 取得某月第一天的零点
     *
     * @return
     */
    public static Date getFirestDayInMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 取得某月最后一天的最后一秒
     *
     * @return
     */
    public static Date getLastDayInMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * @param date1 需要比较的时间 不能为空(null),需要正确的日期格式
     * @param date2 被比较的时间 为空(null)则为当前时间
     * @param stype 返回值类型 0为多少天，1为多少个月，2为多少年
     * @return
     */
    public static int compareDate(String date1, String date2, int stype) {
        int n = 0;

        //String[] u = new String[]{"天", "月", "年"};
        String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";

        date2 = date2 == null ? DateUtils.getCurrentDate() : date2;

        DateFormat df = new SimpleDateFormat(formatStyle);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(date1));
            c2.setTime(df.parse(date2));
        } catch (Exception e3) {
            System.out.println("wrong occured");
        }
        // List list = new ArrayList();
        while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
            // list.add(df.format(c1.getTime())); // 这里可以把间隔的日期存到数组中 打印出来
            n++;
            if (stype == 1) {
                c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
            } else {
                c1.add(Calendar.DATE, 1); // 比较天数，日期+1
            }
        }

        n = n - 1;

        if (stype == 2) {
            n = (int) n / 365;
        }
        // System.out.println(date1 + " -- " + date2 + " 相差多少" + u[stype] + ":"
        // + n);
        return n;
    }

    /**
     * 获取两个时间间隔，精确到分
     */
    public static long getTwoDay(Date startDate, Date endDate) {
        return (startDate.getTime() - endDate.getTime()) / (24 * 60 * 60);

    }

    /**
     * 得到当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        return simple.format(date);
    }

    /**
     * 得到当前日期
     *
     * @return
     */
    public static String getCurrentDateDetail() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simple.format(date);
    }

    /**
     * 将日期转换为GMT时间
     *
     * @param date
     * @return
     */
    public static Date getGmtTime(Date date) {
        TimeZone tz = TimeZone.getDefault();
        return getZoneDate(date, tz);
    }

    /**
     * 取得当前GMT时间，返回字符模式
     *
     * @return
     */
    public static String getGmtTimeStr() {
        final Date currentTime = new Date();
        System.out.println("currentTime offset:"
                + currentTime.getTimezoneOffset());
        final DateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        // Give it to me in GMT time.
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println("currentTime offset:"
                + currentTime.getTimezoneOffset());
        return format.format(currentTime);
    }

    /**
     * 取得当前GMT时间，返回字符模式
     *
     * @return
     * @throws ParseException
     */
    public static String getZoneStrByGmtTime(String gmtTime, TimeZone timeZone)
            throws ParseException {
        final DateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date gmtDate = format.parse(gmtTime);
        System.out.println("sfjaslfjdslj:" + gmtDate.getTimezoneOffset());
        format.setTimeZone(timeZone);

        return format.format(gmtDate);
    }

    /**
     * 取得当前GMT时间，返回date模式
     *
     * @return
     * @throws ParseException
     */
    public static Date getZoneDateByGmtTime(String gmtTime, TimeZone timeZone)
            throws ParseException {
        final DateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date gmtDate = format.parse(gmtTime);
        return gmtDate;
    }

    /**
     * 取得+800时间，返回date模式 格式为 yyyyMMddHHmmssSZ
     *
     * @return
     * @throws ParseException
     */
    public static Date get8ZoneDate(String zoneTimeStr, String formatStr) {
        final DateFormat format = new SimpleDateFormat(formatStr,
                Locale.ENGLISH);

        format.setTimeZone(TimeZone.getTimeZone("+800"));// 默认就为+8
        Date date = null;
        try {
            date = format.parse(zoneTimeStr);
        } catch (ParseException e) {
            throw new RuntimeException("转换出错",e);
        }
        return date;

    }



    /**
     * 取得+800时间，返回date模式 格式为 yyyyMMddHHmmssSZ
     *
     * @return
     * @throws ParseException
     */
    public static Date get8ZoneDate(String zoneTimeStr) {

        return get8ZoneDate(zoneTimeStr, "yyyyMMddHHmmssSSSZ");

    }

    /**
     * 取得-700时间，返回字符模式
     *
     * @param date +800 时间
     * @return
     */
    public static String getUSZoneStr(Date date) {
        return getUSZoneStr(date, "yyyy-MM-dd HH:mm:ss");

    }

    /**
     * 取得太平洋时间，返回字符模式
     *
     * @param date +800 时间
     * @return
     */
    public static String getUSZoneStr(Date date, String formatStr) {
        final DateFormat format = new SimpleDateFormat(formatStr);

        // TimeZone tz = TimeZone.getTimeZone("GMT-8:00");
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        format.setTimeZone(tz);

        return format.format(date);

    }

    /**
     * 取得GMT时间，返回字符模式
     *
     * @param date +800 时间
     * @return
     */
    public static String getGMTStr(Date date, String formatStr) {
        final DateFormat format = new SimpleDateFormat(formatStr);

        TimeZone tz = TimeZone.getTimeZone("GMT");
        format.setTimeZone(tz);
        return format.format(date);
    }

    /**
     * 取得UTC时间，返回字符模式
     *
     * @param date +800 时间
     * @return
     */
    public static String getUTCStr(Date date, String formatStr) {
        final DateFormat format = new SimpleDateFormat(formatStr);

        TimeZone tz = TimeZone.getTimeZone("UTC");
        format.setTimeZone(tz);
        return format.format(date);
    }

    public static Date get8TimeByUTCDate(String date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(date));
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        return calendar.getTime();
    }

    public static Date get8TimeByUTCStr(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        return calendar.getTime();
    }

    private static Date getZoneDate(Date date, TimeZone tz) {
        Date ret = new Date(date.getTime() - tz.getRawOffset());

        // if we are now in DST, back off by the delta. Note that we are
        // checking the GMT date, this is the KEY.
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());

            // check to make sure we have not crossed back into standard time
            // this happens when we are on the cusp of DST (7pm the day before
            // the change for PDT)
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }

        return ret;
    }

    /**
     * 获取所有的时区编号. <br>
     * 排序规则:按照ASCII字符的正序进行排序. <br>
     * 排序时候忽略字符大小写.
     *
     * @return 所有的时区编号(时区编号已经按照字符[忽略大小写]排序).
     */
    public static String[] fecthAllTimeZoneIds() {
        Vector v = new Vector();
        String[] ids = TimeZone.getAvailableIDs();
        for (int i = 0; i < ids.length; i++) {
            v.add(ids[i]);
        }
        Collections.sort(v, String.CASE_INSENSITIVE_ORDER);
        v.copyInto(ids);
        v = null;
        return ids;
    }

    /**
     * 将日期时间字符串根据转换为指定时区的日期时间.
     *
     * @param srcFormater   待转化的日期时间的格式.
     * @param srcDateTime   待转化的日期时间.
     * @param dstFormater   目标的日期时间的格式.
     * @param dstTimeZoneId 目标的时区编号.
     * @return 转化后的日期时间.
     */
    public static String string2Timezone(String srcFormater,
                                         String srcDateTime, String dstFormater, String dstTimeZoneId) {
        if (srcFormater == null || "".equals(srcFormater))
            return null;
        if (srcDateTime == null || "".equals(srcDateTime))
            return null;
        if (dstFormater == null || "".equals(dstFormater))
            return null;
        if (dstTimeZoneId == null || "".equals(dstTimeZoneId))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
        try {
            int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);
            Date d = sdf.parse(srcDateTime);
            long nowTime = d.getTime();
            long newNowTime = nowTime - diffTime;
            d = new Date(newNowTime);
            return getDate(d, dstFormater);
        } catch (ParseException e) {
            // Log.output(e.toString(), Log);
            return null;
        } finally {
            sdf = null;
        }
    }

    /**
     * 获取系统当前默认时区与UTC的时间差.(单位:毫秒)
     *
     * @return 系统当前默认时区与UTC的时间差.(单位:毫秒)
     */
    private static int getDefaultTimeZoneRawOffset() {
        return TimeZone.getDefault().getRawOffset();
    }

    /**
     * 获取指定时区与UTC的时间差.(单位:毫秒)
     *
     * @param timeZoneId 时区Id
     * @return 指定时区与UTC的时间差.(单位:毫秒)
     */
    private static int getTimeZoneRawOffset(String timeZoneId) {
        return TimeZone.getTimeZone(timeZoneId).getRawOffset();
    }

    /**
     * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)
     *
     * @param timeZoneId 时区Id
     * @return 系统当前默认时区与指定时区的时间差.(单位:毫秒)
     */
    private static int getDiffTimeZoneRawOffset(String timeZoneId) {
        return TimeZone.getDefault().getRawOffset()
                - TimeZone.getTimeZone(timeZoneId).getRawOffset();
    }

    /**
     * 将日期时间字符串根据转换为指定时区的日期时间.
     *
     * @param srcDateTime   待转化的日期时间.
     * @param dstTimeZoneId 目标的时区编号.
     * @return 转化后的日期时间.
     * @see #string2Timezone(String, String, String, String)
     */
    public static String string2TimezoneDefault(String srcDateTime,
                                                String dstTimeZoneId) {
        return string2Timezone("yyyy-MM-dd HH:mm:ss", srcDateTime,
                "yyyy-MM-dd HH:mm:ss", dstTimeZoneId);
    }

    /**
     * 获取各时区对应的时间
     *
     * @param date       +8时间
     * @param formatStr  　最终显示的时间格式，如：HH:mm
     * @param timeZoneId 时区ID
     * @return
     */
    public static String getZoneStr(Date date, String formatStr,
                                    String timeZoneId) {
        final DateFormat format = new SimpleDateFormat(formatStr);
        TimeZone tz = TimeZone.getTimeZone(timeZoneId);
        format.setTimeZone(tz);
        return format.format(date);
    }

    /**
     * @param days 输入天数 后几天
     * @return
     */
    public static Calendar getAfterDay(Calendar cl, Integer days) {
        //使用roll方法进行回滚到后一天的时间
        //cl.roll(Calendar.DATE, 1);
        //使用set方法直接设置时间值
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day + days);
        return cl;
    }

    /**
     * @param days 输入天数 早几天
     * @return
     */
    public static Calendar getBeforeDay(Calendar cl, Integer days) {
        //使用roll方法进行回滚到后一天的时间
        //cl.roll(Calendar.DATE, 1);
        //使用set方法直接设置时间值
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day - days);
        return cl;
    }

    /**
     * 获取固定时间段内,每天0点的时间戳
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getDateListByDays(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = new ArrayList<String>();
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            while (cal.getTimeInMillis() <= end.getTime()) {
                list.add(cal.getTimeInMillis()+"");
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 店长ID生成日期规则 方法
     *
     * @param curDate
     * @return
     * @throws ParseException
     */
    public static String getMonthEN(Date curDate) throws ParseException {
        String str = DateFormatUtils.format(curDate, "MM");
        String str2 = DateFormatUtils.format(curDate, "yy");
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date date = sdf.parse(str);
        sdf = new SimpleDateFormat("MMM", Locale.US);

        return str2 + sdf.format(date).toUpperCase();
    }

    public static String getBeforeOrAfterDate(String dateStr, int add) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date lastDay = cal.getTime();
        return sdf.format(date);
    }

    /**
     * 转换距离发货时间方法
     * 1. 倒计时 (1天15小时)
     * 2. 已超过发货期限
     *
     * @param prepareTime
     * @return
     */
    public static String convertPrepareTimeStr(Date prepareTime) {
        if (prepareTime == null){
            return null;
        }
        String resultMsg = "";

        Date now = new Date();
        long between = (prepareTime.getTime() - now.getTime()) / 1000;

        if (between < 0) {
            return "已超过发货期限";
        }

        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;

        if (day > 0){
            resultMsg += day + "天";
        }
        resultMsg += hour + "小时";

        return resultMsg;
    }

    /**
     * 获取某月的某天
     * @param m 月份偏移量 以本月为基础
     * @param d 天数偏移量
     * @return
     */
    public static Date getAnyMonthAnyDay(int m, int d){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, m);
        calendar.set(Calendar.DAY_OF_MONTH, d);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    /**
     * lazada 根据订单创建时间计算预发货时间
     *
     * @param created
     * @return
     */
    public static Date getPrepareTime(Date created) {
        if (created == null)
            return null;
        Date endTime = DateUtils.redirectDate(created, DateUtils.toAfter, 2, DateUtils.directTypeOfDay);

        Calendar cal = Calendar.getInstance();
        cal.setTime(endTime);

        //update by lingfang feng on 2016-10-14
        //reason: 产品要求暂时去掉节假日和双休日的计算时间逻辑
        /*Date newTime = cal.getTime();
		Date endDay = getDate(getDate(newTime, DEFAULT_HOLIDAY_FORMAT), DEFAULT_HOLIDAY_FORMAT);
		boolean holiday = false;

		if (NewYearDayStart.before(endDay) && NewYearDayStop.after(endDay)){

			cal.add(Calendar.DAY_OF_MONTH, 3);
			holiday = true;
		}else if (SpringFestivalStart.before(endDay) && SpringFestivalStop.after(endDay)){

			cal.add(Calendar.DAY_OF_MONTH, 7);
			holiday = true;
		}else if (QingMingFestivalStart.before(endDay) && QingMingFestivalStop.after(endDay)){

			cal.add(Calendar.DAY_OF_MONTH, 3);
			holiday = true;
		}else if (LaborDayStart.before(endDay) && LaborDayStop.after(endDay)){

			cal.add(Calendar.DAY_OF_MONTH, 3);
			holiday = true;
		}else if (DragonBoatStart.before(endDay) && DragonBoatStop.after(endDay)){

			cal.add(Calendar.DAY_OF_MONTH, 3);
			holiday = true;
		}else if (MidAutumnFestivalStart.before(endDay) && MidAutumnFestivalStop.after(endDay)){

			cal.add(Calendar.DAY_OF_MONTH, 3);
			holiday = true;
		}else if (NationalDayStart.before(endDay) && NationalDayStop.after(endDay)){

			cal.add(Calendar.DAY_OF_MONTH, 7);
			holiday = true;
		}

		//判断是否双休日
		if (!holiday){  //非节假日,则判断是否是双休日
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
				cal.add(Calendar.DAY_OF_MONTH, 2);
			}
		}
*/
        return cal.getTime();
    }

    /**
     * 计算两个日期之间相差的天数 和compareDate(dateStr1, dateStr2, type) 方法效果一样
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) {
        if (smdate == null || bdate == null)
            return 0;
        return timesBetween(smdate, bdate, null);
    }

    public static int timesBetween(Date smdate, Date bdate, String type) {
        if (smdate == null || bdate == null)
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();

        long between_days = 0l;
        if (StringUtils.isEmpty(type) || "day".equals(type))
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        else if ("hour".equals(type)) {
            between_days = (time2 - time1) / (1000 * 3600);
        }

        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 将Date类转换为XMLGregorianCalendar
     *
     * @param date
     * @return
     */
    public static XMLGregorianCalendar dateToXmlDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        DatatypeFactory dtf = null;
        try {
            dtf = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
        }
        XMLGregorianCalendar dateType = dtf.newXMLGregorianCalendar();
        dateType.setYear(cal.get(Calendar.YEAR));
        //由于Calendar.MONTH取值范围为0~11,需要加1
        dateType.setMonth(cal.get(Calendar.MONTH) + 1);
        dateType.setDay(cal.get(Calendar.DAY_OF_MONTH));
        dateType.setHour(cal.get(Calendar.HOUR_OF_DAY));
        dateType.setMinute(cal.get(Calendar.MINUTE));
        dateType.setSecond(cal.get(Calendar.SECOND));
        return dateType;
    }

    /**
     * amazon使用 json转date
     *
     * @param xmlJson
     * @return
     */
    public static Date xmlDateJson2Date(JSONObject xmlJson) {

        if (xmlJson == null)
            return null;
        Integer year = xmlJson.getInteger("year");
        Integer month = xmlJson.getInteger("month") - 1;
        Integer day = xmlJson.getInteger("day");
        Integer timezone = xmlJson.getInteger("timezone");
        Integer hour = xmlJson.getInteger("hour");
        Integer minute = xmlJson.getInteger("minute");
        Integer second = xmlJson.getInteger("second");
        boolean valid = xmlJson.getBoolean("valid");

        if (year == 1970 && month == 0 && day == 1 && hour == 0 && minute == 0 && second == 0) {
            second = 1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        calendar.setTimeZone(TimeZone.getTimeZone(timezone + ""));


        DateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        String date = format.format(calendar.getTime());

        return getDate(date);
    }

    public static Date getDateTimeSecond(Date date, int hh, int min, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year, month, day, hh, min, second);
        return cal.getTime();
    }


    /**
     * 将XMLGregorianCalendar转换为Date
     *
     * @param cal
     * @return
     */
    public static Date xmlDate2Date(XMLGregorianCalendar cal) {
        return cal.toGregorianCalendar().getTime();
    }

    public static int sum = 0;

    /**
     * get Calendar of given year
     * @param year
     * @return
     */
    private static Calendar getCalendarFormYear(int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }

    /**
     * get start date of given week no of a year
     * @param year
     * @param weekNo
     * @return
     */
    public static String getStartDayOfWeekNo(int year,int weekNo){
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        String time =  cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);
        try {
            return format.format(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get the end day of given week no of a year.
     * @param year
     * @param weekNo
     * @return
     */
    public static String getEndDayOfWeekNo(int year,int weekNo){
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        String time = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" +
                cal.get(Calendar.DAY_OF_MONTH);

        try {
            return format.format(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertWeeksToDate(String weeks){
        StringBuffer resultDate = new StringBuffer();
        if (StringUtils.isEmpty(weeks)){
            return resultDate.toString();
        }
        String[] weekArr = weeks.split("-");
        if (weekArr.length > 1){
            Integer year = Integer.valueOf(weekArr[0]);
            Integer week = Integer.valueOf(weekArr[1]);
            resultDate.append(getStartDayOfWeekNo(year, week)).append("~")
            .append(getEndDayOfWeekNo(year, week));
        }
        return resultDate.toString();
    }

    /**
     * 分割开始结束时间，转换为周的列表
     * eg:[2017-12-04~2017-12-10,2017-12-11~2017-12-17]
     * @param startDay
     * @param endDay
     * @return
     */
    public static List<String> getWeekList(String startDay, String endDay){

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        List<String> list = new ArrayList<String>();
        // 分割日期，获取返回周的次数
        int compareTime = compareDate(startDay, endDay, 0);
        if (compareTime <= 0){
            return list;
        }
        int weeks = (compareTime + 1) / 7 + (((compareTime + 1) % 7) > 0 ? 1 : 0);
        try {
            for (int i=0; i< weeks; i++){
                Date date = sdf.parse(startDay);
                String monday = getMondayOfThisWeek(date);
                String sunday = getSundayOfThisWeek(date);
                startDay = getDate(redirectDate(getDate(sunday, DATE_FORMAT), toAfter, 1, directTypeOfDay));
                Calendar cal = Calendar.getInstance();
                cal.setTime(getDate(startDay));
                int w = cal.get(Calendar.WEEK_OF_YEAR);
                list.add(startDay.substring(0,4)+"-"+(w<9?"0":"")+w);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 获取固定时间段内,每天0点的时间戳
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getDayList(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String> list = new ArrayList<String>();
        try {
            Date start = sdf.parse(startTime);
            Date end = sdf.parse(endTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            while (cal.getTimeInMillis() <= end.getTime()) {
                list.add(sdf.format(cal.getTime()));
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 分割开始结束时间，转换为周的列表
     * eg:[2017-12-04~2017-12-10,2017-12-11~2017-12-17]
     * @param startDay
     * @param endDay
     * @return
     */
    public static List<String> getDateListByWeeks(String startDay, String endDay){

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        List<String> list = new ArrayList<String>();
        // 分割日期，获取返回周的次数
        int compareTime = compareDate(startDay, endDay, 0);
        if (compareTime <= 0){
            return list;
        }
        int weeks = (compareTime + 1) / 7 + (((compareTime + 1) % 7) > 0 ? 1 : 0);
        try {
            StringBuffer sb;
            for (int i=0; i< weeks; i++){
                sb = new StringBuffer();
                Date date = sdf.parse(startDay);
                String monday = getMondayOfThisWeek(date);
                String sunday = getSundayOfThisWeek(date);
                sb.append(monday).append("~").append(sunday);
                startDay = getDate(redirectDate(getDate(sunday, DATE_FORMAT), toAfter, 1, directTypeOfDay));
                list.add(sb.toString());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 得到周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek(Date time) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0){
            day_of_week = 7;
        }
        c.add(Calendar.DATE, -day_of_week + 1);
        return format.format(c.getTime());
    }

    /**
     * 得到周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek(Date time) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0){
            day_of_week = 7;
        }
        c.add(Calendar.DATE, -day_of_week + 7);
        return format.format(c.getTime());
    }


    public static List<String> getDateListByMonths(String startDay, String endDay){
        List<String> list = new ArrayList<String>();
        int compareTime = compareDate(startDay, endDay, 1);
        if (compareTime < 0){
            return list;
        }
        for (int i = 0; i <= compareTime; i++){

            Date time = getDate(startDay, DATE_FORMAT);
            list.add(getDate(time, "yyyy-MM"));
            startDay = getDate(redirectDate(time, toAfter, 1, directTypeOfMonth));
        }

        return list;
    }

    /***
     * 功能描述:将PST时区时间转换成CTT时区时间<br>
     *
     * @return java.util.Date
     * @date 2018/5/9 下午5:47
     */
    public static Date getCCTFromPST(String dateStr) {
        SimpleDateFormat simpleDateFormatPST = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        simpleDateFormatPST.setTimeZone(TimeZone.getTimeZone("PST"));
        SimpleDateFormat simpleDateFormatCTT = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        simpleDateFormatCTT.setTimeZone(TimeZone.getTimeZone("CTT"));
        Date date;
        try {
            date = simpleDateFormatPST.parse(dateStr);
            return simpleDateFormatCTT.parse(simpleDateFormatCTT.format(date));
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 传入Data类型日期，返回字符串类型时间（ISO8601标准时间）
     * @param date
     * @return
     */
    public static String getISO8601Timestamp(Date date){
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        //TimeZone tz = TimeZone.getTimeZone("GMT-01");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        df.setTimeZone(tz);
        String nowAsISO = df.format(date);
        return nowAsISO;
    }

    public static void main(String[] args) {
        String startTime = "2017-11-22";
        String endTime = "2018-05-03";
        System.out.println(convertWeeksToDate("2017-49"));

//        System.out.println(getMondayOfThisWeek(getDate("2017-12-05", DATE_FORMAT)));
//        System.out.println((compareDate(startTime, endTime, 0)+1)/7);

        System.out.println(getDayList(startTime, endTime));
        System.out.println(getWeekList(startTime, endTime));
        System.out.println(getDateListByMonths(startTime, endTime));

    }

}
