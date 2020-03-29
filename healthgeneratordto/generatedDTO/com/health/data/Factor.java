package com.health.data;

import com.health.data.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tblfactor.
 */
public class Factor implements IGridItemValue {

    private Long id;
    private Long ServerId;
    private String RowId;
    private Long UserId;
    private java.util.Date StartDate;
    private Double Value1;
    private Double Value2;
    private Double Value3;
    private Long FactorTypeId;
    private Long CustomFactorTypeId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient FactorDao myDao;

    private FactorType factorType;
    private Long factorType__resolvedKey;

    private CustomFactorType customFactorType;
    private Long customFactorType__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Factor() {
    }

    public Factor(Long id) {
        this.id = id;
    }

    public Factor(Long id, Long ServerId, String RowId, Long UserId, java.util.Date StartDate, Double Value1, Double Value2, Double Value3, Long FactorTypeId, Long CustomFactorTypeId) {
        this.id = id;
        this.ServerId = ServerId;
        this.RowId = RowId;
        this.UserId = UserId;
        this.StartDate = StartDate;
        this.Value1 = Value1;
        this.Value2 = Value2;
        this.Value3 = Value3;
        this.FactorTypeId = FactorTypeId;
        this.CustomFactorTypeId = CustomFactorTypeId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFactorDao() : null;
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

    public Double getValue1() {
        return Value1;
    }

    public void setValue1(Double Value1) {
        this.Value1 = Value1;
    }

    public Double getValue2() {
        return Value2;
    }

    public void setValue2(Double Value2) {
        this.Value2 = Value2;
    }

    public Double getValue3() {
        return Value3;
    }

    public void setValue3(Double Value3) {
        this.Value3 = Value3;
    }

    public Long getFactorTypeId() {
        return FactorTypeId;
    }

    public void setFactorTypeId(Long FactorTypeId) {
        this.FactorTypeId = FactorTypeId;
    }

    public Long getCustomFactorTypeId() {
        return CustomFactorTypeId;
    }

    public void setCustomFactorTypeId(Long CustomFactorTypeId) {
        this.CustomFactorTypeId = CustomFactorTypeId;
    }

    /** To-one relationship, resolved on first access. */
    public FactorType getFactorType() {
        Long __key = this.FactorTypeId;
        if (factorType__resolvedKey == null || !factorType__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FactorTypeDao targetDao = daoSession.getFactorTypeDao();
            FactorType factorTypeNew = targetDao.load(__key);
            synchronized (this) {
                factorType = factorTypeNew;
            	factorType__resolvedKey = __key;
            }
        }
        return factorType;
    }

    public void setFactorType(FactorType factorType) {
        synchronized (this) {
            this.factorType = factorType;
            FactorTypeId = factorType == null ? null : factorType.getId();
            factorType__resolvedKey = FactorTypeId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public CustomFactorType getCustomFactorType() {
        Long __key = this.CustomFactorTypeId;
        if (customFactorType__resolvedKey == null || !customFactorType__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomFactorTypeDao targetDao = daoSession.getCustomFactorTypeDao();
            CustomFactorType customFactorTypeNew = targetDao.load(__key);
            synchronized (this) {
                customFactorType = customFactorTypeNew;
            	customFactorType__resolvedKey = __key;
            }
        }
        return customFactorType;
    }

    public void setCustomFactorType(CustomFactorType customFactorType) {
        synchronized (this) {
            this.customFactorType = customFactorType;
            CustomFactorTypeId = customFactorType == null ? null : customFactorType.getId();
            customFactorType__resolvedKey = CustomFactorTypeId;
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
