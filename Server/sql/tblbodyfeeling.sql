-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Окт 23 2014 г., 08:44
-- Версия сервера: 5.6.14
-- Версия PHP: 5.5.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `pomnimva_health`
--

-- --------------------------------------------------------

--
-- Структура таблицы `tblbodyfeeling`
--

CREATE TABLE IF NOT EXISTS `tblbodyfeeling` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rowid` binary(16) NOT NULL,
  `userid` int(11) NOT NULL,
  `feelingtypeid` int(11),
  `customfeelingtypeid` int(11),
  `bodyregionid` int(11) NOT NULL,
  `x` int(11),
  `y` int(11),
  `startdate` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `feelingtypeid` (`feelingtypeid`),
  KEY `userid` (`userid`),
  KEY `customfeelingtypeid` (`customfeelingtypeid`),
  KEY `bodyregionid` (`bodyregionid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=7 ;


--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `tblbodyfeeling`
--
ALTER TABLE `tblbodyfeeling`  
  ADD CONSTRAINT `tblbodyfeeling_user_ibfk` FOREIGN KEY (`userid`) REFERENCES `tbluser` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tblbodyfeeling_type_ibfk` FOREIGN KEY (`feelingtypeid`) REFERENCES `tblbodyfeelingtype` (`id`),
  ADD CONSTRAINT `tblcustomfeelingtype_type_ibfk` FOREIGN KEY (`customfeelingtypeid`) REFERENCES `tblcustombodyfeelingtype` (`id`),
  ADD CONSTRAINT `tblbodyfeeling_bodyregion_ibfk` FOREIGN KEY (`bodyregionid`) REFERENCES `tblbodyregion` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
