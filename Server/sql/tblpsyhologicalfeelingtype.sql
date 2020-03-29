-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Хост: db557216498.db.1and1.com
-- Время создания: Фев 11 2015 г., 02:45
-- Версия сервера: 5.1.73-log
-- Версия PHP: 5.4.36-0+deb7u3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `db557216498`
--

-- --------------------------------------------------------

--
-- Структура таблицы `tblpsyhologicalfeelingtype`
--

CREATE TABLE IF NOT EXISTS `tblpsyhologicalfeelingtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feelinggroupid` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `feelinggroupid` (`feelinggroupid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Дамп данных таблицы `tblpsyhologicalfeelingtype`
--

INSERT INTO `tblpsyhologicalfeelingtype` (`id`, `feelinggroupid`, `name`) VALUES
(1, 1, 'название 1'),
(2, 1, 'название 2'),
(3, 2, 'название 3'),
(4, 2, 'название 4');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
