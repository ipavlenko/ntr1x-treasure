CREATE TABLE `entries_categories` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Height` int(11) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Width` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `entries` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CategoryId` bigint(20) NOT NULL,
  `Price` DOUBLE NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Promo` longtext NOT NULL,
  `Description` longtext,
  PRIMARY KEY (`Id`),
  KEY `FK_entries_CategoryId` (`CategoryId`),
  CONSTRAINT `FK_entries_CategoryId` FOREIGN KEY (`CategoryId`) REFERENCES `entries_categories` (`Id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
