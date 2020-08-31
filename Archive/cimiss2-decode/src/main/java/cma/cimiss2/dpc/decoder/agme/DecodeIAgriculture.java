package cma.cimiss2.dpc.decoder.agme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;


import org.cimiss.extra.utils.FileUtils;

import cma.cimiss2.dpc.decoder.tools.utils.ByteShipUtil;




// TODO: Auto-generated Javadoc
/**
 * -------------------------------------------------------------------------------
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年11月14日       Initial creation.
 * </pre>
 * 
 * @author leiming
 *---------------------------------------------------------------------------------
 */
public class DecodeIAgriculture {
	/* 存放数据解析的结果集 */
	//private List<float[][]> parseResult=new ArrayList<float[][]>();	
	/** The parse result. */
	//private List<float[]> parseResult=new ArrayList<float[]>();	
	private float[] parseResult;
	
	/** The i HDR. */
	private float [] iHDR = new float[30];
	
	/** The date temp. */
	private String dateTemp = "";//记录文件时间
	
	/** The Prod code. */
	private String ProdCode = "";//记录文件ProdCode
	
	/** The Description. */
	private String Description = "";//记录文件头文件描述信息
	
	/** The Validtime. */
	private int Validtime = 0;//记录时次
	
	/**
	 * Gets the parses the result.
	 *
	 * @return the parses the result
	 */
	public float[] getParseResult() {
		return parseResult;
	}

	/**
	 * Sets the parses the result.
	 *
	 * @param parseResult the new parses the result
	 */
	public void setParseResult(float[] parseResult) {
		this.parseResult = parseResult;
	}

	/**
	 * Gets the i HDR.
	 *
	 * @return the i HDR
	 */
	public float[] getiHDR() {
		return iHDR;
	}

	/**
	 * Sets the i HDR.
	 *
	 * @param iHDR the new i HDR
	 */
	public void setiHDR(float[] iHDR) {
		this.iHDR = iHDR;
	}

	/**
	 * Gets the date temp.
	 *
	 * @return the date temp
	 */
	public String getDateTemp() {
		return dateTemp;
	}

	/**
	 * Sets the date temp.
	 *
	 * @param dateTemp the new date temp
	 */
	public void setDateTemp(String dateTemp) {
		this.dateTemp = dateTemp;
	}

	/**
	 * Gets the validtime.
	 *
	 * @return the validtime
	 */
	public int getValidtime() {
		return Validtime;
	}

	/**
	 * Sets the validtime.
	 *
	 * @param validtime the new validtime
	 */
	public void setValidtime(int validtime) {
		Validtime = validtime;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		Description = description;
	}
	
	/**
	 * Gets the prod code.
	 *
	 * @return the prod code
	 */
	public String getProdCode() {
		return ProdCode;
	}

	/**
	 * Sets the prod code.
	 *
	 * @param prodCode the new prod code
	 */
	public void setProdCode(String prodCode) {
		ProdCode = prodCode;
	}
	/*调用如下所示
	 *  //解码
		如果文件srcFile扩展名是.hdr，则直接								
		List<float[][]> IMGIAgriculture=IAgriculture(srcFilePath);//解码img文件
	 * 
	 * */
	/**
	 * 函数名：IAgriculture  智慧农业数据解码:将数据按照波段存储到二维数组中。每个波段存储一个二维数组.
	 *
	 * @param strFileName 文件名
	 * @return 解码后的数据集，即图像的像素数值
	 */
    //public List<float[][]> IAgriculture(String strFileName) {
    //public List<float[]> IAgriculture(String strFileName) {	
	public float[] IAgriculture(String strFileName) {	
    	
		float[] Header=readHeader(strFileName);//读头文件
		//得到头文件中图像的行列数和波段数等参数
		float samples = Header[1]; 	
		float lines = Header[2]; 
		float datatype = Header[9]; 
		
		
		//得到文件时间
		Pattern pattern = Pattern.compile("[0-9]*");
		
		String StrTemp[] = (strFileName.split("\\\\")[strFileName.split("\\\\").length-1]).split("\\_");
		
		for(int i=0; i<strFileName.length();i++) {
			if ((pattern.matcher(StrTemp[i]).matches()) && (StrTemp[i].length()>7)){
				if ((StrTemp[i]).length()==14){
					dateTemp = StrTemp[i];
					break;
				}
				else if ((StrTemp[i]).length()==8){
					dateTemp = StrTemp[i] + "000000";
					break;
				}
				else if ((StrTemp[i]).length()==6){
					dateTemp = StrTemp[i] + "00000000";
					break;
				}
			}
		}

		ProdCode = (strFileName.split("\\\\")[strFileName.split("\\\\").length-1]).split("\\_P_")[1].split("\\_PD_")[0];
		Validtime = Integer.parseInt((strFileName.split("\\\\")[strFileName.split("\\\\").length-1]).split("\\_PD_")[1].split("\\_")[0].trim());
		//读取数据文件，将hdr文件名改为img
		String strs=strFileName.substring(0, strFileName.length()-4);
		strFileName =strs+".img";
    	
		//FileInputStream inputFile=null;
		
		//float[][] bandData = new float[samples][lines];	
		
		float[] bandData = new float[(int)(samples*lines)];	
		//long startTime=System.currentTimeMillis();   //获取开始时间   
		try {
	        
			//inputFile = new FileInputStream(strFileName);

	        int  i = 0;
	        
	        //BytesShipUtils BytesToFloat = new BytesShipUtils();
	        
	        //根据ATS入库要求修改解码程序，直接输出一维数据
			int iDataType = (int)datatype;

			
			//read file
			
			byte[] bytes = FileUtils.readFileByMappedByteBuffer(strFileName);

	        /*while (( tempbyte= inputFile.read()) != -1) {
	        	//if((iDataType==4)&&(tempbyte>127)){
	        	//	tempbyte=tempbyte-128;
	        	//}
	        	
	        	bytes.add((byte)tempbyte);
	        	
	        	//if(tempbyte>=255){
	        		System.out.println("before:"+tempbyte);
	        	//}
	        	
	        		
        		//bandData[i] = tempbyte;
        		//System.out.println("before:"+bandData[i]);
        		//i++;
        		
	        } 	*/	
	        
	        //shift data
	        int pos=0;

	        //while(pos<bytes.size()-1){
	        float l=0,m=100000;
	        while(pos<bytes.length){
	        	if(iDataType==1){
	        		if(pos>bandData.length-1) break;
	        		int temp = bytes[pos] & 0xff;//高位补零，防止精度损失
	        		bandData[i] = (float)temp;
	        		//System.out.println(bandData[i]);
	        		
	        		pos++;
	        		i++;
	        	}
	        	else if(iDataType==2){
	        		byte[] tempByte2 = new byte[2];
		        	System.arraycopy(bytes,pos, tempByte2, 0, 2);
		        	pos+=datatype;
		        	bandData[i] = ByteShipUtil.bytesToShort(tempByte2, true);
		        	
   	        	   if(bandData[i]>l){
   	        		   l=bandData[i];
		        	}
		        	else if(bandData[i]<m){
		        		m=bandData[i];
		        	}
		        	
		        	//System.out.println(bandData[i]);
		        	
		        	i++;
	        	}
	        	else if(iDataType==3){
	        		byte[] tempByte2 = new byte[4];
		        	System.arraycopy(bytes,pos, tempByte2, 0, 4);
		        	pos+=datatype;
		        	bandData[i] = ByteShipUtil.bytesToInt(tempByte2, true);
		        	//System.out.println(bandData[i]);
		        	
		        	i++;
	        	}

	        	else if(iDataType==4){
	        		byte[] tempByte2 = new byte[4];
		        	System.arraycopy(bytes,pos, tempByte2, 0, 4);
		        	pos+=datatype;
		        	bandData[i] = ByteShipUtil.bytesToFloat(tempByte2, true);
	   	        	   if(bandData[i]>l){
	   	        		   l=bandData[i];
			        	}
			        	else if(bandData[i]<m){
			        		m=bandData[i];
			        	}
		        	
		        	System.out.println(bandData[i]);
		        	
		        	i++;
	        	}
	        	else if(iDataType==5 ){
	        		byte[] tempByte2 = new byte[8];
		        	System.arraycopy(bytes,pos, tempByte2, 0, 8);
		        	pos+=datatype;
		        	//bandData[i] = ByteShipUtil.bytesToDouble(tempByte2, true);
		        	//System.out.println(bandData[i]);
		        	
		        	i++;
	        	}
	        }

	        System.out.println("m="+m+";;;;;;"+"l="+l);
	        parseResult = bandData;
	        

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

		}
		//long endTime=System.currentTimeMillis(); //获取结束时间
        //System.out.println("程序运行时间： "+(endTime-startTime)+"ms"); 
        
        return parseResult;
    }
	
	/**
	 * 函数名：readHeader  读取头文件.
	 *
	 * @param strFileName 所解码的头文件
	 * @return 图像的行列数和波段数
	 */ 
    private float[] readHeader(String strFileName){
    	
        File file = new File(strFileName);
        
        Long filelength = file.length(); // 获取文件长度

		try {			
			
            //FileInputStream in = new FileInputStream(file);
			//in.read(filecontent);	
    		//String[] fileContentArr = new String(filecontent).split("\n");
			
            InputStreamReader in = new InputStreamReader(new FileInputStream(file),"gbk");
            char[] filecontents = new char[filelength.intValue()];
            
            in.read(filecontents);      
            in.close();
                 

    	    String[] fileContentArr = new String(filecontents).split("\n");
    	    //String strsTEMP="samples";
    	    int j=0,k=0;
    	    for(int i = 0; i <fileContentArr.length; i++){
    	    	if((fileContentArr[i].split("\r")[0]).split("=")[0].trim().equalsIgnoreCase("samples")){
    	    		j=i;//记录samples的位置
    	    	}
    	    	else if((fileContentArr[i].split("\r")[0]).split("=")[0].trim().equalsIgnoreCase("map info")){
    	    		k=i;//记录map info的位置
    	    	} 	    	
    	    }  	    
			iHDR[1]=Float.parseFloat((fileContentArr[j].split("\r")[0]).split("=")[1].trim());//samples   		
            iHDR[2]=Float.parseFloat((fileContentArr[j+1].split("\r")[0]).split("=")[1].trim());//lines
            iHDR[3]=Float.parseFloat(fileContentArr[j+2].split("\r")[0].split("=")[1].trim());//bands
            iHDR[4]=Float.parseFloat(fileContentArr[j+3].split("\r")[0].split("=")[1].trim());//headerOffset
            iHDR[5]=Float.parseFloat(fileContentArr[k].split("\r")[0].split("\\{")[1].split("\\,")[3].trim());//map info: Lat1
            iHDR[6]=Float.parseFloat(fileContentArr[k].split("\r")[0].split("\\{")[1].split("\\,")[4].trim());//map info: Lon1
            iHDR[7]=Float.parseFloat(fileContentArr[k].split("\r")[0].split("\\{")[1].split("\\,")[5].trim());//map info: 间隔Lat
            iHDR[8]=Float.parseFloat(fileContentArr[k].split("\r")[0].split("\\{")[1].split("\\,")[6].trim());//map info: 间隔Lon
            
            iHDR[9]=Float.parseFloat(fileContentArr[j+5].split("\r")[0].split("\\=")[1].trim());//data type: 决定了几个字符合为一个数据值  		

            //System.out.println(iHDR);
            
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//系统强制解决的问题：文件没有找到
			e.printStackTrace();
		} catch (IOException e) {
			//文件读写异常
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return iHDR;
    }

}



