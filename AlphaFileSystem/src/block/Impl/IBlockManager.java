package block.Impl;


import block.BlockMeta;
import block.Interface.BlockManager;
import file.Impl.StringId;
import main.ErrorCode;
import main.Id;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IBlockManager implements BlockManager, Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<IBlock> blocks = new ArrayList<>();
    private StringId bmId;

    public IBlockManager(StringId id, ArrayList<IBlock> blocks) {
        this.bmId = id;
        this.blocks = blocks;
    }

    public StringId getBmId() {
        return bmId;
    }

    public List<IBlock> getBlocks() {
        return blocks;
    }

    public IBlock getBlock(Id indexId) {
        return blocks.get(((IndexId) indexId).getId());
    }

    public IBlock newBlock(byte[] b) {

        if(blocks == null)blocks = new ArrayList<>();

        IBlock iBlock = new IBlock(new IndexId(blocks.size()+1), this);
        iBlock.setData(b);

        blocks.add(iBlock);
        long check = iBlock.checksum(b);

        try {
            File file = new File("BlockManager/"+bmId.getId());
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File("BlockManager/" + bmId.getId() + "/" + iBlock.getIndexId().getId() + ".data");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(b);

            file = new File("BlockManager/"+bmId.getId() + "/" + iBlock.getIndexId().getId() + ".meta");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            BlockMeta blockMeta = new BlockMeta(b.length, check);
            oos.writeObject(blockMeta);
        } catch (IOException e) {
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }
        return iBlock;
    }

    public IBlock newEmptyBlock(int blockSize) {
        return newBlock(new byte[blockSize]);
    }
}
