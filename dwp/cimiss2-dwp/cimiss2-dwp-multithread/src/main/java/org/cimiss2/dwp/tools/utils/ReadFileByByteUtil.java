package org.cimiss2.dwp.tools.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ReadFileByByteUtil {
	public static byte[] getBytes(String filePath){

        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
	
	public static ByteBuffer getByteBufferByFile(String filePath)
	{
		byte[] bytes = getBytes(filePath);
		ByteBuffer bb = getByteBufferByFile(bytes);
		
		return bb;
	}
	
	public static ByteBuffer getByteBufferByFile(byte[] bytes)
	{
		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
		bb.put(bytes);
		bb.flip();
		return bb;
	}
}
