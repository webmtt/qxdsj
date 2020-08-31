/*
 Navicat Premium Data Transfer

 Source Server         : 121.36.24.29
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 121.36.24.29:3360
 Source Schema         : bigdate

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 14/02/2020 10:12:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for upar_chn_mul_ftm_wew_tab
-- ----------------------------
DROP TABLE IF EXISTS `upar_chn_mul_ftm_wew_tab`;
CREATE TABLE `upar_chn_mul_ftm_wew_tab`  (
  `D_RECORD_ID` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记录标识',
  `V01301` varchar(9) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区站号(字符)',
  `V04001` decimal(4, 0) NULL DEFAULT NULL COMMENT '资料观测年',
  `V04002` decimal(2, 0) NULL DEFAULT NULL COMMENT '资料观测月',
  `V04003` decimal(2, 0) NULL DEFAULT NULL COMMENT '资料观测日',
  `TIMES` decimal(2, 0) NULL DEFAULT NULL COMMENT '时次',
  `D_DATETIME` datetime(0) NULL DEFAULT NULL COMMENT '资料时间',
  `ARRIVALTIME` decimal(3, 0) NULL DEFAULT NULL COMMENT '观测高度到达时间',
  `V10009` decimal(10, 4) NULL DEFAULT NULL COMMENT '位势高度',
  `V11001` decimal(10, 4) NULL DEFAULT NULL COMMENT '风向',
  `V11002` decimal(10, 4) NULL DEFAULT NULL COMMENT '风速',
  `Q11001` decimal(1, 0) NULL DEFAULT NULL COMMENT '风向质量控制码',
  `Q11002` decimal(1, 0) NULL DEFAULT NULL COMMENT '风速质量控制码'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '中国高空规定高度层风定时值数据集(v1.0)数据表' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
