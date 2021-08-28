package br.com.i9algo.taxiadv.v2.models.slideshow;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import br.com.i9algo.taxiadv.domain.enums.AdvType;
import br.com.i9algo.taxiadv.v2.helpers.Db;


public class SlideshowItem implements Serializable, Parcelable
{
    public static final String TABLE = "slideshowitem";

    public static final String ID = "id";
    public static final String TOKEN = "token";
    public static final String PLAYLIST_ID = "playlist_id";
    public static final String TYPE = "type";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String CAMPAIGN_ID = "campaign_id";
    public static final String EXIBITION_TIME = "exibition_time";
    public static final String TITLE = "title";
    public static final String SUMMARY = "summary";
    public static final String MAIN_IMAGE = "main_image";
    public static final String MAIN_IMAGE_SOURCE = "main_image_source";
    public static final String NEWS_SOURCE_NAME = "news_source_name";
    public static final String NEWS_SOURCE_IMAGE = "news_source_image";
    public static final String ORDER_SLIDE = "order_slide";
    public static final String ACTION_MODEL = "action_model";
    public static final String ACTION_MODEL_ID = "action_model_id";
    public static final String URL_CONTENT = "url";

    @SerializedName(ID)
    @Expose
    private int id;

    @SerializedName(TOKEN)
    @Expose
    private String token;

    private int playlistId;

    @SerializedName(TYPE)
    @Expose
    private String type;
    @SerializedName(CREATED_AT)
    @Expose
    private String createdAt;
    @SerializedName(UPDATED_AT)
    @Expose
    private String updatedAt;
    @SerializedName(CAMPAIGN_ID)
    @Expose
    private int campaignId;
    @SerializedName(EXIBITION_TIME)
    @Expose
    private int exibitionTime;
    @SerializedName(TITLE)
    @Expose
    private String title;
    @SerializedName(SUMMARY)
    @Expose
    private String summary;
    @SerializedName(MAIN_IMAGE)
    @Expose
    private String mainImage;
    @SerializedName(MAIN_IMAGE_SOURCE)
    @Expose
    private String mainImageSource;

    @SerializedName(NEWS_SOURCE_NAME)
    @Expose
    private String newsSourceName;

    @SerializedName(NEWS_SOURCE_IMAGE)
    @Expose
    private String newsSourceImage;

    @SerializedName(ORDER_SLIDE)
    @Expose
    private int order;

    @SerializedName(ACTION_MODEL)
    @Expose
    private String actionModel;

    @SerializedName(ACTION_MODEL_ID)
    @Expose
    private int actionModelId = -1;

    @SerializedName(URL_CONTENT)
    @Expose
    private String url;

    private final static long serialVersionUID = 5210688040558888777L;

    public final static Parcelable.Creator<SlideshowItem> CREATOR = new Creator<SlideshowItem>() {
        @SuppressWarnings({
                "unchecked"
        })
        public SlideshowItem createFromParcel(Parcel in) {
            return new SlideshowItem(in);
        }

        public SlideshowItem[] newArray(int size) {
            return (new SlideshowItem[size]);
        }
    };

    /**
     * No args constructor for use in serialization
     *
     */
    public SlideshowItem() {
    }

    protected SlideshowItem(Parcel in) {
        this.id = ((int) in.readValue((Integer.class.getClassLoader())));
        this.token = ((String) in.readValue((String.class.getClassLoader())));
        this.playlistId = ((int) in.readValue((Integer.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.campaignId = ((int) in.readValue((Integer.class.getClassLoader())));
        this.exibitionTime = ((int) in.readValue((Integer.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.summary = ((String) in.readValue((String.class.getClassLoader())));
        this.mainImage = ((String) in.readValue((String.class.getClassLoader())));
        this.mainImageSource = ((String) in.readValue((String.class.getClassLoader())));
        this.newsSourceName = ((String) in.readValue((String.class.getClassLoader())));
        this.newsSourceImage = ((String) in.readValue((String.class.getClassLoader())));
        this.order = ((int) in.readValue((Integer.class.getClassLoader())));
        this.actionModel = ((String) in.readValue((String.class.getClassLoader())));
        this.actionModelId = ((int) in.readValue((Integer.class.getClassLoader())));
        this.url = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     *
     * @param summary
     * @param actionModel
     * @param actionModelId
     * @param exibitionTime
     * @param id
     * @param campaignId
     * @param type
     * @param mainImage
     * @param updatedAt
     * @param title
     * @param order
     * @param newsSourceName
     * @param token
     * @param createdAt
     * @param newsSourceImage
     * @param mainImageSource
     */
    public SlideshowItem(int id, String token, int playlistId, String type, String createdAt,
                         String updatedAt, int campaignId, int exibitionTime, String title, String summary, String mainImage,
                         String mainImageSource, String newsSourceName, String newsSourceImage,
                         int order, String actionModel, int actionModelId) {
        super();
        this.id = id;
        this.token = token;
        this.playlistId = playlistId;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.campaignId = campaignId;
        this.exibitionTime = exibitionTime;
        this.title = title;
        this.summary = summary;
        this.mainImage = mainImage;
        this.mainImageSource = mainImageSource;
        this.newsSourceName = newsSourceName;
        this.newsSourceImage = newsSourceImage;
        this.order = order;
        this.actionModel = actionModel;
        this.actionModelId = actionModelId;
    }
    public SlideshowItem(int id, String token, int playlistId, String type, String createdAt,
                         String updatedAt, int campaignId, int exibitionTime, String title, String summary, String mainImage,
                         String mainImageSource, String newsSourceName, String newsSourceImage,
                         int order, String actionModel, int actionModelId, String url) {
        super();
        this.id = id;
        this.token = token;
        this.playlistId = playlistId;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.campaignId = campaignId;
        this.exibitionTime = exibitionTime;
        this.title = title;
        this.summary = summary;
        this.mainImage = mainImage;
        this.mainImageSource = mainImageSource;
        this.newsSourceName = newsSourceName;
        this.newsSourceImage = newsSourceImage;
        this.order = order;
        this.actionModel = actionModel;
        this.actionModelId = actionModelId;
        this.url = url;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isVideo() {
        return AdvType.VIDEO.toString().equalsIgnoreCase(getType());
    }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public int getCampaignId() { return campaignId; }
    public void setCampaignId(int campaignId) { this.campaignId = campaignId; }

    public int getExibitionTime() { return exibitionTime; }
    public void setExibitionTime(int exibitionTime) { this.exibitionTime = exibitionTime; }

    public String getTitle() { return title; }
    public void setTitle(String title) {this.title = title;}

    public String getSummary() {return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getHeader() { return getNewsSourceName() + " - " + getTitle(); }

    public String getMainImage() { return mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }

    public String getMainImageSource() { return mainImageSource; }
    public void setMainImageSource(String mainImageSource) { this.mainImageSource = mainImageSource; }

    public String getNewsSourceName() { return newsSourceName; }
    public void setNewsSourceName(String newsSourceName) { this.newsSourceName = newsSourceName; }

    public String getNewsSourceImage() { return newsSourceImage; }
    public void setNewsSourceImage(String newsSourceImage) { this.newsSourceImage = newsSourceImage; }

    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }

    public String getActionModel() { return actionModel; }
    public void setActionModel(String actionModel) { this.actionModel = actionModel; }

    public int getActionModelId() { return actionModelId; }
    public void setActionModelId(int actionModelId) { this.actionModelId = actionModelId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(token);
        dest.writeValue(playlistId);
        dest.writeValue(type);
        dest.writeValue(createdAt);
        dest.writeValue(updatedAt);
        dest.writeValue(campaignId);
        dest.writeValue(exibitionTime);
        dest.writeValue(title);
        dest.writeValue(summary);
        dest.writeValue(mainImage);
        dest.writeValue(mainImageSource);
        dest.writeValue(newsSourceName);
        dest.writeValue(newsSourceImage);
        dest.writeValue(order);
        dest.writeValue(actionModel);
        dest.writeValue(actionModelId);
        dest.writeValue(url);
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ID, getId());
        values.put(TOKEN, getToken());
        values.put(PLAYLIST_ID, getPlaylistId());
        values.put(TYPE, getType());
        values.put(CREATED_AT, getCreatedAt());
        values.put(UPDATED_AT, getUpdatedAt());
        values.put(CAMPAIGN_ID, getCampaignId());
        values.put(EXIBITION_TIME, getExibitionTime());
        values.put(TITLE, getTitle());
        values.put(SUMMARY, getSummary());
        values.put(MAIN_IMAGE, getMainImage());
        values.put(MAIN_IMAGE_SOURCE, getMainImageSource());
        values.put(NEWS_SOURCE_NAME, getNewsSourceName());
        values.put(NEWS_SOURCE_IMAGE, getNewsSourceImage());
        values.put(ORDER_SLIDE, getOrder());
        values.put(ACTION_MODEL, getActionModel());
        values.put(ACTION_MODEL_ID, getActionModelId());
        values.put(URL_CONTENT, getUrl());
        return values;
    }

    public static SlideshowItem fromCursor(Cursor cursor) {
        int _id = Db.getInt(cursor, ID, -1);
        String _token = Db.getString(cursor, TOKEN, "");
        int _playlistId = Db.getInt(cursor, PLAYLIST_ID, -1);
        String _type = Db.getString(cursor, TYPE, AdvType.DEFAULT.toString());
        String _createdAt = Db.getString(cursor, CREATED_AT, "");
        String _updatedAt = Db.getString(cursor, UPDATED_AT, "");
        int _campaignId = Db.getInt(cursor, CAMPAIGN_ID, 0);
        int _exibitionTime = Db.getInt(cursor, EXIBITION_TIME, 10);
        String _title = Db.getString(cursor, TITLE, "");
        String _summary = Db.getString(cursor, SUMMARY, "");
        String _mainImage = Db.getString(cursor, MAIN_IMAGE, "");
        String _mainImageSource = Db.getString(cursor, MAIN_IMAGE_SOURCE, "");
        String _newsSourceName = Db.getString(cursor, NEWS_SOURCE_NAME, "");
        String _newsSourceImage = Db.getString(cursor, NEWS_SOURCE_IMAGE, "");
        int _order = Db.getInt(cursor, ORDER_SLIDE, -1);
        String _actionModel = Db.getString(cursor, ACTION_MODEL, "");
        int _actionModelId = Db.getInt(cursor, ACTION_MODEL_ID, 0);
        String _url = Db.getString(cursor, URL_CONTENT, "");

        return new SlideshowItem(_id, _token, _playlistId, _type, _createdAt, _updatedAt, _campaignId,
                _exibitionTime, _title, _summary, _mainImage, _mainImageSource, _newsSourceName, _newsSourceImage, _order, _actionModel, _actionModelId, _url);
    }

    public static SlideshowItem fromCursor(Cursor cursor, String basePath) {
        String path = basePath + "_";
        int _id = Db.getInt(cursor, path + ID, -1);
        String _token = Db.getString(cursor, path + TOKEN, "");
        int _playlistId = Db.getInt(cursor, path + PLAYLIST_ID, -1);
        String _type = Db.getString(cursor, path + TYPE, AdvType.DEFAULT.toString());
        String _createdAt = Db.getString(cursor, path + CREATED_AT, "");
        String _updatedAt = Db.getString(cursor, path + UPDATED_AT, "");
        int _campaignId = Db.getInt(cursor, path + CAMPAIGN_ID, 0);
        int _exibitionTime = Db.getInt(cursor, path + EXIBITION_TIME, 10);
        String _title = Db.getString(cursor, path + TITLE, "");
        String _summary = Db.getString(cursor, path + SUMMARY, "");
        String _mainImage = Db.getString(cursor, path + MAIN_IMAGE, "");
        String _mainImageSource = Db.getString(cursor, path + MAIN_IMAGE_SOURCE, "");
        String _newsSourceName = Db.getString(cursor, path + NEWS_SOURCE_NAME, "");
        String _newsSourceImage = Db.getString(cursor, path + NEWS_SOURCE_IMAGE, "");
        int _order = Db.getInt(cursor, path + ORDER_SLIDE, -1);
        String _actionModel = Db.getString(cursor, path + ACTION_MODEL, "");
        int _actionModelId = Db.getInt(cursor, path + ACTION_MODEL_ID, 0);
        String _url = Db.getString(cursor, path + URL_CONTENT, "");

        return new SlideshowItem(_id, _token, _playlistId, _type, _createdAt, _updatedAt, _campaignId,
                _exibitionTime, _title, _summary, _mainImage, _mainImageSource, _newsSourceName, _newsSourceImage, _order, _actionModel, _actionModelId, _url);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "SlideshowItem{" +
                "\r\n\t\t\t\t\t id=" + id +
                "\r\n\t\t\t\t\t playlistId=" + playlistId +
                "\r\n\t\t\t\t\t order=" + order +
                "\r\n\t\t\t\t\t title='" + title +
                "\r\n\t\t\t\t\t summary='" + summary +
                "\r\n\t\t\t\t\t mainImage='" + mainImage +
                "\r\n\t\t\t\t\t mainImageSource='" + mainImageSource +
                "\r\n\t\t\t\t\t newsSourceName='" + newsSourceName +
                "\r\n\t\t\t\t\t newsSourceImage='" + newsSourceImage +
                "\r\n\t\t\t\t\t type=" + type +
                "\r\n\t\t\t\t\t exibitionTime=" + exibitionTime +
                "\r\n\t\t\t\t\t actionModel=" + actionModel +
                "\r\n\t\t\t\t\t actionModelId=" + actionModelId +
                "\r\n\t\t\t\t\t url=" + url +
                "\r\n}";
    }

}