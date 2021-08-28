package br.com.i9algo.taxiadv.v2.injection.module;


import javax.inject.Singleton;

import br.com.i9algo.taxiadv.v2.injection.provides.AppStateManager;
import br.com.i9algo.taxiadv.v2.injection.provides.ProgrammingState;
import dagger.Module;
import dagger.Provides;

@Module
public class StateModule {

    AppStateManager mStateManager;

    ProgrammingState mProgrammingState;

    public StateModule(AppStateManager stateManager, ProgrammingState programmingState) {
        mProgrammingState = programmingState;
        mStateManager = stateManager;
    }

    @Singleton
    @Provides
    AppStateManager providesStateManager() {
        return mStateManager;
    }

    @Singleton
    @Provides
    ProgrammingState providesProgrammingState() {
        return mProgrammingState;
    }

}
