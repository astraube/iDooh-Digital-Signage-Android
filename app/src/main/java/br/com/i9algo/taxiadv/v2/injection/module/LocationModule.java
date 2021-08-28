package br.com.i9algo.taxiadv.v2.injection.module;


import android.content.Context;

import javax.inject.Singleton;

import br.com.i9algo.taxiadv.v2.injection.provides.LocationStateManager;
import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule {

    Context context;

    public LocationModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    LocationStateManager providesLocationStateManager() {
        return new LocationStateManager(context);
    }

}
