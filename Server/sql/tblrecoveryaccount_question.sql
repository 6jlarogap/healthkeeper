-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Фев 19 2015 г., 14:13
-- Версия сервера: 5.6.14
-- Версия PHP: 5.5.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- База данных: `pomnimva_health_test`
--

-- --------------------------------------------------------

--
-- Структура таблицы `tblrecoveryaccount_question`
--

CREATE TABLE IF NOT EXISTS `tblrecoveryaccount_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question` varchar(300) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Дамп данных таблицы `tblrecoveryaccount_question`
--

INSERT INTO `tblrecoveryaccount_question` (`id`, `question`) VALUES
(1, 'Девичья фамилия матери'),
(2, 'Имя домашнего питомца'),
(3, 'Фамилия первого учителя(учительницы).'),
(4, 'Имя лучшего(ей) друга(подруги).');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
