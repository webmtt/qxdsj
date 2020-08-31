CREATE TABLE `upar_chn_mul_ftm_sig_tab`  (
  `D_RECORD_ID` varchar(200)  NOT NULL COMMENT '记录标识',
  `V04001` decimal(4, 0) NOT NULL COMMENT '年',
  `V04002` decimal(2, 0) NOT NULL COMMENT '月',
  `D_DATETIME` datetime(0) NOT NULL COMMENT '资料时间',
  `D_UPDATE_TIME` varchar(50)  NOT NULL COMMENT '时次',
  `V_LEVEL_C` varchar(50)  NOT NULL COMMENT '层号',
  `V10004` decimal(10, 4) NOT NULL COMMENT '气压',
  `V12001` decimal(10, 4) NOT NULL COMMENT '温度',
  `V12003` decimal(10, 4) NOT NULL COMMENT '露点温度',
  `Q_BASICPARAMETER` decimal(2, 0) NOT NULL COMMENT '基本参数质量控制码',
  `Q07004` decimal(2, 0) NOT NULL COMMENT '气压质量控制码',
  `Q12001` decimal(2, 0) NOT NULL COMMENT '温度质量控制码',
  `Q12003` decimal(1, 0) NOT NULL COMMENT '露点温度质量控制码'
)
