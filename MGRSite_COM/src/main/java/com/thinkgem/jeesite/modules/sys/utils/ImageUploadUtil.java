package com.thinkgem.jeesite.modules.sys.utils;

import com.jcraft.jsch.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ImageUploadUtil {

    private static Session session;
    private static ChannelSftp channelSftp;
    /**
     * 获取sftp连接
     * @param ftpaddress
     * @param ftpPassword
     * @param ftpUserName
     */
    public static ChannelSftp getSFTPClient(String ftpaddress, String ftpPassword,
                                            String ftpUserName,int ftpPort) {
        //开始时间  用于计时
        long startTime = System.currentTimeMillis();
        JSch jsch = new JSch();// 创建JSch对象
        Channel channel = null;
        try {
            //根据用户名，主机ip，端口获取一个Session对象
            session = jsch.getSession(ftpUserName, ftpaddress,ftpPort);
            session.setPassword(ftpPassword); // 设置密码
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config); // 为Session对象设置properties
            session.setTimeout(1500); // 设置timeout时间
            session.connect(); // 通过Session建立链接
            channel = session.openChannel("sftp"); // 打开SFTP通道
            channel.connect(); // 建立SFTP通道的连接
            long endTime = System.currentTimeMillis();
            System.out.println("连接sftp成功耗时" + (endTime - startTime) + "毫秒");
            return (ChannelSftp) channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * @Description //判断目录是否存在
     * @Param [directory, sftp]
     * @return boolean
     **/
    public static boolean isDirExist(String directory, ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            System.out.println("目录"+directory+"已存在");
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                System.out.println("目录"+directory+"不存在");
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }
    /**
     * @Description //创建目录
     * @Param [createpath, sftp]
     * @return void
     **/
    public static void createDir(String createpath, ChannelSftp sftp) {
        try {
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString(),sftp)) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    System.out.println("创建目录"+filePath.toString()+"成功");
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                    System.out.println("进入目录"+filePath.toString());
                }
            }
            sftp.cd(createpath);
        } catch (SftpException e) {
//            throw new SystemException("创建路径错误：" + createpath);
        }
    }
    /**
     * @Description //关闭链接资源
     * @Param []
     * @return void
     **/
    public static void close() {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        System.out.println("关闭连接资源");
    }
    /**
     * @Description //上传文件
     * @Param [host:上传主机IP, port:端口22, userName:登录名, password:密码
     *   remoteFile:上传路径,in:输入流格式文件, pathName:文件名称]
     * @return boolean
     **/
    public static boolean uploadFile(String host, int port, String userName, String password, String remoteFile, InputStream in, String pathName){
        try{
            //建立连接
            if (channelSftp == null || !channelSftp.isConnected()) {
                channelSftp=getSFTPClient(host,password,userName,port);
            }
            //判断文件夹是否存在，不存在则创建
            if (isDirExist(remoteFile,channelSftp)) {
                channelSftp.put(in, remoteFile + "/" +pathName);
                System.out.println("文件上传成功,文件路径"+remoteFile + "/" +pathName);
                close();
                return true;
            } else {
                //创建文件夹在上传文件
                createDir(remoteFile ,channelSftp);
                channelSftp.put(in, remoteFile + "/" +pathName);
                System.out.println("文件上传成功,文件路径"+remoteFile + "/" +pathName);
                close();
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
            close();
        }
        return false;

    }

    /**
     * 文件夹上传
     * @param host sftp地址
     * @param port sftp端口
     * @param userName 登录名
     * @param password 密码
     * @param file 上传的文件夹
     * @param remoteFile sftp服务器文件存放路径
     * @return true 成功,false 失败
     */
    public static boolean uploadDstFile(String host, int port, String userName, String password,
                                        MultipartFile[] file, String remoteFile){
        boolean b = false;
        //获取连接
        channelSftp = getSFTPClient(userName,password,host,port);
            b = uploadFiles(file,remoteFile);//循环迭代文件夹
            close();//关闭连接
        return b;
    }

    /**
     * 循环读取文件夹里面的文件上传，不支持文件夹里面嵌套文件夹上传
     * @param file  文件
     * @param remoteFile 服务器路径
     * @return
     */
    private static  boolean uploadFiles(MultipartFile[] file, String remoteFile) {
        try {
            for (int i = 0; i < file.length; i++) {
                InputStream in=null;
                in = file[i].getInputStream();
                if(i==0){//获取设置文件夹名称
                    remoteFile=remoteFile+"/"+file[i].getOriginalFilename().substring(0,file[i].getOriginalFilename().lastIndexOf("/"));
                }
                //获取设置文件名称
                String originalFilename = file[i].getOriginalFilename().substring(file[i].getOriginalFilename().lastIndexOf("/"));
                //判断文件夹是否存在，存在则直接上传
                if (isDirExist(remoteFile, channelSftp)) {
                    channelSftp.put(in, remoteFile+"/"+originalFilename);
                    System.out.println("文件上传成功,文件路径" + remoteFile+"/"+originalFilename);
                } else {
                    //创建文件夹在上传文件
                    createDir(remoteFile, channelSftp);
                    channelSftp.put(in,  remoteFile+"/"+originalFilename);
                    System.out.println("文件上传成功,文件路径"+ remoteFile+"/"+originalFilename);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }
        close();
        return true;
    }

    /*public static String ImageUpload(MultipartFile file, HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IllegalStateException, IOException{
        String savePaths = "";
        if (file != null) {// 判断上传的文件是否为空
            // String path="";// 文件路径
            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称
            //System.out.println("上传的文件原名称:"+fileName);
            // 判断文件类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            if (type != null) {// 判断文件类型是否为空
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    String realPath = "http://121.36.14.224:8083/pic/resource/images/";
                    // 自定义的文件名称
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + fileName;//15312196403485.jpg
                    // 设置存放图片文件的路径
                    String path = realPath +*//*System.getProperty("file.separator")+*//*trueFileName;

                    // 转存文件到指定的路径
                    file.transferTo(new File(path));
                    savePaths = path;
                } else {
                    System.out.println("不是我们想要的文件类型,请按要求重新上传");
                    return null;
                }
            } else {
                System.out.println("文件类型为空");
                return null;
            }
        } else {
            System.out.println("没有找到相对应的文件");
            return null;
        }
        return savePaths;
    }*/

}
