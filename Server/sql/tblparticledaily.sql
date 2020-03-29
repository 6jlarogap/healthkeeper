-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Янв 26 2015 г., 12:39
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
-- Структура таблицы `tblparticledaily`
--

CREATE TABLE IF NOT EXISTS `tblparticledaily` (
`id` int(11) NOT NULL,
  `dt` datetime NOT NULL,
  `proton1mev` bigint(11) DEFAULT NULL,
  `proton10mev` bigint(11) DEFAULT NULL,
  `proton100mev` bigint(11) DEFAULT NULL,
  `electron08mev` bigint(11) DEFAULT NULL,
  `electron2mev` bigint(11) DEFAULT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Данные по частицам' AUTO_INCREMENT=452 ;

--
-- Дамп данных таблицы `tblparticledaily`
--

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblparticledaily`
--
ALTER TABLE `tblparticledaily`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tblparticledaily`
--
ALTER TABLE `tblparticledaily`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=452;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
