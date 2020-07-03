package amo.lib.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StringUtils {
    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                // 判断字符是否为空格、制表符、tab
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 移除前后的括号()
     * @param source
     * @return
     */
    public static String trimBracket(String source) {
        if (source == null) {
            source = "";
        }
        String dest = source;

        if (dest.startsWith("(") && dest.endsWith(")")) {
            dest = dest.substring(1, dest.length() - 1);
            return trimBracket(dest);
        }

        return dest;
    }

    public static Boolean isNullOrEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static Boolean isNotEmpty(String str){
        return !isNullOrEmpty(str);
    }

    public static String substringBefore(String str, String separator)
    {
        if ((isNullOrEmpty(str)) || (separator == null)) {
            return str;
        }
        if (separator.length() == 0) {
            return "";
        }
        Integer pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 将String分割，转为Integer列表
     * @param ids 原字符串
     * @param regex 分割字符串
     * @return Integer列表
     */
    public static List<Integer> split(String ids, String regex)
    {
        List<Integer> idList = new ArrayList<>();
        if(isNotEmpty(ids) && isNotEmpty(regex)) {
            String[] idAttr = ids.split(regex);
            for(String idStr : idAttr) {
                Integer id = getInteger(idStr);

                if(id>0) {
                    idList.add(id);
                }
            }
        }
        return idList;
    }

    /**
     * String转Integer
     * @param str
     * @return
     */
    public static Integer getInteger(String str){
        try{
            return Integer.parseInt(str);
        }
        catch (Exception ex){
            return 0;
        }
    }

    public static Boolean isNotEmpty(List<?> list)
    {
        return !isNullOrEmpty(list);
    }

    public static Boolean isNullOrEmpty(List<?> list){
        if (list == null || list.size() == 0)
        {
            return true;
        }

        return false;
    }

    /**
     * String按照一定格式转Date
     * @param dateStr 原始String
     * @param format  格式
     * @return Date
     */
    public static Date convertStringToDate(String dateStr, String format)
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = simpleDateFormat.parse(dateStr);
            return date;
        }
        catch (Exception ex){
            return null;
        }
    }

    /**
     * Date按照一定格式转String
     * @param date Date
     * @param format 格式
     * @return String
     */
    public static String convertDateToString(Date date, String format)
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            String strDate = simpleDateFormat.format(date);
            return strDate;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
