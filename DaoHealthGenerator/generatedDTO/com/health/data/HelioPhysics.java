package com.health.data;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tblheliophysicsdaily.
 */
public class HelioPhysics {

    private Long id;
    private Long ServerId;
    private String RowId;
    private java.util.Date InfoDate;
    private Integer F10_7;
    private Integer SunspotNumber;
    private Integer SunspotArea;
    private Integer NewRegions;
    private String Xbkgd;
    private Integer FlaresC;
    private Integer FlaresM;
    private Integer FlaresX;
    private Integer FlaresS;
    private Integer Flares1;
    private Integer Flares2;
    private Integer Flares3;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public HelioPhysics() {
    }

    public HelioPhysics(Long id) {
        this.id = id;
    }

    public HelioPhysics(Long id, Long ServerId, String RowId, java.util.Date InfoDate, Integer F10_7, Integer SunspotNumber, Integer SunspotArea, Integer NewRegions, String Xbkgd, Integer FlaresC, Integer FlaresM, Integer FlaresX, Integer FlaresS, Integer Flares1, Integer Flares2, Integer Flares3) {
        this.id = id;
        this.ServerId = ServerId;
        this.RowId = RowId;
        this.InfoDate = InfoDate;
        this.F10_7 = F10_7;
        this.SunspotNumber = SunspotNumber;
        this.SunspotArea = SunspotArea;
        this.NewRegions = NewRegions;
        this.Xbkgd = Xbkgd;
        this.FlaresC = FlaresC;
        this.FlaresM = FlaresM;
        this.FlaresX = FlaresX;
        this.FlaresS = FlaresS;
        this.Flares1 = Flares1;
        this.Flares2 = Flares2;
        this.Flares3 = Flares3;
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

    public Integer getF10_7() {
        return F10_7;
    }

    public void setF10_7(Integer F10_7) {
        this.F10_7 = F10_7;
    }

    public Integer getSunspotNumber() {
        return SunspotNumber;
    }

    public void setSunspotNumber(Integer SunspotNumber) {
        this.SunspotNumber = SunspotNumber;
    }

    public Integer getSunspotArea() {
        return SunspotArea;
    }

    public void setSunspotArea(Integer SunspotArea) {
        this.SunspotArea = SunspotArea;
    }

    public Integer getNewRegions() {
        return NewRegions;
    }

    public void setNewRegions(Integer NewRegions) {
        this.NewRegions = NewRegions;
    }

    public String getXbkgd() {
        return Xbkgd;
    }

    public void setXbkgd(String Xbkgd) {
        this.Xbkgd = Xbkgd;
    }

    public Integer getFlaresC() {
        return FlaresC;
    }

    public void setFlaresC(Integer FlaresC) {
        this.FlaresC = FlaresC;
    }

    public Integer getFlaresM() {
        return FlaresM;
    }

    public void setFlaresM(Integer FlaresM) {
        this.FlaresM = FlaresM;
    }

    public Integer getFlaresX() {
        return FlaresX;
    }

    public void setFlaresX(Integer FlaresX) {
        this.FlaresX = FlaresX;
    }

    public Integer getFlaresS() {
        return FlaresS;
    }

    public void setFlaresS(Integer FlaresS) {
        this.FlaresS = FlaresS;
    }

    public Integer getFlares1() {
        return Flares1;
    }

    public void setFlares1(Integer Flares1) {
        this.Flares1 = Flares1;
    }

    public Integer getFlares2() {
        return Flares2;
    }

    public void setFlares2(Integer Flares2) {
        this.Flares2 = Flares2;
    }

    public Integer getFlares3() {
        return Flares3;
    }

    public void setFlares3(Integer Flares3) {
        this.Flares3 = Flares3;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
