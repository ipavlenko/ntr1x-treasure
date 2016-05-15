CREATE TABLE `offers_categories` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Height` int(11) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Width` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `offers` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Description` longtext,
  `Title` varchar(255) NOT NULL,
  `CategoryId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_offers_CategoryId` (`CategoryId`),
  CONSTRAINT `FK_offers_CategoryId` FOREIGN KEY (`CategoryId`) REFERENCES `offers_categories` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
