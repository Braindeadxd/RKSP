package Third;

public class MyFiles {
    private int fileSize;
    private FileType fileType;

    public MyFiles(int fileSize, FileType fileType){
        this.fileSize =fileSize;
        this.fileType = fileType;
    }
    public int getFileSize(){
        return this.fileSize;
    }
    public FileType getFileType(){
        return this.fileType;
    }
}
