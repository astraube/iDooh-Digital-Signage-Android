package br.com.i9algo.taxiadv.v2.injection.module;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import javax.inject.Singleton;

import androidx.annotation.Nullable;
import br.com.i9algo.taxiadv.v2.CustomApplication;
import br.com.i9algo.taxiadv.v2.injection.model.ObjectManager;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final CustomApplication application;

    private Activity currentActivity;

    public AppModule(CustomApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    CustomApplication provideApplication() {
        return application;
    }

    @Provides
    @Nullable
    Activity provideCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity a) {
        currentActivity = a;
    }

    @Singleton @Provides
    public SharedPreferences provideSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Singleton @Provides
    public Gson provideGson(){
        return new Gson();
    }

    @Singleton @Provides
    public ObjectManager provideObjectManager(SharedPreferences sharedPreferences, Gson gson){
        return new ObjectManager(sharedPreferences, gson);
    }
}
