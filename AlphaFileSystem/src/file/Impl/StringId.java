package file.Impl;

import main.Id;

import java.io.Serializable;

public class StringId implements Id,Serializable{
    private static final long serialVersionUID = 1L;
    private String id;

    public StringId(String id){
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
