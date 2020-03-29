-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Хост: db557216498.db.1and1.com
-- Время создания: Мар 25 2015 г., 03:24
-- Версия сервера: 5.1.73-log
-- Версия PHP: 5.4.39-0+deb7u1

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
-- Структура таблицы `tblcustombodyfeelingtype`
--

CREATE TABLE IF NOT EXISTS `tblcustombodyfeelingtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rowid` binary(16) NOT NULL,
  `userid` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tblcustombodyfeelingtype_user_ibfk` (`userid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Триггеры `tblcustombodyfeelingtype`
--
DROP TRIGGER IF EXISTS `trcustombodyfeelingtype_delete`;
DELIMITER //
CREATE TRIGGER `trcustombodyfeelingtype_delete` BEFORE DELETE ON `tblcustombodyfeelingtype`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = OLD.id, userid = OLD.userid, operationtypeid = 3, changeddate = CURRENT_TIMESTAMP where rowid = OLD.rowid;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trcustombodyfeelingtype_insert`;
DELIMITER //
CREATE TRIGGER `trcustombodyfeelingtype_insert` AFTER INSERT ON `tblcustombodyfeelingtype`
 FOR EACH ROW BEGIN
    INSERT INTO tbljournaloperation Set rowid = NEW.rowid, id = NEW.id, userid = NEW.userid, operationtypeid = 1; 
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trcustombodyfeelingtype_update`;
DELIMITER //
CREATE TRIGGER `trcustombodyfeelingtype_update` AFTER UPDATE ON `tblcustombodyfeelingtype`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = NEW.id, userid = NEW.userid, operationtypeid = 2, changeddate = CURRENT_TIMESTAMP where rowid = NEW.rowid;
END
//
DELIMITER ;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `tblcustombodyfeelingtype`
--
ALTER TABLE `tblcustombodyfeelingtype`
  ADD CONSTRAINT `tblcustombodyfeelingtype_user_ibfk` FOREIGN KEY (`userid`) REFERENCES `tbluser` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
