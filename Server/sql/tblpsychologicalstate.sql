-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Ноя 20 2014 г., 13:21
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
-- Структура таблицы `tblpsychologicalstate`
--

CREATE TABLE IF NOT EXISTS `tblpsychologicalstate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Психологическое состояние' AUTO_INCREMENT=8 ;

--
-- Дамп данных таблицы `tblpsychologicalstate`
--

INSERT INTO `tblpsychologicalstate` (`id`, `name`) VALUES(1, 'бодрость');
INSERT INTO `tblpsychologicalstate` (`id`, `name`) VALUES(2, 'эйфория');
INSERT INTO `tblpsychologicalstate` (`id`, `name`) VALUES(3, 'усталость');
INSERT INTO `tblpsychologicalstate` (`id`, `name`) VALUES(4, 'апатия');
INSERT INTO `tblpsychologicalstate` (`id`, `name`) VALUES(5, 'депрессия');
INSERT INTO `tblpsychologicalstate` (`id`, `name`) VALUES(6, 'отчуждение');
INSERT INTO `tblpsychologicalstate` (`id`, `name`) VALUES(7, 'утрата чувства реальности');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
