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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `description` mediumtext,
  `name` varchar(200) NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE') DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'2025-11-04 09:57:38.621070','admin@gmail.com','','Khoa học kỹ thuật','ACTIVE','2025-11-04 09:57:38.621070',NULL),(2,'2025-11-04 09:57:45.676102','admin@gmail.com','','Giáo khoa - Tham khảo','ACTIVE','2025-11-04 09:57:45.676102',NULL),(3,'2025-11-04 09:57:50.737354','admin@gmail.com','','Sách học ngoại ngữ','ACTIVE','2025-11-04 09:57:50.737354',NULL),(4,'2025-11-04 09:57:55.689037','admin@gmail.com','','Sách tiếng Việt','ACTIVE','2025-11-04 09:57:55.689037',NULL),(5,'2025-11-04 09:58:00.739226','admin@gmail.com','','Manga - Comic','ACTIVE','2025-11-04 09:58:00.739226',NULL),(6,'2025-11-04 09:58:41.517314','admin@gmail.com','','Chính Trị - Pháp Lý - Triết Học','ACTIVE','2025-11-04 09:58:41.517314',NULL),(7,'2025-11-04 09:58:46.000198','admin@gmail.com','','Tiểu Sử Hồi Ký','ACTIVE','2025-11-04 09:58:46.000198',NULL),(8,'2025-11-04 09:58:51.787841','admin@gmail.com','','Tâm lý - Kỹ năng sống','ACTIVE','2025-11-04 09:58:51.787841',NULL),(9,'2025-11-04 09:58:55.618028','admin@gmail.com','','Kinh Tế','ACTIVE','2025-11-04 09:58:55.618028',NULL),(10,'2025-11-04 10:00:54.416976','admin@gmail.com','','Văn học','ACTIVE','2025-11-04 10:00:54.416976',NULL);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-11 17:19:30
