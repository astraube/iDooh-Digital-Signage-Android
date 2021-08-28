package br.com.i9algo.taxiadv.v2.network.taxiadv;

import br.com.i9algo.taxiadv.v2.models.inbound.CategoriesArraylistWrapper;
import br.com.i9algo.taxiadv.v2.models.inbound.Programming;
import br.com.i9algo.taxiadv.v2.models.inbound.SessionResponse;
import br.com.i9algo.taxiadv.v2.models.inbound.SlideshowFillerListWrapper;
import br.com.i9algo.taxiadv.v2.models.inbound.geo.GeofencedAdvertArraylistWrapper;
import br.com.i9algo.taxiadv.v2.models.inbound.sidebar.SidebarItemList;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface AdvService {

    @GET("media/{id}/playlist3/")
    Observable<Programming> getProgramming(@Path("id") String token);

    @GET("media/{id}/playlist3/group/{netGroup}")
    Observable<Programming> getProgramming(@Path("id") String token, @Path("netGroup") String netGroup);

    // DEMO
    @GET("media/{id}/playlist3/demo/{demoId}")
    Observable<Programming> getProgramming(@Path("id") String token, @Path("demoId") int demoId);

    @GET("media/{id}/news/")
    Observable<SlideshowFillerListWrapper> getNews(@Path("id") String token);

    @GET("media/{id}/sidebar2")
    Observable<SidebarItemList> getSidebars(@Path("id") String token);

    @GET("media/{id}/sidebar2/group/{netGroup}")
    Observable<SidebarItemList> getSidebars(@Path("id") String token, @Path("netGroup") String netGroup);

    // DEMO
    @GET("media/{id}/sidebar2/demo/{demoId}")
    Observable<SidebarItemList> getSidebars(@Path("id") String token, @Path("demoId") int demoId);

    @GET("media/{id}/category/")
    Observable<CategoriesArraylistWrapper> getCategories(@Path("id") String token);

    @GET("media/{id}/category/group/{netGroup}")
    Observable<CategoriesArraylistWrapper> getCategories(@Path("id") String token, @Path("netGroup") String netGroup);

    // DEMO
    @GET("media/{id}/category/demo/{demoId}")
    Observable<CategoriesArraylistWrapper> getCategories(@Path("id") String token, @Path("demoId") int demoId);

    @GET("media/{id}/geofence/")
    Observable<GeofencedAdvertArraylistWrapper> getGeofencedAdverts(@Path("id") String token, @Path("lat") String lat, @Path("lng") String lng);

    @POST("media/{id}/share/")
    Observable<Object> postShareSideBarAdvert();

    @FormUrlEncoded
    @POST("device/{devSerial}/associate-car")
    Observable<Object> sendCarPlate(@Path("devSerial") String devSerial, @Field("carBoard") String carBoard);
}
