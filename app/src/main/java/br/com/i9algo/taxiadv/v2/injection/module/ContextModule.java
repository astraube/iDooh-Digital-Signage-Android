package br.com.i9algo.taxiadv.v2.injection.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context providesContext() {
        return this.context;
    }
}
