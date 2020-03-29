package com.health.dependency;

/**
 * Created by alex sh on 19.10.2015.
 */
public class DependencyGroup {

    private int id;
    private String name;
    private String hiddenName;

    public DependencyGroup(int id, String name, String hiddenName) {
        this.id = id;
        this.name = name;
        this.hiddenName = hiddenName;
    }

    public void setId(int id) {

        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHiddenName(String hiddenName) {
        this.hiddenName = hiddenName;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getHiddenName() {
        return hiddenName;
    }


}
