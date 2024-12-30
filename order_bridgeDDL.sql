-- --------------------------------------------------------
-- 호스트:                          222.112.28.226
-- 서버 버전:                        10.3.39-MariaDB-0ubuntu0.20.04.2-log - Ubuntu 20.04
-- 서버 OS:                        debian-linux-gnu
-- HeidiSQL 버전:                  12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- order_bridge 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `order_bridge` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `order_bridge`;

-- 테이블 order_bridge.tb_bom_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_bom_item` (
  `bom_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_item_seq` bigint(20) NOT NULL,
  `child_item_seq` bigint(20) NOT NULL,
  `bom_child_item_quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`bom_item_seq`),
  KEY `FK_tb_item_TO_tb_bom_item_1` (`parent_item_seq`),
  KEY `FK_tb_item_TO_tb_bom_item_2` (`child_item_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_bom_item_1` FOREIGN KEY (`parent_item_seq`) REFERENCES `tb_item` (`item_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_bom_item_2` FOREIGN KEY (`child_item_seq`) REFERENCES `tb_item` (`item_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_client 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_client` (
  `client_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_seq` bigint(20) NOT NULL,
  `client_registration_no` varchar(100) NOT NULL,
  `client_name` varchar(100) NOT NULL,
  `client_phone_no` varchar(30) DEFAULT NULL,
  `client_email` varchar(100) DEFAULT NULL,
  `client_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `client_mod_date` timestamp NULL DEFAULT NULL,
  `client_status` varchar(10) NOT NULL DEFAULT 'ACTIVE',
  `client_Representative` varchar(20) NOT NULL,
  PRIMARY KEY (`client_seq`),
  KEY `FK_tb_user_TO_tb_client_1` (`user_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_client_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_invoice 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_invoice` (
  `invoice_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `sales_order_seq` bigint(20) NOT NULL,
  `user_seq` bigint(20) NOT NULL,
  `invoice_name` varchar(50) NOT NULL,
  `invoice_status` varchar(30) DEFAULT NULL,
  `invoice_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `invoice_mod_date` timestamp NULL DEFAULT NULL,
  `invoice_sale_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `invoice_extended_price` int(11) DEFAULT NULL,
  `invoice_total_quantity` int(11) DEFAULT NULL,
  `invoice_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`invoice_seq`),
  KEY `FK_tb_sales_order_TO_tb_invoice_1` (`sales_order_seq`),
  KEY `FK_tb_user_TO_tb_invoice_1` (`user_seq`),
  CONSTRAINT `FK_tb_sales_order_TO_tb_invoice_1` FOREIGN KEY (`sales_order_seq`) REFERENCES `tb_sales_order` (`sales_order_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_invoice_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_invoice_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_invoice_item` (
  `invoice_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `invoice_seq` bigint(20) NOT NULL,
  `item_seq` bigint(20) NOT NULL,
  `invoice_item_quantity` int(11) NOT NULL,
  `invoice_item_price` int(11) NOT NULL,
  `invoice_item_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`invoice_item_seq`),
  KEY `FK_tb_invoice_TO_tb_invoice_item_1` (`invoice_seq`),
  KEY `FK_tb_item_TO_tb_invoice_item_1` (`item_seq`),
  CONSTRAINT `FK_tb_invoice_TO_tb_invoice_item_1` FOREIGN KEY (`invoice_seq`) REFERENCES `tb_invoice` (`invoice_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_invoice_item_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_item` (
  `item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_seq` bigint(20) NOT NULL,
  `item_unit_seq` bigint(20) NOT NULL,
  `item_name` varchar(100) NOT NULL,
  `item_division` varchar(10) NOT NULL COMMENT '재료, 제품',
  `item_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `item_mod_date` timestamp NULL DEFAULT NULL,
  `item_expiration_hour` int(11) DEFAULT NULL COMMENT '시간',
  `item_image_url` varchar(255) DEFAULT NULL,
  `item_price` int(11) DEFAULT NULL,
  `item_note` varchar(255) DEFAULT NULL,
  `item_status` varchar(20) NOT NULL COMMENT '상태변경',
  `warehouse_seq` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`item_seq`),
  KEY `FK_tb_user_TO_tb_item_1` (`user_seq`),
  KEY `FK_tb_item_unit_TO_tb_item_1` (`item_unit_seq`),
  KEY `FK_tb_item_TO_tb_warehouse_1` (`warehouse_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_warehouse_1` FOREIGN KEY (`warehouse_seq`) REFERENCES `tb_warehouse` (`warehouse_seq`),
  CONSTRAINT `FK_tb_item_unit_TO_tb_item_1` FOREIGN KEY (`item_unit_seq`) REFERENCES `tb_item_unit` (`item_unit_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_item_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_item_inventory 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_item_inventory` (
  `item_inventory_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_seq` bigint(20) NOT NULL,
  `item_inventory_quantity_received` int(11) NOT NULL,
  `item_inventory_remain_amount` int(11) DEFAULT NULL,
  `item_inventory_receipt_date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `item_inventory_expiration_date` timestamp NULL DEFAULT NULL,
  `item_inventory_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`item_inventory_seq`),
  KEY `FK_tb_item_TO_tb_item_inventory_1` (`item_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_item_inventory_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_item_unit 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_item_unit` (
  `item_unit_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_unit_title` varchar(10) NOT NULL,
  PRIMARY KEY (`item_unit_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_notification 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_notification` (
  `notification_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `notification_type` varchar(50) DEFAULT NULL,
  `notification_another_seq` bigint(20) NOT NULL,
  `notification_title` varchar(50) NOT NULL,
  `notification_content` varchar(255) NOT NULL,
  `notification_read_yn` char(1) DEFAULT NULL,
  PRIMARY KEY (`notification_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_production_disbursement 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_production_disbursement` (
  `production_disbursement_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_seq` bigint(20) NOT NULL,
  `work_order_seq` bigint(20) DEFAULT NULL,
  `production_disbursement_name` varchar(50) NOT NULL,
  `production_disbursement_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `production_disbursement_mod_date` timestamp NULL DEFAULT NULL,
  `production_disbursement_departure_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `production_disbursement_total_quantity` int(11) NOT NULL,
  `production_disbursement_status` varchar(10) NOT NULL COMMENT 'BEFORE, AFTER, DELETE',
  `production_disbursement_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`production_disbursement_seq`),
  KEY `FK_tb_user_TO_tb_production_disbursement_1` (`user_seq`),
  KEY `FK_tb_work_order_TO_tb_production_disbursement_1` (`work_order_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_production_disbursement_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`),
  CONSTRAINT `FK_tb_work_order_TO_tb_production_disbursement_1` FOREIGN KEY (`work_order_seq`) REFERENCES `tb_work_order` (`work_order_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_production_disbursement_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_production_disbursement_item` (
  `production_disbursement_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `production_disbursement_seq` bigint(20) NOT NULL,
  `item_seq` bigint(20) NOT NULL,
  `ingredients_warehouse_seq` bigint(20) NOT NULL,
  `production_disbursement_quantity` int(11) NOT NULL,
  `production_disbursement_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`production_disbursement_item_seq`),
  KEY `FK_tb_production_disbursement_TO_tb_production_disbursement_item` (`production_disbursement_seq`),
  KEY `FK_tb_item_TO_tb_production_disbursement_item_1` (`item_seq`),
  KEY `FK_tb_warehouse_TO_tb_production_disbursement_item` (`ingredients_warehouse_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_production_disbursement_item_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`),
  CONSTRAINT `FK_tb_production_disbursement_TO_tb_production_disbursement_item` FOREIGN KEY (`production_disbursement_seq`) REFERENCES `tb_production_disbursement` (`production_disbursement_seq`),
  CONSTRAINT `FK_tb_warehouse_TO_tb_production_disbursement_item` FOREIGN KEY (`ingredients_warehouse_seq`) REFERENCES `tb_warehouse` (`warehouse_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_production_receiving 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_production_receiving` (
  `production_receiving_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_seq` bigint(20) NOT NULL,
  `production_receiving_name` varchar(50) NOT NULL,
  `production_receiving_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `production_receiving_mod_date` timestamp NULL DEFAULT NULL,
  `production_receiving_extended_price` int(11) DEFAULT NULL,
  `production_receiving_note` varchar(255) DEFAULT NULL,
  `production_receiving_status` varchar(50) DEFAULT NULL COMMENT '입고 전, 입고 완료',
  `production_receiving_receipt_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`production_receiving_seq`),
  KEY `FK_tb_user_TO_tb_production_receiving_1` (`user_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_production_receiving_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_production_receiving_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_production_receiving_item` (
  `production_receiving_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `warehouse_seq` bigint(20) NOT NULL,
  `item_seq` bigint(20) NOT NULL,
  `production_receiving_seq` bigint(20) NOT NULL,
  `production_receiving_item_quantity` int(11) NOT NULL,
  `production_receiving_unit_price` int(11) DEFAULT NULL,
  `production_receiving_item_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`production_receiving_item_seq`),
  KEY `FK_tb_item_TO_tb_production_receiving_item_1` (`item_seq`),
  KEY `FK_tb_production_receiving_TO_tb_production_receiving_item_1` (`production_receiving_seq`) USING BTREE,
  KEY `FK_tb_warehouse_TO_tb_production_receiving_item_1` (`warehouse_seq`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_purchase 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_purchase` (
  `purchase_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_order_seq` bigint(20) NOT NULL,
  `user_seq` bigint(20) NOT NULL,
  `warehouse_seq` bigint(20) DEFAULT NULL,
  `purchase_name` varchar(50) NOT NULL,
  `purchase_contract_date` timestamp NULL DEFAULT NULL,
  `purchase_reg_date` timestamp NULL DEFAULT NULL,
  `purchase_mod_date` timestamp NULL DEFAULT NULL,
  `purchase_status` varchar(30) NOT NULL COMMENT '입고 전, 입고 완료',
  `purchase_extended_price` int(11) DEFAULT NULL,
  `purchase_note` varchar(255) DEFAULT NULL,
  `purchase_total_quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`purchase_seq`),
  KEY `FK_tb_purchase_order_TO_tb_purchase_1` (`purchase_order_seq`),
  KEY `FK_tb_user_TO_tb_purchase_1` (`user_seq`),
  CONSTRAINT `FK_tb_purchase_order_TO_tb_purchase_1` FOREIGN KEY (`purchase_order_seq`) REFERENCES `tb_purchase_order` (`purchase_order_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_purchase_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_purchase_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_purchase_item` (
  `purchase_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_seq` bigint(20) NOT NULL,
  `purchase_seq` bigint(20) NOT NULL,
  `purchase_item_quantity` int(11) DEFAULT NULL,
  `purchase_item_price` int(11) DEFAULT NULL,
  `purchase_item_receipt_date` timestamp NULL DEFAULT NULL,
  `purchase_item_status` varchar(30) DEFAULT NULL,
  `purchase_item_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`purchase_item_seq`),
  KEY `FK_tb_item_TO_tb_purchase_item_1` (`item_seq`),
  KEY `FK_tb_purchase_TO_tb_purchase_item_1` (`purchase_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_purchase_item_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`),
  CONSTRAINT `FK_tb_purchase_TO_tb_purchase_item_1` FOREIGN KEY (`purchase_seq`) REFERENCES `tb_purchase` (`purchase_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_purchase_order 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_purchase_order` (
  `purchase_order_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_seq` bigint(20) NOT NULL,
  `sales_order_seq` bigint(20) DEFAULT NULL,
  `user_seq` bigint(20) NOT NULL,
  `purchase_order_name` varchar(50) NOT NULL,
  `purchase_order_status` varchar(20) DEFAULT NULL COMMENT '결재 전, 결재 후, 반려',
  `purchase_order_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `purchase_order_mod_date` timestamp NULL DEFAULT NULL,
  `purchase_order_due_date` timestamp NULL DEFAULT NULL,
  `purchase_order_target_due_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `purchase_order_extended_price` int(11) DEFAULT NULL,
  `purchase_order_note` varchar(255) DEFAULT NULL,
  `purchase_order_total_quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`purchase_order_seq`),
  KEY `FK_tb_client_TO_tb_purchase_order_1` (`client_seq`),
  KEY `FK_tb_sales_order_TO_tb_purchase_order_1` (`sales_order_seq`),
  KEY `FK_tb_user_TO_tb_purchase_order_1` (`user_seq`),
  CONSTRAINT `FK_tb_client_TO_tb_purchase_order_1` FOREIGN KEY (`client_seq`) REFERENCES `tb_client` (`client_seq`),
  CONSTRAINT `FK_tb_sales_order_TO_tb_purchase_order_1` FOREIGN KEY (`sales_order_seq`) REFERENCES `tb_sales_order` (`sales_order_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_purchase_order_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_purchase_order_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_purchase_order_item` (
  `purchase_order_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `purchase_order_seq` bigint(20) NOT NULL,
  `item_seq` bigint(20) NOT NULL,
  `purchase_order_item_quantity` int(11) NOT NULL,
  `purchase_order_item_price` int(11) NOT NULL,
  `purchase_order_item_note` varchar(255) DEFAULT NULL,
  `purchase_order_item_extended_price` int(11) DEFAULT NULL,
  PRIMARY KEY (`purchase_order_item_seq`),
  KEY `FK_tb_purchase_order_TO_tb_purchase_order_item_1` (`purchase_order_seq`),
  KEY `FK_tb_item_TO_tb_purchase_order_item_1` (`item_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_purchase_order_item_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`),
  CONSTRAINT `FK_tb_purchase_order_TO_tb_purchase_order_item_1` FOREIGN KEY (`purchase_order_seq`) REFERENCES `tb_purchase_order` (`purchase_order_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_quotation 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_quotation` (
  `quotation_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_seq` bigint(20) NOT NULL,
  `client_seq` bigint(20) NOT NULL COMMENT '사업자번호',
  `quotation_name` varchar(50) NOT NULL,
  `quotation_status` varchar(10) NOT NULL COMMENT '결재 전, 결재 후, 반려',
  `quotation_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `quotation_mod_date` timestamp NULL DEFAULT NULL,
  `quotation_quotation_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `quotation_effective_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '등록일 기준 30일 뒤',
  `quotation_extended_price` int(11) DEFAULT NULL,
  `quotation_total_quantity` int(11) DEFAULT NULL,
  `quotation_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`quotation_seq`),
  KEY `FK_tb_user_TO_tb_quotation_1` (`user_seq`),
  KEY `FK_tb_client_TO_tb_quotation_1` (`client_seq`),
  CONSTRAINT `FK_tb_client_TO_tb_quotation_1` FOREIGN KEY (`client_seq`) REFERENCES `tb_client` (`client_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_quotation_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_quotation_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_quotation_item` (
  `quotation_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `quotation_seq` bigint(20) NOT NULL,
  `item_seq` bigint(20) NOT NULL,
  `quotation_item_quantity` int(11) NOT NULL,
  `quotation_item_price` int(11) NOT NULL,
  `quotation_item_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`quotation_item_seq`),
  KEY `FK_tb_quotation_TO_tb_quotation_item_1` (`quotation_seq`),
  KEY `FK_tb_item_TO_tb_quotation_item_1` (`item_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_quotation_item_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`),
  CONSTRAINT `FK_tb_quotation_TO_tb_quotation_item_1` FOREIGN KEY (`quotation_seq`) REFERENCES `tb_quotation` (`quotation_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_sales_order 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_sales_order` (
  `sales_order_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `quotation_seq` bigint(20) NOT NULL,
  `user_seq` bigint(20) NOT NULL,
  `client_seq` bigint(20) NOT NULL,
  `sales_order_name` varchar(50) NOT NULL,
  `sales_order_status` varchar(30) NOT NULL COMMENT '결재전, 결재후, 생산중, 생산완료, 출하완료',
  `sales_order_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `sales_order_mod_date` timestamp NULL DEFAULT NULL,
  `sales_order_order_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `sales_order_due_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `sales_order_extended_price` int(11) DEFAULT NULL,
  `sales_order_total_quantity` int(11) DEFAULT NULL,
  `sales_order_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sales_order_seq`),
  KEY `FK_tb_quotation_TO_tb_sales_order_1` (`quotation_seq`),
  KEY `FK_tb_user_TO_tb_sales_order_1` (`user_seq`),
  KEY `FK_tb_client_TO_tb_sales_order_1` (`client_seq`),
  CONSTRAINT `FK_tb_client_TO_tb_sales_order_1` FOREIGN KEY (`client_seq`) REFERENCES `tb_client` (`client_seq`),
  CONSTRAINT `FK_tb_quotation_TO_tb_sales_order_1` FOREIGN KEY (`quotation_seq`) REFERENCES `tb_quotation` (`quotation_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_sales_order_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_sales_order_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_sales_order_item` (
  `sales_order_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `sales_order_seq` bigint(20) NOT NULL,
  `item_seq` bigint(20) NOT NULL,
  `sales_order_item_quantity` int(11) NOT NULL,
  `sales_order_item_price` int(11) NOT NULL,
  `sales_order_item_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sales_order_item_seq`),
  KEY `FK_tb_sales_order_TO_tb_sales_order_item_1` (`sales_order_seq`),
  KEY `FK_tb_item_TO_tb_sales_order_item_1` (`item_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_sales_order_item_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`),
  CONSTRAINT `FK_tb_sales_order_TO_tb_sales_order_item_1` FOREIGN KEY (`sales_order_seq`) REFERENCES `tb_sales_order` (`sales_order_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_shipping_instruction 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_shipping_instruction` (
  `shipping_instruction_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `sales_order_seq` bigint(20) NOT NULL,
  `user_seq` bigint(20) NOT NULL,
  `shipping_instruction_name` varchar(50) NOT NULL,
  `shipping_instruction_address` varchar(50) NOT NULL,
  `shipping_instruction_status` varchar(10) NOT NULL COMMENT '결재 전, 결재 후',
  `shipping_instruction_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `shipping_instruction_mod_date` timestamp NULL DEFAULT NULL,
  `shipping_instruction_scheduled_shipment_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `shipping_instruction_total_quantity` int(11) NOT NULL,
  `shipping_instruction_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`shipping_instruction_seq`),
  KEY `FK_tb_sales_order_TO_tb_shipping_instruction_1` (`sales_order_seq`),
  KEY `FK_tb_user_TO_tb_shipping_instruction_1` (`user_seq`),
  CONSTRAINT `FK_tb_sales_order_TO_tb_shipping_instruction_1` FOREIGN KEY (`sales_order_seq`) REFERENCES `tb_sales_order` (`sales_order_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_shipping_instruction_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_shipping_instruction_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_shipping_instruction_item` (
  `shipping_instruction_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `shipping_instruction_seq` bigint(20) NOT NULL,
  `item_seq` bigint(20) NOT NULL,
  `shipping_instruction_item_quantity` int(11) NOT NULL,
  `shipping_instruction_item_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`shipping_instruction_item_seq`),
  KEY `FK_tb_shipping_instruction_TO_tb_shipping_instruction_item_1` (`shipping_instruction_seq`),
  KEY `FK_tb_item_TO_tb_shipping_instruction_item_1` (`item_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_shipping_instruction_item_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`),
  CONSTRAINT `FK_tb_shipping_instruction_TO_tb_shipping_instruction_item_1` FOREIGN KEY (`shipping_instruction_seq`) REFERENCES `tb_shipping_instruction` (`shipping_instruction_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_shipping_slip 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_shipping_slip` (
  `shipping_slip_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `shipping_instruction_seq` bigint(20) NOT NULL,
  `user_seq` bigint(20) NOT NULL,
  `shipping_slip_name` varchar(50) NOT NULL,
  `shipping_slip_status` varchar(30) DEFAULT NULL,
  `shipping_slip_address` varchar(50) NOT NULL,
  `shipping_slip_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `shipping_slip_mod_date` timestamp NULL DEFAULT NULL,
  `shipping_slip_shipping_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `shipping_slip_total_quantity` int(11) NOT NULL,
  `shipping_slip_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`shipping_slip_seq`),
  KEY `FK_tb_shipping_instruction_TO_tb_shipping_slip_1` (`shipping_instruction_seq`),
  KEY `FK_tb_user_TO_tb_shipping_slip_1` (`user_seq`),
  CONSTRAINT `FK_tb_shipping_instruction_TO_tb_shipping_slip_1` FOREIGN KEY (`shipping_instruction_seq`) REFERENCES `tb_shipping_instruction` (`shipping_instruction_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_shipping_slip_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_shipping_slip_item 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_shipping_slip_item` (
  `shipping_slip_item_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `shipping_slip_seq` bigint(20) NOT NULL,
  `item_seq` bigint(20) NOT NULL,
  `shipping_slip_item_quantity` int(11) NOT NULL,
  `shipping_slip_item_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`shipping_slip_item_seq`),
  KEY `FK_tb_shipping_slip_TO_tb_shipping_slip_item_1` (`shipping_slip_seq`),
  KEY `FK_tb_item_TO_tb_shipping_slip_item_1` (`item_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_shipping_slip_item_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`),
  CONSTRAINT `FK_tb_shipping_slip_TO_tb_shipping_slip_item_1` FOREIGN KEY (`shipping_slip_seq`) REFERENCES `tb_shipping_slip` (`shipping_slip_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_user 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_user` (
  `user_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_employee_no` varchar(20) NOT NULL,
  `user_pwd` varchar(200) NOT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  `user_profile_img_url` varchar(200) DEFAULT NULL,
  `user_role` varchar(200) NOT NULL,
  `user_phone_no` varchar(20) DEFAULT NULL,
  `user_email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_warehouse 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_warehouse` (
  `warehouse_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_seq` bigint(20) NOT NULL,
  `warehouse_name` varchar(100) DEFAULT NULL,
  `warehouse_type` varchar(10) NOT NULL COMMENT '공장, 창고',
  `warehouse_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `warehouse_mod_date` timestamp NULL DEFAULT NULL,
  `warehouse_note` varchar(255) DEFAULT NULL,
  `warehouse_status` varchar(10) NOT NULL DEFAULT 'ACTIVE' COMMENT '창고 상태',
  PRIMARY KEY (`warehouse_seq`),
  KEY `FK_tb_user_TO_tb_warehouse_1` (`user_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_warehouse_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

-- 테이블 order_bridge.tb_work_order 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_work_order` (
  `work_order_seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_seq` bigint(20) NOT NULL,
  `user_seq` bigint(20) NOT NULL,
  `sales_order_seq` bigint(20) NOT NULL,
  `warehouse_seq` bigint(20) NOT NULL,
  `item_seq` bigint(20) NOT NULL,
  `production_receiving_seq` bigint(20) DEFAULT NULL,
  `work_order_name` varchar(50) NOT NULL,
  `work_order_reg_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `work_order_mod_date` timestamp NULL DEFAULT NULL,
  `work_order_indicated_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `work_order_end_date` timestamp NULL DEFAULT NULL,
  `work_order_due_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `work_order_indicated_quantity` int(11) NOT NULL,
  `work_order_work_quantity` int(11) NOT NULL,
  `work_order_status` varchar(10) NOT NULL COMMENT 'BEFORE, AFTER, ONGOING, COMPLETE, STOP, DELETE',
  `work_order_price` int(11) DEFAULT NULL,
  `work_order_note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`work_order_seq`),
  KEY `FK_tb_client_TO_tb_work_order_1` (`client_seq`),
  KEY `FK_tb_user_TO_tb_work_order_1` (`user_seq`),
  KEY `FK_tb_sales_order_TO_tb_work_order_1` (`sales_order_seq`),
  KEY `FK_tb_warehouse_TO_tb_work_order_1` (`warehouse_seq`),
  KEY `FK_tb_item_TO_tb_work_order_1` (`item_seq`),
  KEY `FK_tb_production_receiving_TO_tb_work_order_1` (`production_receiving_seq`),
  CONSTRAINT `FK_tb_client_TO_tb_work_order_1` FOREIGN KEY (`client_seq`) REFERENCES `tb_client` (`client_seq`),
  CONSTRAINT `FK_tb_item_TO_tb_work_order_1` FOREIGN KEY (`item_seq`) REFERENCES `tb_item` (`item_seq`),
  CONSTRAINT `FK_tb_production_receiving_TO_tb_work_order_1` FOREIGN KEY (`production_receiving_seq`) REFERENCES `tb_production_receiving` (`production_receiving_seq`),
  CONSTRAINT `FK_tb_sales_order_TO_tb_work_order_1` FOREIGN KEY (`sales_order_seq`) REFERENCES `tb_sales_order` (`sales_order_seq`),
  CONSTRAINT `FK_tb_user_TO_tb_work_order_1` FOREIGN KEY (`user_seq`) REFERENCES `tb_user` (`user_seq`),
  CONSTRAINT `FK_tb_warehouse_TO_tb_work_order_1` FOREIGN KEY (`warehouse_seq`) REFERENCES `tb_warehouse` (`warehouse_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 내보낼 데이터가 선택되어 있지 않습니다.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
