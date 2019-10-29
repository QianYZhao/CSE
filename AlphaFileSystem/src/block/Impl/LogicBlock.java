package block.Impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LogicBlock implements Serializable {
    private static final long serialVersionUID = 1L;
    private int number;
    private ArrayList<IBlock> iBlocks = new ArrayList<>();

    public LogicBlock(int number,ArrayList<IBlock> iBlocks){
        this.number = number;
        this.iBlocks = iBlocks;
    }

    public byte[] read() {

        int random = (int) (Math.random() * iBlocks.size()-1) + 1;
        return iBlocks.get(random).read();
    }

    public List<IBlock> getiBlocks() {
        return iBlocks;
    }
}

