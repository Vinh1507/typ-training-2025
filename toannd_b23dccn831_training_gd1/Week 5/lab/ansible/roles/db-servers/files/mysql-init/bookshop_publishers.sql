-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bookshop
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `publishers`
--

DROP TABLE IF EXISTS `publishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `publishers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(300) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE') DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK181cduo2nrqq2jfgrs3ls23p7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publishers`
--

LOCK TABLES `publishers` WRITE;
/*!40000 ALTER TABLE `publishers` DISABLE KEYS */;
INSERT INTO `publishers` VALUES (1,'46 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội','2025-11-04 09:39:26.074249','admin@gmail.com','info@thegioipublishers.vn','NXB Thế Giới','02438254068','ACTIVE','2025-11-04 09:39:26.074249',NULL),(2,'175 Giảng Võ, Đống Đa, Hà Nội','2025-11-04 09:39:41.996570','admin@gmail.com','contact@nxblaodong.vn','NXB Lao Động','02435122006','ACTIVE','2025-11-04 09:39:41.996570',NULL),(3,'62 Nguyễn Thị Minh Khai, Quận 3, TP. Hồ Chí Minh','2025-11-04 09:39:52.591457','admin@gmail.com','info@nxbtonghoptphcm.com.vn','NXB Tổng Hợp TP. Hồ Chí Minh','02839303272','ACTIVE','2025-11-04 09:39:52.591457',NULL),(4,'Số 10 Đoàn Trần Nghiệp, Hai Bà Trưng, Hà Nội','2025-11-04 09:39:56.742848','admin@gmail.com','contact@nxbdantri.vn','NXB Dân Trí','02439743456','ACTIVE','2025-11-04 09:39:56.742848',NULL),(5,'64 Bà Triệu, Hoàn Kiếm, Hà Nội','2025-11-04 09:40:00.975162','admin@gmail.com','info@nxbthanhnien.vn','NXB Thanh Niên','02438223739','ACTIVE','2025-11-04 09:40:00.975162',NULL),(6,'Số 40 Ngô Quyền, Hoàn Kiếm, Hà Nội','2025-11-04 09:40:05.120120','admin@gmail.com','sales@nxbhongduc.vn','NXB Hồng Đức','02439361220','ACTIVE','2025-11-04 09:40:05.120120',NULL),(7,'655 Phạm Văn Đồng, Bắc Từ Liêm, Hà Nội','2025-11-04 09:40:11.403909','admin@gmail.com','contact@nxbcongthuong.vn','NXB Công Thương','02437682999','ACTIVE','2025-11-04 09:40:11.403909',NULL),(8,'115 Trần Duy Hưng, Cầu Giấy, Hà Nội','2025-11-04 09:40:16.693660','admin@gmail.com','contact@nxbthongtintruyenthong.vn','NXB Thông Tin Và Truyền Thông','02437832180','ACTIVE','2025-11-04 09:40:16.693660',NULL),(9,'81 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội','2025-11-04 09:40:20.844568','admin@gmail.com','info@gdvn.vn','NXB Giáo Dục Việt Nam','02438221485','ACTIVE','2025-11-04 09:40:20.844568',NULL),(10,'280 An Dương Vương, Quận 5, TP. Hồ Chí Minh','2025-11-04 09:40:44.290690','admin@gmail.com','pub@dhsphcm.edu.vn','NXB Đại Học Sư Phạm','02838350498','ACTIVE','2025-11-04 09:40:44.290690',NULL),(11,'65 Nguyễn Du, Hai Bà Trưng, Hà Nội','2025-11-04 09:40:50.089577','admin@gmail.com','info@nxbhoinhavan.vn','NXB Hội Nhà Văn','02439435288','ACTIVE','2025-11-04 09:40:50.089577',NULL),(12,'4 Tôn Thất Thuyết, Cầu Giấy, Hà Nội','2025-11-04 09:40:57.263796','admin@gmail.com','contact@nxbhanoi.vn','NXB Hà Nội','02437622015','ACTIVE','2025-11-04 09:40:57.263796',NULL),(13,'55 Quang Trung, Hai Bà Trưng, Hà Nội','2025-11-04 09:41:02.134795','admin@gmail.com','contact@kimdong.vn','NXB Kim Đồng','02439439053','ACTIVE','2025-11-04 09:41:02.134795',NULL),(14,'161B Lý Chính Thắng, Quận 3, TP. Hồ Chí Minh','2025-11-04 09:41:08.658902','admin@gmail.com','info@nxbtre.com.vn','NXB Trẻ','02839313706','ACTIVE','2025-11-04 09:41:08.658902',NULL),(15,'6/86 Duy Tân, Cầu Giấy, Hà Nội','2025-11-04 09:41:12.103060','admin@gmail.com','info@ctqg.vn','NXB Chính Trị Quốc Gia Sự Thật','02437562608','ACTIVE','2025-11-04 09:41:12.103060',NULL),(16,'33 Lý Nam Đế, Hoàn Kiếm, Hà Nội','2025-11-04 09:41:24.152922','admin@gmail.com','contact@nxbqdnd.vn','NXB Quân Đội Nhân Dân','02438255253','ACTIVE','2025-11-04 09:41:24.152922',NULL),(17,'1A Hoàng Diệu, Quận 4, TP. Hồ Chí Minh','2025-11-04 09:41:29.886130','admin@gmail.com','contact@kinhtehcm.vn','NXB Kinh Tế TP. Hồ Chí Minh','02839402662','ACTIVE','2025-11-04 09:41:29.886130',NULL),(18,'35 Nguyễn Thị Minh Khai, Quận 1, TP. Hồ Chí Minh','2025-11-04 09:41:33.708701','admin@gmail.com','hcm@ctqg.vn','NXB CTQGST (Chính Trị Quốc Gia ST - TP.HCM)','02839301123','ACTIVE','2025-11-04 09:41:33.708701',NULL),(19,'18 Hàng Chuối, Hai Bà Trưng, Hà Nội','2025-11-04 09:42:00.074748','admin@gmail.com','office@vanhoc.vn','NXB Văn Học','02438223704','ACTIVE','2025-11-04 09:42:00.074748',NULL);
/*!40000 ALTER TABLE `publishers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-11 17:19:31
