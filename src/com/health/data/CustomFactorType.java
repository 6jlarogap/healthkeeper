package com.health.data;

import com.health.data.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.graphics.Color;
// KEEP INCLUDES END
/**
 * Entity mapped to table tblcustomfactortype.
 */
public class CustomFactorType implements IGridItem {

    private Long id;
    private Long ServerId;
    private String RowId;
    private Long UserId;
    private String Name;
    private String FullName;
    private Integer OrdinalNumber;
    private Integer Status;
    private Long UnitDimensionId;
    private Long FactorGroupId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient CustomFactorTypeDao myDao;

    private UnitDimension unitDimension;
    private Long unitDimension__resolvedKey;

    private FactorGroup factorGroup;
    private Long factorGroup__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    public static final String FACTOR_GROUP_COLUMN_NAME = "factorgroupid";
    public static final String UNIT_DIMENSION_COLUMN_NAME = "unitid";
    
    public int OperationTypeId;
    // KEEP FIELDS END

    public CustomFactorType() {
    }

    public CustomFactorType(Long id) {
        this.id = id;
    }

    public CustomFactorType(Long id, Long ServerId, String RowId, Long UserId, String Name, String FullName, Integer OrdinalNumber, Integer Status, Long UnitDimensionId, Long FactorGroupId) {
        this.id = id;
        this.ServerId = ServerId;
        this.RowId = RowId;
        this.UserId = UserId;
        this.Name = Name;
        this.FullName = FullName;
        this.OrdinalNumber = OrdinalNumber;
        this.Status = Status;
        this.UnitDimensionId = UnitDimensionId;
        this.FactorGroupId = FactorGroupId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCustomFactorTypeDao() : null;
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

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public Integer getOrdinalNumber() {
        return OrdinalNumber;
    }

    public void setOrdinalNumber(Integer OrdinalNumber) {
        this.OrdinalNumber = OrdinalNumber;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer Status) {
        this.Status = Status;
    }

    public Long getUnitDimensionId() {
        return UnitDimensionId;
    }

    public void setUnitDimensionId(Long UnitDimensionId) {
        this.UnitDimensionId = UnitDimensionId;
    }

    public Long getFactorGroupId() {
        return FactorGroupId;
    }

    public void setFactorGroupId(Long FactorGroupId) {
        this.FactorGroupId = FactorGroupId;
    }

    /** To-one relationship, resolved on first access. */
    public UnitDimension getUnitDimension() {
        Long __key = this.UnitDimensionId;
        if (unitDimension__resolvedKey == null || !unitDimension__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UnitDimensionDao targetDao = daoSession.getUnitDimensionDao();
            UnitDimension unitDimensionNew = targetDao.load(__key);
            synchronized (this) {
                unitDimension = unitDimensionNew;
            	unitDimension__resolvedKey = __key;
            }
        }
        return unitDimension;
    }

    public void setUnitDimension(UnitDimension unitDimension) {
        synchronized (this) {
            this.unitDimension = unitDimension;
            UnitDimensionId = unitDimension == null ? null : unitDimension.getId();
            unitDimension__resolvedKey = UnitDimensionId;
        }
    }

    /** To-one relationship, resolved on first access. */
    public FactorGroup getFactorGroup() {
        Long __key = this.FactorGroupId;
        if (factorGroup__resolvedKey == null || !factorGroup__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FactorGroupDao targetDao = daoSession.getFactorGroupDao();
            FactorGroup factorGroupNew = targetDao.load(__key);
            synchronized (this) {
                factorGroup = factorGroupNew;
            	factorGroup__resolvedKey = __key;
            }
        }
        return factorGroup;
    }

    public void setFactorGroup(FactorGroup factorGroup) {
        synchronized (this) {
            this.factorGroup = factorGroup;
            FactorGroupId = factorGroup == null ? null : factorGroup.getId();
            factorGroup__resolvedKey = FactorGroupId;
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
    @Override
    public int getColor() {
        int color = Color.GREEN;
        switch (this.Status) {
            case FactorType.NEGATIVE_STATUS:
                color = Color.RED;
                break;
            case FactorType.POSITIVE_STATUS:
                color = Color.GREEN;
                break;
            case FactorType.NEUTRAL_STATUS:
                color = Color.YELLOW;
                break;
            default:
                break;
        }
        return color;
    }


    @Override
    public IGridGroup getGroup() {
        return this.getFactorGroup();
    }

    @Override
    public long getUnitId() {
        if(this.UnitDimensionId != null){
            return this.UnitDimensionId;
        } else {
            return UnitDimension.BOOLEAN_TYPE;
        }
    }
    // KEEP METHODS END

}
