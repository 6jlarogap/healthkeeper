-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Окт 23 2014 г., 10:42
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
-- Структура таблицы `tblweatherhourly`
--

CREATE TABLE IF NOT EXISTS `tblweatherhourly` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cityid` int(11) NOT NULL,
  `dt` datetime NOT NULL COMMENT 'Date and time',
  `temp` float DEFAULT NULL COMMENT 'Temperature in Celsius.',
  `humidity` float NOT NULL COMMENT 'Humidity in %',
  `pressure` float NOT NULL COMMENT 'Atmospheric pressure in hPa',
  `windspeed` float NOT NULL COMMENT 'Wind speed in mps',
  `winddeg` float NOT NULL COMMENT 'Wind direction in degrees (meteorological)',
  `clouds` float NOT NULL COMMENT 'Cloudiness in %',
  `rain` float DEFAULT NULL COMMENT 'Precipitation volume mm.',
  `snow` float DEFAULT NULL COMMENT 'Precipitation volume mm.',
  `temp_min` float DEFAULT NULL COMMENT 'Min temperature.',
  `temp_max` float DEFAULT NULL COMMENT 'Max temperature.',
  PRIMARY KEY (`id`),
  KEY `cityid` (`cityid`),
  KEY `cityid_2` (`cityid`),
  KEY `dt` (`dt`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Данные о погоде' AUTO_INCREMENT=4736 ;

--
-- Ограничения внешнего ключа таблицы `tblweatherhourly`
--
ALTER TABLE `tblweatherhourly`
  ADD CONSTRAINT `tblweatherhourly_ibfk_1` FOREIGN KEY (`cityid`) REFERENCES `tblcity` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
