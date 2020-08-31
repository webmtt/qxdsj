package com.nmpiesat.idata.util;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;


public class Mail {

	public static boolean sendMail(String recipients, String subject, String html, String emailHost, String ip, String password,String port) {
		try {
			Properties prop = new Properties();
			prop.setProperty("mail.host", emailHost);
			prop.setProperty("mail.transport.protocol", "smtp");
			prop.setProperty("mail.smtp.auth", "true");
			prop.setProperty("mail.smtp.port",port);
			Session session = Session.getInstance(prop);
			//开启debug模式，方便看程序发送Email的运行状态
			session.setDebug(true);
			Transport transport = session.getTransport();
			transport.connect(emailHost, ip, password);
			
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

}
