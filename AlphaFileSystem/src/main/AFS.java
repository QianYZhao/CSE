package main;

import INode.DataMap;
import block.Impl.IBlock;
import block.Impl.IBlockManager;
import block.Impl.IndexId;
import constant.FileConstant;
import file.FileMeta;
import file.Impl.IFile;
import file.Impl.IFileManager;
import file.Impl.StringId;

import java.io.*;


public class AFS {
    //single
    private AFS() {

    }

    public static AFS getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final AFS INSTANCE = new AFS();
    }

    //系统初始化函数，调用返回初始状态，清空所有文件。
    public void system_init() {

        for (int i = 1; i <= 100; i++) {
            try {
                File file = new File("afs/FileManager-" + i + ".obj");
                IFileManager iFileManager = new IFileManager(new StringId("fm-" + i), null);

                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(iFileManager);
            } catch (IOException e) {
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            }
        }


        for (int i = 1; i <= 100; i++) {
            try {
                File file = new File("afs/BlockManager-" + i + ".obj");
                IBlockManager iBlockManager = new IBlockManager(new StringId("bm-" + i), null);

                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(iBlockManager);
            } catch (IOException e) {
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            }
        }

    }

    //系统退出将状态写入文件中
    public void store() {
        for (int i = 0; i < 100; i++) {
            try {
                File file = new File("afs/FileManager-" + (i + 1) + ".obj");
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(DataMap.getInstance().getFmSet().get(i));
            } catch (IOException e) {
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            }
        }

        for (int i = 0; i < 100; i++) {
            try {
                File file = new File("afs/BlockManager-" + (i + 1) + ".obj");
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                oos.writeObject(DataMap.getInstance().getBmSet().get(i));
            } catch (IOException e) {
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            }
        }
    }

    //恢复之前文件系统的状态
    public void restore() {
        int fmCount = 0;
        int bmCount = 0;
        try {
            File file = new File(FileConstant.PATH_TO_FMBMCOUNT_FILE);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String str = br.readLine();
            fmCount = Integer.parseInt(str);
            str = br.readLine();
            bmCount = Integer.parseInt(str);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //读fileManager
        for (int i = 1; i <= fmCount; i++) {
            try {
                File file = new File("afs/FileManager-" + i + ".obj");

                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                IFileManager iFileManager = (IFileManager) ois.readObject();
                DataMap.getInstance().getFmSet().add(iFileManager);

            } catch (IOException e) {
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            } catch (ClassNotFoundException e) {
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            }
        }
        //读blockManager

        for (int i = 1; i <= bmCount; i++) {
            try {
                File file = new File("afs/BlockManager-" + i + ".obj");
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                IBlockManager iBlockManager = (IBlockManager) ois.readObject();
                DataMap.getInstance().getBmSet().add(iBlockManager);
            } catch (IOException e) {
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            } catch (ClassNotFoundException e) {
                throw new ErrorCode(ErrorCode.IO_EXCEPTION);
            }
        }


    }

    public byte[] openFile(String filename) {
        IFile iFile = getFile(filename);
        if (iFile != null) return iFile.read((int) iFile.size());
        throw new ErrorCode(ErrorCode.IFILE_NOT_EXISTED);
    }

    public void createFile(String filename) {
        int len = DataMap.getInstance().getFmSet().size();

        IFile iFile = getFile(filename);
        if (iFile != null) {
            throw new ErrorCode(ErrorCode.IFILE_EXISTED);//文件已存在不能再创建；
        }
        for (int i = 0; i < len; i++) {
            if (DataMap.getInstance().getFmSet().get(i).getiFiles() == null || DataMap.getInstance().getFmSet().get(i).getiFiles().size() < 4) {
                DataMap.getInstance().getFmSet().get(i).newFile(new StringId(filename));
                break;
            }
        }
    }

    public void writeFile(String filename, byte[] b) {
        IFile iFile = getFile(filename);
        if (iFile == null) {
            createFile(filename);
            iFile = getFile(filename);
        }
        if (iFile != null) iFile.write(b);
    }

    public byte[] getBlockData(String bmId, int blockId) {
        IBlock iBlock = new IBlock(new IndexId(blockId), new IBlockManager(new StringId(bmId), null));
        return iBlock.read();
    }

    public void copyFile(String copied, String copy) {
        IFile copyIFile = getFile(copy);
        if (copyIFile == null) createFile(copy);

        try {
            FileMeta copiedFileMeta;

            IFile cfile = getFile(copied);
            if (cfile == null) return;

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("FileManager/" + cfile.getFileManager().getFmId().getId() + "/" + copied + ".meta")));
            copiedFileMeta = (FileMeta) ois.readObject();
            ois.close();

            IFile file = getFile(copy);
            file.setSize(cfile.size());

            FileMeta copyFileMeta = new FileMeta(new StringId(copy));
            copyFileMeta.setBlockSize(512);
            copyFileMeta.setFileSize(copiedFileMeta.getSize());
            copyFileMeta.setLogicBlocks(copiedFileMeta.getLogicBlocks());

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("FileManager/" + file.getFileManager().getFmId().getId() + "/" + copy + ".meta")));
            oos.writeObject(copyFileMeta);
        } catch (IOException e) {
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        } catch (ClassNotFoundException e) {
            throw new ErrorCode(ErrorCode.IO_EXCEPTION);
        }
    }

    public void move(String filename, long offset, int where) {
        IFile iFile = getFile(filename);
        if(iFile == null) {
            createFile(filename);
            iFile = getFile(filename);
        }
        iFile.move(offset, where);
    }

    public void setIFileSize(String filename, long newSize) {
        IFile iFile = getFile(filename);
        iFile.setSize(newSize);
    }

    private IFile getFile(String filename) {
        IFile iFile = null;
        for (int i = 0; i < DataMap.getInstance().getFmSet().size(); i++) {
            if (DataMap.getInstance().getFmSet().get(i).getiFiles() != null) {
                iFile = DataMap.getInstance().getFmSet().get(i).getFile(new StringId(filename));
                if (iFile != null) break;
            }
        }
        return iFile;
    }
}
