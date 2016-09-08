CREATE DATABASE sochi_dist DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
CREATE USER 'sochi'@'localhost' IDENTIFIED BY 'sochi';
CREATE USER 'sochi'@'%' IDENTIFIED BY 'sochi';
GRANT ALL ON `sochi\_%`.* TO 'sochi'@'localhost';
GRANT ALL ON `sochi\_%`.* TO 'sochi'@'%';
