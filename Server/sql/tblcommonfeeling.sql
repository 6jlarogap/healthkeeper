-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Хост: db557216498.db.1and1.com
-- Время создания: Мар 23 2015 г., 08:49
-- Версия сервера: 5.1.73-log
-- Версия PHP: 5.4.38-0+deb7u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `db557216498`
--

-- --------------------------------------------------------

--
-- Структура таблицы `tblcommonfeeling`
--

CREATE TABLE IF NOT EXISTS `tblcommonfeeling` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rowid` binary(16) NOT NULL,
  `userid` int(11) NOT NULL,
  `feelingtypeid` int(11) DEFAULT NULL COMMENT 'Идентификатор типа общего ощущения',
  `customfeelingtypeid` int(11) DEFAULT NULL,
  `startdate` datetime NOT NULL COMMENT 'Дата общего ощущения',
  `value1` double DEFAULT NULL COMMENT 'Значение 1',
  `value2` double DEFAULT NULL COMMENT 'Значение 2',
  `value3` double DEFAULT NULL COMMENT 'Значение 3',
  PRIMARY KEY (`id`),
  KEY `feelingtypeid` (`feelingtypeid`,`userid`),
  KEY `customfeelingtypeid` (`customfeelingtypeid`),
  KEY `feelingtypeid_2` (`feelingtypeid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=49 ;


--
-- Триггеры `tblcommonfeeling`
--
DROP TRIGGER IF EXISTS `tblcommonfeeling_delete`;
DELIMITER //
CREATE TRIGGER `tblcommonfeeling_delete` BEFORE DELETE ON `tblcommonfeeling`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = OLD.id, userid = OLD.userid, operationtypeid = 3, changeddate = CURRENT_TIMESTAMP where rowid = OLD.rowid;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `tblcommonfeeling_insert`;
DELIMITER //
CREATE TRIGGER `tblcommonfeeling_insert` AFTER INSERT ON `tblcommonfeeling`
 FOR EACH ROW BEGIN
    INSERT INTO tbljournaloperation Set rowid = NEW.rowid, id = NEW.id, userid = NEW.userid, operationtypeid = 1; 
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `tblcommonfeeling_update`;
DELIMITER //
CREATE TRIGGER `tblcommonfeeling_update` AFTER UPDATE ON `tblcommonfeeling`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = NEW.id, userid = NEW.userid, operationtypeid = 2, changeddate = CURRENT_TIMESTAMP where rowid = NEW.rowid;
END
//
DELIMITER ;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `tblcommonfeeling`
--
ALTER TABLE `tblcommonfeeling`
  ADD CONSTRAINT `tblcommonfeeling_ibfk_1` FOREIGN KEY (`feelingtypeid`) REFERENCES `tblcommonfeelingtype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tblcommonfeeling_ibfk_2` FOREIGN KEY (`customfeelingtypeid`) REFERENCES `tblcustomcommonfeelingtype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
