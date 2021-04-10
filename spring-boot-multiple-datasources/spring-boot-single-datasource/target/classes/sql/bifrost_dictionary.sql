-- ----------------------------
-- Table structure for bifrost_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `bifrost_dictionary`;
CREATE TABLE `bifrost_dictionary` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dict_key` varchar(64) DEFAULT NULL COMMENT '字典key',
  `dict_value` json DEFAULT NULL COMMENT '字典值',
  `dict_group` int(2) DEFAULT '0' COMMENT '字典分组 1 默认组',
  `description` varchar(500) DEFAULT NULL COMMENT '描述信息',
  `creator` varchar(36) NOT NULL COMMENT '创建人',
  `editor` varchar(36) NOT NULL COMMENT '修改人',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_key` (`dict_key`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='字典配置';

-- ----------------------------
-- Records of bifrost_dictionary
-- ----------------------------
BEGIN;
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (1, 'AuditBusinessType', '[{"name": "type1", "dName": "天机1"}, {"name": "type2", "dName": "天机2"}]', 1, '1', 'admin', 'admin', '2021-04-10 12:52:41', '2021-04-10 12:52:41');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (3, 'scoreTrait', '[{"name": "netTrait", "dName": "入网特征", "enDName": "NetTrait"}, {"name": "tradeTrait", "dName": "交易特征", "enDName": "TradeTrait"}, {"name": "industryTrait", "dName": "行业特征", "enDName": "IndustryTrait"}, {"name": "areaTrait", "dName": "地域特征", "enDName": "AreaTrait"}, {"name": "otherTrait", "dName": "其他", "enDName": "OtherTrait"}]', 1, '评级类型', 'admin', 'admin', '2020-01-16 14:59:55', '2020-01-16 15:11:21');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (4, 'riskLevelType', '[{"name": "netTrait", "dName": "入网评级", "enDName": "NetTrait"}, {"name": "tradeTrait", "dName": "交易评级", "enDName": "TradeTrait"}, {"name": "industryTrait", "dName": "行业评级", "enDName": "IndustryetTrait"}, {"name": "areaTrait", "dName": "地域评级", "enDName": "AreaTrait"}, {"name": "otherTrait", "dName": "其他评级", "enDName": "OtherTrait"}, {"name": "final", "dName": "综合评级", "enDName": "FinalTrait"}]', 1, '评级风险类型（评级大类），每一种评级特征都有对应的评级风险类型', 'admin', 'admin', '2020-01-16 15:01:49', '2020-01-16 15:11:26');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (5, 'EventType', '[{"name": "Loan", "dName": "借款事件", "enDName": "loadevent"}]', 1, '事件类型', 'admin', 'admin', '2014-09-25 15:47:00', '2020-05-22 17:14:12');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (29, 'Loan', '[{"name": "agency", "dName": "机构代办"}, {"name": "creditRisk", "dName": "失信风险"}, {"name": "suspiciousLoan", "dName": "异常借款"}]', 1, '借款风险类型', 'admin', 'admin', '2014-09-22 17:05:43', '2020-01-16 16:18:49');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (34, 'channelList', '[{"name": "channel1", "dName": "银河系统"}, {"name": "channel2", "dName": "天策系统"}, {"name": "channel3", "dName": "个贷系统"}]', 1, '业务渠道', 'admin', 'admin', '2019-05-29 10:11:30', '2020-01-17 17:39:52');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (35, 'serviceTypeList', '[{"name": "1", "dName": "身份验证类"}, {"name": "2", "dName": "行为类"}, {"name": "3", "dName": "黑名单类"}, {"name": "4", "dName": "反欺诈类"}, {"name": "5", "dName": "信用评分类"}, {"name": "6", "dName": "人行征信类"}, {"name": "7", "dName": "其他类"}]', 1, '数据服务类型', 'admin', 'admin', '2019-05-29 10:14:53', '2020-01-17 17:40:09');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (36, 'paramDataType', '[{"name": "1", "dName": "字符型"}, {"name": "2", "dName": "整型"}, {"name": "3", "dName": "小数型"}, {"name": "4", "dName": "日期型"}, {"name": "5", "dName": "布尔型"}]', 1, '参数类型', 'admin', 'admin', '2019-05-29 10:21:45', '2020-01-17 17:40:31');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (37, 'scoreRiskLevel', '[{"name": "high", "dName": "高风险", "enDName": "High Risk"}, {"name": "middle", "dName": "中等风险", "enDName": "Middle Risk"}, {"name": "normal", "dName": "一般风险", "enDName": "Normal Risk"}, {"name": "low", "dName": "低风险", "enDName": "Low Risk"}]', 1, '风险等级', 'admin', 'admin', '2019-03-29 20:21:50', '2020-02-12 16:26:08');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (41, 'providerTypeList', '[{"name": "1", "dName": "身份验证类"}, {"name": "2", "dName": "行为类"}, {"name": "3", "dName": "黑名单类"}, {"name": "4", "dName": "反欺诈类"}, {"name": "5", "dName": "信用评分类"}, {"name": "6", "dName": "人行征信类"}, {"name": "7", "dName": "其他类"}]', 1, '数据服务类型', 'admin', 'admin', '2019-03-29 20:21:50', '2020-02-12 16:26:08');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor, gmt_create, gmt_modified) VALUES (42, 'checkTag', '[{"name": "weimaobanli9", "dName": "伪冒办理"}, {"name": "qizhashijian10", "dName": "欺诈事件"}, {"name": "yisiqizha", "dName": "疑似欺诈"}]', 1, '', 'admin', 'admin', '2020-04-26 18:50:58', '2020-05-23 15:09:01');
INSERT INTO `bifrost_dictionary`(id, dict_key, dict_value, dict_group, description, creator, editor) VALUES (43, 'xiaomishuke', '[{"name": "002", "dName": "小米数科", "enDName": "xiaomi"}]', 1, '小米数科', 'ck', 'ck');
COMMIT;

-- 8.0  MySQL json 类型插入时字符串必须转义的，否则插入出错
--
-- BEGIN;
-- INSERT INTO `bifrost_dictionary` VALUES (1, 'AuditBusinessType', '[{\"name\": \"type1\", \"dName\": \"天机1\"}, {\"name\": \"type2\", \"dName\": \"天机2\"}]', 1, '1', 'admin', 'admin', '2021-04-10 12:52:41', '2021-04-10 12:52:41');
-- INSERT INTO `bifrost_dictionary` VALUES (3, 'scoreTrait', '[{\"name\": \"netTrait\", \"dName\": \"入网特征\", \"enDName\": \"NetTrait\"}, {\"name\": \"tradeTrait\", \"dName\": \"交易特征\", \"enDName\": \"TradeTrait\"}, {\"name\": \"industryTrait\", \"dName\": \"行业特征\", \"enDName\": \"IndustryTrait\"}, {\"name\": \"areaTrait\", \"dName\": \"地域特征\", \"enDName\": \"AreaTrait\"}, {\"name\": \"otherTrait\", \"dName\": \"其他\", \"enDName\": \"OtherTrait\"}]', 1, '评级类型', 'admin', 'admin', '2020-01-16 14:59:55', '2020-01-16 15:11:21');
-- INSERT INTO `bifrost_dictionary` VALUES (4, 'riskLevelType', '[{\"name\": \"netTrait\", \"dName\": \"入网评级\", \"enDName\": \"NetTrait\"}, {\"name\": \"tradeTrait\", \"dName\": \"交易评级\", \"enDName\": \"TradeTrait\"}, {\"name\": \"industryTrait\", \"dName\": \"行业评级\", \"enDName\": \"IndustryetTrait\"}, {\"name\": \"areaTrait\", \"dName\": \"地域评级\", \"enDName\": \"AreaTrait\"}, {\"name\": \"otherTrait\", \"dName\": \"其他评级\", \"enDName\": \"OtherTrait\"}, {\"name\": \"final\", \"dName\": \"综合评级\", \"enDName\": \"FinalTrait\"}]', 1, '评级风险类型（评级大类），每一种评级特征都有对应的评级风险类型', 'admin', 'admin', '2020-01-16 15:01:49', '2020-01-16 15:11:26');
-- INSERT INTO `bifrost_dictionary` VALUES (5, 'EventType', '[{\"name\": \"Loan\", \"dName\": \"借款事件\", \"enDName\": \"loadevent\"}]', 1, '事件类型', 'admin', 'admin', '2014-09-25 15:47:00', '2020-05-22 17:14:12');
-- INSERT INTO `bifrost_dictionary` VALUES (29, 'Loan', '[{\"name\": \"agency\", \"dName\": \"机构代办\"}, {\"name\": \"creditRisk\", \"dName\": \"失信风险\"}, {\"name\": \"suspiciousLoan\", \"dName\": \"异常借款\"}]', 1, '借款风险类型', 'admin', 'admin', '2014-09-22 17:05:43', '2020-01-16 16:18:49');
-- INSERT INTO `bifrost_dictionary` VALUES (34, 'channelList', '[{\"name\": \"channel1\", \"dName\": \"银河系统\"}, {\"name\": \"channel2\", \"dName\": \"天策系统\"}, {\"name\": \"channel3\", \"dName\": \"个贷系统\"}]', 1, '业务渠道', 'admin', 'admin', '2019-05-29 10:11:30', '2020-01-17 17:39:52');
-- INSERT INTO `bifrost_dictionary` VALUES (35, 'serviceTypeList', '[{\"name\": \"1\", \"dName\": \"身份验证类\"}, {\"name\": \"2\", \"dName\": \"行为类\"}, {\"name\": \"3\", \"dName\": \"黑名单类\"}, {\"name\": \"4\", \"dName\": \"反欺诈类\"}, {\"name\": \"5\", \"dName\": \"信用评分类\"}, {\"name\": \"6\", \"dName\": \"人行征信类\"}, {\"name\": \"7\", \"dName\": \"其他类\"}]', 1, '数据服务类型', 'admin', 'admin', '2019-05-29 10:14:53', '2020-01-17 17:40:09');
-- INSERT INTO `bifrost_dictionary` VALUES (36, 'paramDataType', '[{\"name\": \"1\", \"dName\": \"字符型\"}, {\"name\": \"2\", \"dName\": \"整型\"}, {\"name\": \"3\", \"dName\": \"小数型\"}, {\"name\": \"4\", \"dName\": \"日期型\"}, {\"name\": \"5\", \"dName\": \"布尔型\"}]', 1, '参数类型', 'admin', 'admin', '2019-05-29 10:21:45', '2020-01-17 17:40:31');
-- INSERT INTO `bifrost_dictionary` VALUES (37, 'scoreRiskLevel', '[{\"name\": \"high\", \"dName\": \"高风险\", \"enDName\": \"High Risk\"}, {\"name\": \"middle\", \"dName\": \"中等风险\", \"enDName\": \"Middle Risk\"}, {\"name\": \"normal\", \"dName\": \"一般风险\", \"enDName\": \"Normal Risk\"}, {\"name\": \"low\", \"dName\": \"低风险\", \"enDName\": \"Low Risk\"}]', 1, '风险等级', 'admin', 'admin', '2019-03-29 20:21:50', '2020-02-12 16:26:08');
-- INSERT INTO `bifrost_dictionary` VALUES (41, 'providerTypeList', '[{\"name\": \"1\", \"dName\": \"身份验证类\"}, {\"name\": \"2\", \"dName\": \"行为类\"}, {\"name\": \"3\", \"dName\": \"黑名单类\"}, {\"name\": \"4\", \"dName\": \"反欺诈类\"}, {\"name\": \"5\", \"dName\": \"信用评分类\"}, {\"name\": \"6\", \"dName\": \"人行征信类\"}, {\"name\": \"7\", \"dName\": \"其他类\"}]', 1, '数据服务类型', 'admin', 'admin', '2019-05-29 10:14:53', '2020-01-17 17:40:09');
-- INSERT INTO `bifrost_dictionary` VALUES (42, 'checkTag', '[{\"name\": \"weimaobanli9\", \"dName\": \"伪冒办理\"}, {\"name\": \"qizhashijian10\", \"dName\": \"欺诈事件\"}, {\"name\": \"yisiqizha\", \"dName\": \"疑似欺诈\"}]', 1, '', 'admin', 'admin', '2020-04-26 18:50:58', '2020-05-23 15:09:01');
-- INSERT INTO `bifrost_dictionary` VALUES (43, 'xiaomishuke', '[{\"name\": \"002\", \"dName\": \"小米数科\", \"enDName\": \"xiaomi\"}]', 1, '小米数科', 'ck', 'ck', '2020-09-16 14:14:23', '2020-09-17 11:18:58');
-- COMMIT;


SET FOREIGN_KEY_CHECKS = 1;