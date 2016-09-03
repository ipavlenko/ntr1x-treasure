ALTER TABLE accounts DROP FOREIGN KEY FK_accounts_ResourceId
ALTER TABLE attachments DROP FOREIGN KEY FK_attachments_ResourceId
ALTER TABLE attachments DROP FOREIGN KEY FK_attachments_RelateId
ALTER TABLE categories DROP FOREIGN KEY FK_categories_ResourceId
ALTER TABLE categories DROP FOREIGN KEY FK_categories_RelateId
ALTER TABLE comments DROP FOREIGN KEY FK_comments_AccountId
ALTER TABLE comments DROP FOREIGN KEY FK_comments_ResourceId
ALTER TABLE comments DROP FOREIGN KEY FK_comments_RelateId
ALTER TABLE goods DROP FOREIGN KEY FK_goods_RelateId
ALTER TABLE goods DROP FOREIGN KEY FK_goods_ResourceId
ALTER TABLE grants DROP FOREIGN KEY FK_grants_ResourceId
ALTER TABLE grants DROP FOREIGN KEY FK_grants_AccountId
ALTER TABLE likes DROP FOREIGN KEY FK_likes_RelateId
ALTER TABLE likes DROP FOREIGN KEY FK_likes_ResourceId
ALTER TABLE likes DROP FOREIGN KEY FK_likes_AccountId
ALTER TABLE publications DROP FOREIGN KEY FK_publications_ResourceId
ALTER TABLE resources_categories DROP FOREIGN KEY FK_resources_categories_ResourceId
ALTER TABLE resources_categories DROP FOREIGN KEY FK_resources_categories_RelateId
ALTER TABLE resources_categories DROP FOREIGN KEY FK_resources_categories_CategoryId
ALTER TABLE sessions DROP FOREIGN KEY FK_sessions_AccountId
ALTER TABLE sessions DROP FOREIGN KEY FK_sessions_ResourceId
ALTER TABLE tags DROP FOREIGN KEY FK_tags_ResourceId
ALTER TABLE tags DROP FOREIGN KEY FK_tags_RelateId
ALTER TABLE aspects DROP FOREIGN KEY FK_aspects_RelateId
DROP TABLE resources
DROP INDEX INDEX_accounts_Email ON accounts
DROP TABLE accounts
DROP INDEX INDEX_attachments_Dir_Path ON attachments
DROP TABLE attachments
DROP TABLE categories
DROP TABLE comments
DROP TABLE goods
DROP INDEX INDEX_grants_Pattern_Action ON grants
DROP TABLE grants
DROP TABLE likes
DROP INDEX INDEX_publications_Published ON publications
DROP TABLE publications
DROP TABLE resources_categories
DROP TABLE sessions
DROP TABLE tags
DROP TABLE aspects
