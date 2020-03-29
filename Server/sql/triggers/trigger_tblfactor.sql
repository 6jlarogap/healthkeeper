DROP TRIGGER IF EXISTS `trfactor_delete`;
DELIMITER //
CREATE TRIGGER `trfactor_delete` BEFORE DELETE ON `tblfactor`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = OLD.id, userid = OLD.userid, operationtypeid = 3, changeddate = CURRENT_TIMESTAMP where rowid = OLD.rowid;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trfactor_insert`;
DELIMITER //
CREATE TRIGGER `trfactor_insert` AFTER INSERT ON `tblfactor`
 FOR EACH ROW BEGIN
    INSERT INTO tbljournaloperation Set rowid = NEW.rowid, id = NEW.id, userid = NEW.userid, operationtypeid = 1; 
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `trfactor_update`;
DELIMITER //
CREATE TRIGGER `trfactor_update` AFTER UPDATE ON `tblfactor`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = NEW.id, userid = NEW.userid, operationtypeid = 2, changeddate = CURRENT_TIMESTAMP where rowid = NEW.rowid;
END
//
DELIMITER ;