package com.health.data;

import com.health.data.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tbluserbodyfeelingtype.
 */
public class UserBodyFeelingType {

    private Long id;
    private Long ServerId;
    private String RowId;
    private Long UserId;
    private Integer Color;
    private Long BodyFeelingTypeId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient UserBodyFeelingTypeDao myDao;

    private BodyFeelingType bodyFeelingType;
    private Long bodyFeelingType__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    public int OperationTypeId;
    // KEEP FIELDS END

    public UserBodyFeelingType() {
    }

    public UserBodyFeelingType(Long id) {
        this.id = id;
    }

    public UserBodyFeelingType(Long id, Long ServerId, String RowId, Long UserId, Integer Color, Long BodyFeelingTypeId) {
        this.id = id;
        this.ServerId = ServerId;
        this.RowId = RowId;
        this.UserId = UserId;
        this.Color = Color;
        this.BodyFeelingTypeId = BodyFeelingTypeId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserBodyFeelingTypeDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServerId() {
        return ServerId;
    }

    public void setServerId(Long ServerId) {
        this.ServerId = ServerId;
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

    public Integer getColor() {
        return Color;
    }

    public void setColor(Integer Color) {
        this.Color = Color;
    }

    public Long getBodyFeelingTypeId() {
        return BodyFeelingTypeId;
    }

    public void setBodyFeelingTypeId(Long BodyFeelingTypeId) {
        this.BodyFeelingTypeId = BodyFeelingTypeId;
    }

    /** To-one relationship, resolved on first access. */
    public BodyFeelingType getBodyFeelingType() {
        Long __key = this.BodyFeelingTypeId;
        if (bodyFeelingType__resolvedKey == null || !bodyFeelingType__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BodyFeelingTypeDao targetDao = daoSession.getBodyFeelingTypeDao();
            BodyFeelingType bodyFeelingTypeNew = targetDao.load(__key);
            synchronized (this) {
                bodyFeelingType = bodyFeelingTypeNew;
            	bodyFeelingType__resolvedKey = __key;
            }
        }
        return bodyFeelingType;
    }

    public void setBodyFeelingType(BodyFeelingType bodyFeelingType) {
        synchronized (this) {
            this.bodyFeelingType = bodyFeelingType;
            BodyFeelingTypeId = bodyFeelingType == null ? null : bodyFeelingType.getId();
            bodyFeelingType__resolvedKey = BodyFeelingTypeId;
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
