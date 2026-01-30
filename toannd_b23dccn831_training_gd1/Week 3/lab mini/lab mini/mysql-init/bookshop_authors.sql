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
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `biography` mediumtext,
  `country` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `created_by` varchar(100) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` enum('FEMALE','MALE','OTHER') DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `status` enum('ACTIVE','DELETED','INACTIVE') DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `updated_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authors`
--

LOCK TABLES `authors` WRITE;
/*!40000 ALTER TABLE `authors` DISABLE KEYS */;
INSERT INTO `authors` VALUES (1,'Bác sĩ tâm thần học, tác giả cuốn \'Dopamine Nation\' nổi tiếng về nghiện và hành vi con người.','Mỹ','2025-11-04 09:17:16.607979','admin@gmail.com','1972-04-21','FEMALE','Anna Lembke','ACTIVE','2025-11-04 09:17:16.607979',NULL),(2,'Giáo sư thần kinh học và tâm lý học, nổi tiếng với tác phẩm \'Why We Sleep\' nghiên cứu về giấc ngủ.','Anh','2025-11-04 09:21:02.130173','admin@gmail.com','1973-10-25','MALE','Matthew Walker','ACTIVE','2025-11-04 09:21:02.130173',NULL),(4,'Tác giả và học giả Trung Quốc, chuyên nghiên cứu về triết học và văn hóa phương Đông.','Trung Quốc','2025-11-04 09:22:32.968124','admin@gmail.com','1968-07-11','MALE','Vương Đào','ACTIVE','2025-11-04 09:22:32.968124',NULL),(5,'Nhà hóa sinh và tác giả sách \'Glucose Revolution\' về khoa học dinh dưỡng và sức khỏe.','Pháp','2025-11-04 09:23:16.722277','admin@gmail.com','1990-03-12','FEMALE','Jessie Inchauspé','ACTIVE','2025-11-04 09:23:16.722277',NULL),(6,'Nhóm tác giả người Trung Quốc chuyên biên soạn các ấn phẩm khoa học kỹ thuật và văn hóa.','Trung Quốc','2025-11-04 09:23:50.860186','admin@gmail.com','1975-05-15','MALE','Dịch Dương','ACTIVE','2025-11-04 09:23:50.860186',NULL),(7,'Nhà nghiên cứu và tác giả người Việt, được biết đến với các công trình về y học và triết lý sống xanh.','Việt Nam','2025-11-04 09:23:59.905450','admin@gmail.com','1948-09-19','MALE','Ngô Đức Vượng','ACTIVE','2025-11-04 09:23:59.905450',NULL),(8,'Bộ ba tác giả hợp tác biên soạn sách chuyên ngành giáo dục và phát triển bản thân.','Trung Quốc','2025-11-04 09:29:15.802656','admin@gmail.com','1978-06-09','MALE','Cao Ngọc Vĩ','ACTIVE','2025-11-04 09:29:15.802656',NULL),(9,'Giáo sư tâm lý học xã hội tại Đại học NYU, nổi tiếng với các nghiên cứu về đạo đức và xã hội hiện đại.','Mỹ','2025-11-04 09:29:24.387473','admin@gmail.com','1963-10-19','MALE','Jonathan Haidt','ACTIVE','2025-11-04 09:29:24.387473',NULL),(10,'Tác giả người Việt nổi bật với những cuốn sách triết lý sống và tư duy phát triển bản thân.','Việt Nam','2025-11-04 09:29:30.406201','admin@gmail.com','1987-12-03','MALE','Ngô Sa Thạch','ACTIVE','2025-11-04 09:29:30.406201',NULL),(11,'Hai cha con nhà khoa học nổi tiếng, đồng tác giả nghiên cứu \'The China Study\' về dinh dưỡng và sức khỏe.','Mỹ','2025-11-04 09:29:41.918507','admin@gmail.com','1934-06-14','MALE','T Collin Campbell','ACTIVE','2025-11-04 09:29:41.918507',NULL),(12,'Nhà khoa học nhận thức, diễn giả và tác giả về tư duy chiến lược và ra quyết định hợp lý.','Mỹ','2025-11-04 09:30:10.204465','admin@gmail.com','1982-02-09','MALE','Dr. Gleb Tsipursky','ACTIVE','2025-11-04 09:30:10.204465',NULL),(13,'Tác giả và dịch giả trẻ, nổi bật trong lĩnh vực sách kỹ năng sống và phát triển bản thân.','Việt Nam','2025-11-04 09:30:15.607996','admin@gmail.com','1989-07-08','FEMALE','Hải Yến','ACTIVE','2025-11-04 09:30:15.607996',NULL),(14,'Giáo sư sinh học phân tử David Sinclair và nhà báo Matthew Laplante, đồng tác giả cuốn \'Lifespan\' nghiên cứu về sự lão hóa và tuổi thọ con người.','Mỹ','2025-11-04 09:30:28.079019','admin@gmail.com','1969-06-30','MALE','TS David A. Sinclair','ACTIVE','2025-11-04 09:30:28.079019',NULL),(15,'Tác giả và giảng viên người Việt, nổi bật với các công trình về kỹ năng sống và phát triển tư duy sáng tạo.','Việt Nam','2025-11-04 09:30:37.187821','admin@gmail.com','1982-09-15','MALE','Phan Bảo Long','ACTIVE','2025-11-04 09:30:37.187821',NULL),(16,'Nhóm tác giả người Trung Quốc, chuyên biên soạn các sách về giáo dục, văn hóa và nghiên cứu xã hội học đương đại.','Trung Quốc','2025-11-04 09:30:48.702117','admin@gmail.com','1975-04-20','MALE','Dư Hạo','ACTIVE','2025-11-04 09:30:48.702117',NULL),(17,'Nhóm biên soạn sách giáo khoa Ngữ văn Việt Nam, có nhiều năm kinh nghiệm trong lĩnh vực giảng dạy và nghiên cứu ngôn ngữ.','Việt Nam','2025-11-04 09:31:10.133072','admin@gmail.com','1960-03-18','MALE','Hoàng Văn Vân','ACTIVE','2025-11-04 09:31:10.133072',NULL),(18,'Nhóm tác giả thuộc Bộ Giáo dục và Đào tạo, tham gia biên soạn chương trình Ngữ văn cấp THPT tại Việt Nam.','Việt Nam','2025-11-04 09:31:24.952410','admin@gmail.com','1965-07-10','MALE','Bùi Mạnh Hùng','ACTIVE','2025-11-04 09:31:24.952410',NULL),(20,'Tác giả nữ người Trung Quốc, được biết đến qua các tác phẩm văn học và truyện thiếu nhi nhân văn.','Trung Quốc','2025-11-04 09:32:23.707544','admin@gmail.com','1979-02-09','FEMALE','Khương Lệ Bình','ACTIVE','2025-11-04 09:32:23.707544',NULL),(21,'Nhóm tác giả người Trung Quốc, nổi bật với các tác phẩm dành cho thiếu nhi và sách giáo dục văn học.','Trung Quốc','2025-11-04 09:32:44.361914','admin@gmail.com','1980-06-22','FEMALE','Tô Anh Hà','ACTIVE','2025-11-04 09:32:44.361914',NULL),(22,'Tác giả và nhà biên soạn người Việt Nam, chuyên viết các tác phẩm về kỹ năng sống và tư duy hiện đại.','Việt Nam','2025-11-04 09:33:13.624059','admin@gmail.com','1974-03-21','MALE','Chính Bình','ACTIVE','2025-11-04 09:33:13.624059',NULL),(23,'Đạo sư yoga người Ấn Độ, giám đốc trung tâm Sivananda Yoga Farm, chuyên giảng dạy thiền định và tâm linh học phương Đông.','Ấn Độ','2025-11-04 09:33:35.604338','admin@gmail.com','1957-06-13','FEMALE','Swami Sitaramananda','ACTIVE','2025-11-04 09:33:35.604338',NULL),(24,'Vận động viên và tác giả người Anh, nổi tiếng với những thí nghiệm thể thao cực hạn và cuốn sách \'The Art of Resilience\'.','Anh','2025-11-04 09:33:39.750826','admin@gmail.com','1985-10-01','MALE','Ross Edgley','ACTIVE','2025-11-04 09:33:39.750826',NULL),(25,'Đạo sư yoga người Ấn Độ, người sáng lập các trung tâm Sivananda Yoga và tác giả nhiều cuốn sách về thiền định và tâm linh.','Ấn Độ','2025-11-04 09:33:44.099711','admin@gmail.com','1927-12-31','MALE','Swami Vishnu Devananda','ACTIVE','2025-11-04 09:33:44.099711',NULL),(26,'Tác giả trẻ người Việt Nam, chuyên viết sách kỹ năng sống và các tác phẩm truyền cảm hứng cho giới trẻ.','Việt Nam','2025-11-04 09:33:50.666576','admin@gmail.com','1990-12-22','MALE','Mai Luân','ACTIVE','2025-11-04 09:33:50.666576',NULL),(27,'Đạo sư, triết gia và nhà tâm linh người Ấn Độ, nổi tiếng với những bài giảng về thiền định, tự do tư tưởng và tình yêu.','Ấn Độ','2025-11-04 09:34:00.064021','admin@gmail.com','1931-12-11','MALE','Osho','ACTIVE','2025-11-04 09:34:00.064021',NULL),(28,'Bậc thầy yoga Ấn Độ, người sáng lập trường phái Iyengar Yoga, được công nhận là một trong những người có ảnh hưởng nhất trong lĩnh vực yoga hiện đại.','Ấn Độ','2025-11-04 09:34:27.436412','admin@gmail.com','1918-12-14','MALE','B.K.S. Iyengar','ACTIVE','2025-11-04 09:34:27.436412',NULL),(29,'Lãnh tụ cách mạng và Chủ tịch nước Việt Nam Dân chủ Cộng hòa, người đặt nền móng cho nền độc lập dân tộc Việt Nam.','Việt Nam','2025-11-04 09:34:40.157145','admin@gmail.com','1890-05-19','MALE','Hồ Chí Minh','ACTIVE','2025-11-04 09:34:40.157145',NULL),(30,'Nhà văn và dịch giả người Việt, chuyên biên soạn các tác phẩm giáo dục và nghiên cứu văn học hiện đại.','Việt Nam','2025-11-04 09:34:46.783891','admin@gmail.com','1955-10-17','MALE','Nguyễn Ngọc Bích','ACTIVE','2025-11-04 09:34:46.783891',NULL),(31,'Nhà nghiên cứu và tác giả người Việt Nam, chuyên viết về tư tưởng triết học và quản trị xã hội.','Việt Nam','2025-11-04 09:35:00.039477','admin@gmail.com','1984-02-14','MALE','Kiều Mai Sơn','ACTIVE','2025-11-04 09:35:00.039477',NULL),(32,'Tác giả người Mỹ, nổi tiếng với cuốn hồi ký \'Educated\' kể về hành trình vượt qua tuổi thơ không được đến trường để vươn tới tri thức.','Mỹ','2025-11-04 09:35:20.780245','admin@gmail.com','1986-09-30','FEMALE','Tara Westover','ACTIVE','2025-11-04 09:35:20.780245',NULL),(33,'Giáo sư truyền thông người Mỹ, nổi tiếng với cuốn \'Amusing Ourselves to Death\' phê phán ảnh hưởng của truyền hình và công nghệ.','Mỹ','2025-11-04 09:36:41.889690','admin@gmail.com','1931-03-08','MALE','Neil Postman','ACTIVE','2025-11-04 09:36:41.889690',NULL);
/*!40000 ALTER TABLE `authors` ENABLE KEYS */;
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
