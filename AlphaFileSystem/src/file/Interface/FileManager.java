package file.Interface;

import main.Id;

public interface FileManager {
    File getFile(Id fileId);
    File newFile(Id fileId);
}
