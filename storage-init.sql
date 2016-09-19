CREATE DATABASE vstore_new_dist DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
CREATE USER 'vstore'@'localhost' IDENTIFIED BY 'vstore';
CREATE USER 'vstore'@'%' IDENTIFIED BY 'vstore';
GRANT ALL ON `vstore\_%`.* TO 'vstore'@'localhost';
GRANT ALL ON `vstore\_%`.* TO 'vstore'@'%';
