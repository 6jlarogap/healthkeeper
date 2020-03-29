-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Хост: db557216498.db.1and1.com
-- Время создания: Апр 17 2015 г., 03:57
-- Версия сервера: 5.1.73-log
-- Версия PHP: 5.4.39-0+deb7u2

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
-- Структура таблицы `tblbodyfeelingtype`
--

CREATE TABLE IF NOT EXISTS `tblbodyfeelingtype` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tblbodyfeelingtype`
--

INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(1, 'боль сжимающая');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(2, 'боль давящая');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(3, 'боль пульсирующая');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(4, 'ощущение «тяжелой» головы');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(5, 'боль острая');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(6, 'боль ноющая');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(7, 'боль тупая');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(8, 'боль жгучая');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(9, 'боль зудящая');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(10, 'головокружение');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(11, 'мелькание "мушек" перед глазами');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(12, 'боль на яркий свет');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(13, 'потемнение');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(14, 'снижение остроты зрения');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(15, 'боль при жевании');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(16, 'сухость');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(17, 'отечность');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(18, 'затрудненное дыхание');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(19, 'боль при глотании');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(20, 'першение');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(21, 'онемение');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(22, 'боль  колющая');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(23, 'шум');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(24, 'нарушение слуха');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(25, 'чувство заложенности');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(26, 'напряжение мышц');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(27, 'чувство сжатия');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(28, 'затрудненный вдох');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(29, 'затрудненный выдох');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(30, 'чувство нехватки воздуха');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(31, 'изжога');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(32, 'чувство тяжести');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(33, 'вздутие');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(34, 'тугоподвижность');
INSERT INTO `tblbodyfeelingtype` (`id`, `name`) VALUES(35, 'чувство холода');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
