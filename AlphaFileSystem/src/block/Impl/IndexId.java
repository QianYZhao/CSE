package block.Impl;

import main.Id;

import java.io.Serializable;

public class IndexId implements Id ,Serializable{
    private static final long serialVersionUID = 1L;
    private int count;

    public IndexId(int count){
        this.count = count;
    }
    public int getId() {
        return count;
    }

    public void setId(int count) {
        this.count = count;
    }
}
