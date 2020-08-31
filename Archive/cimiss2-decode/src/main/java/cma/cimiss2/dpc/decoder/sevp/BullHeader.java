package cma.cimiss2.dpc.decoder.sevp;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 报文头解码类
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午3:29:12   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
	public class BullHeader{
		/**
		 * 报文类型
		 */
		private String tt = "";
		/**
		 * 国家
		 */
		private String aa = "";
		/**
		 * 编码
		 */
		private String ii = "";
		/**
		 * 发报中心
		 */
		private String cccc = "";
		/**
		 * 发报时间（日时分）
		 */
		private String YYGGgg = "";
		/**
		 * 更正报标识
		 */
		private String bbb = "";
		
		public String getTt() {
			return tt;
		}
		public void setTt(String tt) {
			this.tt = tt;
		}
		public String getAa() {
			return aa;
		}
		public void setAa(String aa) {
			this.aa = aa;
		}
		public String getIi() {
			return ii;
		}
		public void setIi(String ii) {
			this.ii = ii;
		}
		public String getCccc() {
			return cccc;
		}
		public void setCccc(String cccc) {
			this.cccc = cccc;
		}
		public String getYYGGgg() {
			return YYGGgg;
		}
		public void setYYGGgg(String yYGGgg) {
			YYGGgg = yYGGgg;
		}
		public String getBbb() {
			return bbb;
		}
		public void setBbb(String bbb) {
			this.bbb = bbb;
		}
		/**
		 * 解析报文头，TTAAII CCCC YYGGgg [BBB]   
		 * @param header 待解析字符串
		 * @param bullheader 存储报文头解析结果
		 * @return int      解析成功返回1；否则返回-1
		 */
		public static int decodeHeader(String header, BullHeader bullheader){
			if(header == null || header.equals(""))
				return -1;
			String sp[] = header.trim().split("\\s+");
			
			if(sp.length == 3 || sp.length == 4){
				// 1. TTAAII 例如 SAAU31(资料类型+国家+号码)
				if(sp[0].length() == 6 || sp[0].length() == 5){
					bullheader.setTt(sp[0].substring(0, 2));
					bullheader.setAa(sp[0].substring(2, 4));
					bullheader.setIi(sp[0].substring(4));
				}
				else
					return -1; // TTAAII 格式错误
				// 2. CCCC编报中心  例如：AMMC、RJTD
				if(sp[1].length() == 4){
					bullheader.setCccc(sp[1]);
				}
				else
					return -1; // CCCC 格式错误
				// 3. YYGGgg, (日期+时+分，分别占两位)
				if(sp[2].length() == 6){
					int i = 0;
					for(i = 0; i < 6; i ++){
						if(!Character.isDigit(sp[2].charAt(i)))
							break;
					}
					if(i == 6)
						bullheader.setYYGGgg(sp[2]);
					else{
						return -1; // YYGGgg 格式错误
					}
				}
				else
					return -1; //YYGGgg 格式错误
				// 4. V_BBB: 更正标识，例如：CCA
				bullheader.setBbb("000");
				if(sp.length == 4){
					String tmp_val = sp[3];
					char s = tmp_val.charAt(0);
					if(tmp_val.length() == 3 && (s == 'A' || s == 'R' ||s == 'C' ||s == 'P')){
						bullheader.setBbb(sp[3]);
					}
					else
						return -1; // V_BBB 格式错误
				}
			} 
			else{
				return -1;// 报头格式错误
			}
			return 1; // 报头解析成功
		}
		
	}
