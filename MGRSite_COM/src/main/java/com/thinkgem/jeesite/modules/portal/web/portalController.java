/*
 * @(#)SchenController.java 2016-8-2
 * 
 * jeaw 版权所有2006~2015。
 */

package com.thinkgem.jeesite.modules.portal.web;

import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.portal.entity.MeetingDetailInfo;
import com.thinkgem.jeesite.modules.portal.entity.MeetingInfo;
import com.thinkgem.jeesite.modules.portal.service.MeetingDetailInfoManageService;
import com.thinkgem.jeesite.modules.portal.service.MeetingInfoManageService;
import com.thinkgem.jeesite.modules.portal.service.MeetingTypeService;
import com.thinkgem.jeesite.modules.sys.dao.ComparasDao;
import com.thinkgem.jeesite.modules.tvmeeting.service.DocDefService;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：
 *
 * @author S.c
 * @version 1.0 2016-8-2
 */
@Controller
@RequestMapping(value="portal/meeting")
public class portalController extends BaseController {

	@Autowired
	private MeetingTypeService meetingTypeService;
	@Autowired
	private MeetingInfoManageService meetingInfoManageService;
	@Autowired
	private MeetingDetailInfoManageService meetingDetailInfoService;
	@Autowired
	private DocDefService docDefService;
	@Autowired
	private ComparasDao comparasDao;
	
	@RequestMapping(value= "meetingtype")
	public String MeetingType(Model model){
		List<MeetingInfo> hylblist=meetingInfoManageService.showMeeting();
		model.addAttribute("hylbList", hylblist);//会议列表
		return "/modules/portal/MeetingInfo";
	}
	
	@RequestMapping(value="/meetingadd")
	public String meetingadd(Model model){
		model.addAttribute("flag", "add");
		model.addAttribute("tid",0);
		return "modules/portal/MeetingInfoAdd";
	}
	
	@RequestMapping(value="meetingsave")
	public String meetingsave(Model model,HttpServletRequest request,HttpServletResponse response) {
		model.addAttribute("flag", "add");
		//long id=Long.valueOf(request.getParameter("id"));
		
		//修改会议名
		String name=request.getParameter("name");
		//修改日期
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
//		System.out.println(time);
//		String times[]=time.split("至");
//		System.out.println(times.length);
//		String startDate=times[0];	
//		String endDate=times[1];
		//会议tid
		int tid=Integer.parseInt(request.getParameter("tid"));
		//修改会议地点
		String place=request.getParameter("place");
		//会议主持
		String host=request.getParameter("host");
		//System.out.println(host);
		//主办单位
		String organizer=request.getParameter("organizer");
		//参与人员
		String participants=request.getParameter("participants");
		//大会亮点
		String abstracts=request.getParameter("abstracts");
		//会议图片
		String thumbnail=request.getParameter("thumbnail");
		MeetingInfo meetingInfo=new MeetingInfo();
		meetingInfo.setTid(tid);
		meetingInfo.setName(name);
		meetingInfo.setHost(host);
		meetingInfo.setPlace(place);
		meetingInfo.setEndDate(endDate);
		meetingInfo.setStartDate(startDate);
		meetingInfo.setParticipants(participants);
		meetingInfo.setAbstracts(abstracts);
		meetingInfo.setOrganizer(organizer);
		meetingInfo.setThumbnail(thumbnail);
		meetingInfo.setInvalid(0);
		//System.out.println(meetingInfo);
		meetingInfoManageService.save(meetingInfo);
		List<MeetingInfo> hylblist=meetingInfoManageService.showMeeting();
		model.addAttribute("hylbList", hylblist);//会议列表
		return "/modules/portal/MeetingInfo";
	}
	
	@RequestMapping(value="meetingupdate")//点击修改按钮弹出JSP显示模块
	public String  meetingupdate(int id,Model model){
		MeetingInfo meetingInfo=meetingInfoManageService.getMeetingInfoById(id);
		long tid=meetingInfo.getTid();
		String name=meetingInfo.getName();
		String startDate=meetingInfo.getStartDate();
		String endDate=meetingInfo.getEndDate();
		String place=meetingInfo.getPlace();
		String host=meetingInfo.getHost();
		String organizer=meetingInfo.getOrganizer();
		String participants=meetingInfo.getParticipants();
		String abstracts=meetingInfo.getAbstracts();
		String thumbnail=meetingInfo.getThumbnail();
		model.addAttribute("flag", "update");
		model.addAttribute("tid",tid);
		model.addAttribute("name", name);
		model.addAttribute("id", id);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("place",place);
		model.addAttribute("host",host);
		model.addAttribute("organizer",organizer);
		model.addAttribute("participants",participants);
		model.addAttribute("abstracts",abstracts);
		model.addAttribute("fileName",thumbnail);
		String hyfl="";
		if(tid==1){
			hyfl="气象云专题";
		}else if(tid==2){
			hyfl="气象年会";
		}else if(tid==3){
			hyfl="CIMISS会议";
		}else if(tid==41){
			hyfl="业务会议";
		}
		model.addAttribute("hyfl",hyfl);
		return "modules/portal/MeetingInfoAdd";
	}
	
	@RequestMapping(value="update")
	public String update(Model model,HttpServletRequest request, HttpServletResponse response){	
		//System.out.println(name+"  "+id);
		//long id=Long.valueOf(request.getParameter("id"));
		int id=Integer.parseInt(request.getParameter("id"));
		//修改会议名
		String name=request.getParameter("name");
		//修改日期
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
//		String time=request.getParameter("time");
//		String times[]=time.split("至");
//		String startDate=times[0];
//		String endDate=times[1];
		//System.out.println("id:"+id+",name:"+name);
		//会议tid
		int tid=Integer.parseInt(request.getParameter("select"));
		//修改会议地点
		String place=request.getParameter("place");
		//会议主持
		String host=request.getParameter("host");
		//主办单位
		String organizer=request.getParameter("organizer");
		//参与人员
		String participants=request.getParameter("participants");
		//大会亮点
		String abstracts=request.getParameter("abstracts");
		//会议图片
		String thumbnail=request.getParameter("thumbnail");
		
		//meetingInfoManageService.update(name,id,tid,startDate,endDate,place,host,organizer,participants,abstracts,thumbnail);
		meetingInfoManageService.update(name,id,tid,startDate,endDate,place,host,organizer,participants,abstracts,thumbnail);
		List<MeetingInfo> hylblist=meetingInfoManageService.showMeeting();
		model.addAttribute("hylbList", hylblist);//会议列表
		return "/modules/portal/MeetingInfo";
	}
	
	@RequestMapping(value="meetingdelete")
	public String delete(int id,Model model){
		meetingInfoManageService.deleteById(id);
		List<MeetingInfo> hylblist=meetingInfoManageService.showMeeting();
		model.addAttribute("hylbList", hylblist);//会议列表
		return "/modules/portal/MeetingInfo";//
	}
	

	@RequestMapping(value= "showjcyt")
	public String showJcyt(Model model,HttpServletRequest request, HttpServletResponse response){
		int pid=Integer.parseInt(request.getParameter("id"));
		//System.out.println(pid);
		List<MeetingDetailInfo> jcytlist=meetingDetailInfoService.getMeetingDetailInfoByPid(pid);
		//System.out.println(jcytlist.size());
		model.addAttribute("jcytlist",jcytlist);
		model.addAttribute("id",pid);
		return "/modules/portal/MeetingDetailInfo";
	}
	
	@RequestMapping(value="deleteJcyt")
	public String deleteJcyt(Model model,HttpServletRequest request, HttpServletResponse response){
		int id=Integer.parseInt(request.getParameter("id"));//议题Id
		int pid=Integer.parseInt(request.getParameter("pid"));//议题Pid
		meetingDetailInfoService.deleteById(id);
		List<MeetingDetailInfo> jcytlist=meetingDetailInfoService.getMeetingDetailInfoByPid(pid);
		model.addAttribute("jcytlist",jcytlist);
		model.addAttribute("id",pid);
		return "/modules/portal/MeetingDetailInfo";
	
	}
	
	@RequestMapping(value="jcytupdate")//点击修改按钮弹出JSP显示模块
	public String  jcytupdate(int id,Model model){
		MeetingDetailInfo meetingDetailInfo=meetingDetailInfoService.getMeetingDetailInfoById(id);
		int pid=meetingDetailInfo.getPid();
		String startTime=meetingDetailInfo.getStartTime();
		String endTime=meetingDetailInfo.getEndTime();
		String date=meetingDetailInfo.getDate();
		String content=meetingDetailInfo.getContent();
		String spokesman=meetingDetailInfo.getSpokesman();
		String filename=meetingDetailInfo.getFileName();
		System.out.println(filename);
		String unit=meetingDetailInfo.getUnit();
		String time=meetingDetailInfo.getTime();
		model.addAttribute("id", id);
		model.addAttribute("pid", pid);
		model.addAttribute("time", time);
		model.addAttribute("flag", "update");
		model.addAttribute("starttime", startTime);
		model.addAttribute("endtime", endTime);
		model.addAttribute("date",date);
		model.addAttribute("content",content);
		model.addAttribute("spokesman",spokesman);
		model.addAttribute("filename",filename);
		model.addAttribute("unit",unit);
		return "modules/portal/MeetingDetailInfoAdd";
	}
	
	@RequestMapping(value="update2")
	public String update2(Model model,HttpServletRequest request, HttpServletResponse response){	
		
		int id=Integer.parseInt(request.getParameter("id"));
		int pid=Integer.parseInt(request.getParameter("pid"));
		//修改内容
		String content=request.getParameter("content");
		//修改时间
		
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		
//		String time=request.getParameter("time");
//		String times[]=time.split("至");
//		String starttime=times[0];
//		String endtime=times[1];
	
		//修改会议日期
		String date=request.getParameter("date");
		//会议发言人
		String spokesman=request.getParameter("spokesman");
		//主办单位
		String unit=request.getParameter("unit");
		//文件名称
		String filename=request.getParameter("filename");
		meetingDetailInfoService.update(id,date, unit, content, spokesman, filename,starttime,endtime);
		List<MeetingDetailInfo> jcytlist=meetingDetailInfoService.getMeetingDetailInfoByPid(pid);
		model.addAttribute("jcytlist",jcytlist);
		model.addAttribute("id", pid);
		return "/modules/portal/MeetingDetailInfo";
	}
	@RequestMapping(value="/jcytadd")
	public String jcytadd(Model model,HttpServletRequest request,HttpServletResponse response){
		model.addAttribute("flag", "add");
		String pid=request.getParameter("id");
		model.addAttribute("pid", pid);
		return "modules/portal/MeetingDetailInfoAdd";
	}
	@RequestMapping(value="jcytsave")
	public String jcytsave(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		model.addAttribute("flag", "add");
		//修改日期
//		String time=request.getParameter("time");
//		String times[]=time.split("-");
//		String startTime=times[0];	
//		String endTime=times[1];
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		//会议tid
		int pid=Integer.parseInt(request.getParameter("pid"));
		//修改会议地点
		String date=request.getParameter("date");
		//会议主持
		String content=request.getParameter("content");
		//System.out.println(host);
		//主办单位
		String spokesman=request.getParameter("spokesman");
		//参与人员
		String unit=request.getParameter("unit");
		//大会亮点
		String filename=request.getParameter("filename");
		MeetingDetailInfo meetingDetailInfo=new MeetingDetailInfo();
		meetingDetailInfo.setContent(content);
		meetingDetailInfo.setDate(date);
		meetingDetailInfo.setEndTime(endtime);
		meetingDetailInfo.setStartTime(starttime);
		meetingDetailInfo.setFileName(filename);
		meetingDetailInfo.setPid(pid);
		meetingDetailInfo.setUnit(unit);
		meetingDetailInfo.setSpokesman(spokesman);
		meetingDetailInfo.setInvalid(0);
		
		meetingDetailInfoService.save(meetingDetailInfo);
		List<MeetingDetailInfo> jcytlist=meetingDetailInfoService.getMeetingDetailInfoByPid(pid);
		model.addAttribute("jcytlist",jcytlist);
		model.addAttribute("id", pid);
		return "/modules/portal/MeetingDetailInfo";
	}
	
	//上传图片
		@RequestMapping(value="/upload")
		public String upload(MultipartFile file,RedirectAttributes redirectAttributes,
				HttpServletResponse response,HttpServletRequest request, Model model) throws Exception {
			//修改会议名
			String name=request.getParameter("name");
			//System.out.println("name"+name);
			//修改日期
//			String time=request.getParameter("time");
//			String times[]=time.split("至");
//			String startDate=times[0];
//			String endDate=times[1];
			
			String startDate=request.getParameter("startDate");
			int tid=Integer.parseInt(request.getParameter("select"));
			System.out.println(tid);
			//System.out.println(tid);
			String endDate=request.getParameter("endDate");
			//会议tid
			//System.out.println("tid"+request.getParameter("tid"));
			//int tid=Integer.parseInt(request.getParameter("tid"));

			
			//修改会议地点
			String place=request.getParameter("place");
			//会议主持
			String host=request.getParameter("host");
			//System.out.println(host);
			//主办单位
			String organizer=request.getParameter("organizer");
			//参与人员
			String participants=request.getParameter("participants");
			//大会亮点
			String abstracts=request.getParameter("abstracts");
			String fileName=file.getOriginalFilename();
			try {
				fileName = URLDecoder.decode(fileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//int num;
			//List<MeetingInfo> list=meetingInfoManageService.getMeetingInfoOneList(fileName);
			//System.out.println(title+list.size());
			//if(list.size()==0){
			//	num=0;//涓嶉噸澶�
			//}else{
			//	num=1;
			//}
			//model.addAttribute("num",num);
			model.addAttribute("fileName",fileName);
			//String last = OriginalFilename.substring(index + 1, OriginalFilename.length());
			//boolean flag = false;
			//String docId=UUID.randomUUID().toString();
			//long size=file.getSize();
			//System.out.println("size=========:"+size);
			String imgServerPath=(String)comparasDao.getComparasByKey("imgServerPath");
			String imgMeetingPath=(String)comparasDao.getComparasByKey("imgmeetingpath");
			imgMeetingPath=imgServerPath+imgMeetingPath;
			//String filenameqq= thumbnail + "." + last;// 文件名称
			File mulu=new File(imgMeetingPath);
			File dir=new File(mulu,fileName); 
			String pathString = "";
			if (!"".equals(imgMeetingPath) && null != imgMeetingPath) {
				// 上传文件不存在则创建文件夹			
				OutputStream fos = null;
				InputStream fis = null;
				String path = imgMeetingPath ;// 拼成上传路径
				File storeDirectory = new File(path);
				try {
					if (!storeDirectory.exists()){
						storeDirectory.mkdirs();
						}
					 if(!dir.exists()) {
							dir.createNewFile(); 
							}
					// 建立文件输出流
					pathString=path+fileName;
					fos = new FileOutputStream(pathString);
					// 建立文件上传流
					fis = file.getInputStream();
					byte[] buffer = new byte[8192];
					int len = 0;
					while ((len = fis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					//msg = "上传成功";
				} catch (IOException e) {
					e.printStackTrace();
					//msg = "文件上传失败！";
				} finally {
					fos.close();
					fis.close();
				}
			
				//}
				//MeetingInfo meetingInfo=meetingInfoManageService.getMeetingInfoById(id);
				//meetingInfo.setThumbnail(fileName);
			}
			String hyfl="";
			if(tid==1){
				hyfl="气象云专题";
			}else if(tid==2){
				hyfl="气象年会";
			}else if(tid==3){
				hyfl="CIMISS会议";
			}else if(tid==41){
				hyfl="业务会议";
			}
			String flag=request.getParameter("flag");
			if(flag.equals("update")){
				int id=Integer.parseInt(request.getParameter("id"));
				model.addAttribute("id", id);
				System.out.println(id);
				hyfl=request.getParameter("hyfl");
				model.addAttribute("hyfl",hyfl);
				System.out.println(hyfl);
			}
			model.addAttribute("flag", flag);
			model.addAttribute("hyfl",hyfl);
			model.addAttribute("tid",tid);
			model.addAttribute("name", name);
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
			model.addAttribute("place",place);
			model.addAttribute("host",host);
			model.addAttribute("organizer",organizer);
			model.addAttribute("participants",participants);
			model.addAttribute("abstracts",abstracts);
			return "modules/portal/MeetingInfoAdd";
			
			}
		
		@RequestMapping(value="/uploadDoc")
		public void uploadDoc(String uploadFileName, MultipartFile file,RedirectAttributes redirectAttributes,
				HttpServletResponse response,HttpServletRequest request) throws Exception {
			String html = "";
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> fliemap = multipartRequest.getFileMap();
			CommonsMultipartFile upload = (CommonsMultipartFile) fliemap.get("upload");
			String fn = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1, uploadFileName.length())
						.toLowerCase();
			String pdftoswfPath = request.getSession().getServletContext()
						.getRealPath("/");
			String path = request.getSession().getServletContext().getResource("").getPath();
			String temp= SpringContextHolder.getRootRealPath()+"/"+"temp"+"/";
			String docId = "";
			String pathString = "";
			docId = UUID.randomUUID().toString();
			String filename=docId+uploadFileName.substring(uploadFileName.lastIndexOf("."),
					uploadFileName.length());
			String msg = "";
			if ("doc".equals(fn) ||"pdf".equals(fn)) {
				OutputStream fos = null;
				InputStream fis = null;
				File file1 = new File(temp);
				try {
					if (!file1.exists()){
						file1.mkdirs();
						}
					// 建立文件输出流
					pathString=temp+filename;
					fos = new FileOutputStream(pathString);
					// 建立文件上传流
					fis = upload.getInputStream();
					byte[] buffer = new byte[8192];
					int len = 0;
					while ((len = fis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					msg = "上传成功";
				} catch (IOException e) {
					e.printStackTrace();
					msg = "文件上传失败！";
				} finally {
					close(fos, fis);
				}
			} else {
				msg = "只能上传.doc和.PDF文件！";
			}
			
			String context="";
			 if (msg.equals("上传成功")&&"doc".equals(fn)) {
				try {
					//String imageServerUrl=((String)comparasDao.getComparasByKey("imageServerUrl")).trim();
					context=doc2Html(docId,temp+filename);
					org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(context);
					Elements elementsBymeta = doc.getElementsByTag("meta");
					for (Element element : elementsBymeta) {
						element.remove();
					}
					Elements elementsByTag = doc.getElementsByTag("style");
					for (Element element : elementsByTag) {
						String html2 = element.html();
						String cssStr = "^([\\.\\#]?\\w+[^{]+\\{[^}]*\\}[\r\n]*)*";
						Pattern pattern = Pattern.compile(cssStr);  
						Matcher matcher = pattern.matcher(html2);
						boolean matches = matcher.matches();
						if(matches){
							System.out.println(true);
							int groupCount = matcher.groupCount();
							System.out.println(groupCount);
							for(int i=0;i<groupCount;i++){
								System.out.println(matcher.group(i));
							}
							String[] split = html2.split("}");
							System.out.println("split.length:"+split.length);
							for (String string : split) {
								String cssStr2 = "^[\r\n]*([\\.\\#]?\\w+[^{]+)\\{([^}]*)[\r\n]*";
								Pattern pattern2 = Pattern.compile(cssStr2);  
								Matcher matcher2 = pattern2.matcher(string);
								boolean matches2 = matcher2.matches();
								if(matches2){
									System.out.println(true);
									int groupCount2 = matcher.groupCount();
									System.out.println("groupCount2:"+groupCount2);
									for(int j=0;j<groupCount2;j++){
										String group1 = matcher2.group(1);
										String group2 = matcher2.group(2);
										if(group1.startsWith(".")){
											String substring = group1.substring(1, group1.length());
											Elements elementsByClass = doc.getElementsByClass(substring);
											for (Element element2 : elementsByClass) {
												element2.attr("style", group2);
											}
										}else if(group1.startsWith("#")){
											String substring = group1.substring(1, group1.length());
											Element elementById = doc.getElementById(substring);
											elementById.attr("style", group2);
										}else{
											Elements elementsByTag2 = doc.getElementsByTag(group1);
											for (Element element2 : elementsByTag2) {
												element2.attr("style", group2);
											}
										}
									}
									
								}else{
									System.out.println(false);
								}
							}
							
						}else{
							System.out.println(false);
						}
						element.remove();
					}
					Element body = doc.body();
					html = body.html();
					html = html.replaceAll("\r\n", "");
				} catch (TransformerException e) {
					msg = "doc转html失败";
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					msg = "doc转html失败";
					e.printStackTrace();
				}
			 }
			renderText(html.replaceAll("\r\n", ""), response);
		}
		public String doc2Html(final String id,String fileName) throws TransformerException, IOException, ParserConfigurationException {
			final String imageServerUrl=((String)comparasDao.getComparasByKey("imageServerUrl")).trim();
			//final imgServerPath=((String)comparasDao.getComparasByKey("imgServerPath")).trim(); 
			//final String noticeStaticPath=((String)comparasDao.getComparasByKey("noticeStaticPath")).trim();
			//final String noticePath=((String)comparasDao.getComparasByKey("noticePath")).trim();
			String temp=null;
			//if(imageServerUrl.contains("localhost")){
				temp= SpringContextHolder.getRootRealPath()+"/"+"temp"+"/";
			/*}else{
				temp=imageServerUrl+"/"+"temp"+"/";
			}*/
			File imageDir = new File(temp + id);
			if (!imageDir.isDirectory()) {
			    imageDir.mkdirs();
			}
			HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
			wordToHtmlConverter.setPicturesManager(new PicturesManager() {
				public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
					return imageServerUrl+"temp"+"/"+id+"/"+id+ suggestedName;
					//return "http://localhost:8080/mgrsite/temp"+"/"+id+"/" +id+ suggestedName;
				}
			});
			wordToHtmlConverter.processDocument(wordDocument);
			// 淇濆瓨鍥剧墖
			List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
			if (pics != null) {
				for (int i = 0; i < pics.size(); i++) {
					Picture pic = (Picture) pics.get(i);
					try {
						String filename = temp+id+"/"+id+pic.suggestFullFileName();
						//String filename = noticePath+id+pic.suggestFullFileName();
						pic.writeImageContent(new FileOutputStream(filename));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			Document htmlDocument = wordToHtmlConverter.getDocument();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(out);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.transform(domSource, streamResult);
			out.close();
			return new String(out.toByteArray());
		}
		
		
		 private void close(OutputStream fos, InputStream fis) {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						System.out.println("FileInputStream关闭失败");
						e.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						System.out.println("FileOutputStream关闭失败");
						e.printStackTrace();
					}
				}
			}
		
		
		@RequestMapping("/Crepeat")
		public void Crepeat(String fileName,HttpServletResponse response,HttpServletRequest request, Model model){
			try {
				fileName = URLDecoder.decode(fileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			int num;
			List<MeetingInfo> list=meetingInfoManageService.getMeetingInfoOneList(fileName);
			System.out.println(fileName);
			if(list.size()==0){
				num=0;//涓嶉噸澶�
			}else{
				num=1;
			}
			model.addAttribute("num",num);
			//return "modules/portal/MeetingDetailInfoAdd";
			renderText(num+"",response);
		}
		
		@RequestMapping("/Crepeatdetail")
		public void Crepeatdetail(String fileName,HttpServletResponse response,HttpServletRequest request, Model model){
			try {
				fileName = URLDecoder.decode(fileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			int num;
			List<MeetingDetailInfo> list=meetingDetailInfoService.getMeetingDetailInfoOneList(fileName);
			if(list.size()==0){
				num=0;//涓嶉噸澶�
			}else{
				num=1;
			}
			
			model.addAttribute("num",num);
			//return "modules/portal/MeetingDetailInfoAdd";
			renderText(num+"",response);
		}
		//上传文件
		@RequestMapping(value="/uploaddetail")
		public String uploaddetail(MultipartFile file,RedirectAttributes redirectAttributes,
				HttpServletResponse response,HttpServletRequest request, Model model) throws Exception {
			String flag=request.getParameter("flag");
//			System.out.println(flag);
			String starttime=request.getParameter("starttime");
			String endtime=request.getParameter("endtime");
			//会议Id
			if(flag.equals("update")){
				int id=Integer.parseInt(request.getParameter("id"));
				model.addAttribute("id", id);
			}
			
			//会议tid
			int pid=Integer.parseInt(request.getParameter("pid"));
			//修改会议地点
			String date=request.getParameter("date");
			//会议主持
			String content=request.getParameter("content");
			//主办单位
			String spokesman=request.getParameter("spokesman");
			//参与人员
			String unit=request.getParameter("unit");
			//大会亮点
			String fileName=file.getOriginalFilename();
//			try {
//				fileName = URLDecoder.decode(fileName, "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
			String imgServerPath=(String)comparasDao.getComparasByKey("imgServerPath");
			String fileMeetingPath=(String)comparasDao.getComparasByKey("filemeetingpath");
			fileMeetingPath=imgServerPath+fileMeetingPath;
			//String filenameqq= thumbnail + "." + last;// 文件名称
			File mulu=new File(fileMeetingPath);
			File dir=new File(mulu,fileName); 
			String pathString = "";
			if (!"".equals(fileMeetingPath) && null != fileMeetingPath) {
				// 上传文件不存在则创建文件夹			
				OutputStream fos = null;
				InputStream fis = null;
				String path = fileMeetingPath ;// 拼成上传路径
				File storeDirectory = new File(path);
				try {
					if (!storeDirectory.exists()){
						storeDirectory.mkdirs();
						}
					 if(!dir.exists()) {
							dir.createNewFile(); 
							}
					// 建立文件输出流
					pathString=path+fileName;
					fos = new FileOutputStream(pathString);
					// 建立文件上传流
					fis = file.getInputStream();
					byte[] buffer = new byte[8192];
					int len = 0;
					while ((len = fis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					//msg = "上传成功";
				} catch (IOException e) {
					e.printStackTrace();
					//msg = "文件上传失败！";
				} finally {
					fos.close();
					fis.close();
				}
			}
			
			
			model.addAttribute("flag", flag);
			model.addAttribute("pid", pid);
			model.addAttribute("starttime", starttime);
			model.addAttribute("endtime", endtime);
			model.addAttribute("date",date);
			model.addAttribute("content",content);
			model.addAttribute("starttime",starttime);
			model.addAttribute("endtime",endtime);
			model.addAttribute("spokesman",spokesman);
			model.addAttribute("filename",fileName);
			model.addAttribute("unit",unit);
			//model.addAttribute("flag","update");
			//System.out.println(unit);
			return "modules/portal/MeetingDetailInfoAdd";
			
			}
		
		
			public static void main(String[] args) {
			
				/*String html2=".b1{white-space-collapsing:preserve;}\r\n.b2{margin: 1.0in 1.25in 1.0in 1.25in;}\r\n.p1{text-align:justify;hyphenate:auto;font-family:Times New Roman;font-size:12pt;}\r\n.p2{text-align:center;hyphenate:auto;font-family:Times New Roman;font-size:12pt;}\r\n.p3{text-align:end;hyphenate:auto;font-family:Times New Roman;font-size:12pt;}\r\n.p4{margin-right:0.33333334in;text-align:start;hyphenate:auto;font-family:Times New Roman;font-size:12pt;}.s1{font-weight:bold;font-style:italic;}";
				//String html2=".b1{white-space-collapsing:preserve;}";
				html2 = html2.replaceAll("\r\n", "");
				String cssStr = "^([\\.\\#]?\\w+[^{]+\\{[^}]*\\})*";
				Pattern pattern = Pattern.compile(cssStr);  
				Matcher matcher = pattern.matcher(html2);
				boolean matches = matcher.matches();
				if(matches){
					System.out.println(true);
					int groupCount = matcher.groupCount();
					System.out.println(groupCount);
					for(int i=0;i<groupCount;i++){
						System.out.println(matcher.group(i));
					}
					String[] split = html2.split("}");
					System.out.println("split.length:"+split.length);
					for (String string : split) {
						String cssStr2 = "^([\\.\\#]?\\w+[^{]+)\\{([^}]*)";
						Pattern pattern2 = Pattern.compile(cssStr2);  
						Matcher matcher2 = pattern2.matcher(string);
						boolean matches2 = matcher2.matches();
						if(matches2){
							System.out.println(true);
							int groupCount2 = matcher.groupCount();
							System.out.println("groupCount2:"+groupCount2);
							for(int j=0;j<groupCount2;j++){
								String group1 = matcher2.group(1);
								String group2 = matcher2.group(2);
								if(group1.startsWith(".")){
									
								}else if(group1.startsWith("#")){
									
								}else{
									
								}
							}
							
						}else{
							System.out.println(false);
						}
					}
					
				}else{
					System.out.println(false);
				}*/
				
				
				URI uri = null;
				try {
					uri = SpringContextHolder.getApplicationContext().getResource("").getURI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(uri);
			}
		}








