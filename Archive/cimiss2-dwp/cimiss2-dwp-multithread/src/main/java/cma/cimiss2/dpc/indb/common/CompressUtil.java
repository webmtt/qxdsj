package cma.cimiss2.dpc.indb.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CompressUtil {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static void createDirectory(String outputDir, String subDir){
		File file = new File(outputDir);
		if(!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + File.separator + subDir);
        }
        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
	}
	
	
	/**
     * 解压缩tar文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     * @param delete 解压后是否删除原压缩包文件
     */
    public static void decompressTar(File file, String targetPath, boolean delete){
        FileInputStream fis = null;
        OutputStream fos = null;
        TarInputStream tarInputStream = null;
        try {
            fis = new FileInputStream(file);
            tarInputStream = new TarInputStream(fis, 1024 * 2);
            // 创建输出目录
            createDirectory(targetPath, null);

            TarEntry entry = null;
            while(true){
                entry = tarInputStream.getNextEntry();
                if( entry == null){
                    break;
                }
                if(entry.isDirectory()){
                    createDirectory(targetPath, entry.getName()); // 创建子目录
                }else{
                    fos = new FileOutputStream(new File(targetPath + File.separator + entry.getName()));
                    int count;
                    byte data[] = new byte[2048];
                    while ((count = tarInputStream.read(data)) != -1) {
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                }
            }
        } catch (IOException e) {
           e.printStackTrace();
        }finally {
            try {
                if(fis != null){
                    fis.close();
                }
                if(fos != null){
                    fos.close();
                }
                if(tarInputStream != null){
                    tarInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	
	/**
     * 解压缩bz2文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     * @param delete 解压后是否删除原压缩包文件
     */
    public static String decompressBZ2(File file, String targetPath, boolean delete){
    	String filename = null;
        FileInputStream fis = null;
        OutputStream fos = null;
        BZip2CompressorInputStream gzbis = null;
        String suffix = ".bz2";
        try {
            fis = new FileInputStream(file);
            gzbis = new BZip2CompressorInputStream(fis);
            BufferedInputStream bis = new BufferedInputStream(gzbis);
            // 创建输出目录
            createDirectory(targetPath, null);
            File tempFile = new File(targetPath + File.separator + file.getName().replace(suffix, ""));
            
            fos = new FileOutputStream(tempFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            int count;
            byte data[] = new byte[1024 * 100];
            while ((count = bis.read(data)) != -1) {
                bos.write(data, 0, count);
            }
            bos.flush();
            bos.close();
            bis.close();
            filename = tempFile.getAbsolutePath();
        } catch (IOException e) {
        	infoLogger.error(""+e);
            e.printStackTrace();
            
        }
        return filename;
    }
	
	
	/**
     * 解压缩tar.bz2文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     * @param delete 解压后是否删除原压缩包文件
     */
    public static void decompressTarBz2(File file, String targetPath, boolean delete){
        FileInputStream fis = null;
        OutputStream fos = null;
        BZip2CompressorInputStream bis = null;
        TarInputStream tis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BZip2CompressorInputStream(fis);
            tis = new TarInputStream(bis, 1024 * 2);
            // 创建输出目录
            createDirectory(targetPath, null);
            TarEntry entry;
            while((entry = tis.getNextEntry()) != null){
                if(entry.isDirectory()){
                    createDirectory(targetPath, entry.getName()); // 创建子目录
                }else{
                    fos = new FileOutputStream(new File(targetPath + File.separator + entry.getName()));
                    int count;
                    byte data[] = new byte[2048];
                    while ((count = tis.read(data)) != -1) {
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fis != null){
                    fis.close();
                }
                if(fos != null){
                    fos.close();
                }
                if(bis != null){
                    bis.close();
                }
                if(tis != null){
                    tis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	
	/**
     * 解压缩tar.gz文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     * @param delete 解压后是否删除原压缩包文件
     */
    public static void decompressTarGz(File file, String targetPath,  boolean delete){
        FileInputStream  fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        GZIPInputStream gzipIn = null;
        TarInputStream tarIn = null;
        OutputStream out = null;
        try {
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            gzipIn = new GZIPInputStream(bufferedInputStream);
            tarIn = new TarInputStream(gzipIn, 1024 * 2);

            // 创建输出目录
            createDirectory(targetPath, null);

            TarEntry entry = null;
            while((entry = tarIn.getNextEntry()) != null){
                if(entry.isDirectory()){ // 是目录
                    createDirectory(targetPath, entry.getName()); // 创建子目录
                }else{ // 是文件
                    File tempFIle = new File(targetPath + File.separator + entry.getName());
                    createDirectory(tempFIle.getParent() + File.separator, null);
                    out = new FileOutputStream(tempFIle);
                    int len =0;
                    byte[] b = new byte[2048];

                    while ((len = tarIn.read(b)) != -1){
                        out.write(b, 0, len);
                    }
                    out.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out != null){
                    out.close();
                }
                if(tarIn != null){
                    tarIn.close();
                }
                if(gzipIn != null){
                    gzipIn.close();
                }
                if(bufferedInputStream != null){
                    bufferedInputStream.close();
                }
                if(fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	/**
	   *  解压缩gz文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     * @param delete 解压后是否删除原压缩包文件
     */
    public static void decompressGz(File file, String targetPath,  boolean delete){
        FileInputStream  fileInputStream = null;
        GZIPInputStream gzipIn = null;
        OutputStream out = null;
        String suffix = ".gz";
        try {
            fileInputStream = new FileInputStream(file);
            gzipIn = new GZIPInputStream(fileInputStream);
            // 创建输出目录
            createDirectory(targetPath, null);

            File tempFile = new File(targetPath + File.separator + file.getName().replace(suffix, ""));
            out = new FileOutputStream(tempFile);
            int count;
            byte data[] = new byte[2048];
            while ((count = gzipIn.read(data)) != -1) {
                out.write(data, 0, count);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out != null){
                    out.close();
                }
                if(gzipIn != null){
                    gzipIn.close();
                }
                if(fileInputStream != null){
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
	/**
     * 解压缩7z文件
     * @param file 压缩包文件
     * @param targetPath 目标文件夹
     * @param delete 解压后是否删除原压缩包文件
     */
    public static void decompress7Z(File file, String targetPath,  boolean delete){
        SevenZFile sevenZFile = null;
        OutputStream outputStream = null;
        try {
            sevenZFile = new SevenZFile(file);
            // 创建输出目录
            createDirectory(targetPath, null);
            SevenZArchiveEntry entry;

            while((entry = sevenZFile.getNextEntry()) != null){
                if(entry.isDirectory()){
                    createDirectory(targetPath, entry.getName()); // 创建子目录
                }else{
                    outputStream = new FileOutputStream(new File(targetPath + File.separator + entry.getName()));
                    int len = 0;
                    byte[] b = new byte[2048];
                    while((len = sevenZFile.read(b)) != -1){
                        outputStream.write(b, 0, len);
                    }
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(sevenZFile != null){
                    sevenZFile.close();
                }
                if(outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
}
