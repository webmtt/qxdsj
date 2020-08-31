DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `type` char(1) COLLATE utf8_bin DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '日志标题',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `remote_addr` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求URI',
  `method` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '操作方式',
  `params` text COLLATE utf8_bin COMMENT '操作提交的数据',
  `exception` text COLLATE utf8_bin COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='日志表';



DROP TABLE IF EXISTS `sys_desk_log`;

CREATE TABLE `sys_desk_log` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '编号',
  `type` char(1) COLLATE utf8_bin DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '日志标题',
  `create_by` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '创建者',
  `create_date` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '创建时间',
  `remote_addr` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求URI',
  `method` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '操作方式',
  `params` text COLLATE utf8_bin COMMENT '操作提交的数据',
  `exception` text COLLATE utf8_bin COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sys_desk_log_create_by` (`create_by`),
  KEY `sys_desk_log_request_uri` (`request_uri`),
  KEY `sys_desk_log_type` (`type`),
  KEY `sys_desk_log_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='前台日志表';


INSERT INTO `sup_adminmenu`(`ID`, `PARENT_ID`, `PARENT_IDS`, `NAME`, `HREF`, `TARGET`, `ICON`, `SORT`, `IS_SHOW`, `IS_ACTIVITI`, `PERMISSION`, `CREATE_BY`, `CREATE_DATE`, `UPDATE_BY`, `UPDATE_DATE`, `REMARKS`, `DEL_FLAG`) VALUES ('cbcdbc5b7966417886fe7937fc662ee0', '3', '0,1,2,3,', '日志管理', '/sys/log', NULL, '', 30, '1', '0', NULL, '1', '2020-07-15 14:58:10', '1', '2020-07-15 14:58:10', NULL, '0');
INSERT INTO `sup_adminmenu`(`ID`, `PARENT_ID`, `PARENT_IDS`, `NAME`, `HREF`, `TARGET`, `ICON`, `SORT`, `IS_SHOW`, `IS_ACTIVITI`, `PERMISSION`, `CREATE_BY`, `CREATE_DATE`, `UPDATE_BY`, `UPDATE_DATE`, `REMARKS`, `DEL_FLAG`) VALUES ('b38979aa86a14099b407ed12e20734ea', 'cbcdbc5b7966417886fe7937fc662ee0', '0,1,2,3,cbcdbc5b7966417886fe7937fc662ee0,', '查看', '', NULL, '', 30, '0', '0', 'sys:log:view', '1', '2020-07-15 14:59:14', '1', '2020-07-15 15:10:11', NULL, '0');

INSERT INTO `sup_adminmenu`(`ID`, `PARENT_ID`, `PARENT_IDS`, `NAME`, `HREF`, `TARGET`, `ICON`, `SORT`, `IS_SHOW`, `IS_ACTIVITI`, `PERMISSION`, `CREATE_BY`, `CREATE_DATE`, `UPDATE_BY`, `UPDATE_DATE`, `REMARKS`, `DEL_FLAG`) VALUES ('127332edd28d49a99f7e1e63273ef5a4', '3', '0,1,2,3,', '前台日志管理', '/sys/desklog', NULL, 'star', 30, '1', '0', '', '1', '2020-07-16 16:15:08', '1', '2020-07-16 16:15:08', NULL, '0');
INSERT INTO `sup_adminmenu`(`ID`, `PARENT_ID`, `PARENT_IDS`, `NAME`, `HREF`, `TARGET`, `ICON`, `SORT`, `IS_SHOW`, `IS_ACTIVITI`, `PERMISSION`, `CREATE_BY`, `CREATE_DATE`, `UPDATE_BY`, `UPDATE_DATE`, `REMARKS`, `DEL_FLAG`) VALUES ('c2f52b0b206d43be95ea949d6c6df1e3', '127332edd28d49a99f7e1e63273ef5a4', '0,1,2,3,127332edd28d49a99f7e1e63273ef5a4,', '查看', '', NULL, '', 30, '1', '0', 'sys:desklog:view', '1', '2020-07-16 16:16:07', '1', '2020-07-16 16:16:07', NULL, '0');



