package com.thinkgem.jeesite.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//import com.ultra.control.quartz.model.Order;

public class Mail {
	
	/*public static void main(String[] args) {
		// for(int i=0;i<100;i++){
		// Mail.sendMail("284484827@qq.com", i+"fdssa", i+"fdsadsa");
		// }
		Mail.sendMail(
				"vvyybb@qq.com",
				"关于中国气象科学数据共享服务网站域名更改的通知",
				"尊敬的用户： <br><br>&nbsp;&nbsp;&nbsp;&nbsp;您好！<br> &nbsp;&nbsp;&nbsp;&nbsp;根据中国气象局办公室对域名更改的要求，对中国气象科学数据共享服务网站（cdc.cma.gov.cn）的域名进行修改，新的域名为：中国气象科学数据共享服务网站（cdc.nmic.cn）。<br> &nbsp;&nbsp;&nbsp;&nbsp;新域名于2月11日正式生效，同时注销旧域名，旧域名不再提供服务。<br> &nbsp;&nbsp;&nbsp;&nbsp;给您带来的不便，敬请原谅！感谢您对我们的支持。<br><br> 国家气象信息中心<br>2015年2月11日");
	}*/
	
	/*public static boolean sendOrderMail(Order order,Integer exTime,String subject, String html,String smptFrom,String smptPasswd,String smptUser,Integer smtpPort,String smtpServer){
		String userName=order.getUsername();
		if(userName.contains("@")){
			int index=userName.indexOf("@");
			String userNameA=userName.substring(0,index);
			String htmlA=html.replace("%name%", userNameA).replace("%orderid%",order.getOrdercode()).replace("%ftppath%", order.getDownloadurl())
					.replace("%timeEmailSendCH%", timeZoneTurn(new Date())).replace("%timeCH%", timeZoneTurn(order.getFinishtime()))
					.replace("%life%", exTime.toString());
			String subjectA=subject.replace("%orderid%", order.getOrdercode());
			boolean flag=sendMail(order.getUsername(),subjectA,htmlA,smptFrom,smptPasswd,smptUser,smtpPort,smtpServer);
			//boolean flag=sendMail("284484827@qq.com",subjectA,htmlA);
			return flag;
		}
		return false;
	}*/
	
	public static boolean sendAlertMail(String errorInfo,String taskid,String errorCode,String recipientsStr,String smptFrom,String smptPasswd,String smptUser,Integer smtpPort,String smtpServer){
		boolean flag=sendMail(recipientsStr,"作业处理 子订单"+errorCode+"订单失败","作业号</br>"+taskid+"</br>错误信息</br>"+errorInfo,smptFrom,smptPasswd,smptUser,smtpPort,smtpServer);
		return false;
	}
	
	public static boolean sendFileAlertMail(String errorInfo,String taskid,String failedDateCode,String failedFileName,String recipientsStr,String smptFrom,String smptPasswd,String smptUser,Integer smtpPort,String smtpServer){
		boolean flag=sendMail(recipientsStr,"作业处理 订单项dataCode**"+failedDateCode+"**fileName**"+failedFileName+"订单项失败","作业号</br>"+taskid+"</br>错误信息</br>"+errorInfo,smptFrom,smptPasswd,smptUser,smtpPort,smtpServer);
		return false;
	}
	
	private static String timeZoneTurn(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		String snow = sdf.format(date); 
		return snow;
	}
	
	public static boolean validateEmail(String email){  
        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(email);  
        return matcher.matches(); 
    }
	
	public static boolean sendMail(String recipients, String subject, String html,String smptFrom,String smptPasswd,String smptUser,Integer smtpPort,String smtpServer) {
		if(validateEmail(recipients)){
			try {
				// 1.创建配置信息类
				Properties props = new Properties();
				//props.put("mail.smtp.port", 25);
				if(smtpPort==0){
					props.put("mail.smtp.port", 25);
				}else{
					props.put("mail.smtp.port", smtpPort);
				}
				props.put("mail.smtp.auth", "true");
				//props.put("mail.smtp.host", "smtp.163.com");
				//props.put("mail.smtp.host", "rays.cma.gov.cn");
				props.put("mail.smtp.host", smtpServer);
				
				// 2.创建连接
				Session session = Session.getDefaultInstance(props, null);
				session.setDebug(false); // 是否调试
				//session.setDebug(true);
				Transport transport = session.getTransport("smtp");
				//transport.connect("smtp.163.com", "", "");
				//transport.connect("rays.cma.gov.cn", "cdc@cma.gov.cn", "cdc123");
				transport.connect(smtpServer, smptUser, smptPasswd);
				
				// 3.设置邮件信息
				MimeMessage msg = new MimeMessage(session);
					// 主题
					msg.setSubject(subject);
					// 时间
					msg.setSentDate(new Date());
					// 发件人
					//msg.setFrom(new InternetAddress("cdc@cma.gov.cn"));
					msg.setFrom(new InternetAddress(smptFrom));
					
					// 收件人
					msg.setRecipients(Message.RecipientType.TO, recipients);
					
					msg.setContent(html, "text/html;charset=UTF-8"); // html
					
					msg.saveChanges();
					
				
				transport.sendMessage(msg, msg.getAllRecipients());
				
				
				// 4. 关闭Transport连接
				transport.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 发送邮件（带附件）
	 * @param recipients
	 * @param subject
	 * @param html
	 * @param smptFrom
	 * @param smptPasswd
	 * @param smptUser
	 * @param smtpPort
	 * @param smtpServer
	 * @param fileAr 文件全路径数组
	 * @return
	 */
//	public static boolean sendMail(String recipients, String subject, String html,String smptFrom,String smptPasswd,String smptUser,Integer smtpPort,String smtpServer,String[] fileAr) {
//		try {
//			// 1.创建配置信息类
//			Properties props = new Properties();
//			//props.put("mail.smtp.port", 25);
//			if(smtpPort==0){
//				props.put("mail.smtp.port", 25);
//			}else{
//				props.put("mail.smtp.port", smtpPort);
//			}
//			props.put("mail.smtp.auth", "true");
//			//props.put("mail.smtp.host", "smtp.163.com");
//			//props.put("mail.smtp.host", "rays.cma.gov.cn");
//			props.put("mail.smtp.host", smtpServer);
//
//			// 2.创建连接
//			Session session = Session.getDefaultInstance(props, null);
//			session.setDebug(false); // 是否调试
//			//session.setDebug(true);
//			Transport transport = session.getTransport("smtp");
//			//transport.connect("smtp.163.com", "", "");
//			//transport.connect("rays.cma.gov.cn", "cdc@cma.gov.cn", "cdc123");
//			transport.connect(smtpServer, smptUser, smptPasswd);
//
//			// 3.设置邮件信息
//			MimeMessage msg = new MimeMessage(session);
//				// 主题
//				msg.setSubject(subject);
//				// 时间
//				msg.setSentDate(new Date());
//				// 发件人
//				//msg.setFrom(new InternetAddress("cdc@cma.gov.cn"));
//				msg.setFrom(new InternetAddress(smptFrom));
//
//				// 收件人
//				msg.setRecipients(Message.RecipientType.TO, recipients);
//
//
//				// 构造Multipart
//				Multipart mp = new MimeMultipart();
//				// 向Multipart添加正文
//				MimeBodyPart mbpContent = new MimeBodyPart();
//				mbpContent.setContent(html, "text/html;charset=UTF-8");
//				// 向MimeMessage添加（Multipart代表正文）
//				mp.addBodyPart(mbpContent);
//				Vector file = new Vector();// 附件文件集合
//				for(String st:fileAr){
//					file.addElement(st);
//				}
//				// 向Multipart添加附件
//				Enumeration efile = file.elements();
//				while (efile.hasMoreElements()) {
//					MimeBodyPart mbpFile = new MimeBodyPart();
//					String filename = efile.nextElement().toString();
//					FileDataSource fds = new FileDataSource(filename);
//					mbpFile.setDataHandler(new DataHandler(fds));
//					//<span style="color: #ff0000;">//这个方法可以解决附件乱码问题。</span>
//					String filenameSt= new String(fds.getName().getBytes(),"ISO-8859-1");
//					mbpFile.setFileName(filenameSt);
//					// 向MimeMessage添加（Multipart代表附件）
//					mp.addBodyPart(mbpFile);
//				}
//				file.removeAllElements();
//				// 向Multipart添加MimeMessage
//				msg.setContent(mp);
//
//
//				//msg.setContent(html, "text/html;charset=UTF-8"); // html
//
//				msg.saveChanges();
//
//
//			transport.sendMessage(msg, msg.getAllRecipients());
//
//
//			// 4. 关闭Transport连接
//			transport.close();
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	public static boolean sendMail(String recipients, String subject, String html,String emailhost,String ip,String password,Integer port) {
		try {
			// 1.创建配置信息类
			Properties props = new Properties();
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", emailhost);
			
			// 2.创建连接
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(false); // 是否调试
			Transport transport = session.getTransport("smtp");
			transport.connect(emailhost, ip, password);
			
			// 3.设置邮件信息
			MimeMessage msg = new MimeMessage(session);
				// 主题
				msg.setSubject(subject);
				// 时间
				msg.setSentDate(new Date());
				// 发件人
				msg.setFrom(new InternetAddress(ip));
				
				// 收件人
				msg.setRecipients(Message.RecipientType.TO, recipients);
				
				msg.setContent(html, "text/html;charset=UTF-8"); // html
				
				msg.saveChanges();
				
			
			transport.sendMessage(msg, msg.getAllRecipients());
			
			
			// 4. 关闭Transport连接
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 发送邮件 返回区分失败原因
	 * @param recipients 收件人
	 * @param subject 邮件标题
	 * @param html 邮件内容
	 * @return 1发送成功 0由于10.0.66.45条数限制失败 -2其他原因失败
	 */
	public static int sendMailReturnFailInfo(String recipients, String subject, String html) {
		try {
			// 1.创建配置信息类
			Properties props = new Properties();
			props.put("mail.smtp.port", 25);
			
			props.put("mail.smtp.auth", "true");
			//props.put("mail.smtp.host", "123.125.50.138");
			props.put("mail.smtp.host", "10.0.66.45");
			
			// 2.创建连接
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(false); // 是否调试
			//session.setDebug(true);
			Transport transport = session.getTransport("smtp");
			//transport.connect("smtp.163.com", "cdc_nmic@163.com", "cdcnmic");
			//transport.connect("123.125.50.138", "cdc_nmic@163.com", "cdcnmic");
			transport.connect("10.0.66.45", "cdc@cma.gov.cn", "cdc123");
			
			// 3.设置邮件信息
			MimeMessage msg = new MimeMessage(session);
				// 主题
				msg.setSubject(subject);
				// 时间
				msg.setSentDate(new Date());
				// 发件人
				msg.setFrom(new InternetAddress("cdc@cma.gov.cn"));
				
				// 收件人
				msg.setRecipients(Message.RecipientType.TO, recipients);
				
				msg.setContent(html, "text/html;charset=UTF-8"); // html
				
				msg.saveChanges();
				
			
			transport.sendMessage(msg, msg.getAllRecipients());
			
			
			// 4. 关闭Transport连接
			transport.close();
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			String errorInfo=e.getMessage();
			if(errorInfo.contains("Could not connect to SMTP host:")||errorInfo.contains("554 sender is rejected:")){
				return 0;
			}else{
				return -2;
			}
		}
	}

}
