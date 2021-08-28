package br.com.i9algo.taxiadv.v2.injection.module;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import br.com.i9algo.taxiadv.libs.utilcode.util.PhoneUtils;
import br.com.i9algo.taxiadv.v2.injection.model.SessionTimeManager;
import br.com.i9algo.taxiadv.v2.injection.provides.Session;
import dagger.Module;
import dagger.Provides;

@Module
public class SessionModule {
    private Context context;

    public SessionModule(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    @Singleton
    @Provides
    public Session provideSession(){
        return new Session(PhoneUtils.getSerial());
    }

    @Singleton @Provides
    public SessionTimeManager provideSessionTimeManager(Session session){
        return new SessionTimeManager(session);
    }
}
