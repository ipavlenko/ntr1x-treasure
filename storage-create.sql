CREATE TABLE resources (Id BIGINT AUTO_INCREMENT NOT NULL, DTYPE VARCHAR(31), Alias VARCHAR(255), PRIMARY KEY (Id))
CREATE TABLE accounts (ResourceId BIGINT NOT NULL, Email VARCHAR(255), Pwdhash VARCHAR(255), Random INTEGER, PRIMARY KEY (ResourceId))
CREATE UNIQUE INDEX INDEX_accounts_Email ON accounts (Email)
CREATE TABLE attachments (ResourceId BIGINT NOT NULL, Dir VARCHAR(255), Original VARCHAR(255), Path VARCHAR(255), RelateId BIGINT NOT NULL, PRIMARY KEY (ResourceId))
CREATE UNIQUE INDEX INDEX_attachments_Dir_Path ON attachments (Dir, Path)
CREATE TABLE categories (ResourceId BIGINT NOT NULL, Description LONGTEXT, Title VARCHAR(255), RelateId BIGINT NOT NULL, PRIMARY KEY (ResourceId))
CREATE TABLE comments (ResourceId BIGINT NOT NULL, Message LONGTEXT, Published DATETIME, AccountId BIGINT NOT NULL, RelateId BIGINT NOT NULL, PRIMARY KEY (ResourceId))
CREATE TABLE goods (ResourceId BIGINT NOT NULL, Content LONGTEXT, Promo LONGTEXT, Title VARCHAR(255), RelateId BIGINT NOT NULL, PRIMARY KEY (ResourceId))
CREATE TABLE grants (ResourceId BIGINT NOT NULL, Action VARCHAR(255), Pattern VARCHAR(255), AccountId BIGINT NOT NULL, PRIMARY KEY (ResourceId))
CREATE UNIQUE INDEX INDEX_grants_Pattern_Action ON grants (Pattern, Action)
CREATE TABLE likes (ResourceId BIGINT NOT NULL, Value INTEGER, AccountId BIGINT NOT NULL, RelateId BIGINT NOT NULL, PRIMARY KEY (ResourceId))
CREATE TABLE publications (ResourceId BIGINT NOT NULL, Content LONGTEXT, Promo LONGTEXT, Published DATETIME, Title VARCHAR(255), PRIMARY KEY (ResourceId))
CREATE INDEX INDEX_publications_Published ON publications (Published)
CREATE TABLE resources_categories (ResourceId BIGINT NOT NULL, CategoryId BIGINT NOT NULL, RelateId BIGINT NOT NULL, PRIMARY KEY (ResourceId))
CREATE TABLE sessions (ResourceId BIGINT NOT NULL, Signature INTEGER, AccountId BIGINT NOT NULL, PRIMARY KEY (ResourceId))
CREATE TABLE tags (ResourceId BIGINT NOT NULL, Value VARCHAR(255), RelateId BIGINT NOT NULL, PRIMARY KEY (ResourceId))
ALTER TABLE accounts ADD CONSTRAINT FK_accounts_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE attachments ADD CONSTRAINT FK_attachments_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE attachments ADD CONSTRAINT FK_attachments_RelateId FOREIGN KEY (RelateId) REFERENCES resources (Id)
ALTER TABLE categories ADD CONSTRAINT FK_categories_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE categories ADD CONSTRAINT FK_categories_RelateId FOREIGN KEY (RelateId) REFERENCES resources (Id)
ALTER TABLE comments ADD CONSTRAINT FK_comments_AccountId FOREIGN KEY (AccountId) REFERENCES resources (Id)
ALTER TABLE comments ADD CONSTRAINT FK_comments_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE comments ADD CONSTRAINT FK_comments_RelateId FOREIGN KEY (RelateId) REFERENCES resources (Id)
ALTER TABLE goods ADD CONSTRAINT FK_goods_RelateId FOREIGN KEY (RelateId) REFERENCES resources (Id)
ALTER TABLE goods ADD CONSTRAINT FK_goods_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE grants ADD CONSTRAINT FK_grants_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE grants ADD CONSTRAINT FK_grants_AccountId FOREIGN KEY (AccountId) REFERENCES resources (Id) ON DELETE CASCADE
ALTER TABLE likes ADD CONSTRAINT FK_likes_RelateId FOREIGN KEY (RelateId) REFERENCES resources (Id)
ALTER TABLE likes ADD CONSTRAINT FK_likes_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE likes ADD CONSTRAINT FK_likes_AccountId FOREIGN KEY (AccountId) REFERENCES resources (Id)
ALTER TABLE publications ADD CONSTRAINT FK_publications_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE resources_categories ADD CONSTRAINT FK_resources_categories_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE resources_categories ADD CONSTRAINT FK_resources_categories_RelateId FOREIGN KEY (RelateId) REFERENCES resources (Id) ON DELETE CASCADE
ALTER TABLE resources_categories ADD CONSTRAINT FK_resources_categories_CategoryId FOREIGN KEY (CategoryId) REFERENCES resources (Id)
ALTER TABLE sessions ADD CONSTRAINT FK_sessions_AccountId FOREIGN KEY (AccountId) REFERENCES resources (Id) ON DELETE CASCADE
ALTER TABLE sessions ADD CONSTRAINT FK_sessions_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE tags ADD CONSTRAINT FK_tags_ResourceId FOREIGN KEY (ResourceId) REFERENCES resources (Id)
ALTER TABLE tags ADD CONSTRAINT FK_tags_RelateId FOREIGN KEY (RelateId) REFERENCES resources (Id) ON DELETE CASCADE
