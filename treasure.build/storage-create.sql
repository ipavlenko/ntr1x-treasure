CREATE TABLE catalog_persons_categories (Id BIGINT AUTO_INCREMENT NOT NULL, Height INTEGER, Name VARCHAR(255) NOT NULL UNIQUE, Title VARCHAR(255) NOT NULL, Width INTEGER, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE content_publications_entries (Id BIGINT AUTO_INCREMENT NOT NULL, Date DATE, Description LONGTEXT, Promo LONGTEXT NOT NULL, Title VARCHAR(255) NOT NULL, CategoryId BIGINT NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE security_resources (Id BIGINT AUTO_INCREMENT NOT NULL, Locked TINYINT(1) default 0 NOT NULL, Name VARCHAR(255) NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE security_actions (Id BIGINT AUTO_INCREMENT NOT NULL, Data LONGBLOB, DateTime DATETIME NOT NULL, Name VARCHAR(255) NOT NULL, Signature INTEGER NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE security_sessions (Id BIGINT AUTO_INCREMENT NOT NULL, Signature INTEGER NOT NULL, UserId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE content_adverts_entries (Id BIGINT AUTO_INCREMENT NOT NULL, Description LONGTEXT, Promo LONGTEXT NOT NULL, Title VARCHAR(255) NOT NULL, Url VARCHAR(255) NOT NULL, CategoryId BIGINT NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE catalog_banners_categories (Id BIGINT AUTO_INCREMENT NOT NULL, Height INTEGER, Name VARCHAR(255) NOT NULL UNIQUE, Title VARCHAR(255) NOT NULL, Width INTEGER, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE security_requests (Id BIGINT AUTO_INCREMENT NOT NULL, Token VARCHAR(255) NOT NULL UNIQUE, ActionId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE catalog_objects_categories (Id BIGINT AUTO_INCREMENT NOT NULL, Height INTEGER, Name VARCHAR(255) NOT NULL UNIQUE, Title VARCHAR(255) NOT NULL, Width INTEGER, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE catalog_objects_entries (Id BIGINT AUTO_INCREMENT NOT NULL, Description LONGTEXT, Promo LONGTEXT NOT NULL, Title VARCHAR(255) NOT NULL, CategoryId BIGINT NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE catalog_persons_entries (Id BIGINT AUTO_INCREMENT NOT NULL, Description LONGTEXT, Gender INTEGER, Name VARCHAR(255) NOT NULL, Promo LONGTEXT NOT NULL, CategoryId BIGINT NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE security_portals (Id BIGINT AUTO_INCREMENT NOT NULL, CookieDomain VARCHAR(255) NOT NULL, CookieName VARCHAR(255) NOT NULL, Name VARCHAR(255) NOT NULL UNIQUE, Title VARCHAR(255) NOT NULL, Url VARCHAR(255) NOT NULL UNIQUE, PRIMARY KEY (Id))
CREATE TABLE security_privileges (Id BIGINT AUTO_INCREMENT NOT NULL, Action VARCHAR(255) NOT NULL, ResourceId BIGINT NOT NULL, UserId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE catalog_offers_categories (Id BIGINT AUTO_INCREMENT NOT NULL, Height INTEGER, Name VARCHAR(255) NOT NULL UNIQUE, Title VARCHAR(255) NOT NULL, Width INTEGER, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE content_publications_categories (Id BIGINT AUTO_INCREMENT NOT NULL, Name VARCHAR(255) NOT NULL UNIQUE, Title VARCHAR(255) NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE catalog_banners_entries (Id BIGINT AUTO_INCREMENT NOT NULL, Description LONGTEXT, Promo LONGTEXT NOT NULL, Title VARCHAR(255) NOT NULL, CategoryId BIGINT NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE content_adverts_categories (Id BIGINT AUTO_INCREMENT NOT NULL, Height INTEGER NOT NULL, Name VARCHAR(255) NOT NULL UNIQUE, Title VARCHAR(255) NOT NULL, Width INTEGER NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE security_providers (Id BIGINT AUTO_INCREMENT NOT NULL, ClientId VARCHAR(255) NOT NULL, ClientSecret VARCHAR(255) NOT NULL, Name VARCHAR(255) NOT NULL, RedirectUri VARCHAR(255) NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE security_users (Id BIGINT AUTO_INCREMENT NOT NULL, Admin TINYINT(1) default 0 NOT NULL, Confirmed TINYINT(1) default 0 NOT NULL, Date DATE NOT NULL, Email VARCHAR(255) NOT NULL, Locked TINYINT(1) default 0 NOT NULL, Name VARCHAR(255) NOT NULL, PwdHash VARCHAR(255), Reference VARCHAR(255) NOT NULL, Source VARCHAR(255) NOT NULL, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
CREATE TABLE catalog_offers_entries (Id BIGINT AUTO_INCREMENT NOT NULL, Description LONGTEXT, Promo LONGTEXT NOT NULL, Title VARCHAR(255) NOT NULL, CategoryId BIGINT NOT NULL, ObjectId BIGINT, PortalId BIGINT NOT NULL, PRIMARY KEY (Id))
ALTER TABLE security_resources ADD CONSTRAINT UNQ_security_resources_0 UNIQUE (PortalId, Name)
ALTER TABLE security_privileges ADD CONSTRAINT UNQ_security_privileges_0 UNIQUE (UserId, ResourceId, Action)
ALTER TABLE security_providers ADD CONSTRAINT UNQ_security_providers_0 UNIQUE (PortalId, Name)
ALTER TABLE security_users ADD CONSTRAINT UNQ_security_users_0 UNIQUE (PortalId, Source, Reference, Email)
ALTER TABLE catalog_persons_categories ADD CONSTRAINT FK_catalog_persons_categories_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE content_publications_entries ADD CONSTRAINT FK_content_publications_entries_CategoryId FOREIGN KEY (CategoryId) REFERENCES content_publications_categories (Id)
ALTER TABLE content_publications_entries ADD CONSTRAINT FK_content_publications_entries_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE security_resources ADD CONSTRAINT FK_security_resources_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE security_actions ADD CONSTRAINT FK_security_actions_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE security_sessions ADD CONSTRAINT FK_security_sessions_UserId FOREIGN KEY (UserId) REFERENCES security_users (Id)
ALTER TABLE content_adverts_entries ADD CONSTRAINT FK_content_adverts_entries_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE content_adverts_entries ADD CONSTRAINT FK_content_adverts_entries_CategoryId FOREIGN KEY (CategoryId) REFERENCES content_publications_categories (Id)
ALTER TABLE catalog_banners_categories ADD CONSTRAINT FK_catalog_banners_categories_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE security_requests ADD CONSTRAINT FK_security_requests_ActionId FOREIGN KEY (ActionId) REFERENCES security_actions (Id)
ALTER TABLE catalog_objects_categories ADD CONSTRAINT FK_catalog_objects_categories_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE catalog_objects_entries ADD CONSTRAINT FK_catalog_objects_entries_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE catalog_objects_entries ADD CONSTRAINT FK_catalog_objects_entries_CategoryId FOREIGN KEY (CategoryId) REFERENCES catalog_objects_categories (Id)
ALTER TABLE catalog_persons_entries ADD CONSTRAINT FK_catalog_persons_entries_CategoryId FOREIGN KEY (CategoryId) REFERENCES catalog_persons_categories (Id)
ALTER TABLE catalog_persons_entries ADD CONSTRAINT FK_catalog_persons_entries_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE security_privileges ADD CONSTRAINT FK_security_privileges_ResourceId FOREIGN KEY (ResourceId) REFERENCES security_resources (Id)
ALTER TABLE security_privileges ADD CONSTRAINT FK_security_privileges_UserId FOREIGN KEY (UserId) REFERENCES security_users (Id)
ALTER TABLE catalog_offers_categories ADD CONSTRAINT FK_catalog_offers_categories_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE content_publications_categories ADD CONSTRAINT FK_content_publications_categories_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE catalog_banners_entries ADD CONSTRAINT FK_catalog_banners_entries_CategoryId FOREIGN KEY (CategoryId) REFERENCES catalog_banners_categories (Id)
ALTER TABLE catalog_banners_entries ADD CONSTRAINT FK_catalog_banners_entries_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE content_adverts_categories ADD CONSTRAINT FK_content_adverts_categories_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE security_providers ADD CONSTRAINT FK_security_providers_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE security_users ADD CONSTRAINT FK_security_users_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE catalog_offers_entries ADD CONSTRAINT FK_catalog_offers_entries_PortalId FOREIGN KEY (PortalId) REFERENCES security_portals (Id)
ALTER TABLE catalog_offers_entries ADD CONSTRAINT FK_catalog_offers_entries_ObjectId FOREIGN KEY (ObjectId) REFERENCES catalog_objects_entries (Id)
ALTER TABLE catalog_offers_entries ADD CONSTRAINT FK_catalog_offers_entries_CategoryId FOREIGN KEY (CategoryId) REFERENCES catalog_offers_categories (Id)
