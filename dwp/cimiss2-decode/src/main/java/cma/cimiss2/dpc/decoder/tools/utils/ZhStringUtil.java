package cma.cimiss2.dpc.decoder.tools.utils;

import java.util.regex.Pattern;

/**
 * @author ：yck
 * @date ：Created in 2020/1/16 0016 9:44
 * @description：判断数字字符串和汉字
 * @modified By：
 * @version: 1.0$
 */
public class ZhStringUtil {

    /**
     * 判断字符串是否为数字
     * @param str
     * @return
     */
    public static boolean isNumberic(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断一个字符串是否为字母
     * @param fstrData
     * @return
     */
    public static boolean check(String fstrData) {
        char c = fstrData.charAt(0);
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为汉字
     * @param str
     * @return
     */
    public static boolean vd(String str) {

        char[] chars = str.toCharArray();
        boolean isUtf8 = false;
        for (int i = 0; i < chars.length; i++) {
            byte[] bytes = ("" + chars[i]).getBytes();
            if (bytes.length == 2) {
                int[] ints = new int[2];
                ints[0] = bytes[0] & 0xff;
                ints[1] = bytes[1] & 0xff;

                if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40 && ints[1] <= 0xFE) {
                    isUtf8 = true;
                    break;
                }
            }
        }
        return isUtf8;
    }
}
