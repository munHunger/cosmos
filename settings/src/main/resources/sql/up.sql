CREATE SCHEMA `cosmos` ;

CREATE TABLE `cosmos`.`setting` (
  `id` VARCHAR(32) NOT NULL,
  `name` VARCHAR(256) NULL,
  `type` VARCHAR(7) NOT NULL,
  `regex` VARCHAR(512) NULL,
  `parent_id` VARCHAR(32) NULL,
  PRIMARY KEY (`id`));




INSERT INTO `cosmos`.`setting` (`id`, `name`, `type`) VALUES ('ID1', 'root', 'group');
INSERT INTO `cosmos`.`setting` (`id`, `name`, `type`, `regex`, `parent_id`) VALUES ('ID2', 'child', 'string', '.*', 'ID1');
INSERT INTO `cosmos`.`setting` (`id`, `name`, `type`, `parent_id`) VALUES ('ID3', 'child2', 'group', 'ID1');
INSERT INTO `cosmos`.`setting` (`id`, `name`, `type`, `regex`, `parent_id`) VALUES ('ID4', 'leaf', 'string', '.+', 'ID3');
