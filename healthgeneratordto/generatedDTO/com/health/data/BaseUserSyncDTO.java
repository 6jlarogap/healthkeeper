package com.health.data;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table BASE_USER_SYNC_DTO.
 */
public class BaseUserSyncDTO {

    private Long id;
    private Long ServerId;
    private String RowId;
    private Long UserId;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public BaseUserSyncDTO() {
    }

    public BaseUserSyncDTO(Long id) {
        this.id = id;
    }

    public BaseUserSyncDTO(Long id, Long ServerId, String RowId, Long UserId) {
        this.id = id;
        this.ServerId = ServerId;
        this.RowId = RowId;
        this.UserId = UserId;
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

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}