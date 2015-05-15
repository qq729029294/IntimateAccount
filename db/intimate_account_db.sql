SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `intimate_account_db` DEFAULT CHARACTER SET utf8 ;
USE `intimate_account_db` ;

-- -----------------------------------------------------
-- Table `intimate_account_db`.`user_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`user_tbl` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `nickname` VARCHAR(45) NOT NULL,
  `description` VARCHAR(255) NULL COMMENT ' /* comment truncated */ /*(0:off-line;1:online;2:invisible)*/',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `create_time` TIMESTAMP NULL,
  PRIMARY KEY (`user_id`));


-- -----------------------------------------------------
-- Table `intimate_account_db`.`account_book_member_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`account_book_member_tbl` (
  `account_book_id` INT NOT NULL,
  `member_user_id` INT NOT NULL,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY (`account_book_id`, `member_user_id`));


-- -----------------------------------------------------
-- Table `intimate_account_db`.`user_extend_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`user_extend_tbl` (
  `user_id` INT NOT NULL,
  `avatar_image` BLOB NULL,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`));


-- -----------------------------------------------------
-- Table `intimate_account_db`.`account_book_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`account_book_tbl` (
  `account_book_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `description` VARCHAR(255) NULL,
  `create_user_id` INT NOT NULL,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `create_time` TIMESTAMP NULL,
  PRIMARY KEY (`account_book_id`));


-- -----------------------------------------------------
-- Table `intimate_account_db`.`account_item_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`account_item_tbl` (
  `account_item_id` INT NOT NULL AUTO_INCREMENT,
  `account_book_id` INT NOT NULL,
  `account_category_id` INT NOT NULL,
  `create_user_id` INT NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `water_value` INT NULL DEFAULT 0,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`account_item_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `intimate_account_db`.`account_target_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`account_target_tbl` (
  `account_target_id` INT NOT NULL AUTO_INCREMENT,
  `account_book_id` INT NOT NULL,
  `type` INT NOT NULL,
  `target_value` DOUBLE NULL,
  `description` VARCHAR(255) NULL,
  `create_user_id` INT NOT NULL,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `create_time` TIMESTAMP NULL,
  PRIMARY KEY (`account_target_id`));


-- -----------------------------------------------------
-- Table `intimate_account_db`.`account_category_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`account_category_tbl` (
  `account_category_id` INT NOT NULL AUTO_INCREMENT,
  `account_book_id` INT NOT NULL,
  `category` VARCHAR(45) NOT NULL,
  `type` INT NOT NULL,
  `super_category` VARCHAR(45) NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY (`account_category_id`));


-- -----------------------------------------------------
-- Table `intimate_account_db`.`account_book_delete_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`account_book_delete_tbl` (
  `account_book_id` INT NOT NULL,
  `create_user_id` INT NOT NULL,
  `create_time` TIMESTAMP NOT NULL,
  `delete_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);


-- -----------------------------------------------------
-- Table `intimate_account_db`.`account_category_delete_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`account_category_delete_tbl` (
  `account_category_id` INT NOT NULL,
  `create_user_id` INT NOT NULL,
  `create_time` TIMESTAMP NOT NULL,
  `delete_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);


-- -----------------------------------------------------
-- Table `intimate_account_db`.`account_item_delete_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `intimate_account_db`.`account_item_delete_tbl` (
  `account_category_id` INT NOT NULL,
  `create_user_id` INT NOT NULL,
  `create_time` TIMESTAMP NOT NULL,
  `delete_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
