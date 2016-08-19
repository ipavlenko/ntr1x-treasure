CREATE TABLE `resources` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `DTYPE` varchar(31) DEFAULT NULL,
  `Name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `posts` (
  `ResourceId` bigint(20) NOT NULL,
  `Content` longtext,
  `Promo` longtext,
  `Published` datetime DEFAULT NULL,
  `Title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ResourceId`),
  KEY `INDEX_posts_Published` (`Published`),
  CONSTRAINT `FK_posts_ResourceId` FOREIGN KEY (`ResourceId`) REFERENCES `resources` (`Id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tags` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Value` varchar(255) DEFAULT NULL,
  `RelatesId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_tags_RelatesId` (`RelatesId`),
  CONSTRAINT `FK_tags_RelatesId` FOREIGN KEY (`RelatesId`) REFERENCES `resources` (`Id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
