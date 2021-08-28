package br.com.i9algo.taxiadv.v2.models.outbound;

public class ReserveActionModel {

    private String name;
    private String phone;
    private String date;

    @Override
    public String toString() {
        return "ReserveActionModel{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public ReserveActionModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ReserveActionModel(String name, String phone, String date) {
        this.name = name;
        this.phone = phone;
        this.date = date;
    }
}
