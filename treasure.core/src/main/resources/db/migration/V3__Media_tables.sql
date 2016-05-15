CREATE TABLE `media_categories` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Height` int(11) DEFAULT NULL,
  `Width` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `Name` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `media` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Category` bigint(20) NOT NULL,
  `Title` varchar(255) DEFAULT NULL,
  `Description` longtext,
  `Published` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_media_category_key` (`Category`),
  CONSTRAINT `FK_media_category_key` FOREIGN KEY (`Category`) REFERENCES `media_categories` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
