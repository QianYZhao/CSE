package file.Impl;


import INode.DataMap;
import block.Impl.LogicBlock;
import file.FileMeta;

import file.Interface.File;
import main.ErrorCode;

import java.io.*;
import java.util.ArrayList;


public class IFile implements File, Serializable {

    private static final long serialVersionUID = 1L;
    int MOVE_CURR = 0;
    int MOVE_HEAD = 1;
    int MOVE_TAIL = 2;

    private int cur = 0;//当前读写位置
    private StringId fileId;
    private IFileManager iFileManager;

    private long size;

    IFile(StringId fileId, IFileManager fileManager) {
        this.fileId = fileId;
        this.iFileManager = fileManager;
    }

    public StringId getFileId() {
        return fileId;
    }

    public IFileManager getFileManager() {
        return iFileManager;
    }

    public byte[] read(int length) {
        byte[] b = new byte[length];

        try {
            java.io.File file = new java.io.File("FileManager/" + iFileManager.getFmId().getId() + "/" + fileId.getId() + ".meta");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            FileMeta fileMeta = (FileMeta) ois.readObject();
            ois.close();

            int count = 0;
            boolean stop = false;
            for (int i = 0; i < fileMeta.getLogicBlocks().size(); i++) {
                byte[] tmp = fileMeta.getLogicBlocks().get(i).read();
                for (int j = 0; j < tmp.length; j++) {
                    if (count >= length) stop = true;
                    if (stop) break;
                    b[count++] = tmp[j];
                }
                if (stop) break;
            }

        } catch (IOException e) {
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        } catch (ClassNotFoundException e) {
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }

        return b;
    }

    public void write(byte[] b) {
        size = cur + b.length;//改写文件的大小
        //改FileMeta
        FileMeta fileMeta = null;
        try {
            java.io.File file = new java.io.File("FileManager/" + iFileManager.getFmId().getId() + "/" + fileId.getId() + ".meta");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            fileMeta = (FileMeta) ois.readObject();
            ois.close();
        } catch (IOException e) {
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        } catch (ClassNotFoundException e) {
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }
        fileMeta.setFileSize(size);

        int len;
        int count = 0;

        int mod = cur % 512;
        int lbnum_modify = (int) cur / 512;
        len = b.length + mod + 512 - (b.length + mod) % 512;

        byte[] writeBytes = new byte[len];

        if (mod != 0) {
            LogicBlock oldLogicBlock = fileMeta.getLogicBlocks().get(lbnum_modify);
            byte[] data = oldLogicBlock.read();
            for (int i = 0; i < mod; i++) {
                writeBytes[count++] = data[i];
            }
        }

        //删除序列号为lbnum_modify之后的logicBlocks；
        while (fileMeta.getLogicBlocks().size() > lbnum_modify) {
            fileMeta.getLogicBlocks().remove(fileMeta.getLogicBlocks().size() - 1);
        }

        for (int i = 0; i < b.length; i++) {
            writeBytes[count++] = b[i];
        }

        while (count < len) {
            writeBytes[count++] = 0x00;//不足补0
        }

        //写入block里
        int block_number = len / 512;
        //分割成block大小
        byte[][] data = new byte[block_number][512];
        int num = 0;
        int bi = 0;//当前写的位置
        while (bi < len) {
            for (int j = 0; j < 512; j++) {
                data[num][j] = writeBytes[bi++];
            }
            num++;
        }

        count = 0;
        int logicNum = fileMeta.getLogicBlocks().size();
        while (count < block_number) {

            LogicBlock logicBlock = new LogicBlock(logicNum++, new ArrayList<>());

            for (int i = 0; i < 3; i++) {
                //TODO：应该使用某种分配算法分配副本的数量，而不是3
                int random = (int) (Math.random() * DataMap.getInstance().getBmSet().size() - 1) + 1;
                logicBlock.getiBlocks().add(DataMap.getInstance().getBmSet().get(random).newBlock(data[count]));
            }
            fileMeta.getLogicBlocks().add(logicBlock);
            count++;
        }
        cur = 0;

        try {
            java.io.File file = new java.io.File("FileManager/" + iFileManager.getFmId().getId() + "/" + fileId.getId() + ".meta");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(fileMeta);
        } catch (IOException e) {
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }
    }

    public long move(long offset, int where) {
        if (where == MOVE_CURR) {
            cur += offset;
        } else if (where == MOVE_HEAD) {
            cur = 0;
            cur += offset;
        } else if (where == MOVE_TAIL) {
            cur = (int) size;
            cur += offset;
        }
        if (cur > size || cur < 0) throw new ErrorCode(ErrorCode.CURSOR_MOVE_ILLEGAL);
        return cur;
    }

    public void close() {

    }

    public long size() {
        return size;
    }

    public void setSize(long newSize) {
        if (newSize > size) {
            byte[] b = new byte[(int) (newSize - size)];
            for (int i = 0; i < newSize - size; i++) {
                b[i++] = 0x00;
            }
            cur = (int) size;
            write(b);
            cur = 0;
        }

        this.size = newSize;
    }

}
