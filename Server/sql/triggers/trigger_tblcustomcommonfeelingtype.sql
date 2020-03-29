DROP TRIGGER IF EXISTS `trcustomcommonfeelingtype_delete`;
DELIMITER //
CREATE TRIGGER `trcustomcommonfeelingtype_delete` BEFORE DELETE ON `tblcustomcommonfeelingtype`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = OLD.id, userid = OLD.userid, operationtypeid = 3, changeddate = CURRENT_TIMESTAMP where rowid = OLD.rowid;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trcustomcommonfeelingtype_insert`;
DELIMITER //
CREATE TRIGGER `trcustomcommonfeelingtype_insert` AFTER INSERT ON `tblcustomcommonfeelingtype`
 FOR EACH ROW BEGIN
    INSERT INTO tbljournaloperation Set rowid = NEW.rowid, id = NEW.id, userid = NEW.userid, operationtypeid = 1; 
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trcustomcommonfeelingtype_update`;
DELIMITER //
CREATE TRIGGER `trcustomcommonfeelingtype_update` AFTER UPDATE ON `tblcustomcommonfeelingtype`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = NEW.id, userid = NEW.userid, operationtypeid = 2, changeddate = CURRENT_TIMESTAMP where rowid = NEW.rowid;
END
//
DELIMITER ;