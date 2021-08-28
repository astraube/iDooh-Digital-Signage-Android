package br.com.i9algo.taxiadv.v2.download;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

import br.com.i9algo.taxiadv.v2.helpers.Db;

public class DownloadItem implements Serializable {

    public static final String TABLE = "download";
    public static final String ID = "id";

    public static final String SLIDE_ID = "slide_id";
    public static final String ORIGINAL_URL = "original_URL";

    public static final String TASK_ID = "task_id";
    public static final String FILE_LOCATION = "file_location";

    public static final String STATUS = "status";

    private int id;
    private int slideId;
    private String originalURL;
    private int taskId;
    private String fileLocation;
    private int status;

    public DownloadItem() {
    }

    public DownloadItem(int id, int slideId, String originalURL, int taskId, String fileLocation, int status) {
        this.id = id;
        this.slideId = slideId;
        this.originalURL = originalURL;
        this.taskId = taskId;
        this.fileLocation = fileLocation;
        this.status = status;
    }

    public DownloadItem(int slideId, String originalURL, int taskId, String fileLocation, int status) {
        this.slideId = slideId;
        this.originalURL = originalURL;
        this.taskId = taskId;
        this.fileLocation = fileLocation;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlideId() {
        return slideId;
    }

    public void setSlideId(int slideId) {
        this.slideId = slideId;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public static DownloadItem fromCursor(Cursor cursor) {

        int slideId = Db.getInt(cursor, SLIDE_ID, -1);

        int id = Db.getInt(cursor, ID, -1);

        int taskId = Db.getInt(cursor, TASK_ID, -1);

        String originalURL = Db.getString(cursor, ORIGINAL_URL, "");
        String fileLocation = Db.getString(cursor, FILE_LOCATION, "");

        int status = Db.getInt(cursor, STATUS, -1);

        return new DownloadItem(id, slideId, originalURL, taskId, fileLocation, status);
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (getId() != 0){
            values.put(ID, getId());
        }
        values.put(SLIDE_ID, getSlideId());
        values.put(TASK_ID, getTaskId());
        values.put(ORIGINAL_URL, getOriginalURL());
        values.put(FILE_LOCATION, getFileLocation());
        values.put(STATUS, getStatus());

        return values;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DownloadItem{" +
                "id=" + id +
                ", slideId=" + slideId +
                ", originalURL='" + originalURL + '\'' +
                ", taskId=" + taskId +
                ", fileLocation='" + fileLocation + '\'' +
                '}';
    }
}
