-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Окт 27 2014 г., 06:47
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
-- Структура таблицы `tblgeophysicsdaily`
--

CREATE TABLE IF NOT EXISTS `tblgeophysicsdaily` (
`id` int(11) NOT NULL,
  `dt` datetime DEFAULT NULL,
  `ap` int(11) DEFAULT NULL COMMENT 'Ap-индекс (измеряется в нанотеслах)',
  `kp` varchar(4) DEFAULT NULL COMMENT 'планетарный Kp-индекс'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='таблица ежедневных геофизических параметров' AUTO_INCREMENT=638 ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblgeophysicsdaily`
--
ALTER TABLE `tblgeophysicsdaily`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tblgeophysicsdaily`
--
ALTER TABLE `tblgeophysicsdaily`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=638;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
