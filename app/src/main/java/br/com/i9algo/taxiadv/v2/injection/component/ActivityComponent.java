package br.com.i9algo.taxiadv.v2.injection.component;

import android.content.Context;

import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import br.com.i9algo.taxiadv.ui.fragments.AboutFragment;
import br.com.i9algo.taxiadv.v2.injection.model.MainThreadBus;
import br.com.i9algo.taxiadv.v2.injection.model.SessionTimeManager;
import br.com.i9algo.taxiadv.v2.injection.module.AppModule;
import br.com.i9algo.taxiadv.v2.injection.module.BusModule;
import br.com.i9algo.taxiadv.v2.injection.module.ContextModule;
import br.com.i9algo.taxiadv.v2.injection.module.DatabaseModule;
import br.com.i9algo.taxiadv.v2.injection.module.LocationModule;
import br.com.i9algo.taxiadv.v2.injection.module.NetworkModule;
import br.com.i9algo.taxiadv.v2.injection.module.ServiceModule;
import br.com.i9algo.taxiadv.v2.injection.module.SessionModule;
import br.com.i9algo.taxiadv.v2.injection.provides.AppStateManager;
import br.com.i9algo.taxiadv.v2.injection.provides.LocationStateManager;
import br.com.i9algo.taxiadv.v2.injection.provides.ProgrammingState;
import br.com.i9algo.taxiadv.v2.views.BaseActivity;
import br.com.i9algo.taxiadv.v2.views.LauncherActivity;
import br.com.i9algo.taxiadv.v2.views.slideshow.AdvertSlideshowActivity;
import br.com.i9algo.taxiadv.v2.views.slideshow.SideBarAdvertFragment;
import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class, AppModule.class, DatabaseModule.class, BusModule.class, SessionModule.class, LocationModule.class, ContextModule.class})
public interface ActivityComponent {

    Context context();

    MainThreadBus bus();

    SqlBrite sqlBrite();

    AppStateManager appstateManager();

    SessionTimeManager sessionTimeManager();

    LocationStateManager locationStateManager();

    ProgrammingState programmingState();

    SchedulerComponent schedulerComponent(ServiceModule serviceModule);

    void inject(AdvertSlideshowActivity activity);

    void inject(BaseActivity activity);

    void inject(SideBarAdvertFragment navigationFragment);

    void inject(LauncherActivity activity);

    void inject(AboutFragment aboutFragment);
}

