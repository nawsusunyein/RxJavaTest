package com.example.rxjavaandroidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DisposableTest extends AppCompatActivity {

    private static final String TAG = DisposableTest.class.getSimpleName();
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disposable_test);
        Observable<String> animalsObservable = getAnimalsObservable();
        Observer<String> animalObserver = getAnimalsObserver();

        animalsObservable
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(animalObserver);

    }

    private Observer<String> getAnimalsObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d(TAG,"on subscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"Name : " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"Error : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"All items are emitted");
            }
        };
    }



    private Observable<String> getAnimalsObservable(){
        return Observable.just("Tiger","Lion","Bird","Cat","Dog");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        disposable.dispose();
    }
}
