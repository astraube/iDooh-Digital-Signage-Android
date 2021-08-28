package br.com.i9algo.taxiadv.v2.models.slideshow;

import java.io.Serializable;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import br.com.i9algo.taxiadv.v2.helpers.DateFormatHelper;
import br.com.i9algo.taxiadv.v2.helpers.Db;

public class SlideshowPlaylist implements Serializable, Parcelable
{

    public static final String TABLE = "slideshowplaylist";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String START_AT = "start_at";
    public static final String EXPIRES_AT = "expires_at";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";

    private List<SlideshowItem> items;

    @SerializedName(ID)
    @Expose
    private int id;
    @SerializedName(NAME)
    @Expose
    private String name;
    @SerializedName(CREATED_AT)
    @Expose
    private String createdAt;
    @SerializedName(UPDATED_AT)
    @Expose
    private String updatedAt;
    @SerializedName(START_AT)
    @Expose
    private String startAt;
    @SerializedName(EXPIRES_AT)
    @Expose
    private String expiresAt;
    @SerializedName(START_TIME)
    @Expose
    private String startTime;
    @SerializedName(END_TIME)
    @Expose
    private String endTime;

    public final static Parcelable.Creator<SlideshowPlaylist> CREATOR = new Creator<SlideshowPlaylist>() {
        @SuppressWarnings({
                "unchecked"
        })
        public SlideshowPlaylist createFromParcel(Parcel in) {
            return new SlideshowPlaylist(in);
        }
        public SlideshowPlaylist[] newArray(int size) {
            return (new SlideshowPlaylist[size]);
        }
    };

    private final static long serialVersionUID = 3621939545419320198L;

    /**
     * No args constructor for use in serialization
     *
     */
    public SlideshowPlaylist() { }

    protected SlideshowPlaylist(Parcel in) {
        this.id = ((int) in.readValue((Integer.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.startAt = ((String) in.readValue((String.class.getClassLoader())));
        this.expiresAt = ((String) in.readValue((String.class.getClassLoader())));
        this.startTime = ((String) in.readValue((String.class.getClassLoader())));
        this.endTime = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     *
     * @param expiresAt
     * @param endTime
     * @param startTime
     * @param id
     * @param updatedAt
     * @param createdAt
     * @param name
     * @param startAt
     */
    public SlideshowPlaylist(List<SlideshowItem> items, int id, String name, String createdAt, String updatedAt, String startAt, String expiresAt, String startTime, String endTime) {
        super();
        this.items = items;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startAt = startAt;
        this.expiresAt = expiresAt;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setItems(List<SlideshowItem> items) { this.items = items; }
    public List<SlideshowItem> getItems() { return items; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCreatedAt() {return createdAt;}
    public void setCreatedAt(String createdAt) {this.createdAt = createdAt;}

    public String getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}

    public String getStartAt() {return startAt;}
    public void setStartAt(String startAt) {this.startAt = startAt;}

    public String getExpiresAt() {return expiresAt;}
    public void setExpiresAt(String expiresAt) {this.expiresAt = expiresAt;}

    public String getStartTime() {return startTime;}
    public void setStartTime(String startTime) {this.startTime = startTime;}

    public String getEndTime() {return endTime;}
    public void setEndTime(String endTime) {this.endTime = endTime;}

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
        dest.writeValue(startAt);
        dest.writeValue(expiresAt);
        dest.writeValue(startTime);
        dest.writeValue(endTime);
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ID, getId());
        values.put(NAME, getName());
        values.put(CREATED_AT, getCreatedAt());
        values.put(UPDATED_AT, getUpdatedAt());
        values.put(START_AT, getStartAt());
        values.put(EXPIRES_AT, getExpiresAt());
        values.put(START_TIME, DateFormatHelper.getMilitaryTime(getStartTime()));
        values.put(END_TIME, DateFormatHelper.getMilitaryTime(getEndTime()));
        return values;
    }

    public static SlideshowPlaylist fromCursor(Cursor cursor) {
        SlideshowPlaylist playlist = new SlideshowPlaylist();

        int id = Db.getInt(cursor, ID, -1);
        playlist.setId(id);

        playlist.setName( Db.getString(cursor, NAME, "") );
        playlist.setCreatedAt( Db.getString(cursor, CREATED_AT, "") );
        playlist.setUpdatedAt( Db.getString(cursor, UPDATED_AT, "") );
        playlist.setStartAt( Db.getString(cursor, START_AT, "") );
        playlist.setExpiresAt( Db.getString(cursor, EXPIRES_AT, "") );
        playlist.setStartTime( Integer.toString(Db.getInt(cursor, START_TIME, 0)) );
        playlist.setEndTime( Integer.toString(Db.getInt(cursor, END_TIME, 0)) );

        return playlist;
    }

    @Override
    public String toString() {
        String itemsString = "";

        for (SlideshowItem item : items){
            itemsString = itemsString + "\n" + item.toString();
        }

        return "SlideshowPlaylist{" +
                "\n id =" + id +
                "\n name ='" + name +
                "\n createdAt ='" + createdAt +
                "\n updatedAt ='" + updatedAt +
                "\n startAt ='" + startAt +
                "\n expiresAt ='" + expiresAt +
                "\n startTime ='" + startTime +
                "\n timeEnd ='" + endTime +
                "\n itemsList=" + itemsString +
                "\n}";
    }

    public int describeContents() {
        return 0;
    }

}