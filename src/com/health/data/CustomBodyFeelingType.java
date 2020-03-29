package com.health.data;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tblcustombodyfeelingtype.
 */
public class CustomBodyFeelingType implements IDictionaryDTO, IBaseUserSyncDTO {

    private Long id;
    private Long ServerId;
    private String RowId;
    private Long UserId;
    private String name;

    // KEEP FIELDS - put your custom fields here
    public int OperationTypeId;
    // KEEP FIELDS END

    public CustomBodyFeelingType() {
    }

    public CustomBodyFeelingType(Long id) {
        this.id = id;
    }

    public CustomBodyFeelingType(Long id, Long ServerId, String RowId, Long UserId, String name) {
        this.id = id;
        this.ServerId = ServerId;
        this.RowId = RowId;
        this.UserId = UserId;
        this.name = name;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}