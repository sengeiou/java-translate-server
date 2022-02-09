# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 39.103.201.20 (MySQL 5.7.36)
# Database: translate_db_prod
# Generation Time: 2022-02-09 06:56:50 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table tb_global_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_global_config`;

CREATE TABLE `tb_global_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `config_title` varchar(255) DEFAULT NULL COMMENT '配置标题',
  `config_desc` varchar(255) DEFAULT NULL COMMENT '配置描述',
  `config_extra1` varchar(255) DEFAULT NULL COMMENT '附加配置1',
  `config_extra2` varchar(255) DEFAULT NULL COMMENT '附加配置2',
  `config_extra3` varchar(255) DEFAULT NULL COMMENT '附加配置3',
  `config_extra4` varchar(255) DEFAULT NULL COMMENT '附加配置4',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` int(2) DEFAULT '0' COMMENT '1 翻译工具jar 0 其他',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='全局配置表';

LOCK TABLES `tb_global_config` WRITE;
/*!40000 ALTER TABLE `tb_global_config` DISABLE KEYS */;

INSERT INTO `tb_global_config` (`id`, `config_title`, `config_desc`, `config_extra1`, `config_extra2`, `config_extra3`, `config_extra4`, `update_time`, `type`)
VALUES
	(44,'翻译工具下载V1J','','http://www.cretinzp.com:8029/file/2022/02/09/阿念翻译同步助手.jar',NULL,NULL,NULL,'2021-12-30 14:44:33',1);

/*!40000 ALTER TABLE `tb_global_config` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_str_translate_item
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_str_translate_item`;

CREATE TABLE `tb_str_translate_item` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `android_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'Android key',
  `android_package` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Android 包名 用于区分字符串来源',
  `android_source` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'Android 源内容',
  `ios_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'iOS key',
  `ios_package` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'iOS 包名 用于区分字符串来源',
  `ios_source` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'iOS 源内容',
  `translate_source` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '翻译前的源中文内容',
  `translate_en` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '翻译后的英文内容',
  `translate_tw` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '翻译后的繁体内容',
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `merge_status` int(2) NOT NULL DEFAULT '1' COMMENT '合并状态 1 正常 0 有冲突',
  `app_id` varchar(16) CHARACTER SET utf8mb4 DEFAULT 'lunar' COMMENT 'app_id 字符串 不是数字',
  `confirm_status` int(2) NOT NULL DEFAULT '0' COMMENT '确认状态 0 未确定 1 已确定',
  PRIMARY KEY (`id`),
  KEY `index_android_key` (`android_key`),
  KEY `index_ios_key` (`ios_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='APP国际化翻译表';

LOCK TABLES `tb_str_translate_item` WRITE;
/*!40000 ALTER TABLE `tb_str_translate_item` DISABLE KEYS */;

INSERT INTO `tb_str_translate_item` (`id`, `android_key`, `android_package`, `android_source`, `ios_key`, `ios_package`, `ios_source`, `translate_source`, `translate_en`, `translate_tw`, `add_time`, `update_time`, `merge_status`, `app_id`, `confirm_status`)
VALUES
	(1,X'6170705F6E616D65','/app/src/main/res/values','Demo',X'696F735F6170705F6E616D65','','Demo','Demo','Demo','','2022-02-09 14:32:28','2022-02-09 14:36:39',1,'lunar',1),
	(2,X'6D6F64756C65315F636F6E74656E745F37','/module4/src/main/res/values','我是内容7',X'6D6F64756C655F696F735F636F6E74656E7437','','我是内容7','我是内容7','I am content 7','','2022-02-09 14:32:28','2022-02-09 14:36:43',1,'lunar',0),
	(3,X'6D6F64756C65315F636F6E74656E745F38','/module4/src/main/res/values','我是内容8',X'6D6F64756C655F696F735F636F6E74656E7438','','我是内容8','我是内容8','I am content 8','','2022-02-09 14:32:28','2022-02-09 14:36:45',1,'lunar',0),
	(4,X'6D6F64756C65315F636F6E74656E745F35','/module3/src/main/res/values','我是内容5',X'6D6F64756C655F696F735F636F6E74656E7435','','我是内容5','我是内容5','I am content 5','','2022-02-09 14:32:28','2022-02-09 14:36:47',1,'lunar',0),
	(5,X'6D6F64756C65315F636F6E74656E745F36','/module3/src/main/res/values','我是内容6',X'6D6F64756C655F696F735F636F6E74656E7436','','我是内容6','我是内容6','I am content 6','','2022-02-09 14:32:28','2022-02-09 14:36:49',1,'lunar',0),
	(6,X'6D6F64756C65315F636F6E74656E745F39','/module5/src/main/res/values','我是内容9',X'6D6F64756C655F696F735F636F6E74656E7439','','我是内容9','我是内容9','I am content 9','','2022-02-09 14:32:28','2022-02-09 14:36:51',1,'lunar',0),
	(7,X'6D6F64756C65315F636F6E74656E745F3130','/module5/src/main/res/values','我是内容10',X'6D6F64756C655F696F735F636F6E74656E743130','','我是内容10','我是内容10','I am content 10','','2022-02-09 14:32:28','2022-02-09 14:36:53',1,'lunar',0),
	(8,X'6D6F64756C65315F636F6E74656E745F33','/mudule2/src/main/res/values','我是内容3',X'6D6F64756C655F696F735F636F6E74656E7433','','我是内容3','我是内容3','I am content 3','','2022-02-09 14:32:28','2022-02-09 14:36:56',1,'lunar',0),
	(9,X'6D6F64756C65315F636F6E74656E745F34','/mudule2/src/main/res/values','我是内容4',X'6D6F64756C655F696F735F636F6E74656E7434','','我是内容4','我是内容4','I am content 4','','2022-02-09 14:32:28','2022-02-09 14:36:58',1,'lunar',0),
	(10,X'6D6F64756C65315F636F6E74656E745F31','/module1/src/main/res/values','我是内容1',X'6D6F64756C655F696F735F636F6E74656E7431','','我是内容1','我是内容1','I am content 1','','2022-02-09 14:32:28','2022-02-09 14:37:00',1,'lunar',0),
	(11,X'6D6F64756C65315F636F6E74656E745F32','/module1/src/main/res/values','我是内容2',X'6D6F64756C655F696F735F636F6E74656E7432','','我是内容2','我是内容2','I am content 2','','2022-02-09 14:32:28','2022-02-09 14:37:02',1,'lunar',0);

/*!40000 ALTER TABLE `tb_str_translate_item` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_str_translate_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_str_translate_log`;

CREATE TABLE `tb_str_translate_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `translate_user` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '操作者',
  `translate_en` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '翻译内容',
  `translate_id` int(10) NOT NULL DEFAULT '0' COMMENT '翻译条目id',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='APP国际化翻译表 操作日志';

LOCK TABLES `tb_str_translate_log` WRITE;
/*!40000 ALTER TABLE `tb_str_translate_log` DISABLE KEYS */;

INSERT INTO `tb_str_translate_log` (`id`, `translate_user`, `translate_en`, `translate_id`, `update_time`)
VALUES
	(1,'穆仙念','Demo',1,'2022-02-09 14:36:39'),
	(2,'穆仙念','I am content 7',2,'2022-02-09 14:36:43'),
	(3,'穆仙念','I am content 8',3,'2022-02-09 14:36:45'),
	(4,'穆仙念','I am content 5',4,'2022-02-09 14:36:47'),
	(5,'穆仙念','I am content 6',5,'2022-02-09 14:36:49'),
	(6,'穆仙念','I am content 9',6,'2022-02-09 14:36:51'),
	(7,'穆仙念','I am content 10',7,'2022-02-09 14:36:53'),
	(8,'穆仙念','I am content 3',8,'2022-02-09 14:36:56'),
	(9,'穆仙念','I am content 4',9,'2022-02-09 14:36:58'),
	(10,'穆仙念','I am content 1',10,'2022-02-09 14:37:00'),
	(11,'穆仙念','I am content 2',11,'2022-02-09 14:37:02');

/*!40000 ALTER TABLE `tb_str_translate_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_str_translate_module
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_str_translate_module`;

CREATE TABLE `tb_str_translate_module` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '路径值',
  `path_label` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '路径标签',
  `add_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index_path` (`path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='APP国际化翻译 模块对应表';

LOCK TABLES `tb_str_translate_module` WRITE;
/*!40000 ALTER TABLE `tb_str_translate_module` DISABLE KEYS */;

INSERT INTO `tb_str_translate_module` (`id`, `path`, `path_label`, `add_time`)
VALUES
	(9,'/app/src/main/res/values','APP模块','2022-02-09 14:32:51'),
	(10,'/module1/src/main/res/values','module1','2022-02-09 14:33:05'),
	(11,'/module3/src/main/res/values','module3','2022-02-09 14:33:17'),
	(12,'/module4/src/main/res/values','module4','2022-02-09 14:33:14'),
	(13,'/module5/src/main/res/values','module5','2022-02-09 14:33:21'),
	(14,'/mudule2/src/main/res/values','module2','2022-02-09 14:33:24');

/*!40000 ALTER TABLE `tb_str_translate_module` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table tb_str_translate_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tb_str_translate_user`;

CREATE TABLE `tb_str_translate_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户账号',
  `password` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户昵称',
  `status` tinyint(2) DEFAULT '0' COMMENT '状态  0 正常 1 不可用',
  `token` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户token',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='APP国际化翻译用户表';

LOCK TABLES `tb_str_translate_user` WRITE;
/*!40000 ALTER TABLE `tb_str_translate_user` DISABLE KEYS */;

INSERT INTO `tb_str_translate_user` (`id`, `username`, `password`, `nickname`, `status`, `token`, `update_time`)
VALUES
	(1,'muxiannian','3210409ed847c30dc99f0ac570444431','穆仙念',0,'e8afa4ca3840482caf4f603a1607852c','2022-01-18 18:03:22');

/*!40000 ALTER TABLE `tb_str_translate_user` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
