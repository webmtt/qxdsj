CREATE TABLE `radi_bmb_dat_tab`  (
  `D_RECORD_ID` varchar(200) NOT NULL COMMENT '记录标识',
  `V01301` varchar(30)  NULL DEFAULT NULL COMMENT '区站号',
  `V04001` decimal(4, 0) NULL DEFAULT NULL COMMENT '年',
  `V04002` decimal(2, 0) NULL DEFAULT NULL COMMENT '月',
  `V04003` decimal(2, 0) NULL DEFAULT NULL COMMENT '日',
  `V06001` decimal(10, 4) NULL DEFAULT NULL COMMENT '经度',
  `V05001` decimal(10, 4) NULL DEFAULT NULL COMMENT '纬度',
  `V07001` decimal(10, 4) NULL DEFAULT NULL COMMENT '观测场或平台拔海高度',
  `SITELEVEL` decimal(4, 0) NULL DEFAULT NULL COMMENT '站点级别',
  `V07032_WATER` decimal(2, 0) NULL DEFAULT NULL COMMENT '水平面光热总辐射表距地高度',
  `V07032_3_LAT` decimal(2, 0) NULL DEFAULT NULL COMMENT '纬度光热总辐射表距地高度',
  `V07032_3_LAT_N15` decimal(2, 0) NULL DEFAULT NULL COMMENT '纬度+15°光热总辐射表距地高度',
  `V07032_3_LAT_S15` decimal(2, 0) NULL DEFAULT NULL COMMENT '纬度-15°光热总辐射表距地高度',
  `V07032_3_E` varchar(9)  NULL DEFAULT NULL COMMENT '东垂直面光热总辐射表距地高度',
  `V07032_3_S` decimal(6, 0) NULL DEFAULT NULL COMMENT '南垂直面光热总辐射表距地高度',
  `V07032_3_W` decimal(10, 4) NULL DEFAULT NULL COMMENT '西垂直面光热总辐射表距地高度',
  `V07032_DISS` decimal(10, 4) NULL DEFAULT NULL COMMENT '水平面光热散射辐射表距地高度',
  `V07032_BIAXIAL` decimal(10, 4) NULL DEFAULT NULL COMMENT '双轴跟踪光热总辐射表距地高度',
  `V07032_UNIAXIAL` decimal(6, 0) NULL DEFAULT NULL COMMENT '单轴跟踪光热总辐射表距地高度',
  `V07032_REF` decimal(6, 0) NULL DEFAULT NULL COMMENT '水平面光热反射辐射表距地高度',
  `V07032_LAT_PHOTO` varchar(6)  NULL DEFAULT NULL COMMENT '纬度光电总辐射表距地高度',
  `V07032_WATER_PHOTO` char(3)  NULL DEFAULT NULL COMMENT '水平面光电反射辐射表距地高度',
  `V07032_ROTATE_PHOTO` decimal(10, 4) NULL DEFAULT NULL COMMENT '旋转架光电总辐射表距地高度',
  `V07032_BIAXIAL_PHOTO` decimal(10, 4) NULL DEFAULT NULL COMMENT '双轴跟踪光热直接辐射表距地高度',
  `V07032_4` decimal(10, 4) NULL DEFAULT NULL COMMENT '紫外辐射表(UV)距地高度',
  `V07032_6` decimal(10, 4) NULL DEFAULT NULL COMMENT '紫外辐射表(UVA)距地高度',
  `V07032_7` decimal(10, 4) NULL DEFAULT NULL COMMENT '紫外辐射表(UVB)距地高度',
  `V07032_5` decimal(10, 4) NULL DEFAULT NULL COMMENT '大气长波辐射表距地高度',
  `V02183` decimal(10, 4) NULL DEFAULT NULL COMMENT '地面长波辐射表距地高度',
  `V07032_8` decimal(10, 4) NULL DEFAULT NULL COMMENT '光合有效辐射表距地高度',
  `HEIGH_HORIZ_POLYSILICON` decimal(3, 0) NULL DEFAULT NULL COMMENT '水平面多晶硅电池距地高度',
  `HEIGH_HORIZ_MONOCRY` decimal(10, 4) NULL DEFAULT NULL COMMENT '水平面单晶硅电池距地高度',
  `HEIGH_HORIZ_NON` decimal(10, 4) NULL DEFAULT NULL COMMENT '水平面非晶硅电池距地高度',
  `HEIGH_LAT_POLYSILICON` decimal(10, 4) NULL DEFAULT NULL COMMENT '纬度多晶硅电池距地高度',
  `HEIGH_LAT_MONOCRY` decimal(10, 4) NULL DEFAULT NULL COMMENT '纬度单晶硅电池距地高度',
  `HEIGH_LAT_NON` decimal(10, 4) NULL DEFAULT NULL COMMENT '纬度非晶硅电池距地高度',
  `HEIGH_BIAXIAL_POLYSILICON` decimal(10, 4) NULL DEFAULT NULL COMMENT '双轴跟踪多晶硅电池距地高度',
  `HEIGH_BIAXIAL_MONOCRY` decimal(10, 4) NULL DEFAULT NULL COMMENT '双轴跟踪单晶硅电池距地高度',
  `HEIGH_BIAXIAL_NON` decimal(10, 4) NULL DEFAULT NULL COMMENT '双轴跟踪非晶硅电池距地高度',
  `HEIGH_UNIAXIAL_NON1` decimal(10, 4) NULL DEFAULT NULL COMMENT '单轴多晶硅电池距地高度',
  `HEIGH_UNIAXIAL_NON2` decimal(10, 4) NULL DEFAULT NULL COMMENT '单轴单晶硅电池距地高度',
  `HEIGH_UNIAXIAL_NON3` decimal(10, 4) NULL DEFAULT NULL COMMENT '单轴非晶硅电池距地高度'
)