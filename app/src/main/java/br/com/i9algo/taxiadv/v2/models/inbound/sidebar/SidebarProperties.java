package br.com.i9algo.taxiadv.v2.models.inbound.sidebar;

import java.util.ArrayList;
import java.util.List;

public class SidebarProperties {

    public static final String TITLE = "title";

    private String title;
    private List<SidebarPropertiesItem> properties = new ArrayList<>();


    public SidebarProperties(String _title, List<SidebarPropertiesItem> _properties) {
        this.title = _title;
        this.properties = _properties;
    }
    public SidebarProperties() { }
    public SidebarProperties(String _title) { this.title = _title; }

    public String getTitle() { return title; }
    public void setTitle(String _title) { this.title = _title; }

    public List<SidebarPropertiesItem> getProperties() { return properties; }
    public void setProperties(List<SidebarPropertiesItem> _properties) { this.properties = _properties; }
}
