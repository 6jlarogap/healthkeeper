-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Хост: db557216498.db.1and1.com
-- Время создания: Мар 24 2015 г., 03:05
-- Версия сервера: 5.1.73-log
-- Версия PHP: 5.4.39-0+deb7u1

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
-- Структура таблицы `tblcommonfeelingtype`
--

CREATE TABLE IF NOT EXISTS `tblcommonfeelingtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `feelinggroupid` int(11) NOT NULL,
  `ordinalnumber` int(11) NOT NULL,
  `unitid` int(11) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `feelinggroupid` (`feelinggroupid`),
  KEY `unitid` (`unitid`),
  KEY `unitid_2` (`unitid`),
  KEY `status` (`status`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=39 ;

--
-- Дамп данных таблицы `tblcommonfeelingtype`
--

INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(1, 1, 1, NULL, 'веселье', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(2, 1, 2, NULL, 'возбуждение', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(3, 1, 3, NULL, 'добродушие', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(4, 1, 4, NULL, 'нежность', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(5, 1, 5, NULL, 'радость, радостное волнение', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(6, 1, 6, NULL, 'уверенность', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(7, 1, 7, NULL, 'умиление', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(8, 1, 8, NULL, 'гнев', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(9, 1, 9, NULL, 'горе', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(10, 1, 10, NULL, 'грусть', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(11, 1, 11, NULL, 'напряжение', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(12, 1, 12, NULL, 'печаль', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(13, 1, 13, NULL, 'подавленность', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(14, 1, 14, NULL, 'раздражительность', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(15, 1, 15, NULL, 'страх', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(16, 1, 16, NULL, 'тревога', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(17, 1, 17, NULL, 'уныние', 3);

INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(18, 2, 1, NULL, 'высокая работоспособность', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(19, 2, 2, NULL, 'бодрость', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(20, 2, 3, NULL, 'низкая работоспособность', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(21, 2, 4, NULL, 'повышенная утомляемость', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(22, 2, 5, NULL, 'усталость', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(23, 2, 6, NULL, 'слабость', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(24, 2, 7, NULL, 'вялость', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(25, 2, 8, NULL, 'бессилие', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(26, 2, 9, NULL, 'нарушение координации', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(27, 2, 10, NULL, 'шаткость походки', 3);

INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(28, 3, 1, NULL, 'глубокий сон', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(29, 3, 2, NULL, 'сонливость', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(30, 3, 3, NULL, 'бессонница', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(31, 3, 4, NULL, 'трудное засыпание', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(32, 3, 5, NULL, 'недостаток сна', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(33, 3, 6, NULL, 'беспокойный сон', 3);

INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(34, 4, 1, NULL, 'нормальный аппетит', 1);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(35, 4, 2, NULL, 'рвота', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(36, 4, 3, NULL, 'диарея', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(37, 4, 4, NULL, 'запор', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(38, 4, 5, NULL, 'изжога', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(39, 4, 6, NULL, 'тошнота', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(40, 4, 7, NULL, 'повышенный аппетит', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(41, 4, 8, NULL, 'пониженный аппетит', 3);

INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(42, 5, 1, NULL, 'озноб', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(43, 5, 2, NULL, 'жар', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(44, 5, 3, NULL, 'повышенное потоотделение', 3);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(45, 5, 4, NULL, 'пульс', 2);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(46, 5, 5, NULL, 'давление', 2);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(47, 5, 6, NULL, 'температура', 2);
INSERT INTO `tblcommonfeelingtype` (`id`, `feelinggroupid`, `ordinalnumber`, `unitid`, `name`, `status`) VALUES(48, 5, 7, NULL, 'вес', 2);


--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `tblcommonfeelingtype`
--
ALTER TABLE `tblcommonfeelingtype`
  ADD CONSTRAINT `tblcommonfeelingtype_ibfk_1` FOREIGN KEY (`unitid`) REFERENCES `tblunitdimension` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
