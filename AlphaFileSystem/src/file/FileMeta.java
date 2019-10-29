package file;

import block.Impl.LogicBlock;
import file.Impl.StringId;
import main.Id;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileMeta implements Serializable {
    private static final long serialVersionUID = 1L;
    private StringId fileMetaId;
    private long fileSize;//文件大小
    private int blockSize;

    private ArrayList<LogicBlock> logicBlocks = new ArrayList<>();

    public FileMeta(StringId id) {
        this.fileMetaId = id;
    }

    public ArrayList<LogicBlock> getLogicBlocks() {
        return logicBlocks;
    }

    public long getSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setLogicBlocks(ArrayList<LogicBlock> logicBlocks) {
        for(int i=0;i<logicBlocks.size();i++){
            this.logicBlocks.add(logicBlocks.get(i));
        }
    }

    public StringId getFileMetaId() {
        return fileMetaId;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
}
