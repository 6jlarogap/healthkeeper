
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


CREATE TABLE IF NOT EXISTS `tblunitdimension` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Единицы измерения(личных факторов, ощущений)';

--
-- Дамп данных таблицы `tblcity`
--

INSERT INTO `tblunitdimension` (`id`, `name`) VALUES(1, 'Безразмерная');
INSERT INTO `tblunitdimension` (`id`, `name`) VALUES(2, 'Значение1');
INSERT INTO `tblunitdimension` (`id`, `name`) VALUES(3, 'Значение1 и значение2');
INSERT INTO `tblunitdimension` (`id`, `name`) VALUES(4, 'Время1 и время2');
INSERT INTO `tblunitdimension` (`id`, `name`) VALUES(5, 'Время1, время2, значение1');
