-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Апр 01 2014 г., 14:38
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
-- Структура таблицы `tblweather`
--

CREATE TABLE IF NOT EXISTS `tblweather` (
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
  `isdaily` bit(1) NOT NULL COMMENT '1 - запись содержит погоду на весь день, 0 - запись содержит погоду на указанное время',
  `temp_min` float DEFAULT NULL COMMENT 'Min temperature.',
  `temp_max` float DEFAULT NULL COMMENT 'Max temperature.',
  `temp_morning` float DEFAULT NULL COMMENT 'Morning temperature.',
  `temp_day` float DEFAULT NULL COMMENT 'Day temperature.',
  `temp_evening` float DEFAULT NULL COMMENT 'Evening temperature.',
  `temp_night` float DEFAULT NULL COMMENT 'Night temperature.',
  PRIMARY KEY (`id`),
  KEY `cityid` (`cityid`),
  KEY `isdaily` (`isdaily`),  
  KEY `dt` (`dt`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Данные о погоде' AUTO_INCREMENT=1232 ;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `tblweather`
--
ALTER TABLE `tblweather`
  ADD CONSTRAINT `tblweather_ibfk_1` FOREIGN KEY (`cityid`) REFERENCES `tblcity` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
