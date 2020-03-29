-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Хост: 127.0.0.1
-- Время создания: Апр 01 2014 г., 14:37
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
-- Структура таблицы `tblcity`
--

CREATE TABLE IF NOT EXISTS `tblcity` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `lat` float NOT NULL,
  `lng` float NOT NULL,
  `name_ru` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Таблица городов';

--
-- Дамп данных таблицы `tblcity`
--

INSERT INTO `tblcity` (`id`, `name`, `lat`, `lng`, `name_ru`) VALUES
(460413, 'Daugavpils', 55.8833, 26.5333, NULL),
(468866, 'Yartsevo', 55.0667, 32.6964, NULL),
(479028, 'Unecha', 52.8459, 32.6739, NULL),
(491687, 'Smolensk', 54.7817, 32.04, 'Смоленск'),
(517269, 'Novozybkov', 52.5396, 31.9275, NULL),
(522410, 'Nevel', 56.0209, 29.9284, NULL),
(547475, 'Klintsy', 52.7602, 32.2393, NULL),
(593063, 'Visaginas', 55.6, 26.4167, NULL),
(593116, 'Vilnius', 54.6892, 25.2798, 'Вильнюс'),
(593672, 'Utena', 55.5, 25.6, NULL),
(593733, 'Ukmerge', 55.25, 24.75, NULL),
(594739, 'Siauliai', 55.9333, 23.3167, NULL),
(595213, 'Rokiskis', 55.9667, 25.5833, NULL),
(595449, 'Radviliskis', 55.8167, 23.5333, NULL),
(596128, 'Panevezys', 55.7333, 24.35, NULL),
(597231, 'Marijampole', 54.5667, 23.35, NULL),
(598272, 'Kedainiai', 55.2883, 23.9747, NULL),
(598316, 'Kaunas', 54.9, 23.9, 'Каунас'),
(598818, 'Jonava', 55.0833, 24.2833, NULL),
(599757, 'Druskininkai', 54.0167, 23.9667, NULL),
(601084, 'Alytus', 54.4, 24.05, NULL),
(618800, 'Horad Zhodzina', 54.0983, 28.3325, 'Жодино'),
(618806, 'Zhlobin', 52.8926, 30.024, 'Жлобин'),
(620127, 'Vitsyebsk', 55.1905, 30.2033, NULL),
(620181, 'Vilyeyka', 54.4896, 26.9062, NULL),
(620391, 'Vawkavysk', 53.1561, 24.4513, NULL),
(621074, 'Svyetlahorsk', 52.6329, 29.7389, NULL),
(621266, 'Stowbtsy', 53.4785, 26.7434, NULL),
(621713, 'Smarhon', 54.4791, 26.3934, 'Сморгонь'),
(621741, 'Slutsk', 53.0274, 27.5597, NULL),
(621754, 'Slonim', 53.0867, 25.3219, NULL),
(622113, 'Shchuchin', 53.6014, 24.7465, NULL),
(622428, 'Salihorsk', 52.7876, 27.5415, 'Солигорск'),
(622739, 'Rahachow', 53.0934, 30.0495, NULL),
(622794, 'Rechytsa', 52.3617, 30.3916, NULL),
(622997, 'Pruzhany', 52.5567, 24.4644, NULL),
(623317, 'Polatsk', 55.4859, 28.7756, 'Полоцк'),
(623549, 'Pinsk', 52.1229, 26.0951, NULL),
(623760, 'Pastavy', 55.1168, 26.8326, NULL),
(624034, 'Asipovichy', 53.2933, 28.6422, NULL),
(624079, 'Orsha', 54.5153, 30.4053, NULL),
(624784, 'Navapolatsk', 55.5363, 28.6424, NULL),
(624785, 'Navahrudak', 53.6, 25.8333, NULL),
(625144, 'Minsk', 53.9, 27.5667, 'Минск'),
(625324, 'Mazyr', 52.0495, 29.2456, 'Мозырь'),
(625367, 'Masty', 53.4122, 24.5387, NULL),
(625409, 'Marina Horka', 53.509, 28.147, NULL),
(625625, 'Maladzyechna', 54.3205, 26.8644, 'Молодечно'),
(625665, 'Mahilyow', 53.9139, 30.3364, NULL),
(625743, 'Lyepyel', 54.8814, 28.6932, NULL),
(625818, 'Luninyets', 52.2472, 26.8047, NULL),
(626081, 'Lida', 53.8833, 25.2997, 'Лида'),
(626450, 'Krychaw', 53.6945, 31.7099, NULL),
(627083, 'Kalodzishchy', 53.944, 27.7823, NULL),
(627145, 'Kobryn', 52.2138, 24.3564, NULL),
(627800, 'Ivatsevichy', 52.709, 25.3401, NULL),
(627904, 'Hrodna', 53.6884, 23.8258, 'Гродно'),
(627905, 'Horki', 54.2861, 30.9842, NULL),
(627907, 'Homyel', 52.4345, 30.9754, NULL),
(627908, 'Hlybokaye', 55.1382, 27.6879, NULL),
(628634, 'Dzyarzhynsk', 53.6832, 27.138, NULL),
(629018, 'Dobrush', 52.4089, 31.3237, NULL),
(629447, 'Bykhaw', 53.5193, 30.2469, NULL),
(629454, 'Byaroza', 52.5314, 24.9786, NULL),
(629634, 'Brest', 52.1, 23.7, 'Брест'),
(630376, 'Horad Barysaw', 54.2312, 28.5049, NULL),
(630429, 'Baranavichy', 53.1327, 26.0139, 'Барановичи'),
(630468, 'Babruysk', 53.15, 29.2333, 'Бобруйск'),
(694792, 'Sarny', 51.338, 26.6019, NULL),
(698131, 'Ovruch', 51.3246, 28.8035, NULL),
(703464, 'Kuznetsovsk', 51.6833, 25.8667, NULL),
(710735, 'Chernihiv', 51.5055, 31.2849, NULL),
(758651, 'Sokolka', 53.4072, 23.5023, NULL),
(771158, 'Hajnowka', 52.7433, 23.5812, NULL),
(775986, 'Bielsk Podlaski', 52.7651, 23.1865, NULL),
(6930327, 'Slavutich', 51.5196, 30.7343, NULL),
(10722858, 'Svetlogorsk', 52.6333, 29.7333, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
