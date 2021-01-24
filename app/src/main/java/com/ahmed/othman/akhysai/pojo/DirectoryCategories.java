package com.ahmed.othman.akhysai.pojo;

public class DirectoryCategories {
    private int id;
    private String is_active;
    private String name;

    public DirectoryCategories(int id, String is_active, String name) {
        this.id = id;
        this.is_active = is_active;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
