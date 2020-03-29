-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Хост: db557216498.db.1and1.com
-- Время создания: Июн 10 2015 г., 07:57
-- Версия сервера: 5.1.73-log
-- Версия PHP: 5.4.41-0+deb7u1

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
-- Структура таблицы `tblbodycomplainttype`
--

CREATE TABLE IF NOT EXISTS `tblbodycomplainttype` (
  `id` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tblbodycomplainttype`
--

INSERT INTO `tblbodycomplainttype` (`id`, `name`) VALUES(1, 'головная боль');
INSERT INTO `tblbodycomplainttype` (`id`, `name`) VALUES(7, 'боль в грудной клетке слева');
INSERT INTO `tblbodycomplainttype` (`id`, `name`) VALUES(8, 'затрудненное дыхание');
INSERT INTO `tblbodycomplainttype` (`id`, `name`) VALUES(11, 'боль в суставах');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
