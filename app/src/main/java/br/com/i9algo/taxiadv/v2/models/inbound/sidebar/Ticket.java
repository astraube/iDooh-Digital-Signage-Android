package br.com.i9algo.taxiadv.v2.models.inbound.sidebar;

import java.io.Serializable;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ticket implements Serializable, Parcelable
{

    private final static long serialVersionUID = 7633730933393803940L;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("symbol")
    @Expose
    private String symbol;

    public final static Parcelable.Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @SuppressWarnings({
                "unchecked"
        })
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        public Ticket[] newArray(int size) {
            return (new Ticket[size]);
        }
    };

    protected Ticket(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.price = ((Double) in.readValue((Double.class.getClassLoader())));
        this.currency = ((String) in.readValue((String.class.getClassLoader())));
        this.symbol = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Ticket() { }


    public Ticket(Cursor cursor) {
        //this.type = Db.getString(cursor, GEOJSON_GEOMETRY_TYPE, "");

    }

    /**
     *
     * @param title
     * @param price
     * @param symbol
     * @param description
     * @param currency
     */
    public Ticket(String title, String description, Double price, String currency, String symbol) {
        super();
        this.title = title;
        this.description = description;
        this.price = price;
        this.currency = currency;
        this.symbol = symbol;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(description);
        dest.writeValue(price);
        dest.writeValue(currency);
        dest.writeValue(symbol);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return new com.google.gson.Gson().toJson(this);
    }
}