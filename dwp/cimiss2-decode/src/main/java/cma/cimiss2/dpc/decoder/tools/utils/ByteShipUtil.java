
package cma.cimiss2.dpc.decoder.tools.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Optional;


public class ByteShipUtil{
	
	public static<T> Optional<byte[]> objectToBytes(T obj){
        byte[] bytes = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream sOut;
        try {
            sOut = new ObjectOutputStream(out);
            sOut.writeObject(obj);
            sOut.flush();
            bytes= out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(bytes);
    }
	/**
     * 对象转Byte数组
     * @param obj
     * @return
     */
    public static byte[] objectToByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
        	System.out.println("objectToByteArray failed, " + e);
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                	System.out.println("close objectOutputStream failed, " + e);
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                	System.out.println("close byteArrayOutputStream failed, " + e);
                }
            }
        }
        return bytes;
    }

    /**
     * Byte数组转对象
     * @param bytes
     * @return
     */
    public static Object byteArrayToObject(byte[] bytes) {
        Object obj = null;
        ByteArrayInputStream byteArrayInputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            obj = objectInputStream.readObject();
        } catch (Exception e) {
            System.out.println("byteArrayToObject failed, " + e);
        } finally {
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                	System.out.println("close byteArrayInputStream failed, " + e);
                }
            }
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                	System.out.println("close objectInputStream failed, " + e);
                }
            }
        }
        return obj;
    }
	/**
	 * long类型转成byte数组  
	 * @param number long型数值
	 * @return 
	 */
	public static byte[] longToBytes(long number) {  
	        long temp = number;  
	        byte[] b = new byte[8];  
	        for (int i = 0; i < b.length; i++) {  
	            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位  
	        temp = temp >> 8; // 向右移8位  
	    }  
	    return b;  
	}  
	/**
	 * byte数组转成long 
	 * @param b
	 * @return
	 */
	public static long bytesToLong(byte[] b,boolean isLittleEndian) {  
	  
	    long s = 0;  
	    if(isLittleEndian)
		{
		    long s0 = b[0] & 0xff;// 最低位  
		    long s1 = b[1] & 0xff;  
		    long s2 = b[2] & 0xff;  
		    long s3 = b[3] & 0xff;  
		    long s4 = b[4] & 0xff;// 最低位  
		    long s5 = b[5] & 0xff;  
		    long s6 = b[6] & 0xff;  
		    long s7 = b[7] & 0xff;  
		  
		    // s0不变  
		    s1 <<= 8;  
		    s2 <<= 16;  
		    s3 <<= 24;  
		    s4 <<= 8 * 4;  
		    s5 <<= 8 * 5;  
		    s6 <<= 8 * 6;  
		    s7 <<= 8 * 7;  
		    s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;  
		}
	    else{
	    	long s0 = b[7] & 0xff;// 最低位  
		    long s1 = b[6] & 0xff;  
		    long s2 = b[5] & 0xff;  
		    long s3 = b[4] & 0xff;  
		    long s4 = b[3] & 0xff;// 最低位  
		    long s5 = b[2] & 0xff;  
		    long s6 = b[1] & 0xff;  
		    long s7 = b[0] & 0xff;  
		  
		    // s0不变  
		    s1 <<= 8;  
		    s2 <<= 16;  
		    s3 <<= 24;  
		    s4 <<= 8 * 4;  
		    s5 <<= 8 * 5;  
		    s6 <<= 8 * 6;  
		    s7 <<= 8 * 7;  
		    s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
	    }
	    return s;  
	}  
	public static byte[] shortToBytes(int number) {  
	      
	    int temp = number;  
	    byte[] b = new byte[2];  
	  
	    for (int i = 0; i < b.length; i++) {  
	        b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位  
	        temp = temp >> 8; // 向右移8位  
	    }  
	    return b;  
	}  
	public static int bytesToShort(byte[] b,boolean isLittleEndian) { 
		int s = 0;  
		if(isLittleEndian)
		{
		    int s0 = b[0] & 0xff;// 最低位  
		    int s1 = b[1] & 0xff;   
		      
		    s1 <<= 8;  
		    s = s0 | s1;  
		}
		else {
			int s0 = b[1] & 0xff;// 最低位  
		    int s1 = b[0] & 0xff;  
		
		    s1 <<= 8;  
		    s = s0 | s1;  
		}
	    return s;  
	}  
	public static byte[] intToBytes(int number) {  
	      
	    int temp = number;  
	    byte[] b = new byte[4];  
	  
	    for (int i = 0; i < b.length; i++) {  
	        b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位  
	        temp = temp >> 8; // 向右移8位  
	    }  
	    return b;  
	}  
	  
	public static int bytesToInt(byte[] b,boolean isLittleEndian) { 
		int s = 0;  
		if(isLittleEndian)
		{
		    int s0 = b[0] & 0xff;// 最低位  
		    int s1 = b[1] & 0xff;  
		    int s2 = b[2] & 0xff;  
		    int s3 = b[3] & 0xff;  
		    
		    s3 <<= 24;  
		    s2 <<= 16;  
		    s1 <<= 8;  
		    s = s0 | s1 | s2 | s3;  
		}
		else {
			int s0 = b[3] & 0xff;// 最低位  
		    int s1 = b[2] & 0xff;  
		    int s2 = b[1] & 0xff;  
		    int s3 = b[0] & 0xff;  
		    
		    s3 <<= 24;  
		    s2 <<= 16;  
		    s1 <<= 8;  
		    s = s0 | s1 | s2 | s3;  
		}
	    return s;  
	}  
	//浮点到字节转换  
	public static byte[] floatToBytes(double d)  
	{  
	    byte writeBuffer[]= new byte[4];  
	     long v = Double.doubleToLongBits(d);   
	        writeBuffer[0] = (byte)(v >>> 24);  
	        writeBuffer[1] = (byte)(v >>> 16);  
	        writeBuffer[2] = (byte)(v >>>  8);  
	        writeBuffer[3] = (byte)(v >>>  0);  
	        return writeBuffer;  
	  
	}  
	  
	//字节到浮点转换  
	public static float bytesToFloat(byte[] readBuffer,boolean isLittleEndian)  
	{  
		if(isLittleEndian)
		{
			return Float.intBitsToFloat((
	                ((int)(readBuffer[3] & 255) << 24) +  
	                ((readBuffer[2] & 255) << 16) +  
	                ((readBuffer[1] & 255) <<  8) +  
	                ((readBuffer[0] & 255) <<  0)) 
	          );  
		}
		else{
		     return Float.intBitsToFloat((
		                ((int)(readBuffer[0] & 255) << 24) +  
		                ((readBuffer[1] & 255) << 16) +  
		                ((readBuffer[2] & 255) <<  8) +  
		                ((readBuffer[3] & 255) <<  0))  
		          );  
		}
	}  
	//浮点到字节转换  
	public static byte[] doubleToBytes(double d)  
	{  
	    byte writeBuffer[]= new byte[8];  
	     long v = Double.doubleToLongBits(d);  
	        writeBuffer[0] = (byte)(v >>> 56);  
	        writeBuffer[1] = (byte)(v >>> 48);  
	        writeBuffer[2] = (byte)(v >>> 40);  
	        writeBuffer[3] = (byte)(v >>> 32);  
	        writeBuffer[4] = (byte)(v >>> 24);  
	        writeBuffer[5] = (byte)(v >>> 16);  
	        writeBuffer[6] = (byte)(v >>>  8);  
	        writeBuffer[7] = (byte)(v >>>  0);  
	        return writeBuffer;  
	  
	}  
	  
	//字节到浮点转换  
	public static double bytesToDouble(byte[] readBuffer,boolean isLittleEndian)  
	{  
		if(isLittleEndian)
		{
			return Double.longBitsToDouble((((long)readBuffer[7] << 56) +  
	                ((long)(readBuffer[6] & 255) << 48) +  
	                ((long)(readBuffer[5] & 255) << 40) +  
	                ((long)(readBuffer[4] & 255) << 32) +  
	                ((long)(readBuffer[3] & 255) << 24) +  
	                ((readBuffer[2] & 255) << 16) +  
	                ((readBuffer[1] & 255) <<  8) +  
	                ((readBuffer[0] & 255) <<  0))  
	          );  
		}
		else{
		     return Double.longBitsToDouble((((long)readBuffer[0] << 56) +  
		                ((long)(readBuffer[1] & 255) << 48) +  
		                ((long)(readBuffer[2] & 255) << 40) +  
		                ((long)(readBuffer[3] & 255) << 32) +  
		                ((long)(readBuffer[4] & 255) << 24) +  
		                ((readBuffer[5] & 255) << 16) +  
		                ((readBuffer[6] & 255) <<  8) +  
		                ((readBuffer[7] & 255) <<  0))  
		          );  
		}
	}  
	public static String byteToString(ByteBuffer buffer){
		CharBuffer charBuffer = null;  
		try {  
			Charset charset = Charset.forName("UTF-8");  
			CharsetDecoder decoder = charset.newDecoder();  
			charBuffer = decoder.decode(buffer);  
			buffer.flip();  
			return charBuffer.toString();  
		} catch (Exception ex) {  
		ex.printStackTrace();  
		return null;  
		}  
	}
	
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static ByteBuffer bytesToByteBuffer(byte[] bytes){
		if(bytes==null || bytes.length==0)return null;
		return ByteBuffer.wrap(bytes);
	}
}
