package com.health.data;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table tblunitdimension.
 */
public class UnitDimension {

    private Long id;
    private String Name;

    // KEEP FIELDS - put your custom fields here
    public static final long BOOLEAN_TYPE = 1;
    public static final long NUMBER_TYPE = 2;
    public static final long NUMBER_NUMBER_TYPE = 3;
    public static final long TIME_TIME_TYPE = 4;
    public static final long TIME_TIME_NUMBER_TYPE = 5;
    // KEEP FIELDS END

    public UnitDimension() {
    }

    public UnitDimension(Long id) {
        this.id = id;
    }

    public UnitDimension(Long id, String Name) {
        this.id = id;
        this.Name = Name;
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

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}