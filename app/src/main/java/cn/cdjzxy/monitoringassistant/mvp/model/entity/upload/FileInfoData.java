package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

public class FileInfoData {


    /**
     * Id : 00000000-0000-0000-0000-000000000000
     * FileName : string
     * FilePath : string
     * UpdateTime : 2019-06-15T01:43:45.073Z
     * FileType : 0
     * Code : 0
     * CodeMsg : string
     */

    private String Id;
    private String FileName;
    private String FilePath;
    private String UpdateTime;
    private int FileType;
    private int Code;
    private String CodeMsg;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public int getFileType() {
        return FileType;
    }

    public void setFileType(int FileType) {
        this.FileType = FileType;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getCodeMsg() {
        return CodeMsg;
    }

    public void setCodeMsg(String CodeMsg) {
        this.CodeMsg = CodeMsg;
    }
}
