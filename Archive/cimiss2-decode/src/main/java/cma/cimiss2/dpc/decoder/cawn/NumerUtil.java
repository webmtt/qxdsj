package cma.cimiss2.dpc.decoder.cawn;

public class NumerUtil {
	
	public static double to_double(String str, double default_val){
		if(!"-999.9".equals(str)){
			try {
				return Double.parseDouble(str);
			} catch (Exception e) {
				return default_val;
			}
		}else{
			return default_val;
		}
	}
	
	public static int to_int(String str, int defalut_val){
		if(!"-999.9".equals(str)){
			try {
				return Integer.parseInt(str);
			} catch (Exception e) {
				return defalut_val;
			}
		}else{
			return defalut_val;
		}
	}

}
