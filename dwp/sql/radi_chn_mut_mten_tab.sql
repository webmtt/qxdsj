CREATE TABLE `radi_chn_mut_mten_tab`  (
  `D_RECORD_ID` varchar(20) NOT NULL COMMENT '记录标识',
  `V01301` varchar(30)  NULL DEFAULT NULL COMMENT '区站号',
  `V05001` decimal(10, 4) NULL DEFAULT NULL COMMENT '纬度',
  `V06001` decimal(10, 4) NULL DEFAULT NULL COMMENT '经度',
  `V07001` decimal(10, 4) NULL DEFAULT NULL COMMENT '拔海高度',
  `V04002` decimal(2, 0) NULL DEFAULT NULL COMMENT '月',
  `V04290` decimal(2, 0) NULL DEFAULT NULL COMMENT '旬',
  `V14320` decimal(10, 4) NULL DEFAULT NULL COMMENT '总辐射曝辐量',
  `V14308` decimal(10, 4) NULL DEFAULT NULL COMMENT '净全辐射曝辐量',
  `V14309` decimal(10, 4) NULL DEFAULT NULL COMMENT '散射辐射曝辐量',
  `V14302` decimal(10, 4) NULL DEFAULT NULL COMMENT '水平面直接辐射曝辐量',
  `V14306` decimal(10, 4) NULL DEFAULT NULL COMMENT '反射辐射曝辐量',
  `V143_st` decimal(10, 4) NULL DEFAULT NULL COMMENT '垂直面直接辐射曝辐量',
  `V14027` decimal(10, 4) NULL DEFAULT NULL COMMENT '反射比',
  `V14320_1` decimal(6, 0) NULL DEFAULT NULL COMMENT '总辐射最大辐照度',
  `V14320_stime` decimal(6, 0) NULL DEFAULT NULL COMMENT '总辐射最大辐照度出现时间',
  `V14320_etime` decimal(7, 0) NULL DEFAULT NULL COMMENT '总辐射最大辐照度出现时间',
  `V14308_1` decimal(7, 0) NULL DEFAULT NULL COMMENT '净全辐射最大辐照度',
  `V14308_stime` decimal(6, 0) NULL DEFAULT NULL COMMENT '净全辐射最大辐照度出现时间',
  `V14308_etime` decimal(7, 0) NULL DEFAULT NULL COMMENT '净全辐射最大辐照度出现时间',
  `V143_st_1` decimal(7, 0) NULL DEFAULT NULL COMMENT '垂直直接辐射最大辐照度',
  `V143_st_stime` decimal(6, 0) NULL DEFAULT NULL COMMENT '垂直直接辐射最大辐照度出现时间',
  `V143_st_etim` decimal(7, 0) NULL DEFAULT NULL COMMENT '垂直直接辐射最大辐照度出现时间'
) 
