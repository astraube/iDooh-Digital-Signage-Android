package br.com.i9algo.taxiadv.v2.models.inbound;

import com.google.gson.annotations.SerializedName;

public class SessionResponse {

    @SerializedName("data")
    private String token;

    public SessionResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
