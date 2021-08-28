package br.com.i9algo.taxiadv.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import br.com.i9algo.taxiadv.domain.enums.AdvType;
import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;

public class PlaylistItem implements Parcelable {

	private SlideshowItem slideshowItem;
	
	private String mToken;
	private String mName;
	private String mDescription;
	private String mOrderItem;
	private String mFileName;
	private String mFileUrl;
	private AdvType mType;
	private String mDateInsert;
	private int mLoaded;
	
	public PlaylistItem () { super(); }
	

	public String getToken() { return mToken; }
	public void setToken(String value) { this.mToken = value; }

	public String getName() { return mName; }
	public void setName(String value) { this.mName = value; }

	public String getDescription() { return mDescription; }
	public void setDescription(String value) { this.mDescription = value; }

	public String getOrder() { return mOrderItem; }
	public void setOrder(String value) { this.mOrderItem = value; }

	// TODO Verificar necessidade do nome do arquivo
	public String getFileName() { return mFileName; }
	public void setFileName(String value) { this.mFileName = value; }
	
	public String getFileUrl() { return mFileUrl; }
	public void setFileUrl(String value) { this.mFileUrl = value; }

	public AdvType getType() { return mType; }
	public void setType(String value) { this.mType = AdvType.valueOf(value); }
	public void setType(AdvType value) { this.mType = value; }

	public String getDateInsert() { return mDateInsert; }
	public void setDateInsert(String value) { this.mDateInsert = value; }
	
	public int getLoaded() { return mLoaded; }
	public void setLoaded(int value) { this.mLoaded = value; }


	public SlideshowItem getSlideshowItem() {
		return slideshowItem;
	}

	public void setSlideshowItem(SlideshowItem slideshowItem) {
		this.slideshowItem = slideshowItem;
	}

	// PARCELABLE
    public PlaylistItem(Parcel parcel) {
    	setToken(parcel.readString());
    	setName(parcel.readString());
    	setDescription(parcel.readString());
    	setOrder(parcel.readString());
    	setFileName(parcel.readString());
    	setFileUrl(parcel.readString());
    	setType(parcel.readString());
    	setDateInsert(parcel.readString());
    	setLoaded(parcel.readInt());
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getToken());
        dest.writeString(getName());
        dest.writeString(getDescription());
        dest.writeString(getOrder());
        dest.writeString(getFileName());
        dest.writeString(getFileUrl());
        dest.writeString(getType().toString());
        dest.writeString(getDateInsert());
        dest.writeInt(getLoaded());
    }
    public static final Creator<PlaylistItem> CREATOR = new Creator<PlaylistItem>(){
        @Override
        public PlaylistItem createFromParcel(Parcel source) {
            return new PlaylistItem(source);
        }
        @Override
        public PlaylistItem[] newArray(int size) {
            return new PlaylistItem[size];
        }
    };
}
