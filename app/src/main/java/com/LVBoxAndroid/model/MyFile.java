package com.LVBoxAndroid.model;

import com.google.firebase.database.Exclude;

public class MyFile {

    private String name;
    private String extension;
    private String version;
    private String path;
    private String alive;
    private String owner;
    private String state;
    private String id;

    public MyFile(String name){
        this.name = name;
    }
    public MyFile(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlive() {
        return alive;
    }

    public void setAlive(String alive) {
        this.alive = alive;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Exclude
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
