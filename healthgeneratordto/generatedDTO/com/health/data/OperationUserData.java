package com.health.data;

import com.health.data.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tbloperationuserdata.
 */
public class OperationUserData {

    private Long id;
    private Long ClientId;
    private Long ServerId;
    private Integer OperationType;
    private Integer TableId;
    private java.util.Date OperationDate;
    private String RowId;
    private Long UserId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient OperationUserDataDao myDao;

    private User user;
    private Long user__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public OperationUserData() {
    }

    public OperationUserData(Long id) {
        this.id = id;
    }

    public OperationUserData(Long id, Long ClientId, Long ServerId, Integer OperationType, Integer TableId, java.util.Date OperationDate, String RowId, Long UserId) {
        this.id = id;
        this.ClientId = ClientId;
        this.ServerId = ServerId;
        this.OperationType = OperationType;
        this.TableId = TableId;
        this.OperationDate = OperationDate;
        this.RowId = RowId;
        this.UserId = UserId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOperationUserDataDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return ClientId;
    }

    public void setClientId(Long ClientId) {
        this.ClientId = ClientId;
    }

    public Long getServerId() {
        return ServerId;
    }

    public void setServerId(Long ServerId) {
        this.ServerId = ServerId;
    }

    public Integer getOperationType() {
        return OperationType;
    }

    public void setOperationType(Integer OperationType) {
        this.OperationType = OperationType;
    }

    public Integer getTableId() {
        return TableId;
    }

    public void setTableId(Integer TableId) {
        this.TableId = TableId;
    }

    public java.util.Date getOperationDate() {
        return OperationDate;
    }

    public void setOperationDate(java.util.Date OperationDate) {
        this.OperationDate = OperationDate;
    }

    public String getRowId() {
        return RowId;
    }

    public void setRowId(String RowId) {
        this.RowId = RowId;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long UserId) {
        this.UserId = UserId;
    }

    /** To-one relationship, resolved on first access. */
    public User getUser() {
        Long __key = this.UserId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
            	user__resolvedKey = __key;
            }
        }
        return user;
    }

    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            UserId = user == null ? null : user.getId();
            user__resolvedKey = UserId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}