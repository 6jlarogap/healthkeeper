DROP TRIGGER IF EXISTS `trcustomfactortype_delete`;
DELIMITER //
CREATE TRIGGER `trcustomfactortype_delete` BEFORE DELETE ON `tblcustomfactortype`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = OLD.id, userid = OLD.userid, operationtypeid = 3, changeddate = CURRENT_TIMESTAMP where rowid = OLD.rowid;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trcustomfactortype_insert`;
DELIMITER //
CREATE TRIGGER `trcustomfactortype_insert` AFTER INSERT ON `tblcustomfactortype`
 FOR EACH ROW BEGIN
    INSERT INTO tbljournaloperation Set rowid = NEW.rowid, id = NEW.id, userid = NEW.userid, operationtypeid = 1; 
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trcustomfactortype_update`;
DELIMITER //
CREATE TRIGGER `trcustomfactortype_update` AFTER UPDATE ON `tblcustomfactortype`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = NEW.id, userid = NEW.userid, operationtypeid = 2, changeddate = CURRENT_TIMESTAMP where rowid = NEW.rowid;
END
//
DELIMITER ;