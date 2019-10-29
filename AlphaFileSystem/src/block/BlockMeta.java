package block;

import java.io.Serializable;

public class BlockMeta implements Serializable{
    private static final long serialVersionUID = 1L;
    private int blockSize;
    private long check;

    public BlockMeta(int blockSize, long check) {
        this.blockSize = blockSize;
        this.check = check;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public long getCheck() {
        return check;
    }

    public void setCheck(long check) {
        this.check = check;
    }
}
