package br.com.i9algo.taxiadv.v2.models.inbound.sidebar;

import java.util.List;

public class SidebarItemList {

   private Data data;

    public SidebarItemList(List<SidebarItem> items) {
        this.data = new Data(items);
    }
    public SidebarItemList() {
    }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public class Data{

        private List<SidebarItem> sidebars;

        public Data(List<SidebarItem> items) {
            this.sidebars = items;
        }

        public List<SidebarItem> getSidebars() {
            return sidebars;
        }

        public void setSidebars(List<SidebarItem> sidebars) {
            this.sidebars = sidebars;
        }
    }
}
