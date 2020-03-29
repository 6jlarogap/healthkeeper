-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Хост: db557216498.db.1and1.com
-- Время создания: Фев 11 2015 г., 02:44
-- Версия сервера: 5.1.73-log
-- Версия PHP: 5.4.36-0+deb7u3

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
-- Структура таблицы `tblpsyhologicalfeeling`
--

CREATE TABLE IF NOT EXISTS `tblpsyhologicalfeeling` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rowid` binary(16) NOT NULL,
  `userid` int(11) NOT NULL,
  `feelingtypeid` int(11) NOT NULL COMMENT 'Идентификатор типа общего ощущения',
  `startdate` datetime NOT NULL COMMENT 'Дата общего ощущения',
  `value` int(11) DEFAULT NULL COMMENT 'Значение общего ощущения. Обычно не указывается.',
  PRIMARY KEY (`id`),
  KEY `feelingtypeid` (`feelingtypeid`,`userid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=37 ;

--
-- Дамп данных таблицы `tblpsyhologicalfeeling`
--


--
-- Триггеры `tblpsyhologicalfeeling`
--
DROP TRIGGER IF EXISTS `tblpsyhologicalfeeling_delete`;
DELIMITER //
CREATE TRIGGER `tblpsyhologicalfeeling_delete` BEFORE DELETE ON `tblpsyhologicalfeeling`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = OLD.id, userid = OLD.userid, operationtypeid = 3, changeddate = CURRENT_TIMESTAMP where rowid = OLD.rowid;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `tblpsyhologicalfeeling_insert`;
DELIMITER //
CREATE TRIGGER `tblpsyhologicalfeeling_insert` AFTER INSERT ON `tblpsyhologicalfeeling`
 FOR EACH ROW BEGIN
    INSERT INTO tbljournaloperation Set rowid = NEW.rowid, id = NEW.id, userid = NEW.userid, operationtypeid = 1; 
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `tblpsyhologicalfeeling_update`;
DELIMITER //
CREATE TRIGGER `tblpsyhologicalfeeling_update` AFTER UPDATE ON `tblpsyhologicalfeeling`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = NEW.id, userid = NEW.userid, operationtypeid = 2, changeddate = CURRENT_TIMESTAMP where rowid = NEW.rowid;
END
//
DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
