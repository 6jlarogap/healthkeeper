package com.health.data;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tblzodiaks.
 */
public class ZodiakType {

    private Long id;
    private String Name;
    private String Name2;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ZodiakType() {
    }

    public ZodiakType(Long id) {
        this.id = id;
    }

    public ZodiakType(Long id, String Name, String Name2) {
        this.id = id;
        this.Name = Name;
        this.Name2 = Name2;
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

    public String getName2() {
        return Name2;
    }

    public void setName2(String Name2) {
        this.Name2 = Name2;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
