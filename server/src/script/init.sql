CREATE DATABASE IF NOT EXISTS chat;
use chat;

DROP TABLE IF EXISTS user_group;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS user_group(
  `id` BIGINT(15) NOT NULL AUTO_INCREMENT,
  `group_id` VARCHAR(24) NOT NULL ,
  `group_name` VARCHAR(32) NOT NULL ,
  `group_user_ids` TEXT NOT NULL COMMENT 'group users set',
  `create_time` TIMESTAMP DEFAULT now(),
  `update_time` TIMESTAMP DEFAULT now(),
  PRIMARY KEY (`id`),
  unique index group_index(group_id(14))
)ENGINE =innodb;

CREATE TABLE IF NOT EXISTS users(
  `id` BIGINT(15) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(24) NOT NULL ,
  `user_name` VARCHAR(32) NOT NULL,
  `user_password` VARCHAR(32) NOT NULL,
  `user_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0 normal ,1 group',
  `create_time` TIMESTAMP DEFAULT now(),
  `update_time` TIMESTAMP DEFAULT now(),
  PRIMARY KEY(`id`),
  unique INDEX user_index(user_id(14))
)ENGINE =innodb;




