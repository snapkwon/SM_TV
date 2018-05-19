package vn.digital.signage.android.api.response;

import java.io.Serializable;
import java.util.List;

public class FileResponse extends BaseResponse implements Serializable {

    private String fileName;

    private String link;

    private List<String> lists;

    private int size;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getLists() {
        return lists;
    }

    public void setLists(List<String> lists) {
        this.lists = lists;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
