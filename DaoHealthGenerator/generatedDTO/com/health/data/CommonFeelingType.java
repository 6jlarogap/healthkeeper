package com.health.data;

import com.health.data.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.graphics.Color;
// KEEP INCLUDES END
/**
 * Entity mapped to table tblcommonfeelingtype.
 */
public class CommonFeelingType implements IGridItem, IGridGroup {

    private Long id;
    private String Name;
    private String FullName;
    private Integer OrdinalNumber;
    private Integer Status;
    private Long UnitDimensionId;
    private Long CommonFeelingGroupId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient CommonFeelingTypeDao myDao;

    private UnitDimension unitDimension;
    private Long unitDimension__resolvedKey;

    private CommonFeelingGroup commonFeelingGroup;
    private Long commonFeelingGroup__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public CommonFeelingType() {
    }

    public CommonFeelingType(Long id) {
        this.id = id;
    }

    public CommonFeelingType(Long id, String Name, String FullName, Integer OrdinalNumber, Integer Status, Long UnitDimensionId, Long CommonFeelingGroupId) {
        this.id = id;
        this.Name = Name;
        this.FullName = FullName;
        this.OrdinalNumber = OrdinalNumber;
        this.Status = Status;
        this.UnitDimensionId = UnitDimensionId;
        this.CommonFeelingGroupId = CommonFeelingGroupId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCommonFeelingTypeDao() : null;
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

    public Long getCommonFeelingGroupId() {
        return CommonFeelingGroupId;
    }

    public void setCommonFeelingGroupId(Long CommonFeelingGroupId) {
        this.CommonFeelingGroupId = CommonFeelingGroupId;
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
    public CommonFeelingGroup getCommonFeelingGroup() {
        Long __key = this.CommonFeelingGroupId;
        if (commonFeelingGroup__resolvedKey == null || !commonFeelingGroup__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CommonFeelingGroupDao targetDao = daoSession.getCommonFeelingGroupDao();
            CommonFeelingGroup commonFeelingGroupNew = targetDao.load(__key);
            synchronized (this) {
                commonFeelingGroup = commonFeelingGroupNew;
            	commonFeelingGroup__resolvedKey = __key;
            }
        }
        return commonFeelingGroup;
    }

    public void setCommonFeelingGroup(CommonFeelingGroup commonFeelingGroup) {
        synchronized (this) {
            this.commonFeelingGroup = commonFeelingGroup;
            CommonFeelingGroupId = commonFeelingGroup == null ? null : commonFeelingGroup.getId();
            commonFeelingGroup__resolvedKey = CommonFeelingGroupId;
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
        return this.getCommonFeelingGroup();
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
