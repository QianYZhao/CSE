package block.Interface;

import block.Impl.IBlockManager;
import main.Id;

public interface Block {
    Id getIndexId();
    BlockManager getBlockManager();
    byte[] read();
    int blockSize();
}

