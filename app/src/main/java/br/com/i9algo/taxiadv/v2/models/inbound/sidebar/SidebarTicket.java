package br.com.i9algo.taxiadv.v2.models.inbound.sidebar;

public class SidebarTicket {

    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String CURRENCY = "currency";
    public static final String SYMBOL = "symbol";

    private String title;
    private String description;
    private String price;
    private String currency;
    private String symbol;


    public SidebarTicket(String _title, String _description, String _price, String _currency, String _symbol) {
        this.title = _title;
        this.description = _description;
        this.price = _price;
        this.currency = _currency;
        this.symbol = _symbol;
    }
    public SidebarTicket(String _title, String _price) {
        this.title = _title;
        this.price = _price;
    }
    public SidebarTicket() { }

    public String getTitle() { return title; }
    public void setTitle(String _title) { this.title = _title; }

    public String getDescription() { return description; }
    public void setDescription(String _description) { this.description = _description; }

    public String getPrice() { return price; }
    public void setPrice(String _price) { this.price = _price; }

    public String getCurrency() { return currency; }
    public void setCurrency(String _currency) { this.currency = _currency; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String _symbol) { this.symbol = _symbol; }
}
