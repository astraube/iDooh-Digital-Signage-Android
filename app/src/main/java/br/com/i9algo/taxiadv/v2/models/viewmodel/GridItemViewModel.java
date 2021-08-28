package br.com.i9algo.taxiadv.v2.models.viewmodel;

import android.database.Cursor;
import android.text.TextUtils;

import java.util.Date;

import br.com.i9algo.taxiadv.v2.helpers.Db;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItem;
import br.com.i9algo.taxiadv.v2.utils.DateUtils;

public class GridItemViewModel {

    private int position;
    private int sidebarId;
    private String coverImage;
    private String itemName;
    private String url;
    private String type;
    private String startAt;

    public static GridItemViewModel fromCursor(Cursor cursor) {
        int id = Db.getInt(cursor, SidebarItem.ID, -1);
        int posi = id;

        String cover_image = Db.getString(cursor, SidebarItem.COVER_IMAGE, "");
        String url = Db.getString(cursor, SidebarItem.URL, "");
        String itemName = Db.getString(cursor, SidebarItem.TITLE, "");
        String type = Db.getString(cursor, SidebarItem.TYPE, "");
        String startAt = Db.getString(cursor, SidebarItem.DATE_START_EVENT, "");

        return new GridItemViewModel(posi, id, cover_image, itemName, url, type, startAt);
    }

    public GridItemViewModel() { }

    public GridItemViewModel(int position, int sidebarId, String coverImage, String itemName, String url, String type, String startAt) {
        this.position = position;
        this.sidebarId = sidebarId;
        this.coverImage = coverImage;
        this.itemName = itemName;
        this.url = url;
        this.type = type;
        this.startAt = startAt;
    }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public int getSidebarId() { return sidebarId; }
    public void setSidebarId(int sidebarId) { this.sidebarId = sidebarId; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getURL() { return url; }
    public void setURL(String url) { this.url = url; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDateStartEvent() { return startAt; }
    public void setDateStartEvent(String eventAt) { this.startAt = startAt; }
    public Date getDateStartEventAsDate(){
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            if (TextUtils.isEmpty(startAt))
                return null;

            return DateUtils.parseISO8601Date(startAt);
            //return formatter.parse(startAt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
