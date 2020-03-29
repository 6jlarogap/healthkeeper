-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Ноя 20 2014 г., 09:00
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
-- Структура таблицы `tblheliophysicsdaily`
--

CREATE TABLE IF NOT EXISTS `tblheliophysicsdaily` (
`id` int(11) NOT NULL,
  `dt` datetime DEFAULT NULL,
  `f10_7` int(11) DEFAULT NULL,
  `sunspot_number` int(11) DEFAULT NULL COMMENT 'Количество пятен',
  `sunspot_area` int(11) DEFAULT NULL COMMENT 'Суммарная площадь пятен',
  `new_regions` int(11) DEFAULT NULL COMMENT 'Количество новых пятен',
  `xbkgd` varchar(20) DEFAULT NULL COMMENT 'Фоновое рентгеновское излучение (Мягкий рентген)',
  `flares_c` int(11) DEFAULT NULL COMMENT 'Количество вспышек класса C (интесивность в пике от 1,0×10−6 до 10−5)',
  `flares_m` int(11) DEFAULT NULL COMMENT 'Количество вспышек класса M (интесивность в пике от 1,0×10−5 до 10−4)',
  `flares_x` int(11) DEFAULT NULL COMMENT 'Количество вспышек класса X (интесивность в пике больше 10−4)',
  `flares_s` int(11) DEFAULT NULL COMMENT 'Количество вспышек класса S (площадь < 100 миллионных полусферы)',
  `flares_1` int(11) DEFAULT NULL COMMENT 'Количество вспышек класса S (площадь 100 - 250 миллионных полусферы)',
  `flares_2` int(11) DEFAULT NULL COMMENT 'Количество вспышек класса S (площадь 250 - 600 миллионных полусферы)',
  `flares_3` int(11) DEFAULT NULL COMMENT 'Количество вспышек класса S (площадь 600 - 1200 миллионных полусферы)'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='таблица ежедневных гелиофизических параметров' AUTO_INCREMENT=599 ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblheliophysicsdaily`
--
ALTER TABLE `tblheliophysicsdaily`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tblheliophysicsdaily`
--
ALTER TABLE `tblheliophysicsdaily`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=599;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
