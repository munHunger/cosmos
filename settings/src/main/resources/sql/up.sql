CREATE SCHEMA `cosmos` ;

CREATE TABLE `cosmos`.`setting` (
  `id` VARCHAR(64) NOT NULL,
  `name` VARCHAR(256) NULL,
  `type` VARCHAR(7) NOT NULL,
  `regex` VARCHAR(512) NULL,
  `value` VARCHAR(512) NULL,
  `parent_id` VARCHAR(64) NULL,
  PRIMARY KEY (`id`));