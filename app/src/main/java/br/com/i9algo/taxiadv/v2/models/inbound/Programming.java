package br.com.i9algo.taxiadv.v2.models.inbound;

import java.util.List;

import br.com.i9algo.taxiadv.v2.models.slideshow.SlideshowPlaylist;

public class Programming {

    private Data data;

    public Programming(Data data) {
        this.data = data;
    }

    public Programming(List<SlideshowPlaylist> playlist_array) {
        this.data = new Data(playlist_array);
    }

    public Programming() {
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setData(List<SlideshowPlaylist> playlist_array) {
        this.data = new Data(playlist_array);
    }

    public int getNumberOfPlaylists() {
        if (data != null && data.getPlaylist_array() != null){
            return getData().getPlaylist_array().size();
        }else{
            return -1;
        }
    }

    public int getTotalNumberOfSlidesOnAllPlaylists() {
        if (data != null && data.getPlaylist_array() != null){
            int total = 0;
            for (SlideshowPlaylist playlist :  data.getPlaylist_array()){
                total = total + playlist.getItems().size();
            }
            return total;
        }else{
            return -1;
        }
    }

    public class Data {
        private List<SlideshowPlaylist> playlists;

        public List<SlideshowPlaylist> getPlaylist_array() {
            return playlists;
        }

        public void setPlaylist_array(List<SlideshowPlaylist> playlist_array) {
            this.playlists = playlist_array;
        }

        public Data(List<SlideshowPlaylist> playlist_array) {
            this.playlists = playlist_array;
        }
    }
}
