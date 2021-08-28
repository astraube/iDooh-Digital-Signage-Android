package br.com.i9algo.taxiadv.v2.injection.module;

import javax.inject.Singleton;

import br.com.i9algo.taxiadv.domain.constants.Constants;
import br.com.i9algo.taxiadv.v2.network.taxiadv.AdvService;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    AdvService provideAdvService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.SERVER_ENDPOINT_API_V3)
                //.baseUrl(RemoteConfigs.getEndpointApi())
                .client(httpClient.build())
                .build();
        return retrofit.create(AdvService.class);
    }

}
