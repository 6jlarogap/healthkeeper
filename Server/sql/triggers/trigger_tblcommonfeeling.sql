DROP TRIGGER IF EXISTS `tblcommonfeeling_delete`;
DELIMITER //
CREATE TRIGGER `tblcommonfeeling_delete` BEFORE DELETE ON `tblcommonfeeling`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = OLD.id, userid = OLD.userid, operationtypeid = 3, changeddate = CURRENT_TIMESTAMP where rowid = OLD.rowid;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `tblcommonfeeling_insert`;
DELIMITER //
CREATE TRIGGER `tblcommonfeeling_insert` AFTER INSERT ON `tblcommonfeeling`
 FOR EACH ROW BEGIN
    INSERT INTO tbljournaloperation Set rowid = NEW.rowid, id = NEW.id, userid = NEW.userid, operationtypeid = 1; 
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `tblcommonfeeling_update`;
DELIMITER //
CREATE TRIGGER `tblcommonfeeling_update` AFTER UPDATE ON `tblcommonfeeling`
 FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = NEW.id, userid = NEW.userid, operationtypeid = 2, changeddate = CURRENT_TIMESTAMP where rowid = NEW.rowid;
END
//
DELIMITER ;