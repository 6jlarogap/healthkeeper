package com.health.data;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tblsun.
 */
public class Sun {

    private Long id;
    private Long ServerId;
    private String RowId;
    private java.util.Date InfoDate;
    private java.util.Date SunRise;
    private java.util.Date SunSet;
    private String DayLength;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Sun() {
    }

    public Sun(Long id) {
        this.id = id;
    }

    public Sun(Long id, Long ServerId, String RowId, java.util.Date InfoDate, java.util.Date SunRise, java.util.Date SunSet, String DayLength) {
        this.id = id;
        this.ServerId = ServerId;
        this.RowId = RowId;
        this.InfoDate = InfoDate;
        this.SunRise = SunRise;
        this.SunSet = SunSet;
        this.DayLength = DayLength;
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

    public java.util.Date getInfoDate() {
        return InfoDate;
    }

    public void setInfoDate(java.util.Date InfoDate) {
        this.InfoDate = InfoDate;
    }

    public java.util.Date getSunRise() {
        return SunRise;
    }

    public void setSunRise(java.util.Date SunRise) {
        this.SunRise = SunRise;
    }

    public java.util.Date getSunSet() {
        return SunSet;
    }

    public void setSunSet(java.util.Date SunSet) {
        this.SunSet = SunSet;
    }

    public String getDayLength() {
        return DayLength;
    }

    public void setDayLength(String DayLength) {
        this.DayLength = DayLength;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}