-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Окт 23 2014 г., 14:26
-- Версия сервера: 5.5.39
-- Версия PHP: 5.4.31

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
-- Структура таблицы `tblmoonphase`
--

CREATE TABLE IF NOT EXISTS `tblmoonphase` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(30) DEFAULT NULL,
  `name_ru` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Фазы Луны';

--
-- Дамп данных таблицы `tblmoonphase`
--

INSERT INTO `tblmoonphase` (`id`, `name`, `name_ru`) VALUES
(1, 'New moon', 'Новолуние'),
(2, 'Waxing crescent', 'Молодая луна'),
(3, 'First quarter', 'Первая четверть'),
(4, 'Waxing gibbous', 'Прибывающая луна'),
(5, 'Full moon', 'Полнолуние'),
(6, 'Waning gibbous', 'Убывающая луна'),
(7, 'Third quarter', 'Последняя четверть'),
(8, 'Waning crescent', 'Старая луна');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblmoonphase`
--
ALTER TABLE `tblmoonphase`
 ADD PRIMARY KEY (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
