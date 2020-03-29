-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Дек 16 2014 г., 14:10
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
-- Структура таблицы `tbluserdetail`
--

CREATE TABLE IF NOT EXISTS `tbluserdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `maritalstatusid` int(11) DEFAULT NULL COMMENT 'Семейное положение',
  `socialstatusid` int(11) DEFAULT NULL COMMENT 'Социальное положение',
  `pressureid` int(11) DEFAULT NULL COMMENT 'Давление',
  `height` int(11) DEFAULT NULL COMMENT 'см.',
  `weight` int(11) DEFAULT NULL COMMENT 'кг.',
  `footdistance` int(11) DEFAULT NULL COMMENT 'Пройденное расстояние за день(км.)',
  `sleeptime` int(11) DEFAULT NULL COMMENT 'Сон в часах',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Личные данные' AUTO_INCREMENT=4 ;



--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `tbluserdetail`
--
ALTER TABLE `tbluserdetail`
  ADD CONSTRAINT `tbluserdetail_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `tbluser` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
