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
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `district` varchar(100) DEFAULT NULL,
  `is_default` bit(1) DEFAULT NULL,
  `phone` varchar(15) NOT NULL,
  `receiver_name` varchar(100) NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE') DEFAULT NULL,
  `street` varchar(255) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `ward` varchar(100) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1fa36y2oqhao3wgg2rw1pi459` (`user_id`),
  CONSTRAINT `FK1fa36y2oqhao3wgg2rw1pi459` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,'Hà Nội','2025-11-04 13:34:55.269382','admin@gmail.com','Quận Hà Đông',_binary '','0987654320','Trần Văn An','ACTIVE','Số 99, Đường Thanh Bình','2025-11-04 13:58:53.964705','admin@gmail.com','Phường Mộ Lao',1),(2,'Hòa Bình','2025-11-04 13:36:00.593990','admin@gmail.com','Huyện Lạc Sơn',_binary '\0','0987654320','Nguyễn Đức Toàn','ACTIVE','Số 99, Đường Thanh Niên','2025-11-04 13:36:00.593990',NULL,'Thị Trấn Vụ Bản',1),(3,'Hà Nội','2025-11-04 13:39:08.697555','admin@gmail.com','Quận Đống Đa',_binary '\0','0987654320','Nguyễn Thị Trang','ACTIVE','Số 42, Phố Lê Thanh Nghị','2025-11-04 13:49:55.465994','admin@gmail.com','Phường Trung Kính',1);
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
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
