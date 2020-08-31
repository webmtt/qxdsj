package cma.cimiss2.dpc.split_msg.common;

public class StringUtil {
	
	/**
     * @param str 原字符串
     * @param sToFind 需要查找的字符串
     * @return 返回在原字符串中sToFind出现的次数
     */
	public static int countStr(String str,String sToFind) {
		int num = 0;
        while (str.contains(sToFind)) {
            str = str.substring(str.indexOf(sToFind) + sToFind.length());
            num ++;
        }
        return num;
	}

}
