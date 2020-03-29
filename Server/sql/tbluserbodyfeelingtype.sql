CREATE TABLE IF NOT EXISTS `tbluserbodyfeelingtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rowid` binary(16) NOT NULL,
  `userid` int(11) NOT NULL,
  `feelingtypeid` int(11) DEFAULT NULL,
  `color` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `feelingtypeid` (`feelingtypeid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

ALTER TABLE `tbluserbodyfeelingtype`
  ADD CONSTRAINT `tbluserbodyfeelingtype_ibfk_2` FOREIGN KEY (`feelingtypeid`) REFERENCES `tblbodyfeelingtype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `tbluserbodyfeelingtype_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `tbluser` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;