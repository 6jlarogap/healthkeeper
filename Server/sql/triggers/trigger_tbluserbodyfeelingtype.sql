DROP TRIGGER IF EXISTS `truserbodyfeelingtype_insert`;
DELIMITER //
CREATE  TRIGGER truserbodyfeelingtype_insert AFTER INSERT ON tbluserbodyfeelingtype 
FOR EACH ROW BEGIN
    INSERT INTO tbljournaloperation Set rowid = NEW.rowid, id = NEW.id, userid = NEW.userid, operationtypeid = 1; 
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `truserbodyfeelingtype_update`;
DELIMITER //
CREATE  TRIGGER truserbodyfeelingtype_update AFTER UPDATE ON tbluserbodyfeelingtype 
FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = NEW.id, userid = NEW.userid, operationtypeid = 2, changeddate = CURRENT_TIMESTAMP where rowid = NEW.rowid;
END
//
DELIMITER ;
DROP TRIGGER IF EXISTS `truserbodyfeelingtype_delete`;
DELIMITER //
CREATE  TRIGGER truserbodyfeelingtype_delete BEFORE DELETE ON tbluserbodyfeelingtype 
FOR EACH ROW BEGIN
    UPDATE tbljournaloperation Set id = OLD.id, userid = OLD.userid, operationtypeid = 3, changeddate = CURRENT_TIMESTAMP where rowid = OLD.rowid;
END
//
DELIMITER ;