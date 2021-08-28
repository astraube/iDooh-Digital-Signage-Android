package br.com.i9algo.taxiadv.v2.models.inbound;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowItem;

public class SlideshowFillerListWrapper {

    public static final String TABLE = "fillers";


    public int getNumberOfFillers(){
        if (data != null && data.getFillerList() != null){
            return getData().getFillerList().size();
        }else{
            return -1;
        }
    }

    public SlideshowFillerListWrapper() { }
    public SlideshowFillerListWrapper(Data data) {
        this.data = data;
    }

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("items")
        private List<SlideshowItem> fillerList;

        public List<SlideshowItem> getFillerList() {
            return fillerList;
        }

        public void setFillerList(List<SlideshowItem> fillerList) {
            this.fillerList = fillerList;
        }

        public Data(List<SlideshowItem> fillerList) {
            this.fillerList = fillerList;
        }

        public Data() {
        }
    }
}
