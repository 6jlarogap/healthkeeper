-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Апр 01 2014 г., 14:37
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
-- Структура таблицы `tbljournaloperation`
--

CREATE TABLE IF NOT EXISTS `tbljournaloperation` (
  `rowid` binary(16) NOT NULL,
  `id` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `changeddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `operationtypeid` tinyint(1) NOT NULL,
  UNIQUE KEY `rowid` (`rowid`),
  KEY `userid` (`userid`),
  KEY `operationtypeid` (`operationtypeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tbljournaloperation`
--

ALTER TABLE `tbljournaloperation`
  ADD CONSTRAINT `tbljournaloperation_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `tbluser` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tbljournaloperation_ibfk_2` FOREIGN KEY (`operationtypeid`) REFERENCES `tbljournaloperationtype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
