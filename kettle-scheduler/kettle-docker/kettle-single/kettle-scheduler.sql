-- 创建数据库
create database if not exists `kettle-scheduler` default character set utf8 collate utf8_general_ci;

use kettle-scheduler;

-- ----------------------------
-- Table structure for k_category
-- ----------------------------
DROP TABLE IF EXISTS `k_category`;
CREATE TABLE `k_category`  (
  `category_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（1：存在；0：删除）',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_category
-- ----------------------------
INSERT INTO `k_category` VALUES (1, '文件推送', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (2, '库表推送', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (3, '接口推送', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (4, '文件获取', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (5, '库表获取', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (6, '接口获取', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);

-- ----------------------------
-- Table structure for k_job
-- ----------------------------
DROP TABLE IF EXISTS `k_job`;
CREATE TABLE `k_job`  (
  `job_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '作业ID',
  `category_id` int(11) NULL DEFAULT NULL,
  `job_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业名称',
  `job_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '任务描述',
  `job_type` int(11) NULL DEFAULT NULL COMMENT '1:数据库资源库；2:上传的文件',
  `job_path` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '作业保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）',
  `job_repository_id` int(11) NULL DEFAULT NULL COMMENT '作业的资源库ID',
  `job_quartz` int(11) NULL DEFAULT 1 COMMENT '定时策略（外键ID）',
  `job_record` int(11) NULL DEFAULT NULL COMMENT '作业执行记录（外键ID）',
  `job_log_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志级别(basic，detail，error，debug，minimal，rowlevel）',
  `job_status` int(11) NULL DEFAULT NULL COMMENT '状态（1：正在运行；2：已停止）',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（1：存在；0：删除）',
  PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_job
-- ----------------------------

-- ----------------------------
-- Table structure for k_job_monitor
-- ----------------------------
DROP TABLE IF EXISTS `k_job_monitor`;
CREATE TABLE `k_job_monitor`  (
  `monitor_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '监控作业ID',
  `monitor_job` int(11) NULL DEFAULT NULL COMMENT '监控的作业ID',
  `monitor_success` int(11) NULL DEFAULT NULL COMMENT '成功次数',
  `monitor_fail` int(11) NULL DEFAULT NULL COMMENT '失败次数',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加人',
  `monitor_status` int(11) NULL DEFAULT NULL COMMENT '监控状态（是否启动，1:启动；2:停止）',
  `run_status` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '运行状态（起始时间-结束时间,起始时间-结束时间……）',
  `last_execute_time` datetime(0) NULL DEFAULT NULL,
  `next_execute_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`monitor_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_job_monitor
-- ----------------------------

-- ----------------------------
-- Table structure for k_job_record
-- ----------------------------
DROP TABLE IF EXISTS `k_job_record`;
CREATE TABLE `k_job_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '作业记录ID',
  `record_job` int(11) NULL DEFAULT NULL COMMENT '作业ID',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '启动时间',
  `stop_time` datetime(0) NULL DEFAULT NULL COMMENT '停止时间',
  `record_status` int(11) NULL DEFAULT NULL COMMENT '任务执行结果（1：成功；2：失败）',
  `log_file_path` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '作业日志记录文件保存位置',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加人',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_job_record
-- ----------------------------

-- ----------------------------
-- Table structure for k_quartz
-- ----------------------------
DROP TABLE IF EXISTS `k_quartz`;
CREATE TABLE `k_quartz`  (
  `quartz_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `quartz_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '任务描述',
  `quartz_cron` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时策略',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（1：存在；0：删除）',
  PRIMARY KEY (`quartz_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_quartz
-- ----------------------------
INSERT INTO `k_quartz` VALUES (1, '立即执行一次', NULL, '2017-05-27 14:44:13', 1, '2017-05-27 14:44:13', 1, 1);
INSERT INTO `k_quartz` VALUES (2, '每周一0点执行一次', '0 0 0 ? * 2', '2017-05-27 14:56:38', 1, '2017-05-27 14:56:38', 1, 1);
INSERT INTO `k_quartz` VALUES (3, '每月1日0点执行一次', '0 0 0 1 * ?', '2017-05-27 14:56:38', 1, '2017-05-27 14:56:38', 1, 1);
INSERT INTO `k_quartz` VALUES (4, '每日0点执行一次', '0 0 0 * * ?', '2017-05-27 14:44:13', 1, '2017-05-27 14:44:15', 1, 1);
INSERT INTO `k_quartz` VALUES (31, '每分钟执行一次', '0 * * * * ?', '2018-10-16 14:12:44', 1, '2018-10-16 14:12:44', 1, 1);
INSERT INTO `k_quartz` VALUES (32, '每小时执行一次', '0 0 * * * ?', '2020-06-06 17:50:52', 1, '2020-06-06 17:50:52', 1, 1);
INSERT INTO `k_quartz` VALUES (33, '每5分钟执行一次', '0 0/5 * * * ?', '2020-06-06 18:08:22', 1, '2020-06-06 18:08:22', 1, 1);

-- ----------------------------
-- Table structure for k_repository
-- ----------------------------
DROP TABLE IF EXISTS `k_repository`;
CREATE TABLE `k_repository`  (
  `repository_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `repository_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库名称',
  `repository_username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录用户名',
  `repository_password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `repository_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库类型（MYSQL、ORACLE）',
  `database_access` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库访问模式（\"Native\", \"ODBC\", \"OCI\", \"Plugin\", \"JNDI\")',
  `database_host` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库主机名或者IP地址',
  `database_port` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库端口号',
  `database_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源库数据库名称',
  `database_username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库登录账号',
  `database_password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库登录密码',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（1：存在；0：删除）',
  PRIMARY KEY (`repository_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_repository
-- ----------------------------

-- ----------------------------
-- Table structure for k_repository_type
-- ----------------------------
DROP TABLE IF EXISTS `k_repository_type`;
CREATE TABLE `k_repository_type`  (
  `repository_type_id` int(11) NOT NULL,
  `repository_type_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `repository_type_des` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`repository_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_repository_type
-- ----------------------------
INSERT INTO `k_repository_type` VALUES (1, 'INGRES', 'Ingres');
INSERT INTO `k_repository_type` VALUES (2, 'INTERBASE', 'Borland Interbase');
INSERT INTO `k_repository_type` VALUES (3, 'INFOBRIGHT', 'Infobright');
INSERT INTO `k_repository_type` VALUES (4, 'ORACLE', 'Oracle');
INSERT INTO `k_repository_type` VALUES (5, 'EXTENDB', 'ExtenDB');
INSERT INTO `k_repository_type` VALUES (6, 'MSACCESS', 'MS Access');
INSERT INTO `k_repository_type` VALUES (7, 'SYBASE', 'Sybase');
INSERT INTO `k_repository_type` VALUES (8, 'PALO', 'Palo MOLAP Server');
INSERT INTO `k_repository_type` VALUES (9, 'INFORMIX', 'Informix');
INSERT INTO `k_repository_type` VALUES (10, 'LucidDB', 'LucidDB');
INSERT INTO `k_repository_type` VALUES (11, 'TERADATA', 'Teradata');
INSERT INTO `k_repository_type` VALUES (12, 'UNIVERSE', 'UniVerse database');
INSERT INTO `k_repository_type` VALUES (13, 'MONETDB', 'MonetDB');
INSERT INTO `k_repository_type` VALUES (14, 'CACHE', 'Intersystems Cache');
INSERT INTO `k_repository_type` VALUES (15, 'MSSQL', 'MS SQL Server');
INSERT INTO `k_repository_type` VALUES (16, 'KettleThin', 'Pentaho Data Services');
INSERT INTO `k_repository_type` VALUES (17, 'GREENPLUM', 'Greenplum');
INSERT INTO `k_repository_type` VALUES (18, 'GENERIC', 'Generic database');
INSERT INTO `k_repository_type` VALUES (19, 'IMPALA', 'Impala');
INSERT INTO `k_repository_type` VALUES (20, 'SQLITE', 'SQLite');
INSERT INTO `k_repository_type` VALUES (21, 'REMEDY-AR-SYSTEM', 'Remedy Action Request System');
INSERT INTO `k_repository_type` VALUES (22, 'MONDRIAN', 'Native Mondrian');
INSERT INTO `k_repository_type` VALUES (23, 'HIVE2', 'Hadoop Hive 2');
INSERT INTO `k_repository_type` VALUES (24, 'NETEZZA', 'Netezza');
INSERT INTO `k_repository_type` VALUES (25, 'VERTICA5', 'Vertica 5+');
INSERT INTO `k_repository_type` VALUES (26, 'POSTGRESQL', 'PostgreSQL');
INSERT INTO `k_repository_type` VALUES (27, 'EXASOL4', 'Exasol 4');
INSERT INTO `k_repository_type` VALUES (28, 'HYPERSONIC', 'Hypersonic');
INSERT INTO `k_repository_type` VALUES (29, 'AS/400', 'AS/400');
INSERT INTO `k_repository_type` VALUES (30, 'ORACLERDB', 'Oracle RDB');
INSERT INTO `k_repository_type` VALUES (31, 'DBASE', 'dBase III, IV or 5');
INSERT INTO `k_repository_type` VALUES (32, 'IMPALASIMBA', 'Cloudera Impala');
INSERT INTO `k_repository_type` VALUES (33, 'KINGBASEES', 'KingbaseES');
INSERT INTO `k_repository_type` VALUES (34, 'SAPR3', 'SAP ERP System');
INSERT INTO `k_repository_type` VALUES (35, 'SQLBASE', 'Gupta SQL Base');
INSERT INTO `k_repository_type` VALUES (36, 'DERBY', 'Apache Derby');
INSERT INTO `k_repository_type` VALUES (37, 'VERTICA', 'Vertica');
INSERT INTO `k_repository_type` VALUES (38, 'INFINIDB', 'Calpont InfiniDB');
INSERT INTO `k_repository_type` VALUES (39, 'HIVE', 'Hadoop Hive');
INSERT INTO `k_repository_type` VALUES (40, 'MYSQL', 'MySQL');
INSERT INTO `k_repository_type` VALUES (41, 'MSSQLNATIVE', 'MS SQL Server (Native)');
INSERT INTO `k_repository_type` VALUES (42, 'H2', 'H2');
INSERT INTO `k_repository_type` VALUES (43, 'SAPDB', 'MaxDB (SAP DB)');
INSERT INTO `k_repository_type` VALUES (44, 'SPARKSIMBA', 'SparkSQL');
INSERT INTO `k_repository_type` VALUES (45, 'VECTORWISE', 'Ingres VectorWise');
INSERT INTO `k_repository_type` VALUES (46, 'DB2', 'IBM DB2');
INSERT INTO `k_repository_type` VALUES (47, 'NEOVIEW', 'Neoview');
INSERT INTO `k_repository_type` VALUES (48, 'SYBASEIQ', 'SybaseIQ');
INSERT INTO `k_repository_type` VALUES (49, 'REDSHIFT', 'Redshift');
INSERT INTO `k_repository_type` VALUES (50, 'FIREBIRD', 'Firebird SQL');
INSERT INTO `k_repository_type` VALUES (51, 'OpenERPDatabaseMeta', 'OpenERP Server');

-- ----------------------------
-- Table structure for k_trans
-- ----------------------------
DROP TABLE IF EXISTS `k_trans`;
CREATE TABLE `k_trans`  (
  `trans_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '转换ID',
  `category_id` int(11) NULL DEFAULT NULL,
  `trans_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转换名称',
  `trans_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '转换描述',
  `trans_type` int(11) NULL DEFAULT NULL COMMENT '1:数据库资源库；2:上传的文件',
  `trans_path` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '转换保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）',
  `trans_repository_id` int(11) NULL DEFAULT NULL COMMENT '转换的资源库ID',
  `trans_quartz` int(11) NULL DEFAULT 1 COMMENT '定时策略（外键ID）',
  `trans_record` int(11) NULL DEFAULT NULL COMMENT '转换执行记录（外键ID）',
  `trans_log_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志级别(basic，detail，error，debug，minimal，rowlevel）',
  `trans_status` int(11) NULL DEFAULT NULL COMMENT '状态（1：正在运行；2：已停止）',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（1：存在；0：删除）',
  PRIMARY KEY (`trans_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_trans
-- ----------------------------

-- ----------------------------
-- Table structure for k_trans_monitor
-- ----------------------------
DROP TABLE IF EXISTS `k_trans_monitor`;
CREATE TABLE `k_trans_monitor`  (
  `monitor_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '监控转换ID',
  `monitor_trans` int(11) NULL DEFAULT NULL COMMENT '监控的转换的ID',
  `monitor_success` int(11) NULL DEFAULT NULL COMMENT '成功次数',
  `monitor_fail` int(11) NULL DEFAULT NULL COMMENT '失败次数',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加人',
  `monitor_status` int(11) NULL DEFAULT NULL COMMENT '监控状态（是否启动，1:启动；2:停止）',
  `run_status` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '运行状态（起始时间-结束时间,起始时间-结束时间……）',
  `last_execute_time` datetime(0) NULL DEFAULT NULL,
  `next_execute_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`monitor_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_trans_monitor
-- ----------------------------

-- ----------------------------
-- Table structure for k_trans_record
-- ----------------------------
DROP TABLE IF EXISTS `k_trans_record`;
CREATE TABLE `k_trans_record`  (
  `record_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '转换记录ID',
  `record_trans` int(11) NULL DEFAULT NULL COMMENT '转换ID',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '启动时间',
  `stop_time` datetime(0) NULL DEFAULT NULL COMMENT '停止时间',
  `record_status` int(11) NULL DEFAULT NULL COMMENT '任务执行结果（1：成功；2：失败）',
  `log_file_path` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '转换日志记录文件保存位置',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加人',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_trans_record
-- ----------------------------

-- ----------------------------
-- Table structure for k_user
-- ----------------------------
DROP TABLE IF EXISTS `k_user`;
CREATE TABLE `k_user`  (
  `u_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `u_nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `u_email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `u_phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用于电话',
  `u_account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户账号',
  `u_password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（1：存在；0：删除）',
  PRIMARY KEY (`u_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of k_user
-- ----------------------------
INSERT INTO `k_user` VALUES (1, 'admin', NULL, NULL, 'admin', 'b1276925a59fd8d9e1a53c10637f271d', NULL, NULL, NULL, NULL, 1);

-- ----------------------------
-- Table structure for km_kettle_log
-- ----------------------------
DROP TABLE IF EXISTS `km_kettle_log`;
CREATE TABLE `km_kettle_log`  (
  `id` int(32) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `batch_id` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '批次号',
  `job_id` int(32) NULL DEFAULT NULL COMMENT '作业id',
  `job_record_id` int(32) NULL DEFAULT NULL COMMENT '作业记录id',
  `category_id` int(32) NULL DEFAULT NULL,
  `type` int(1) NULL DEFAULT NULL COMMENT '类型（1：job，2：trans）',
  `count` bigint(20) NULL DEFAULT NULL COMMENT '同步条数',
  `date` date NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'kettle日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of km_kettle_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
