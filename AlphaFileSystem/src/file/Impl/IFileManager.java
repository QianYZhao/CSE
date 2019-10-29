package file.Impl;

import constant.FileConstant;
import file.FileMeta;
import file.Interface.FileManager;
import main.ErrorCode;
import main.Id;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class IFileManager implements FileManager,Serializable{
    private static final long serialVersionUID = 1L;
    private StringId fmId;
    private ArrayList<IFile>iFiles = new ArrayList<>();

    public IFileManager(StringId id,ArrayList<IFile>files){
        this.fmId = id;
        this.iFiles = files;
    }


    public StringId getFmId() {
        return fmId;
    }

    public List<IFile> getiFiles() {
        return iFiles;
    }

    public IFile newFile(Id fileId){
        IFile iFile = new IFile((StringId) fileId,this);
        //写fileMeta
        if(iFiles == null)iFiles = new ArrayList<>();
        iFiles.add(iFile);

        FileMeta fileMeta = new FileMeta((StringId) fileId);
        fileMeta.setFileSize(0);//还未分配block
        fileMeta.setBlockSize(512);

        try {
            File file = new File("FileManager/"+fmId.getId());
            if(!file.exists()){
                file.mkdir();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("FileManager/"+fmId.getId()+"/"+((StringId) fileId).getId()+".meta"));
            oos.writeObject(fileMeta);
        }catch (IOException e){
            throw new ErrorCode(1);
        }

        return iFile;
    }

    public IFile getFile(Id fileId){
        for(int i=0;i<iFiles.size();i++){
            if(iFiles.get(i).getFileId().getId().equals(((StringId)fileId).getId())){
                return iFiles.get(i);
            }
        }
        return null;
    }

}
