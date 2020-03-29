-- phpMyAdmin SQL Dump
-- version 4.1.14.8
-- http://www.phpmyadmin.net
--
-- Хост: db557216498.db.1and1.com
-- Время создания: Июн 10 2015 г., 07:58
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
-- Структура таблицы `tblbodyregion`
--

CREATE TABLE IF NOT EXISTS `tblbodyregion` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `fullname` varchar(200) NOT NULL,
  `complaintid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `complaintid` (`complaintid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tblbodyregion`
--

INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(1, 'теменная область', 'теменная область', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(2, 'лобная область', 'лобная область', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(3, 'височная область (правая)', 'височная область (правая)', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(4, 'височная область (левая)', 'височная область (левая)', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(5, 'область переносицы', 'область переносицы', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(6, 'область глаза (правого)', 'область глаза (правого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(7, 'область  глаза (левого)', 'область  глаза (левого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(8, 'область щеки (правой)', 'область щеки (правой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(9, 'область щеки  (левой)', 'область щеки  (левой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(10, 'область носоглотки', 'область носоглотки', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(11, 'область рта, зубов, гортани', 'область рта, зубов, гортани', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(12, 'область нижней челюсти', 'область нижней челюсти', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(13, 'область уха (правого)', 'область уха (правого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(14, 'область уха (левого)', 'область уха (левого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(15, 'передняя область шеи', 'передняя область шеи', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(16, 'боковая область шеи (правая)', 'боковая область шеи (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(17, 'боковая область шеи (левая)', 'боковая область шеи (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(18, 'область ключицы (правой)', 'область ключицы (правой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(19, 'область ключицы  (левой)', 'область ключицы  (левой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(20, 'грудная область (правая)', 'грудная область (правая)', 8);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(22, 'область молочной железы  (правая)', 'область молочной железы  (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(23, 'область молочной железы  (левая)', 'область молочной железы  (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(21, 'грудная область (левая)', 'грудная область (левая)', 7);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(24, 'межрёберная грудная область', 'межрёберная грудная область', 8);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(25, 'подрёберная область (правая)', 'подрёберная область (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(26, 'подрёберная область (левая)', 'подрёберная область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(27, 'область солнечного сплетения', 'область солнечного сплетения', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(28, 'боковая область живота (правая)', 'боковая область живота (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(29, 'боковая область живота (левая)', 'боковая область живота (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(30, 'пупочная область', 'пупочная область', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(31, 'паховая область (правая)', 'паховая область (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(32, 'паховая область (левая)', 'паховая область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(33, 'лобковая область', 'лобковая область', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(34, 'область тазобедренного сустава (правого)', 'область тазобедренного сустава (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(35, 'область тазобедренного сустава (левого)', 'область тазобедренного сустава (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(36, 'область бедра (правого)', 'область бедра (правого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(37, 'область бедра (левого)', 'область бедра (левого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(38, 'область половых органов (м)', 'область половых органов (м)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(39, 'область половых органов (ж)', 'область половых органов (ж)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(40, 'область коленного сустава (правого)', 'область коленного сустава (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(41, 'область коленного сустава (левого)', 'область коленного сустава (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(42, 'область голени (правой)', 'область голени (правой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(43, 'область голени (левой)', 'область голени (левой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(44, 'область голеностопного сустава (правого)', 'область голеностопного сустава (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(45, 'область голеностопного сустава (левого)', 'область голеностопного сустава (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(46, 'область стопы (правой)', 'область стопы (правой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(47, 'область стопы (левой)', 'область стопы (левой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(48, 'область пальцев ноги (правой)', 'область пальцев ноги (правой)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(49, 'область пальцев ноги (левой)', 'область пальцев ноги (левой)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(50, 'область подошвы (правой)', 'область подошвы (правой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(51, 'область подошвы (левой)', 'область подошвы (левой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(52, 'область плечевого сустава (правого)', 'область плечевого сустава (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(53, 'область плечевого сустава (левого)', 'область плечевого сустава (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(54, 'подмышечная область (правая)', 'подмышечная область (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(55, 'подмышечная область (левая)', 'подмышечная область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(56, 'область плеча (правого)', 'область плеча (правого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(57, 'область плеча (левого)', 'область плеча (левого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(58, 'область локтевого сустава (правого) ', 'область локтевого сустава (правого) ', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(59, 'область локтевого сустава (левого) ', 'область локтевого сустава (левого) ', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(60, 'область предплечья (правого)', 'область предплечья (правого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(61, 'область предплечья (левого)', 'область предплечья (левого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(62, 'область запястья (правого)', 'область запястья (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(63, 'область запястья (левого)', 'область запястья (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(64, 'область кисти (правой)', 'область кисти (правой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(65, 'область кисти (левой)', 'область кисти (левой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(66, 'область пальцев руки (правой)', 'область пальцев руки (правой)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(67, 'область пальцев руки (левой)', 'область пальцев руки (левой)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(101, 'теменная область', 'теменная область', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(102, 'височная область (левая)', 'височная область (левая)', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(103, 'височная область (правая)', 'височная область (правая)', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(104, 'затылочная область', 'затылочная область', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(105, 'область основания черепа', 'область основания черепа', 1);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(106, 'задняя область шеи (левая)', 'задняя область шеи (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(107, 'задняя область шеи (правая)', 'задняя область шеи (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(108, 'дельтовидная область шеи (левая)', 'дельтовидная область шеи (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(109, 'дельтовидная область шеи (правая)', 'дельтовидная область шеи (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(110, 'область уха (левого)', 'область уха (левого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(111, 'область уха (правого)', 'область уха (правого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(112, 'область позвоночника (верх)', 'область позвоночника (верх)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(113, 'область позвоночника (середина)', 'область позвоночника (середина)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(114, 'область позвоночника (низ)', 'область позвоночника (низ)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(115, 'область позвоночника (крестец, копчик)', 'область позвоночника (крестец, копчик)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(116, 'межлопаточная область (левая)', 'межлопаточная область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(117, 'межлопаточная область (правая)', 'межлопаточная область (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(118, 'лопаточная область (левая)', 'лопаточная область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(119, 'лопаточная область (правая)', 'лопаточная область (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(120, 'подлопаточная область (левая)', 'подлопаточная область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(121, 'подлопаточная область(правая)', 'подлопаточная область(правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(122, 'поясничная область (левая)', 'поясничная область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(123, 'поясничная область (правая)', 'поясничная область (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(124, 'область тазобедренного сустава (левого)', 'область тазобедренного сустава (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(125, 'область тазобедренного сустава (правого)', 'область тазобедренного сустава (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(126, 'ягодичная область (левая)', 'ягодичная область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(127, 'ягодичная область (правая)', 'ягодичная область (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(128, 'область промежности', 'область промежности', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(129, 'область бедра (левого)', 'область бедра (левого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(130, 'область бедра (правого)', 'область бедра (правого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(131, 'область коленного сустава (левого)', 'область коленного сустава (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(132, 'область коленного сустава (правого)', 'область коленного сустава (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(133, 'икроножная область (левая)', 'икроножная область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(134, 'икроножная область (правая)', 'икроножная область (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(135, 'область голеностопного сустава (левого)', 'область голеностопного сустава (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(136, 'область голеностопного сустава (правого)', 'область голеностопного сустава (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(137, 'область пятки (левой)', 'область пятки (левой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(138, 'область пятки (правой)', 'область пятки (правой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(139, 'область стопы (левой)', 'область стопы (левой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(140, 'область стопы (правой)', 'область стопы (правой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(141, 'область пальцев ноги (левой)', 'область пальцев ноги (левой)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(142, 'область пальцев ноги (правой)', 'область пальцев ноги (правой)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(143, 'область плечевого сустава (левого)', 'область плечевого сустава (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(144, 'область плечевого сустава (правого)', 'область плечевого сустава (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(145, 'подмышечная область (правая)', 'подмышечная область (правая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(146, 'подмышечная область (левая)', 'подмышечная область (левая)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(147, 'область плеча (правого)', 'область плеча (правого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(148, 'область плеча (левого)', 'область плеча (левого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(149, 'область локтевого сустава (правого) ', 'область локтевого сустава (правого) ', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(150, 'область локтевого сустава (левого) ', 'область локтевого сустава (левого) ', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(151, 'область предплечья (правого)', 'область предплечья (правого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(152, 'область предплечья (левого)', 'область предплечья (левого)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(153, 'область запястья (правого)', 'область запястья (правого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(154, 'область запястья (левого)', 'область запястья (левого)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(155, 'область кисти (правой)', 'область кисти (правой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(156, 'область кисти (левой)', 'область кисти (левой)', NULL);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(157, 'область пальцев руки (правой)', 'область пальцев руки (правой)', 11);
INSERT INTO `tblbodyregion` (`id`, `name`, `fullname`, `complaintid`) VALUES(158, 'область пальцев руки (левой)', 'область пальцев руки (левой)', 11);

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `tblbodyregion`
--
ALTER TABLE `tblbodyregion`
  ADD CONSTRAINT `tblbodyregion_ibfk_1` FOREIGN KEY (`complaintid`) REFERENCES `tblbodycomplainttype` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
