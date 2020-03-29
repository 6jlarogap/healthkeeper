package com.health.data;

import com.health.data.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tblcomplaint.
 */
public class Complaint {

    private Long id;
    private Long ServerId;
    private String RowId;
    private Long UserId;
    private java.util.Date StartDate;
    private Integer Count;
    private Long BodyComplaintTypeId;
    private Long CommonFeelingTypeId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ComplaintDao myDao;

    private BodyComplaintType bodyComplaintType;
    private Long bodyComplaintType__resolvedKey;

    private CommonFeelingType commonFeelingType;
    private Long commonFeelingType__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Complaint() {
    }

    public Complaint(Long id) {
        this.id = id;
    }

    public Complaint(Long id, Long ServerId, String RowId, Long UserId, java.util.Date StartDate, Integer Count, Long BodyComplaintTypeId, Long CommonFeelingTypeId) {
        this.id = id;
        this.ServerId = ServerId;
        this.RowId = RowId;
        this.UserId = UserId;
        this.StartDate = StartDate;
        this.Count = Count;
        this.BodyComplaintTypeId = BodyComplaintTypeId;
        this.CommonFeelingTypeId = CommonFeelingTypeId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getComplaintDao() : null;
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

    public java.util.Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(java.util.Date StartDate) {
        this.StartDate = StartDate;
    }

    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer Count) {
        this.Count = Count;
    }

    public Long getBodyComplaintTypeId() {
        return BodyComplaintTypeId;
    }

    public void setBodyComplaintTypeId(Long BodyComplaintTypeId) {
        this.BodyComplaintTypeId = BodyComplaintTypeId;
    }

    public Long getCommonFeelingTypeId() {
        return CommonFeelingTypeId;
    }

    public void setCommonFeelingTypeId(Long CommonFeelingTypeId) {
        this.CommonFeelingTypeId = CommonFeelingTypeId;
    }

    /** To-one relationship, resolved on first access. */
    public BodyComplaintType getBodyComplaintType() {
        Long __key = this.BodyComplaintTypeId;
        if (bodyComplaintType__resolvedKey == null || !bodyComplaintType__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BodyComplaintTypeDao targetDao = daoSession.getBodyComplaintTypeDao();
            BodyComplaintType bodyComplaintTypeNew = targetDao.load(__key);
            synchronized (this) {
                bodyComplaintType = bodyComplaintTypeNew;
            	bodyComplaintType__resolvedKey = __key;
            }
        }
        return bodyComplaintType;
    }

    public void setBodyComplaintType(BodyComplaintType bodyComplaintType) {
        synchronized (this) {
            this.bodyComplaintType = bodyComplaintType;
            BodyComplaintTypeId = bodyComplaintType == null ? null : bodyComplaintType.getId();
            bodyComplaintType__resolvedKey = BodyComplaintTypeId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public CommonFeelingType getCommonFeelingType() {
        Long __key = this.CommonFeelingTypeId;
        if (commonFeelingType__resolvedKey == null || !commonFeelingType__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CommonFeelingTypeDao targetDao = daoSession.getCommonFeelingTypeDao();
            CommonFeelingType commonFeelingTypeNew = targetDao.load(__key);
            synchronized (this) {
                commonFeelingType = commonFeelingTypeNew;
            	commonFeelingType__resolvedKey = __key;
            }
        }
        return commonFeelingType;
    }

    public void setCommonFeelingType(CommonFeelingType commonFeelingType) {
        synchronized (this) {
            this.commonFeelingType = commonFeelingType;
            CommonFeelingTypeId = commonFeelingType == null ? null : commonFeelingType.getId();
            commonFeelingType__resolvedKey = CommonFeelingTypeId;
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
