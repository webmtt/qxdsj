package cma.cimiss2.dpc.decoder.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Aouthor: xzh
 * @create: 2017-11-22 10:29
 */
public class FileNameHandle {
    public static String getFileTime(String str) {
        try {
            String regx = "[0-9]*";
            Pattern pattern = Pattern.compile(regx);
            Matcher matcher = pattern.matcher(str);
//          boolean matches = matcher.matches();
            while (matcher.find()) {
                String group = matcher.group();
                if (group != null && group.length() == 14) {
                    return group;
                }
            }
        } catch (Exception e) {
            System.out.println("正则匹配错误");
        }
        return null;
    }

    public static boolean isHour(String fileTime) {
        String substring = "";
        try {
            substring = fileTime.substring(10, 12);

        } catch (Exception e) {
            System.out.println("fileTime sub error!");
        }
        if ("00".equals(substring)) {
            return true;
        } else {
            return false;
        }
    }
}
