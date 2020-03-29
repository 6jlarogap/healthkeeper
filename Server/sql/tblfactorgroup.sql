-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Хост: localhost
-- Время создания: Фев 25 2015 г., 11:01
-- Версия сервера: 5.5.36-cll-lve
-- Версия PHP: 5.4.23

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
-- Структура таблицы `tblfactorgroup`
--

CREATE TABLE IF NOT EXISTS `tblfactorgroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=9 ;

--
-- Дамп данных таблицы `tblfactorgroup`
--

INSERT INTO `tblfactorgroup` (`id`, `name`) VALUES(1, 'Сон');
INSERT INTO `tblfactorgroup` (`id`, `name`) VALUES(2, 'Питание');
INSERT INTO `tblfactorgroup` (`id`, `name`) VALUES(3, 'Психоэмоциональная нагрузка');
INSERT INTO `tblfactorgroup` (`id`, `name`) VALUES(4, 'Физическая нагрузка');
INSERT INTO `tblfactorgroup` (`id`, `name`) VALUES(5, 'Умственная нагрузка ');
INSERT INTO `tblfactorgroup` (`id`, `name`) VALUES(6, 'Лекарства');
INSERT INTO `tblfactorgroup` (`id`, `name`) VALUES(7, 'Вредные факторы');
INSERT INTO `tblfactorgroup` (`id`, `name`) VALUES(8, 'Полезные факторы');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
