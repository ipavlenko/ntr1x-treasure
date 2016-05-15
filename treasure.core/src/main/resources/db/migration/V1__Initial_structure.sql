CREATE TABLE `security_portals` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `CookieDomain` varchar(255) NOT NULL,
  `CookieName` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Url` varchar(255) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`),
  UNIQUE KEY `Url` (`Url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `security_resources` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Locked` tinyint(1) NOT NULL DEFAULT '0',
  `Name` varchar(255) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UNQ_security_resources_0` (`PortalId`,`Name`),
  CONSTRAINT `FK_security_resources_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `security_users` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Admin` tinyint(1) NOT NULL DEFAULT '0',
  `Confirmed` tinyint(1) NOT NULL DEFAULT '0',
  `Date` date NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Locked` tinyint(1) NOT NULL DEFAULT '0',
  `Name` varchar(255) NOT NULL,
  `PwdHash` varchar(255) DEFAULT NULL,
  `Reference` varchar(255) NOT NULL,
  `Source` varchar(255) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UNQ_security_users_0` (`PortalId`,`Source`,`Reference`,`Email`),
  CONSTRAINT `FK_security_users_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `security_actions` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Data` longblob,
  `DateTime` datetime NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Signature` int(11) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_security_actions_PortalId` (`PortalId`),
  CONSTRAINT `FK_security_actions_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `security_privileges` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Action` varchar(255) NOT NULL,
  `ResourceId` bigint(20) NOT NULL,
  `UserId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UNQ_security_privileges_0` (`UserId`,`ResourceId`,`Action`),
  KEY `FK_security_privileges_ResourceId` (`ResourceId`),
  CONSTRAINT `FK_security_privileges_ResourceId` FOREIGN KEY (`ResourceId`) REFERENCES `security_resources` (`Id`),
  CONSTRAINT `FK_security_privileges_UserId` FOREIGN KEY (`UserId`) REFERENCES `security_users` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `security_providers` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ClientId` varchar(255) NOT NULL,
  `ClientSecret` varchar(255) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `RedirectUri` varchar(255) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UNQ_security_providers_0` (`PortalId`,`Name`),
  CONSTRAINT `FK_security_providers_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `security_requests` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Token` varchar(255) NOT NULL,
  `ActionId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Token` (`Token`),
  KEY `FK_security_requests_ActionId` (`ActionId`),
  CONSTRAINT `FK_security_requests_ActionId` FOREIGN KEY (`ActionId`) REFERENCES `security_actions` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `security_sessions` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Signature` int(11) NOT NULL,
  `UserId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_security_sessions_UserId` (`UserId`),
  CONSTRAINT `FK_security_sessions_UserId` FOREIGN KEY (`UserId`) REFERENCES `security_users` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `catalog_banners_categories` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Height` int(11) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Width` int(11) DEFAULT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`),
  KEY `FK_catalog_banners_categories_PortalId` (`PortalId`),
  CONSTRAINT `FK_catalog_banners_categories_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `catalog_banners_entries` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Description` longtext,
  `Promo` longtext NOT NULL,
  `Title` varchar(255) NOT NULL,
  `CategoryId` bigint(20) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_catalog_banners_entries_CategoryId` (`CategoryId`),
  KEY `FK_catalog_banners_entries_PortalId` (`PortalId`),
  CONSTRAINT `FK_catalog_banners_entries_CategoryId` FOREIGN KEY (`CategoryId`) REFERENCES `catalog_banners_categories` (`Id`),
  CONSTRAINT `FK_catalog_banners_entries_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `catalog_objects_categories` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Height` int(11) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Width` int(11) DEFAULT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`),
  KEY `FK_catalog_objects_categories_PortalId` (`PortalId`),
  CONSTRAINT `FK_catalog_objects_categories_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE `catalog_objects_entries` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Description` longtext,
  `Promo` longtext NOT NULL,
  `Title` varchar(255) NOT NULL,
  `CategoryId` bigint(20) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_catalog_objects_entries_PortalId` (`PortalId`),
  KEY `FK_catalog_objects_entries_CategoryId` (`CategoryId`),
  CONSTRAINT `FK_catalog_objects_entries_CategoryId` FOREIGN KEY (`CategoryId`) REFERENCES `catalog_objects_categories` (`Id`),
  CONSTRAINT `FK_catalog_objects_entries_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

CREATE TABLE `catalog_offers_categories` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Height` int(11) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Width` int(11) DEFAULT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`),
  KEY `FK_catalog_offers_categories_PortalId` (`PortalId`),
  CONSTRAINT `FK_catalog_offers_categories_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `catalog_offers_entries` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Description` longtext,
  `Promo` longtext NOT NULL,
  `Title` varchar(255) NOT NULL,
  `CategoryId` bigint(20) NOT NULL,
  `ObjectId` bigint(20) DEFAULT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_catalog_offers_entries_PortalId` (`PortalId`),
  KEY `FK_catalog_offers_entries_ObjectId` (`ObjectId`),
  KEY `FK_catalog_offers_entries_CategoryId` (`CategoryId`),
  CONSTRAINT `FK_catalog_offers_entries_CategoryId` FOREIGN KEY (`CategoryId`) REFERENCES `catalog_offers_categories` (`Id`),
  CONSTRAINT `FK_catalog_offers_entries_ObjectId` FOREIGN KEY (`ObjectId`) REFERENCES `catalog_objects_entries` (`Id`),
  CONSTRAINT `FK_catalog_offers_entries_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `catalog_persons_categories` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Height` int(11) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Width` int(11) DEFAULT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`),
  KEY `FK_catalog_persons_categories_PortalId` (`PortalId`),
  CONSTRAINT `FK_catalog_persons_categories_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `catalog_persons_entries` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Description` longtext,
  `Gender` int(11) DEFAULT NULL,
  `Name` varchar(255) NOT NULL,
  `Promo` longtext NOT NULL,
  `CategoryId` bigint(20) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_catalog_persons_entries_CategoryId` (`CategoryId`),
  KEY `FK_catalog_persons_entries_PortalId` (`PortalId`),
  CONSTRAINT `FK_catalog_persons_entries_CategoryId` FOREIGN KEY (`CategoryId`) REFERENCES `catalog_persons_categories` (`Id`),
  CONSTRAINT `FK_catalog_persons_entries_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `content_adverts_categories` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Height` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Width` int(11) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`),
  KEY `FK_content_adverts_categories_PortalId` (`PortalId`),
  CONSTRAINT `FK_content_adverts_categories_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `content_adverts_entries` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Description` longtext,
  `Promo` longtext NOT NULL,
  `Title` varchar(255) NOT NULL,
  `Url` varchar(255) NOT NULL,
  `CategoryId` bigint(20) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_content_adverts_entries_PortalId` (`PortalId`),
  KEY `FK_content_adverts_entries_CategoryId` (`CategoryId`),
  CONSTRAINT `FK_content_adverts_entries_CategoryId` FOREIGN KEY (`CategoryId`) REFERENCES `content_adverts_categories` (`Id`),
  CONSTRAINT `FK_content_adverts_entries_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `content_publications_categories` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Name` (`Name`),
  KEY `FK_content_publications_categories_PortalId` (`PortalId`),
  CONSTRAINT `FK_content_publications_categories_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `content_publications_entries` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `Date` date DEFAULT NULL,
  `Description` longtext,
  `Promo` longtext NOT NULL,
  `Title` varchar(255) NOT NULL,
  `CategoryId` bigint(20) NOT NULL,
  `PortalId` bigint(20) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_content_publications_entries_CategoryId` (`CategoryId`),
  KEY `FK_content_publications_entries_PortalId` (`PortalId`),
  CONSTRAINT `FK_content_publications_entries_CategoryId` FOREIGN KEY (`CategoryId`) REFERENCES `content_publications_categories` (`Id`),
  CONSTRAINT `FK_content_publications_entries_PortalId` FOREIGN KEY (`PortalId`) REFERENCES `security_portals` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
