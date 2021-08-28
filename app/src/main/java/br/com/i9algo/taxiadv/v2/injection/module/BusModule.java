package br.com.i9algo.taxiadv.v2.injection.module;


import javax.inject.Singleton;

import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
import dagger.Module;
import dagger.Provides;

@Module
public class BusModule {

    @Singleton
    @Provides
    MainThreadBus providesBus() {
        return new MainThreadBus();
    }
}
