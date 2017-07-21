CREATE TABLE `cosmos`.`movie` (
  `movie_id` VARCHAR(64) NOT NULL,
  `image_url` VARCHAR(255) NULL,
  `title` VARCHAR(255) NULL,
  `year` INT NULL,
  PRIMARY KEY (`movie_id`));

CREATE TABLE `cosmos`.`rating` (
  `movie_id` VARCHAR(64) NOT NULL,
  `provider` VARCHAR(45) NOT NULL,
  `rating` DOUBLE NULL,
  `vote_count` INT NULL,
  PRIMARY KEY (`movie_id`, `provider`));

CREATE TABLE `cosmos`.`actors` (
  `movie_id` VARCHAR(64) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`movie_id`, `name`));


CREATE TABLE `cosmos`.`directors` (
  `movie_id` VARCHAR(64) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`movie_id`, `name`));

CREATE TABLE `cosmos`.`writers` (
  `movie_id` VARCHAR(64) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`movie_id`, `name`));

CREATE TABLE `cosmos`.`genre` (
  `movie_id` VARCHAR(64) NOT NULL,
  `genre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`movie_id`, `genre`));

CREATE TABLE `cosmos`.`extended_movie` (
  `movie_id` VARCHAR(64) NOT NULL,
  `description` VARCHAR(2048) NULL,
  `poster_url` VARCHAR(255) NULL,
  PRIMARY KEY (`movie_id`));
