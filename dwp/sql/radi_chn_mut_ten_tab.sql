CREATE TABLE `radi_chn_mut_ten_tab`  (
  `D_RECORD_ID` varchar(20)  NULL DEFAULT NULL COMMENT '记录标识',
  `V01301` varchar(30)  NULL DEFAULT NULL COMMENT '区站号',
  `V05001` decimal(10, 4) NULL DEFAULT NULL COMMENT '纬度',
  `V06001` decimal(10, 4) NULL DEFAULT NULL COMMENT '经度',
  `V07001` decimal(10, 4) NULL DEFAULT NULL COMMENT '拔海高度',
  `V04001` decimal(4, 0) NULL DEFAULT NULL COMMENT '年',
  `V04002` decimal(2, 0) NULL DEFAULT NULL COMMENT '月',
  `V04290` decimal(2, 0) NULL DEFAULT NULL COMMENT '旬',
  `V14320` decimal(10, 0) NULL DEFAULT NULL COMMENT '总辐射旬总量',
  `V14308` decimal(10, 0) NULL DEFAULT NULL COMMENT '净全辐射旬总量',
  `V14309` decimal(10, 0) NULL DEFAULT NULL COMMENT '散射辐射旬总量',
  `V14302` decimal(10, 0) NULL DEFAULT NULL COMMENT '水平面直接辐射旬总量',
  `V14306` decimal(10, 0) NULL DEFAULT NULL COMMENT '反射辐射旬总量',
  `V143_st` decimal(10, 0) NULL DEFAULT NULL COMMENT '垂直面直接辐射旬总量'
)