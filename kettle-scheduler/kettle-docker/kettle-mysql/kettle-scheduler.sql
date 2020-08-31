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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_category
-- ----------------------------
INSERT INTO `k_category` VALUES (1, '文件推送', '2020-03-17 10:53:29', 1, '2020-05-06 17:40:46', 1, 1);
INSERT INTO `k_category` VALUES (2, '库表推送', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (3, '接口推送', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (4, '文件获取', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (5, '库表获取', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (6, '接口获取', '2020-03-17 10:53:29', 1, '2020-03-17 14:44:30', 1, 1);
INSERT INTO `k_category` VALUES (9, 'sdfadf', '2020-05-13 17:58:47', 1, '2020-05-13 17:58:47', 1, 0);

-- ----------------------------
-- Table structure for k_job
-- ----------------------------
DROP TABLE IF EXISTS `k_job`;
CREATE TABLE `k_job`  (
  `job_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '作业ID',
  `category_id` int(11) NULL DEFAULT NULL,
  `job_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业名称',
  `job_description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `job_type` int(11) NULL DEFAULT NULL COMMENT '1:数据库资源库；2:上传的文件',
  `job_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）',
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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_job
-- ----------------------------
INSERT INTO `k_job` VALUES (12, 4, 'FTP下载作业', '从前置机下载FTP文件', 1, '/库表获取ftp信息下载文件作业', 6, 1, NULL, 'basic', 1, '2020-03-17 10:54:39', 1, '2020-03-17 10:54:39', 1, 1);
INSERT INTO `k_job` VALUES (13, 1, 'FTP推送文件至远程服务器上', '从本地上传文件', 1, '/库表获取ftp信息推送文件至远程作业', 6, 1, NULL, 'rowlevel', 2, '2020-03-17 11:09:01', 1, '2020-03-17 11:09:01', 1, 0);
INSERT INTO `k_job` VALUES (14, 1, '本地上传文件', '上传到前置机', 1, '/库表获取ftp信息推送文件至远程作业', 6, 1, NULL, 'basic', 2, '2020-03-17 11:15:17', 1, '2020-03-17 11:15:17', 1, 1);
INSERT INTO `k_job` VALUES (15, 1, '批量同步表', '将本地数据库表同步到前置机中', 1, '/批量同步', 6, 1, NULL, 'basic', 2, '2020-03-17 14:24:04', 1, '2020-03-17 14:24:04', 1, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_job_monitor
-- ----------------------------
INSERT INTO `k_job_monitor` VALUES (10, 12, 2, 1, 1, 1, '1584413687381-1584414336485,1584414339640-1584414961722,1584414964156-', '2020-03-17 11:16:04', NULL);
INSERT INTO `k_job_monitor` VALUES (11, 13, 0, 2, 1, 2, '1584414548618-1584414663728,1584414777496-1584414870891', '2020-03-17 11:12:57', NULL);
INSERT INTO `k_job_monitor` VALUES (12, 14, 1, 1, 1, 2, '1584414922436-1584415051795,1584415054207-1586429202223', '2020-03-17 11:17:34', NULL);
INSERT INTO `k_job_monitor` VALUES (13, 15, 3, 0, 1, 2, '1584426253191-1584426621394,1584428378615-1584430510825,1584430527673-1586428995591', '2020-03-17 15:35:28', NULL);

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
  `log_file_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业日志记录文件保存位置',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加人',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 400 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_job_record
-- ----------------------------
INSERT INTO `k_job_record` VALUES (390, 12, '2020-03-17 10:54:47', '2020-03-17 10:54:50', 2, 'D:\\data-integration\\logs/1/@库表获取ftp信息下载文件作业-log/1584413689965.txt', 1);
INSERT INTO `k_job_record` VALUES (391, 12, '2020-03-17 11:05:40', '2020-03-17 11:05:42', 1, 'D:\\data-integration\\logs/1/@库表获取ftp信息下载文件作业-log/1584414342038.txt', 1);
INSERT INTO `k_job_record` VALUES (392, 13, '2020-03-17 11:09:09', '2020-03-17 11:09:10', 2, 'D:\\data-integration\\logs/1/@库表获取ftp信息推送文件至远程作业-log/1584414549643.txt', 1);
INSERT INTO `k_job_record` VALUES (393, 13, '2020-03-17 11:12:57', '2020-03-17 11:12:58', 2, 'D:\\data-integration\\logs/1/@库表获取ftp信息推送文件至远程作业-log/1584414778445.txt', 1);
INSERT INTO `k_job_record` VALUES (394, 14, '2020-03-17 11:15:22', '2020-03-17 11:15:23', 2, 'D:\\data-integration\\logs/1/@库表获取ftp信息推送文件至远程作业-log/1584414923428.txt', 1);
INSERT INTO `k_job_record` VALUES (395, 12, '2020-03-17 11:16:04', '2020-03-17 11:16:07', 1, 'D:\\data-integration\\logs/1/@库表获取ftp信息下载文件作业-log/1584414966782.txt', 1);
INSERT INTO `k_job_record` VALUES (396, 14, '2020-03-17 11:17:34', '2020-03-17 11:17:39', 1, 'D:\\data-integration\\logs/1/@库表获取ftp信息推送文件至远程作业-log/1584415059078.txt', 1);
INSERT INTO `k_job_record` VALUES (397, 15, '2020-03-17 14:24:13', '2020-03-17 14:24:14', 1, 'D:\\data-integration\\logs/1/@批量同步-log/1584426254253.txt', 1);
INSERT INTO `k_job_record` VALUES (398, 15, '2020-03-17 14:59:39', '2020-03-17 14:59:40', 1, 'D:\\data-integration\\logs/1/@批量同步-log/1584428379651.txt', 1);
INSERT INTO `k_job_record` VALUES (399, 15, '2020-03-17 15:35:28', '2020-03-17 15:35:29', 1, 'D:\\data-integration\\logs/1/@批量同步-log/1584430528721.txt', 1);

-- ----------------------------
-- Table structure for k_quartz
-- ----------------------------
DROP TABLE IF EXISTS `k_quartz`;
CREATE TABLE `k_quartz`  (
  `quartz_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `quartz_description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `quartz_cron` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时策略',
  `add_time` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加者',
  `edit_time` datetime(0) NULL DEFAULT NULL COMMENT '编辑时间',
  `edit_user` int(11) NULL DEFAULT NULL COMMENT '编辑者',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '是否删除（1：存在；0：删除）',
  PRIMARY KEY (`quartz_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_quartz
-- ----------------------------
INSERT INTO `k_quartz` VALUES (1, '立即执行一次', NULL, '2017-05-27 14:44:13', 1, '2017-05-27 14:44:13', NULL, 1);
INSERT INTO `k_quartz` VALUES (2, '每周一0点执行一次', '0 0 0 ? * 2', '2017-05-27 14:56:38', 1, '2017-05-27 14:56:38', NULL, 1);
INSERT INTO `k_quartz` VALUES (3, '每月1日0点执行一次', '0 0 0 1 * ?', '2017-05-27 14:56:38', 1, '2017-05-27 14:56:38', NULL, 1);
INSERT INTO `k_quartz` VALUES (4, '每日0点执行一次', '0 0 0 * * ?', '2017-05-27 14:44:13', 1, '2017-05-27 14:44:15', NULL, 1);
INSERT INTO `k_quartz` VALUES (31, '每分钟执行一次', '0 * * * * ?', '2018-10-16 14:12:44', 6, '2018-10-16 14:12:44', 6, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_repository
-- ----------------------------
INSERT INTO `k_repository` VALUES (6, 'kettle-repository', 'admin', 'admin', 'MYSQL', 'Native', '121.36.24.29', '3360', 'kettle-repository', 'pieaccountC', 'Piesat@123', '2020-03-17 10:52:02', 1, '2020-03-17 10:52:02', 1, 1);
INSERT INTO `k_repository` VALUES (7, 'localhost-kettle-repository', 'admin', 'admin', 'MYSQL', 'Native', 'localhost', '3306', 'kettle-repository', 'root', '123456', '2020-03-18 15:12:46', 1, '2020-03-18 15:12:46', 1, 1);

-- ----------------------------
-- Table structure for k_repository_type
-- ----------------------------
DROP TABLE IF EXISTS `k_repository_type`;
CREATE TABLE `k_repository_type`  (
  `repository_type_id` int(11) NOT NULL,
  `repository_type_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `repository_type_des` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`repository_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
  `trans_description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转换描述',
  `trans_type` int(11) NULL DEFAULT NULL COMMENT '1:数据库资源库；2:上传的文件',
  `trans_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转换保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）',
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
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_trans
-- ----------------------------
INSERT INTO `k_trans` VALUES (23, 2, '库表同步转换', '', 1, '/库表同步推送转换', 6, 1, NULL, 'basic', 1, '2020-03-17 14:31:57', 1, '2020-03-17 14:31:57', 1, 1);
INSERT INTO `k_trans` VALUES (24, 5, '库表推送数据', '从前置机中抓取数据', 1, '/库表同步抓取转换', 6, 1, NULL, 'basic', 1, '2020-03-17 14:35:29', 1, '2020-03-17 14:35:29', 1, 1);
INSERT INTO `k_trans` VALUES (25, 6, '接口同步转换', '', 1, '/接口同步转换', 6, 1, NULL, 'basic', 2, '2020-03-17 14:43:42', 1, '2020-03-17 14:43:42', 1, 1);
INSERT INTO `k_trans` VALUES (26, 3, '接口推送转换', '', 1, '/接口同步转换', 6, 1, NULL, 'basic', 1, '2020-03-17 14:56:43', 1, '2020-03-17 14:56:43', 1, 1);
INSERT INTO `k_trans` VALUES (27, 1, '文件同步转换', NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `k_trans` VALUES (28, 4, '文件推送转换', NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

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
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_trans_monitor
-- ----------------------------
INSERT INTO `k_trans_monitor` VALUES (20, 23, 2, 0, 1, 1, '1584426721929-1584428660354,1584428662460-', '2020-03-17 15:04:22', NULL);
INSERT INTO `k_trans_monitor` VALUES (21, 24, 2, 0, 1, 1, '1584426943886-1584428655914,1584428658006-', '2020-03-17 15:04:18', NULL);
INSERT INTO `k_trans_monitor` VALUES (22, 25, 0, 1, 1, 2, '1584427507695-1584428165745', '2020-03-17 14:45:08', NULL);
INSERT INTO `k_trans_monitor` VALUES (23, 26, 2, 5, 1, 1, '1584428208470-1584428269139,1584428312210-1584428648044,1584428650994-1584429759691,1584429761989-1584430601004,1584430613980-1584694483340,1584694486746-1584695897600,1584695900503-', '2020-03-20 17:18:21', NULL);

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
  `log_file_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '转换日志记录文件保存位置',
  `add_user` int(11) NULL DEFAULT NULL COMMENT '添加人',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_trans_record
-- ----------------------------
INSERT INTO `k_trans_record` VALUES (32, 24, '2020-03-17 14:35:44', '2020-03-17 14:35:45', 1, 'D:\\data-integration\\logs/1/@库表同步抓取转换-log/1584426945187.txt', 1);
INSERT INTO `k_trans_record` VALUES (45, 24, '2020-03-17 15:04:18', '2020-03-17 15:04:19', 1, 'D:\\data-integration\\logs/1/@库表同步抓取转换-log/1584428659242.txt', 1);
INSERT INTO `k_trans_record` VALUES (58, 25, '2020-03-17 14:45:08', '2020-03-17 22:19:02', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584427508916.txt', 1);
INSERT INTO `k_trans_record` VALUES (60, 23, '2020-03-17 15:04:22', '2020-03-17 15:04:24', 1, 'D:\\data-integration\\logs/1/@库表同步推送转换-log/1584428663678.txt', 1);
INSERT INTO `k_trans_record` VALUES (64, 23, '2020-03-17 14:32:02', '2020-03-17 14:32:03', 1, 'D:\\data-integration\\logs/1/@库表同步推送转换-log/1584426723147.txt', 1);
INSERT INTO `k_trans_record` VALUES (66, 26, '2020-03-17 14:58:32', '2020-03-17 19:19:24', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584428313418.txt', 1);
INSERT INTO `k_trans_record` VALUES (77, 26, '2020-03-20 16:54:47', '2020-03-20 16:54:49', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584694488956.txt', 1);
INSERT INTO `k_trans_record` VALUES (79, 26, '2020-03-17 14:56:48', '2020-03-17 17:19:06', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584428209646.txt', 1);
INSERT INTO `k_trans_record` VALUES (87, 26, '2020-03-17 15:36:54', '2020-03-25 18:19:51', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584430615179.txt', 1);
INSERT INTO `k_trans_record` VALUES (88, 26, '2020-03-17 15:04:11', '2020-03-17 16:19:32', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584428652177.txt', 1);
INSERT INTO `k_trans_record` VALUES (90, 26, '2020-03-17 15:22:42', '2020-03-17 23:19:44', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584756286138.txt', 1);
INSERT INTO `k_trans_record` VALUES (100, 27, '2020-03-21 22:21:47', '2020-03-21 23:21:55', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584694488956.txt', 1);
INSERT INTO `k_trans_record` VALUES (110, 28, '2020-03-21 22:23:32', '2020-03-21 23:23:40', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584694488956.txt', 1);
INSERT INTO `k_trans_record` VALUES (120, 27, '2020-03-22 19:22:27', '2020-03-22 20:22:35', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584694488956.txt', 1);
INSERT INTO `k_trans_record` VALUES (135, 28, '2020-03-21 22:23:00', '2020-03-21 23:23:15', 1, 'D:\\data-integration\\logs/1/@接口同步转换-log/1584694488956.txt', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of k_user
-- ----------------------------
INSERT INTO `k_user` VALUES (1, 'admin', NULL, NULL, 'admin', 'b1276925a59fd8d9e1a53c10637f271d', NULL, NULL, NULL, NULL, 1);

-- ----------------------------
-- Table structure for km_kettle_error_log
-- ----------------------------
DROP TABLE IF EXISTS `km_kettle_error_log`;
CREATE TABLE `km_kettle_error_log`  (
  `id` int(32) NOT NULL COMMENT 'id',
  `batch_id` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '批次号',
  `job_id` int(32) NULL DEFAULT NULL COMMENT '作业id',
  `job_record_id` int(32) NULL DEFAULT NULL COMMENT '作业记录id',
  `data_ids` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据ids',
  `category_id` int(32) NULL DEFAULT NULL,
  `type` int(1) NULL DEFAULT NULL COMMENT '类型（1：job，2：trans）',
  `count` bigint(20) NULL DEFAULT NULL COMMENT '同步条数',
  `sync_count` int(10) NULL DEFAULT NULL COMMENT '重新同步次数',
  `status` int(1) NULL DEFAULT NULL COMMENT '是否重新同步成功（0：失败，1：成功）',
  `error_description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '错误描述',
  `start_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'kettle日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of km_kettle_error_log
-- ----------------------------
INSERT INTO `km_kettle_error_log` VALUES (0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-05-06 16:37:05', '2020-05-06 16:37:11', '2020-05-06 16:37:11', '2020-05-06 16:37:11');

-- ----------------------------
-- Table structure for km_kettle_log
-- ----------------------------
DROP TABLE IF EXISTS `km_kettle_log`;
CREATE TABLE `km_kettle_log`  (
  `id` int(32) NOT NULL COMMENT 'id',
  `batch_id` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '批次号',
  `job_id` int(32) NULL DEFAULT NULL COMMENT '作业id',
  `job_record_id` int(32) NULL DEFAULT NULL COMMENT '作业记录id',
  `category_id` int(32) NULL DEFAULT NULL,
  `type` int(1) NULL DEFAULT NULL COMMENT '类型（1：job，2：trans）',
  `count` bigint(20) NULL DEFAULT NULL COMMENT '同步条数',
  `date` date NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'kettle日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of km_kettle_log
-- ----------------------------
INSERT INTO `km_kettle_log` VALUES (1, '1', 12, 390, 4, 1, 1, '2020-05-05', '2020-05-05 17:17:07', '2020-05-05 17:17:07');
INSERT INTO `km_kettle_log` VALUES (2, '1', 12, 391, 4, 1, 2, '2020-05-06', '2020-05-06 17:17:07', '2020-05-06 17:17:07');
INSERT INTO `km_kettle_log` VALUES (3, '1', 13, 392, 1, 1, 3, '2020-05-06', '2020-05-06 17:17:07', '2020-05-06 17:17:07');
INSERT INTO `km_kettle_log` VALUES (4, '1', 13, 393, 1, 1, 4, '2020-05-06', '2020-05-06 17:17:07', '2020-05-06 17:17:07');
INSERT INTO `km_kettle_log` VALUES (5, '1', 14, 394, 4, 1, 5, '2020-05-06', '2020-05-06 17:17:07', '2020-05-06 17:17:07');
INSERT INTO `km_kettle_log` VALUES (6, '1', 12, 395, 1, 1, 6, '2020-05-06', '2020-05-06 17:17:07', '2020-05-06 17:17:07');
INSERT INTO `km_kettle_log` VALUES (7, '1', 14, 396, 4, 1, 7, '2020-05-06', '2020-05-06 17:17:07', '2020-05-06 17:17:07');
INSERT INTO `km_kettle_log` VALUES (8, '1', 15, 397, 1, 1, 8, '2020-05-06', '2020-05-06 17:17:07', '2020-05-06 17:17:07');
INSERT INTO `km_kettle_log` VALUES (9, '1', 15, 398, 1, 1, 9, '2020-05-06', '2020-05-06 17:17:07', '2020-05-06 17:17:07');
INSERT INTO `km_kettle_log` VALUES (10, '1', 15, 399, 1, 1, 10, '2020-05-06', '2020-05-06 17:17:07', '2020-05-06 17:17:07');

-- ----------------------------
-- Table structure for km_kettle_log_copy
-- ----------------------------
DROP TABLE IF EXISTS `km_kettle_log_copy`;
CREATE TABLE `km_kettle_log_copy`  (
  `OID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对象主键',
  `OCODE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对象代码',
  `ONAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对象名称',
  `ODESCRIBE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '对象描述',
  `OORDER` int(11) NULL DEFAULT NULL COMMENT '对象排序',
  `SIMPLE_SPELL` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对象简拼',
  `FULL_SPELL` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '对象全拼',
  `CREATE_DATE` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_DATE` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新时间',
  `CREATE_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `UPDATE_USER` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `EXPAND` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '扩展信息',
  `IS_DISABLE` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否禁用',
  `FLAG1` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `FLAG2` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用2',
  `DATA_BILL` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据账单',
  `JOB` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作业',
  `START_TIME` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始时间',
  `END_TIME` varchar(14) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `ETLFLAG` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '抽取标志',
  `RESULT` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果',
  `INPUT_COUNT` int(11) NULL DEFAULT NULL COMMENT '读取量',
  `ADD_COUNT` int(11) NULL DEFAULT NULL COMMENT '新增量',
  `REPEAT_COUNT` int(11) NULL DEFAULT NULL COMMENT '重复量',
  `INVALID_COUNT` int(11) NULL DEFAULT NULL COMMENT '无效量',
  PRIMARY KEY (`OID`) USING BTREE,
  INDEX `IDX_M_K_LOG_CREATE_DATE`(`CREATE_DATE`) USING BTREE,
  INDEX `IDX_M_K_LOG_UPDATE_DATE`(`UPDATE_DATE`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'kettle日志' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
