/*
 Navicat Premium Data Transfer

 Source Server         : vmware-mysql8
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 192.168.31.125:3306
 Source Schema         : penguin

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 30/04/2024 13:30:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for penguin_article
-- ----------------------------
DROP TABLE IF EXISTS `penguin_article`;
CREATE TABLE `penguin_article`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL COMMENT '发表用户',
  `article_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文章内容html格式',
  `article_content_md` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '文章内容Markdown格式',
  `article_newstime` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `article_status` int NULL DEFAULT NULL COMMENT '文章状态 0已发布1草稿2回收站',
  `article_summary` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章摘要',
  `article_thumbnail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '略缩图',
  `article_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章标题',
  `article_type` int NULL DEFAULT NULL COMMENT '文章类型0原创1转载',
  `article_post` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'post文章 page页面',
  `article_comment` int NULL DEFAULT NULL COMMENT '是否开启评论 0开启1不开启',
  `article_updatetime` datetime NULL DEFAULT NULL COMMENT '文章最后修改时间',
  `article_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章路径',
  `article_views` bigint NULL DEFAULT 0 COMMENT '访问量统计',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `MAYDAY_ARTICLE_URL`(`article_url`) USING BTREE,
  INDEX `MAYDAY_ARTICLE_USERID`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_article_category
-- ----------------------------
DROP TABLE IF EXISTS `penguin_article_category`;
CREATE TABLE `penguin_article_category`  (
  `article_id` int NOT NULL,
  `category_id` bigint NOT NULL,
  INDEX `MAYDAY_ARTILE_ID`(`article_id`) USING BTREE,
  INDEX `MAYDAY_ARTILE_CATEGORY_ID`(`category_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `penguin_article_tag`;
CREATE TABLE `penguin_article_tag`  (
  `article_id` int NOT NULL,
  `tag_id` bigint NOT NULL,
  INDEX `MAYDAY_ARTILE_ID`(`article_id`) USING BTREE,
  INDEX `MAYDAY_ARTILE_TAG_ID`(`tag_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_attachment
-- ----------------------------
DROP TABLE IF EXISTS `penguin_attachment`;
CREATE TABLE `penguin_attachment`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `picture_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片名称',
  `picture_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片路径',
  `picture_small_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '略缩图',
  `picture_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片类型',
  `picture_create_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传时间',
  `picture_size` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件大小',
  `picture_suffix` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后缀',
  `picture_wh` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '尺寸',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1397 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_category
-- ----------------------------
DROP TABLE IF EXISTS `penguin_category`;
CREATE TABLE `penguin_category`  (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `category_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类路径',
  `category_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_link
-- ----------------------------
DROP TABLE IF EXISTS `penguin_link`;
CREATE TABLE `penguin_link`  (
  `link_id` int NOT NULL AUTO_INCREMENT,
  `link_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `link_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `link_logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接logo',
  `link_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`link_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_logs
-- ----------------------------
DROP TABLE IF EXISTS `penguin_logs`;
CREATE TABLE `penguin_logs`  (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `log_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `log_content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `log_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `log_date` datetime NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2433 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_menu
-- ----------------------------
DROP TABLE IF EXISTS `penguin_menu`;
CREATE TABLE `penguin_menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT,
  `menu_icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `menu_sort` int NULL DEFAULT NULL COMMENT '排序',
  `menu_target` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打开方式',
  `menu_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_options
-- ----------------------------
DROP TABLE IF EXISTS `penguin_options`;
CREATE TABLE `penguin_options`  (
  `option_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设置名',
  `option_value` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '设置内容',
  PRIMARY KEY (`option_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_tag
-- ----------------------------
DROP TABLE IF EXISTS `penguin_tag`;
CREATE TABLE `penguin_tag`  (
  `tag_id` int NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签名称',
  `tag_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签路径',
  PRIMARY KEY (`tag_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_theme
-- ----------------------------
DROP TABLE IF EXISTS `penguin_theme`;
CREATE TABLE `penguin_theme`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `theme_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题名(url)',
  `theme_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题描述',
  `theme_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主题预览图',
  `theme_status` int NULL DEFAULT 0 COMMENT '0未启用1已启用',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguin_user
-- ----------------------------
DROP TABLE IF EXISTS `penguin_user`;
CREATE TABLE `penguin_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `login_enable` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否禁用登录',
  `login_error` int NULL DEFAULT NULL COMMENT '登录失败次数',
  `login_last_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `user_portrait` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `user_explain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  `user_display_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示名称',
  `user_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `user_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `google_secret` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '谷歌私钥',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguins_order
-- ----------------------------
DROP TABLE IF EXISTS `penguins_order`;
CREATE TABLE `penguins_order`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_no` bigint NULL DEFAULT NULL COMMENT '订单编号',
  `order_type` int NULL DEFAULT NULL COMMENT '订单类型：1充值 2卖出',
  `order_status` int NULL DEFAULT NULL COMMENT '订单状态',
  `order_time` datetime NULL DEFAULT NULL COMMENT '下单时间',
  `order_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '下单用户',
  `order_user_real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '下单用实名',
  `order_user_account` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '下单用户账号',
  `order_user_nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '下单用户昵称',
  `order_price_cny` decimal(10, 2) NULL DEFAULT NULL COMMENT '下单金额CNY',
  `order_price_saro` decimal(20, 4) NULL DEFAULT NULL COMMENT '下单金额saro',
  `order_unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '下单单价',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单实付金额',
  `order_transactor_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '交易人',
  `order_transactor_account` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '交易用户账号',
  `transactor_proof_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '交易凭证路径',
  `order_actual_saro` decimal(20, 4) NULL DEFAULT NULL COMMENT '实际saro',
  `order_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `user_snapshot` varchar(5000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户快照(ps:json)',
  `merchant_snapshot` varchar(5000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '商家快照(ps:json)',
  `confirm_time` datetime NULL DEFAULT NULL COMMENT '放行时间',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `update_time` datetime NULL DEFAULT NULL,
  `back_message` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '退货情况信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2474 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for penguins_user
-- ----------------------------
DROP TABLE IF EXISTS `penguins_user`;
CREATE TABLE `penguins_user`  (
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户id',
  `user_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户名称',
  `user_account` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户账号',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户昵称',
  `user_phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户电话',
  `user_phone2` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户电话2',
  `user_phone3` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户电话3',
  `user_status` int NULL DEFAULT NULL COMMENT '用户状态：1正常',
  `user_role` int NULL DEFAULT NULL COMMENT '用户角色： 1用户 2商家',
  `bank_favour` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '收款人',
  `user_bank` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户开户行',
  `user_bank_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用户银行卡号',
  `user_bank_branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '开户支行',
  `user_saro_number` decimal(20, 4) NULL DEFAULT NULL COMMENT '用户saro数量',
  `recharge_unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '充值单价（每个saro的价格）',
  `recovery_unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '回收单价（每个saro的价格）',
  `min_recharge` decimal(10, 2) NULL DEFAULT NULL COMMENT '最小充值数，单位CNY',
  `max_recharge` decimal(10, 2) NULL DEFAULT NULL COMMENT '最大充值数，单位CNY',
  `user_access_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '临时令牌',
  `user_trans_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '交易密码',
  `user_sec` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '正割',
  `user_trans_pwd_error_count` int NULL DEFAULT 0 COMMENT '交易密码错误次数',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '交易用户表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
