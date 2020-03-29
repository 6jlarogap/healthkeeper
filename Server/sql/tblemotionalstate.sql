-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Ноя 20 2014 г., 13:20
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
-- Структура таблицы `tblemotionalstate`
--

CREATE TABLE IF NOT EXISTS `tblemotionalstate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `ispositive` int(11) NOT NULL COMMENT '0 - негативное, 1 - позитивное',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='Эмоциональное состояние' AUTO_INCREMENT=36 ;

--
-- Дамп данных таблицы `tblemotionalstate`
--

INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(1, 'веселье', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(2, 'возбуждение', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(3, 'гордость', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(4, 'добродушие', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(5, 'любовь', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(6, 'нежность', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(7, 'подъем', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(8, 'радость, радостное волнение', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(9, 'уверенность', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(10, 'умиление', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(11, 'щедрость', 1);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(12, 'алчность', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(13, 'гнев', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(14, 'гордыня', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(15, 'горе', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(16, 'грубость', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(17, 'грусть', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(18, 'дискомфорт', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(19, 'зависть', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(20, 'застенчивость', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(21, 'злость', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(22, 'напряжение', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(23, 'ненависть', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(24, 'отвращение', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(25, 'печаль', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(26, 'подавленность', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(27, 'презрение', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(28, 'раздражительность', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(29, 'сожаление', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(30, 'страх', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(31, 'стресс', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(32, 'тревога', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(33, 'угнетенность', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(34, 'уныние', 0);
INSERT INTO `tblemotionalstate` (`id`, `name`, `ispositive`) VALUES(35, 'чувство вины', 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
