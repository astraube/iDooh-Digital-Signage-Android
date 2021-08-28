package br.com.i9algo.taxiadv.v2.helpers.defaults;

import rx.Subscriber;

public class DefaultSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
        // no-op by default
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        // no-op by default
    }
}
