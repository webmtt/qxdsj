package com.thinkgem.jeesite.modules.access.utils;


//@Component("scheduledTaskManager")
public class CountIPNumTask {
//	protected Logger logger = LoggerFactory.getLogger(getClass());
//
//	@Autowired
//	private AccessFunConfigService accessFunConfigService;
//	@Autowired
//	private AccessIPService accessipservice;
//	@Autowired
//	private AccessFuncInfoService accessfuncinfoservice;
//	@Autowired
//	private AccessDataDao accessDataDao;
//	
//	 /*
//     * 全部都是1小时后过期
//     * */
//	@Transactional
//	public void countIPNum() {
//		logger.info("IP统计缓存开始：  "+new Date());
//		putCacheIPNum();
//		logger.info("IP统计缓存完成: "+new Date()+"当前今日IP"+(Integer) CacheMapUtil.getCache("todayIPNum", 0));
//	}
//	
//	 /*
//     * 全部都是1小时后过期
//     * */
//	@Transactional
//	public void countPVNum() {
//		//今天的pv
//		int todayPVNum = accessfuncinfoservice.getStatisticPVNum(1);
//		CacheMapUtil.putCache("todayPVNum", todayPVNum);
//	}
//	
//	
//     public void putCacheIPNum(){
//		//每个栏目今日IP
//		List<AccessFunConfig> itemList = accessFunConfigService.findFirstItems();
//		Map<String, Integer> dataMap = new HashMap();
//		Map IPNumMap = CacheMapUtil.getCacheMap();
//		int[] day=new int[]{0,-1,-7,-30};
//		String menuId ="";
//		dataMap=accessipservice.countIPNum(0,menuId);
//		IPNumMap.put("IP"+day[0]+menuId, dataMap);
//		for(int i=0;i<itemList.size();i++){
//			menuId=itemList.get(i).getFuncItemId();
//			dataMap=accessipservice.countIPNum(0,menuId);
//			IPNumMap.put("IP"+day[0]+menuId, dataMap);
//		}
//		CacheMapUtil.putCache("IPcountNum"+day[0], IPNumMap);
//		//今日ip
//		Integer todayIPNum=0;
//		todayIPNum = accessipservice.count(0);
//		CacheMapUtil.putCache("todayIPNum", todayIPNum);
//		
//		//今天的下载量
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();
//		String todayDownNum = accessipservice.downLoadSize( sdf.format(date));
//		CacheMapUtil.putCache("todayDownNum", todayDownNum);
//	}
//     public void saveAccessDateData(int num){
//    	 Date ndate=new Date();
// 		Calendar c=Calendar.getInstance();
// 		c.setTime(ndate);
// 		c.add(Calendar.DAY_OF_MONTH, -1);
// 		Date yesterday=c.getTime();
// 		SimpleDateFormat sdfn=new SimpleDateFormat("yyyyMMdd");
// 		accessDataDao.saveYesterdayIpNum(sdfn.format(yesterday), num);
//     }
//     
//     /*
//      * 全部都是48小时后过期
//      * */
//     @Transactional
//     public void putLastCacheIPNum(){
//    	//每个栏目昨日，一周，一月IP
//    	 logger.info("昨日，一周，一月IP统计缓存开始：  "+new Date());
//    	 List<AccessFunConfig> itemList = accessFunConfigService.findFirstItems();
// 		
// 		Map IPNumMap = CacheMapUtil.getCacheMap();
// 		int[] day=new int[]{-1,-7,-30};		
// 		for(int d=0;d<day.length;d++){
// 			String menuId ="";
// 			Map<String, Integer> dataMap = new HashMap();
//	 		dataMap=accessipservice.countIPNum(day[d],menuId);
//	 		IPNumMap.put("IP"+day[d]+menuId, dataMap);
//	 		
//		 		for(int i=0;i<itemList.size();i++){
//		 			menuId=itemList.get(i).getFuncItemId();
//		 			dataMap=accessipservice.countIPNum(day[d],menuId);
//		 			IPNumMap.put("IP"+day[d]+menuId, dataMap);
//		 		}
//	 		CacheMapUtil.putCache("IPcountNum"+day[d], IPNumMap);
// 		}
//    	 //昨日，一周，一月IP,总的
//    	 int yesterdayIPNum = 0;
//    	 int lastWeekIPNum = 0;
//    	 int lastMonthIPNum = 0;
//    	 int totalIPNum = 0;
//    	 yesterdayIPNum = accessipservice.count(-1);
//		 CacheMapUtil.putCache("yesterdayIPNum", yesterdayIPNum);
//		
//		 lastWeekIPNum = accessipservice.count(-7);
//		 CacheMapUtil.putCache("lastWeekIPNum", lastWeekIPNum);
//		 lastMonthIPNum = accessipservice.count(-30);
//		 CacheMapUtil.putCache("lastMonthIPNum", lastMonthIPNum);
//		 totalIPNum = accessipservice.count(1);
//		 CacheMapUtil.putCache("totalIPNum", totalIPNum);
//		 logger.info("昨日，一周，一月，总计IP统计缓存结束：  "+new Date());
//		 
//		 //昨天下载量
//		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();
//		Calendar calendar = Calendar.getInstance();
//		 calendar.setTime(date);
//		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		String yesterdayDownNum = accessipservice.downLoadSize(sdf.format(calendar.getTime()));
//		CacheMapUtil.putCache("yesterdayDownNum", yesterdayDownNum);
//		 
//		//一周的下载量
//		calendar.setTime(date);
//		calendar.add(Calendar.DAY_OF_MONTH, -7);
//		String lastWeekDownNum = accessipservice.downLoadSize(sdf.format(calendar.getTime()),sdf.format(date));
//		CacheMapUtil.putCache("lastWeekDownNum", lastWeekDownNum);
//				
//		//一月的下载量
//		calendar.setTime(date);
//		calendar.add(Calendar.DAY_OF_MONTH, -30);
//		String lastMonthDownNum = accessipservice.downLoadSize(sdf.format(calendar.getTime()),sdf.format(date));
//		CacheMapUtil.putCache("lastMonthDownNum", lastMonthDownNum);
//		
//		//总的下载量
//		calendar.setTime(date);
//		calendar.add(Calendar.DAY_OF_MONTH, -1000);
//		String allDownNum = accessipservice.downLoadSize(sdf.format(calendar.getTime()),sdf.format(date));
//		CacheMapUtil.putCache("allDownNum", allDownNum);
//		
//		int yesterdayPVNum = accessfuncinfoservice.getStatisticPVNum(2);
//		CacheMapUtil.putCache("yesterdayPVNum", yesterdayPVNum);
//		
//		int lastWeekPVNum = accessfuncinfoservice.getStatisticPVNum(3);
//		CacheMapUtil.putCache("lastWeekPVNum", lastWeekPVNum);
//		
//		int lastMonthPVNum = accessfuncinfoservice.getStatisticPVNum(4);
//		CacheMapUtil.putCache("lastMonthPVNum", lastMonthPVNum);
//		
//		int totalPVNum = accessfuncinfoservice.getStatisticPVNum(5);
//		CacheMapUtil.putCache("totalPVNum", totalPVNum);
//		
//		saveAccessDateData(yesterdayIPNum);//昨天的访问量保存到表中
//     }
}