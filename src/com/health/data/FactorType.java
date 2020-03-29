package com.health.data;

import com.health.data.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.graphics.Color;
// KEEP INCLUDES END
/**
 * Entity mapped to table tblfactortype.
 */
public class FactorType implements IGridItem {

    private Long id;
    private String Name;
    private Integer OrdinalNumber;
    private Integer Status;
    private Long UnitDimensionId;
    private Long FactorGroupId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient FactorTypeDao myDao;

    private UnitDimension unitDimension;
    private Long unitDimension__resolvedKey;

    private FactorGroup factorGroup;
    private Long factorGroup__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    public static final int POSITIVE_STATUS = 1; //зеленый
    public static final int NEUTRAL_STATUS = 2; //желтый
    public static final int NEGATIVE_STATUS = 3; //красный

    public static final String FACTOR_GROUP_COLUMN_NAME = "factorgroupid";
    public static final String UNIT_DIMENSION_COLUMN_NAME = "unitid";
    // KEEP FIELDS END

    public FactorType() {
    }

    public FactorType(Long id) {
        this.id = id;
    }

    public FactorType(Long id, String Name, Integer OrdinalNumber, Integer Status, Long UnitDimensionId, Long FactorGroupId) {
        this.id = id;
        this.Name = Name;
        this.OrdinalNumber = OrdinalNumber;
        this.Status = Status;
        this.UnitDimensionId = UnitDimensionId;
        this.FactorGroupId = FactorGroupId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFactorTypeDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
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
    public String getFullName() {
        return String.format("%d. %s", this.OrdinalNumber,  this.Name);
    }


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
        return this.getGroup();
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