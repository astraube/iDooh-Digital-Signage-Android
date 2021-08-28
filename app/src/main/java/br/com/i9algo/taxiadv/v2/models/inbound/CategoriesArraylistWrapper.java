package br.com.i9algo.taxiadv.v2.models.inbound;

import java.util.ArrayList;
import java.util.List;

public class CategoriesArraylistWrapper {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public CategoriesArraylistWrapper() {
    }

    public CategoriesArraylistWrapper(List<Category> categories) {
        this.data = new Data(categories);
    }

    public class Data{
        private List<Category> categories = new ArrayList<>();

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public Data(List<Category> categories) {
            this.categories = categories;
        }
    }
}
