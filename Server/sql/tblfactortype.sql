-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Хост: db557216498.db.1and1.com
-- Время создания: Мар 20 2015 г., 05:31
-- Версия сервера: 5.1.73-log
-- Версия PHP: 5.4.38-0+deb7u1

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
-- Структура таблицы `tblfactortype`
--

CREATE TABLE IF NOT EXISTS `tblfactortype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `factorgroupid` int(11) NOT NULL,
  `unitid` int(11) DEFAULT NULL,
  `name` varchar(200) NOT NULL,
  `status` int(11) NOT NULL,
  `ordinalnumber` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `factorgroupid` (`factorgroupid`),
  KEY `unitid` (`unitid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=29 ;

--
-- Дамп данных таблицы `tblfactortype`
--

INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(1, 1, NULL, 'хороший', 1, 1);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(2, 1, NULL, 'обычный', 2, 2);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(3, 1, NULL, 'плохой', 3, 3);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(4, 1, NULL, 'бессоница', 3, 4);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(5, 1, NULL, 'чтение триллера', 2, 5);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(6, 1, NULL, 'просмотр ТВ', 2, 6);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(7, 1, NULL, 'прогулка', 1, 7);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(8, 1, NULL, 'время сна', 2, 8);

INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(9, 2, NULL, 'жирное', 3, 1);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(10, 2, NULL, 'острое', 2, 2);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(11, 2, NULL, 'сладкое', 2, 3);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(12, 2, NULL, 'соленое', 3, 4);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(13, 2, NULL, 'кислое', 2, 5);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(14, 2, NULL, 'кофе', 2, 6);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(15, 2, NULL, 'переедание', 3, 7);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(16, 2, NULL, 'жидкость', 2, 8);

INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(17, 3, NULL, 'стресс', 3, 1);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(18, 3, NULL, 'высокая', 3, 2);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(19, 3, NULL, 'обычная', 2, 3);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(20, 3, NULL, 'слабая', 1, 4);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(21, 3, NULL, 'испуг', 3, 5);

INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(22, 4, NULL, 'сильная', 2, 1);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(23, 4, NULL, 'обычная', 2, 2);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(24, 4, NULL, 'слабая', 2, 3);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(25, 4, NULL, 'длительная', 2, 4);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(26, 4, NULL, 'тяжелая ноша', 3, 5);

INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(27, 5, NULL, 'высокая', 2, 1);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(28, 5, NULL, 'обычная', 1, 2);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(29, 5, NULL, 'малая', 2, 3);

INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(30, 7, NULL, 'курение', 3, 1);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(31, 7, NULL, 'алкоголь', 3, 2);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(32, 7, NULL, 'наркотики', 3, 3);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(33, 7, NULL, 'неприятности', 3, 4);

INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(34, 8, NULL, 'прогулка', 1, 1);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(35, 8, NULL, 'музыка', 1, 2);
INSERT INTO `tblfactortype` (`id`, `factorgroupid`, `unitid`, `name`, `status`, `ordinalnumber`) VALUES(36, 8, NULL, 'гимнастика', 1, 3);


--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `tblfactortype`
--
ALTER TABLE `tblfactortype`
  ADD CONSTRAINT `tblfactortype_ibfk_2` FOREIGN KEY (`unitid`) REFERENCES `tblunitdimension` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tblfactortype_ibfk_1` FOREIGN KEY (`factorgroupid`) REFERENCES `tblfactorgroup` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
