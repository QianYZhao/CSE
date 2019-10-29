package INode;

import block.Impl.IBlock;
import block.Impl.IBlockManager;
import block.Impl.LogicBlock;
import file.Impl.IFileManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataMap {
    //single
    private  List<IFileManager>fmSet = new LinkedList<>();

    private  List<IBlockManager>bmSet = new LinkedList<>();

    private DataMap(){
    }

    public List<IBlockManager> getBmSet() {
        return bmSet;
    }

    public List<IFileManager> getFmSet() {
        return fmSet;
    }

    public static DataMap getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final DataMap INSTANCE = new DataMap();
    }

}
