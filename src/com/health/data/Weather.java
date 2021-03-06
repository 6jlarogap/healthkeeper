package com.health.data;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tblweatherhourly.
 */
public class Weather {

    private Long id;
    private Long ServerId;
    private String RowId;
    private java.util.Date InfoDate;
    private Double Temperature;
    private Double MinTemperature;
    private Double MaxTemperature;
    private Double Pressure;
    private Double Humidity;
    private Double WindSpeed;
    private Double WindDeg;
    private Double Clouds;
    private Double Rain;
    private Double Snow;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Weather() {
    }

    public Weather(Long id) {
        this.id = id;
    }

    public Weather(Long id, Long ServerId, String RowId, java.util.Date InfoDate, Double Temperature, Double MinTemperature, Double MaxTemperature, Double Pressure, Double Humidity, Double WindSpeed, Double WindDeg, Double Clouds, Double Rain, Double Snow) {
        this.id = id;
        this.ServerId = ServerId;
        this.RowId = RowId;
        this.InfoDate = InfoDate;
        this.Temperature = Temperature;
        this.MinTemperature = MinTemperature;
        this.MaxTemperature = MaxTemperature;
        this.Pressure = Pressure;
        this.Humidity = Humidity;
        this.WindSpeed = WindSpeed;
        this.WindDeg = WindDeg;
        this.Clouds = Clouds;
        this.Rain = Rain;
        this.Snow = Snow;
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

    public Double getTemperature() {
        return Temperature;
    }

    public void setTemperature(Double Temperature) {
        this.Temperature = Temperature;
    }

    public Double getMinTemperature() {
        return MinTemperature;
    }

    public void setMinTemperature(Double MinTemperature) {
        this.MinTemperature = MinTemperature;
    }

    public Double getMaxTemperature() {
        return MaxTemperature;
    }

    public void setMaxTemperature(Double MaxTemperature) {
        this.MaxTemperature = MaxTemperature;
    }

    public Double getPressure() {
        return Pressure;
    }

    public void setPressure(Double Pressure) {
        this.Pressure = Pressure;
    }

    public Double getHumidity() {
        return Humidity;
    }

    public void setHumidity(Double Humidity) {
        this.Humidity = Humidity;
    }

    public Double getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(Double WindSpeed) {
        this.WindSpeed = WindSpeed;
    }

    public Double getWindDeg() {
        return WindDeg;
    }

    public void setWindDeg(Double WindDeg) {
        this.WindDeg = WindDeg;
    }

    public Double getClouds() {
        return Clouds;
    }

    public void setClouds(Double Clouds) {
        this.Clouds = Clouds;
    }

    public Double getRain() {
        return Rain;
    }

    public void setRain(Double Rain) {
        this.Rain = Rain;
    }

    public Double getSnow() {
        return Snow;
    }

    public void setSnow(Double Snow) {
        this.Snow = Snow;
    }

    // KEEP METHODS - put your custom methods here
    public Double getPrecipitation(){
        Double result = null;
        if(this.Rain != null || this.Snow != null){
            result = (this.Rain != null ? this.Rain : 0) + (this.Snow != null ? this.Snow : 0);
        }
        return result;
    }
    // KEEP METHODS END

}
