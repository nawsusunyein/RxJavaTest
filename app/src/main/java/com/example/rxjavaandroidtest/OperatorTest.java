package com.example.rxjavaandroidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class OperatorTest extends AppCompatActivity {

    private static final String TAG = OperatorTest.class.getSimpleName();
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_test);
        Observable<String> personObservable = getPersonNamesObservable();
        Observer<String> personObservers = getPersonNamesObserver();

        personObservable
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("b");
                    }
                })
                .subscribeWith(personObservers);

    }

    private Observer<String> getPersonNamesObserver(){
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
                Log.d(TAG,"All items are transmitted");
            }
        };
    }


    private Observable<String> getPersonNamesObservable(){
        return Observable.fromArray("bbm","abcde","ccccmm","ddllmk","abc","benk","mmrd","vndt","bbma","imba","beekom");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        disposable.dispose();
    }
}
