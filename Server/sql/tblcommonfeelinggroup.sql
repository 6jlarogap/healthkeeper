-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Окт 30 2014 г., 15:58
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
-- Структура таблицы `tblcommonfeelinggroup`
--

CREATE TABLE IF NOT EXISTS `tblcommonfeelinggroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Дамп данных таблицы `tblcommonfeelinggroup`
--

INSERT INTO `tblcommonfeelinggroup` (`id`, `name`) VALUES(1, 'Психоэмоциональные ощущения');
INSERT INTO `tblcommonfeelinggroup` (`id`, `name`) VALUES(2, 'Физические');
INSERT INTO `tblcommonfeelinggroup` (`id`, `name`) VALUES(3, 'Сон');
INSERT INTO `tblcommonfeelinggroup` (`id`, `name`) VALUES(4, 'Желудочно-кишечный тракт');
INSERT INTO `tblcommonfeelinggroup` (`id`, `name`) VALUES(5, 'Другие');


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
