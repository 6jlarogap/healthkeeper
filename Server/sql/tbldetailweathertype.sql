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
-- Структура таблицы `tbldetailweathertype`
--

CREATE TABLE IF NOT EXISTS `tbldetailweathertype` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `icon` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tbldetailweathertype`
--

INSERT INTO `tbldetailweathertype` (`id`, `name`, `icon`) VALUES
(200, 'thunderstorm with light rain', '11d.png'),
(201, 'thunderstorm with rain', '11d.png'),
(202, 'thunderstorm with heavy rain', '11d.png'),
(210, 'light thunderstorm', '11d.png'),
(211, 'thunderstorm', '11d.png'),
(212, 'heavy thunderstorm', '11d.png'),
(221, 'ragged thunderstorm', '11d.png'),
(230, 'thunderstorm with light drizzle', '11d.png'),
(231, 'thunderstorm with drizzle', '11d.png'),
(232, 'thunderstorm with heavy drizzle', '11d.png'),
(300, 'light intensity drizzle', '09d.png'),
(301, 'drizzle', '09d.png'),
(302, 'heavy intensity drizzle', '09d.png'),
(310, 'light intensity drizzle rain', '09d.png'),
(311, 'drizzle rain', '09d.png'),
(312, 'heavy intensity drizzle rain', '09d.png'),
(313, 'shower rain and drizzle', '09d.png'),
(314, 'heavy shower rain and drizzle', '09d.png'),
(321, 'shower drizzle', '09d.png'),
(500, 'light rain', '10d.png'),
(501, 'moderate rain', '10d.png'),
(502, 'heavy intensity rain', '10d.png'),
(503, 'very heavy rain', '10d.png'),
(504, 'extreme rain', '10d.png'),
(511, 'freezing rain', '13d.png'),
(520, 'light intensity shower rain', '09d.png'),
(521, 'shower rain', '09d.png'),
(522, 'heavy intensity shower rain', '09d.png'),
(531, 'ragged shower rain', '09d.png'),
(600, 'light snow', '13d.png'),
(601, 'snow', '13d.png'),
(602, 'heavy snow', '13d.png'),
(611, 'sleet', '13d.png'),
(612, 'shower sleet', '13d.png'),
(615, 'light rain and snow', '13d.png'),
(616, 'rain and snow', '13d.png'),
(620, 'light shower snow', '13d.png'),
(621, 'shower snow', '13d.png'),
(622, 'heavy shower snow', '13d.png'),
(701, 'mist', '50d.png'),
(711, 'smoke', '50d.png'),
(721, 'haze', '50d.png'),
(731, 'Sand/Dust Whirls', '50d.png'),
(741, 'Fog', '50d.png'),
(751, 'sand', '50d.png'),
(761, 'dust', '50d.png'),
(762, 'VOLCANIC ASH', '50d.png'),
(771, 'SQUALLS', '50d.png'),
(781, 'TORNADO', '50d.png'),
(800, 'sky is clear', '01d.png'),
(801, 'few clouds', '02d.png'),
(802, 'scattered clouds', '03d.png'),
(803, 'broken clouds', '04d.png'),
(804, 'overcast clouds', '04d.png'),
(900, 'tornado', ''),
(901, 'tropical storm', ''),
(902, 'hurricane', ''),
(903, 'cold', ''),
(904, 'hot', ''),
(905, 'windy', ''),
(906, 'hail', ''),
(950, 'Setting', ''),
(951, 'Calm', ''),
(952, 'Light breeze', ''),
(953, 'Gentle Breeze', ''),
(954, 'Moderate breeze', ''),
(955, 'Fresh Breeze', ''),
(956, 'Strong breeze', ''),
(957, 'High wind, near gale', ''),
(958, 'Gale', ''),
(959, 'Severe Gale', ''),
(960, 'Storm', ''),
(961, 'Violent Storm', ''),
(962, 'Hurricane', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
