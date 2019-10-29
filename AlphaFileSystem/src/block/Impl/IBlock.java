package block.Impl;


import block.BlockMeta;
import block.Interface.Block;
import main.ErrorCode;
import main.Id;

import java.io.*;

public class IBlock implements Block ,Serializable{

    private static final long serialVersionUID = 1L;
    private IndexId id;
    private IBlockManager iBlockManager;
    private int size;//512Bytes
    private byte[] data = new byte[512];

    public IBlock(IndexId id,IBlockManager iBlockManager){
        this.id = id;
        this.iBlockManager = iBlockManager;
    }

    public IndexId getIndexId(){
        return id;
    }

    public IBlockManager getBlockManager(){
        return iBlockManager;
    }

    public byte[] read(){
        try{
            File file = new File("BlockManager/"+iBlockManager.getBmId().getId()+"/"+id.getId()+".data");
            FileInputStream fis = new FileInputStream(file);
            fis.read(data);
        }catch (IOException e){
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }

        try{
            File file = new File("BlockManager/"+iBlockManager.getBmId().getId()+"/"+id.getId()+".meta");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            BlockMeta blockMeta = (BlockMeta) ois.readObject();
            if(checksum(data) != blockMeta.getCheck())throw new ErrorCode(ErrorCode.CHECKSUM_CHECK_FAILED);//检查check码是否相同
        }catch (IOException e){
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }catch (ClassNotFoundException e){
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }

        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int blockSize(){
        return size;
    }

    public long checksum(byte[] bs){
        long h = bs.length;

        for(byte b:bs){
            long lb = (long)b;
            h = ( (h<<1) | (h>>63) | ((lb & 0xc3)<<41) | ((lb & 0xa7)<< 12)) + lb*91871341 + 1821349192;
        }
        return h;
    }
}
