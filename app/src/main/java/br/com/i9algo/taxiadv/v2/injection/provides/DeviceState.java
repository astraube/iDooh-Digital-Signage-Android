package br.com.i9algo.taxiadv.v2.injection.provides;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.i9algo.taxiadv.v2.helpers.defaults.DefaultSubscriber;
import br.com.i9algo.taxiadv.v2.models.DeviceProfile;
import rx.Observable;
import rx.subjects.PublishSubject;

@Singleton
public class DeviceState {

    private DeviceProfile deviceProfile;

    private PublishSubject<DeviceInformation> profileSubject = PublishSubject.create();
    private Observable<DeviceInformation> profileObservable = profileSubject.cache(1);

    @Inject
    public DeviceState() {
        profileObservable.subscribe(new DefaultSubscriber<DeviceInformation>());
    }

    public void initialize(DeviceProfile profile) {
        deviceProfile = profile;
        DeviceInformation info = getProfileInformation();
        profileSubject.onNext(info);
    }

    private DeviceInformation getProfileInformation() {
        DeviceInformation pi = new DeviceInformation();
        pi.var1 = deviceProfile.getVar1();
        pi.var2 = deviceProfile.getVar2();
        pi.var3 = deviceProfile.getVar3();
        return pi;
    }

    public Observable<DeviceInformation> getProfileInformationObservable() {
        return profileObservable;
    }

    public static class DeviceInformation {
        public String var1 = "";
        public String var2 = "";
        public String var3 = "";

    }
}
