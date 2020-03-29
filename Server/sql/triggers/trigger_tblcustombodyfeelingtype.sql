DROP TRIGGER IF EXISTS `trcustombodyfeelingtype_delete`;
DELIMITER //
CREATE TRIGGER `trcustombodyfeelingtype_delete` BEFORE DELETE ON `tblcustombodyfeelingtype`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = OLD.id, userid = OLD.userid, operationtypeid = 3, changeddate = CURRENT_TIMESTAMP where rowid = OLD.rowid;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trcustombodyfeelingtype_insert`;
DELIMITER //
CREATE TRIGGER `trcustombodyfeelingtype_insert` AFTER INSERT ON `tblcustombodyfeelingtype`
 FOR EACH ROW BEGIN
    INSERT INTO tbljournaloperation Set rowid = NEW.rowid, id = NEW.id, userid = NEW.userid, operationtypeid = 1; 
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trcustombodyfeelingtype_update`;
DELIMITER //
CREATE TRIGGER `trcustombodyfeelingtype_update` AFTER UPDATE ON `tblcustombodyfeelingtype`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = NEW.id, userid = NEW.userid, operationtypeid = 2, changeddate = CURRENT_TIMESTAMP where rowid = NEW.rowid;
END
//
DELIMITER ;