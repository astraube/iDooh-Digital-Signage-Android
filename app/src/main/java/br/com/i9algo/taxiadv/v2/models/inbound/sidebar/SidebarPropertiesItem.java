package br.com.i9algo.taxiadv.v2.models.inbound.sidebar;

public class SidebarPropertiesItem {
    
    private String content;
    private String icon;
    private int layoutpropertiesid;

    public SidebarPropertiesItem() {
    }

    public SidebarPropertiesItem(String _content, String _icon, int _layoutpropertiesid) {
        this.content = _content;
        this.icon = _icon;
        this.layoutpropertiesid = _layoutpropertiesid;
    }

    public String getContent() { return content; }
    public void setContent(String _content) { this.content = _content; }

    public String getIcon() { return icon; }
    public void setIcon(String _icon) { this.icon = _icon; }
}
