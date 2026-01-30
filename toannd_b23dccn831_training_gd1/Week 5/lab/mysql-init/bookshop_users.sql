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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `email` varchar(200) NOT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE') DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  `role_id` bigint NOT NULL,
  `refresh_token` mediumtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,'2025-11-04 09:09:29.513477','',NULL,'admin@gmail.com',NULL,'admin','$2a$10$jsqPoxGT7TznJL3JOanpqucFmAgv22Rf2TuRtcezMV.Yn4vLD1aAa','0987654321','ACTIVE','2025-11-07 16:40:49.088057','admin@gmail.com',1,'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJleHAiOjE3ODg0NTM2NDksImlhdCI6MTc2MjUzMzY0OX0.G5aVS1QfoR8RN5sfX09TXpp0oVT6bruLdLsg21vG_dUbQP61avNXN__z4yN2JnDqFcCnnGMDH-MPVbu24mL7Ow'),(5,NULL,'2025-11-09 09:53:36.714879','anonymousUser',NULL,'luckyn135@gmail.com',NULL,'Nguyễn Đức Toàn','$2a$10$jeracurPP4voixErAj71n.miHmECTjeyEjUHsQ73ISu8cju2oKavG','0987728992','ACTIVE','2025-11-09 10:57:32.675156','anonymousUser',2,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
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
