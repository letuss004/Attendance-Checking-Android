-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `android_atten` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `android_atten` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`userType`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`userType` ;

CREATE TABLE IF NOT EXISTS `mydb`.`userType` (
  `id` INT NOT NULL,
  `student` VARCHAR(45) NULL,
  `lecturer` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`account`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`account` ;

CREATE TABLE IF NOT EXISTS `mydb`.`account` (
  `userID` VARCHAR(45) NOT NULL,
  `accountcol` VARCHAR(45) NULL,
  `userType` INT NULL,
  `phoneNum` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `fullName` VARCHAR(45) NULL,
  PRIMARY KEY (`userID`),
  INDEX `fk_userType_idx` (`userType` ASC) VISIBLE,
  CONSTRAINT `fk_userType`
    FOREIGN KEY (`userType`)
    REFERENCES `mydb`.`userType` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Cources`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Cources` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Cources` (
  `id` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`manageCources`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`manageCources` ;

CREATE TABLE IF NOT EXISTS `mydb`.`manageCources` (
  `id` INT NOT NULL,
  `userID` VARCHAR(45) NULL,
  `courcesID` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_userID_idx` (`userID` ASC) VISIBLE,
  INDEX `fk_courseID_idx` (`courcesID` ASC) VISIBLE,
  CONSTRAINT `fk_userID`
    FOREIGN KEY (`userID`)
    REFERENCES `mydb`.`account` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_courseID`
    FOREIGN KEY (`courcesID`)
    REFERENCES `mydb`.`Cources` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
