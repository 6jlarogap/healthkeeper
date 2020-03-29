-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Ноя 20 2014 г., 12:09
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
-- Структура таблицы `tblbodyfeelingtype_level`
--

CREATE TABLE IF NOT EXISTS `tblbodyfeelingtype_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feelingtypeid` int(11) NOT NULL,
  `levelid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `feelingtypeid` (`feelingtypeid`),
  KEY `levelid` (`levelid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=52 ;

--
-- Дамп данных таблицы `tblbodyfeelingtype_level`
--

INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(1, 1, 1);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(2, 1, 1);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(3, 1, 2);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(4, 1, 3);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(5, 1, 4);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(6, 11, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(7, 11, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(8, 11, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(9, 73, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(10, 73, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(11, 73, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(12, 4, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(13, 4, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(14, 4, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(15, 28, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(16, 28, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(17, 28, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(18, 43, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(19, 43, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(20, 43, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(21, 44, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(22, 44, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(23, 44, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(24, 68, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(25, 68, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(26, 68, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(27, 64, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(28, 64, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(29, 64, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(30, 20, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(31, 20, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(32, 20, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(33, 66, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(34, 66, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(35, 66, 7);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(36, 3, 8);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(37, 61, 8);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(38, 81, 8);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(39, 3, 9);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(40, 61, 9);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(41, 81, 9);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(42, 3, 10);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(43, 61, 10);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(44, 81, 10);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(45, 46, 11);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(46, 46, 12);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(47, 46, 13);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(48, 46, 14);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(49, 80, 5);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(50, 80, 6);
INSERT INTO `tblbodyfeelingtype_level` (`id`, `feelingtypeid`, `levelid`) VALUES(51, 80, 7);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
