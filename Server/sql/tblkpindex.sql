-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Ноя 19 2014 г., 12:15
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
-- Структура таблицы `tblkpindex`
--

CREATE TABLE IF NOT EXISTS `tblkpindex` (
  `id` int(11) NOT NULL DEFAULT '0',
  `intvalue` int(11) DEFAULT NULL,
  `strvalue` varchar(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Таблица соответствия различных обозначений Kp-индекса';

--
-- Дамп данных таблицы `tblkpindex`
--

INSERT INTO `tblkpindex` (`id`, `intvalue`, `strvalue`) VALUES
(0, 0, '0o'),
(1, 3, '0+'),
(2, 7, '1-'),
(3, 10, '1o'),
(4, 13, '1+'),
(5, 17, '2-'),
(6, 20, '2o'),
(7, 23, '2+'),
(8, 27, '3-'),
(9, 30, '3o'),
(10, 33, '3+'),
(11, 37, '4-'),
(12, 40, '4o'),
(13, 43, '4+'),
(14, 47, '5-'),
(15, 50, '5o'),
(16, 53, '5+'),
(17, 57, '6-'),
(18, 60, '6o'),
(19, 63, '6+'),
(20, 67, '7-'),
(21, 70, '7o'),
(22, 73, '7+'),
(23, 77, '8-'),
(24, 80, '8o'),
(25, 83, '8+'),
(26, 87, '9-'),
(27, 90, '9o');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblkpindex`
--
ALTER TABLE `tblkpindex`
 ADD PRIMARY KEY (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
