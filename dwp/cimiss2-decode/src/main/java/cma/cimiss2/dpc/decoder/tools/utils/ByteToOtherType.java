package cma.cimiss2.dpc.decoder.tools.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ByteToOtherType {
	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
	 * 
	 * @param value
	 *            要转换的int值
	 * @return byte数组
	 */
	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt2（）配套使用
	 * 
	 * @param value
	 * @return
	 */
	public static byte[] intToBytes2(int value) {
		byte[] src = new byte[4];
		src[0] = (byte) ((value >> 24) & 0xFF);
		src[1] = (byte) ((value >> 16) & 0xFF);
		src[2] = (byte) ((value >> 8) & 0xFF);
		src[3] = (byte) (value & 0xFF);
		return src;
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
	 * 
	 * @param src
	 *            byte数组
	 * @param offset
	 *            从数组的第offset位开始
	 * @return int数值
	 */
	public static int bytesToInt(byte[] src, int offset) {
		int value;
		value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
				| ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
		return value;
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
	 *
	 * @param src
	 *            byte数组
	 * @param offset
	 *            从数组的第offset位开始
	 * @return int数值
	 */
	public static int bytesToInt(byte[] src, int offset, int start) {
		int value;
		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			bytes[i] = src[i + start];
		}
		src = bytes;
		value = (int) ((src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
				| ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24));
		return value;
	}

	/**
	 * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
	 */
	public static int bytesToInt2(byte[] src, int offset) {
		int value;
		value = (int) (((src[offset] & 0xFF) << 24)
				| ((src[offset + 1] & 0xFF) << 16)
				| ((src[offset + 2] & 0xFF) << 8) | (src[offset + 3] & 0xFF));
		return value;
	}

	/**
	 * 把时间转换成14位字节数组
	 * 
	 * @param date
	 * @return
	 */
	public static byte[] convertDate(Date date) {
		byte[] dates = new byte[14];
		Calendar Cld = Calendar.getInstance();
		int YY = Cld.get(Calendar.YEAR);// 年
		int MM = Cld.get(Calendar.MONTH) + 1;// 月
		int DD = Cld.get(Calendar.DATE);// 日
		int HH = Cld.get(Calendar.HOUR_OF_DAY);// 时
		int mm = Cld.get(Calendar.MINUTE);// 分
		int SS = Cld.get(Calendar.SECOND);// 秒
		int MI = Cld.get(Calendar.MILLISECOND);// 毫秒
		// 由整型而来,因此格式不加0,如 2017/5/5-1:1:32:694
		byte[] yyByteArray = shortToByte((short) YY);
		System.arraycopy(yyByteArray, 0, dates, 0, 2);// 0-1

		byte[] mmByteArray = shortToByte((short) MM);
		System.arraycopy(mmByteArray, 0, dates, 2, 2);// 2-3

		byte[] ddByteArray = shortToByte((short) DD);
		System.arraycopy(ddByteArray, 0, dates, 4, 2);// 4-5

		byte[] hhByteArray = shortToByte((short) HH);
		System.arraycopy(hhByteArray, 0, dates, 6, 2);// 6-7

		byte[] minByteArray = shortToByte((short) mm);
		System.arraycopy(minByteArray, 0, dates, 8, 2);// 8-9

		byte[] ssByteArray = shortToByte((short) SS);
		System.arraycopy(ssByteArray, 0, dates, 10, 2);// 10-11

		byte[] misByteArray = shortToByte((short) MI);
		System.arraycopy(misByteArray, 0, dates, 12, 2);// 12-13
		return dates;
	}

	/**
	 * 注释：short到字节数组的转换！
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByte(short number) {
		int temp = number;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();
			// 将最低位保存在最低位
			temp = temp >> 8;// 向右移8位
		}
		return b;
	}

	/**
	 * 注释：字节数组到short的转换！
	 * 
	 * @param b
	 * @return
	 */
	public static short byteToShort(byte[] b) {
		short s = 0;
		short s0 = (short) (b[0] & 0xff);// 最低位
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}
	
	public static short byteToShort(byte b) {
		short s = 0;
		short s0 = (short) (b & 0xff);// 最低位
//		short s1 = (short) (b[1] & 0xff);
//		s1 <<= 8;
		s = (short) (s0);
		return s;
	}
	public static short byteToShort(byte b, byte b1) {
		short s = 0;
		short s0 = (short) (b & 0xff);// 最低位
		short s1 = (short) (b1 & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	/**
	 * 字节转换为浮点
	 * 
	 * @param b
	 *            字节（至少4个字节）
	 * @param index
	 *            开始位置
	 * @return
	 */
	public static float byteToFloat(byte[] b, int index) {
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}
	
	public static float byteToFloat(byte[] b)
	{
		ByteBuffer buf=ByteBuffer.allocateDirect(4); //无额外内存的直接缓存
		buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
		buf.put(b);
		buf.rewind();
		float f2=buf.getFloat();
		
		return f2;
	}

	/**
	 * 字节转换为浮点
	 * 
	 * @param b
	 *            字节（至少4个字节）
	 * @param index
	 *            开始位置
	 * @return
	 */
	public static float[] byteToFloatArray(byte[] b) {
		int count = b.length;
		float[] result = new float[count / 4];
		for (int i = 0; i < count; i = i + 4) {
			int l;
			l = b[i + 0];
			l &= 0xff;
			l |= ((long) b[i + 1] << 8);
			l &= 0xffff;
			l |= ((long) b[i + 2] << 16);
			l &= 0xffffff;
			l |= ((long) b[i + 3] << 24);
			result[i / 4] = Float.intBitsToFloat(l);
		}

		return result;
	}

	/**
	 * 浮点转换为字节
	 * 
	 * @param f
	 * @return
	 */
	public static byte[] float2Byte(float f) {

		// 把float转换为byte[]
		int fbit = Float.floatToIntBits(f);

		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (fbit >> (24 - i * 8));
		}

		// 翻转数组
		int len = b.length;
		// 建立一个与源数组元素类型相同的数组
		byte[] dest = new byte[len];
		// 为了防止修改源数组，将源数组拷贝一份副本
		System.arraycopy(b, 0, dest, 0, len);
		byte temp;
		// 将顺位第i个与倒数第i个交换
		for (int i = 0; i < len / 2; ++i) {
			temp = dest[i];
			dest[i] = dest[len - i - 1];
			dest[len - i - 1] = temp;
		}

		return dest;
	}

	public static String[] byteToStringArray(byte[] bytes, int length) {
		int count = bytes.length / length;
		String[] result = new String[count];
		for (int i = 0; i < count; i++) {
			result[i] = new String(Arrays.copyOfRange(bytes, i * length,
					(i + 1) * length));
		}

		return result;
	}

	public static byte[] charToByte(char c) {
		byte[] b = new byte[2];
		b[0] = (byte) ((c & 0xFF00) >> 8);
		b[1] = (byte) (c & 0xFF);

		return b;
	}

	public static char byteToChar(byte[] b) {
		int hi = (b[0] & 0xFF) << 8;
		int lo = b[1] & 0xFF;

		return (char) (hi | lo);
	}

	public static char[] byteToCharArray(byte[] bytes) {
		Charset cs = Charset.forName("GBK");
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes).flip();
		CharBuffer cb = cs.decode(bb);

		return cb.array();
	}

	// 从byte数组的index处的连续4个字节获得一个int
	public static int getInt(byte[] arr, int index) {
		return (0xff000000 & (arr[index + 0] << 24))
				| (0x00ff0000 & (arr[index + 1] << 16))
				| (0x0000ff00 & (arr[index + 2] << 8))
				| (0x000000ff & arr[index + 3]);
	}

	// 从byte数组的index处的连续4个字节获得一个float
	public static float getFloat(byte[] arr, int index) {
		return Float.intBitsToFloat(getInt(arr, index));
	}
}
