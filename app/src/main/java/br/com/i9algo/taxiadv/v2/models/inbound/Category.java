package br.com.i9algo.taxiadv.v2.models.inbound;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.i9algo.taxiadv.v2.helpers.Db;
import br.com.i9algo.taxiadv.v2.helpers.Logger;
import br.com.i9algo.taxiadv.v2.utils.DateUtils;

@IgnoreExtraProperties
public class Category implements Serializable, Parcelable {

    public static final String DEFAULT_COVER = "assets://cover_sidebar.jpg";

    public static final String TABLE = "category";

    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String ICON_URL = "icon_url";
    public static final String COVER_IMAGE = "cover_image";
    public static final String BGCOLOR = "bg_color";
    public static final String GRID_COLUMNS = "grid_columns";

    @SerializedName(ID)
    @Expose
    private int id;

    @SerializedName(CREATED_AT)
    @Expose
    private String createdAt;

    @SerializedName(UPDATED_AT)
    @Expose
    private String updatedAt;

    @SerializedName(NAME)
    @Expose
    private String name;

    @SerializedName(DESCRIPTION)
    @Expose
    private String description;

    @SerializedName(ICON_URL)
    @Expose
    private String iconUrl;

    @SerializedName(COVER_IMAGE)
    @Expose
    private String coverImage;

    @SerializedName(BGCOLOR)
    @Expose
    private String bgColor = "#00000000"; // transparent

    @SerializedName(GRID_COLUMNS)
    @Expose
    private int gridColumns;

    public final static Creator<Category> CREATOR = new Creator<Category>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }
        public Category[] newArray(int size) {
            return (new Category[size]);
        }
    };

    private final static long serialVersionUID = 8969981899865194503L;

    protected Category(Parcel in) {
        this.id = ((int) in.readValue((Integer.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.coverImage = ((String) in.readValue((String.class.getClassLoader())));
        this.bgColor = ((String) in.readValue((String.class.getClassLoader())));
        this.iconUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Category() { }

    /**
     *
     * @param iconUrl
     * @param gridColumns
     * @param id
     * @param updatedAt
     * @param bgColor
     * @param createdAt
     * @param description
     * @param name
     * @param coverImage
     */
    public Category(int id, String createdAt, String updatedAt, String name, String description, String iconUrl, String coverImage,
                    String bgColor, int gridColumns) {
        super();
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.coverImage = coverImage;
        this.bgColor = bgColor;
        this.gridColumns = gridColumns;
    }

    public String getTableName() { return TABLE;    }

    public int getId() { return id;    }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage){ this.coverImage = coverImage; }

    public String getBgColor() { return bgColor; }
    public void setBgColor(String color) { this.bgColor = color; }

    public String getIconURL() { return iconUrl; }
    public void setIconURL(String iconURL) { this.iconUrl = iconURL; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public Date getCreatedAtAsDate(){
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (TextUtils.isEmpty(createdAt))
                return null;

            return DateUtils.parseISO8601Date(createdAt);
            //return formatter.parse(createdAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    public Date getUpdatedAtAsDate(){
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (TextUtils.isEmpty(updatedAt))
                return null;

            return DateUtils.parseISO8601Date(updatedAt);
            //return formatter.parse(createdAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public int getGridColumns() { return gridColumns; }
    public void setGridColumns(int gridColumns) { this.gridColumns = gridColumns; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
        dest.writeValue(name);
        dest.writeValue(description);
        dest.writeValue(coverImage);
        dest.writeValue(bgColor);
        dest.writeValue(iconUrl);
        dest.writeValue(gridColumns);
    }


    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ID, getId());
        values.put(NAME, getName());
        values.put(DESCRIPTION, getDescription());
        values.put(COVER_IMAGE, getCoverImage());
        values.put(BGCOLOR, getBgColor());
        values.put(ICON_URL, getIconURL());
        values.put(CREATED_AT, getCreatedAt());
        values.put(UPDATED_AT, getUpdatedAt());
        values.put(GRID_COLUMNS, getGridColumns());
        return values;
    }

    public static Category fromCursor(Cursor cursor) {
        Category cat = new Category();

        cat.setId( Db.getInt(cursor, ID, -1) );
        cat.setCreatedAt( Db.getString(cursor, CREATED_AT, "") );
        cat.setUpdatedAt( Db.getString(cursor, UPDATED_AT, "") );
        cat.setName( Db.getString(cursor, NAME, "") );
        cat.setDescription( Db.getString(cursor, DESCRIPTION, "") );
        cat.setCoverImage( Db.getString(cursor, COVER_IMAGE, Category.DEFAULT_COVER) );
        cat.setBgColor( Db.getString(cursor, BGCOLOR, "#00000000") );
        cat.setIconURL( Db.getString(cursor, ICON_URL, "") );
        cat.setGridColumns( Db.getInt(cursor, GRID_COLUMNS, 3) );

        return cat;
    }

    public static Category fromDataSnapshot(DataSnapshot snapshot) {
        Category cat = new Category();

        cat.setId( snapshot.child(ID).getValue(Integer.class) );
        cat.setCreatedAt( snapshot.child(CREATED_AT).getValue(String.class) );
        cat.setUpdatedAt( snapshot.child(UPDATED_AT).getValue(String.class) );
        cat.setName( snapshot.child(NAME).getValue(String.class) );
        cat.setDescription( snapshot.child(DESCRIPTION).getValue(String.class) );
        cat.setCoverImage( snapshot.child(COVER_IMAGE).getValue(String.class) );
        cat.setBgColor( snapshot.child(BGCOLOR).getValue(String.class) );
        cat.setIconURL( snapshot.child(ICON_URL).getValue(String.class) );
        cat.setGridColumns( snapshot.child(GRID_COLUMNS).getValue(Integer.class) );

        return cat;
    }

    public static Category fromDataDocmentSnapshot(QueryDocumentSnapshot doc) {
        Category cat = new Category();

        cat.setId( doc.getLong(ID).intValue() );

        Date creatAt = doc.getDate(CREATED_AT);
        if (creatAt != null) {
            cat.setCreatedAt( DateUtils.formatISO8601Date(creatAt) );
        }

        Date upAt = doc.getDate(UPDATED_AT);
        if (upAt != null) {
            cat.setUpdatedAt( DateUtils.formatISO8601Date(upAt) );
        }

        cat.setName( doc.get(NAME, String.class) );
        cat.setDescription( doc.get(DESCRIPTION, String.class) );
        cat.setCoverImage( doc.get(COVER_IMAGE, String.class) );
        cat.setBgColor( doc.get(BGCOLOR, String.class) );
        cat.setIconURL( doc.get(ICON_URL, String.class) );
        cat.setGridColumns( doc.getLong(GRID_COLUMNS).intValue() );

        return cat;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put(ID, id);
        result.put(CREATED_AT, createdAt);
        result.put(UPDATED_AT, updatedAt);
        result.put(NAME, name);
        result.put(DESCRIPTION, description);
        result.put(COVER_IMAGE, coverImage);
        result.put(BGCOLOR, bgColor);
        result.put(ICON_URL, iconUrl);
        result.put(GRID_COLUMNS, gridColumns);

        return result;
    }

    @Override
    public String toString() {

        return "SlideshowPlaylist{" +
                "\n id =" + id +
                "\n createdAt ='" + createdAt +
                "\n updatedAt ='" + updatedAt +
                "\n name ='" + name +
                "\n description ='" + description +
                "\n coverImage ='" + coverImage +
                "\n bgColor ='" + bgColor +
                "\n iconURL ='" + iconUrl +
                "\n gridColumns=" + gridColumns +
                "\n}";
    }

    public int describeContents() {
        return 0;
    }
}
