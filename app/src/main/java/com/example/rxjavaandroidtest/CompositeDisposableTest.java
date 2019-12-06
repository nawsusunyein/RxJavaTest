package com.example.rxjavaandroidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CompositeDisposableTest extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String TAG = CompositeDisposableTest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite_disposable_test);
        Observable<String> animalObservables = getAnimalsNameObservalbe();
        DisposableObserver<String> animalObserversWithB = getAnimalsNameObserver();
        DisposableObserver<String> animalObserversWithC = getAnimalsNameObserver();
        compositeDisposable.add(
                animalObservables
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return s.toLowerCase().startsWith("b");
                            }
                        })
                .subscribeWith(animalObserversWithB)
        );
        
        compositeDisposable.add(
          animalObservables
          .observeOn(Schedulers.io())
          .subscribeOn(AndroidSchedulers.mainThread())
          .filter(new Predicate<String>() {
              @Override
              public boolean test(String s) throws Exception {
                  return s.toLowerCase().startsWith("c");
              }
          })
          .map(new Function<String,String>(){
            public String apply(String s) throws Exception {
                return s.toUpperCase();
            }
        })
                  .subscribeWith(animalObserversWithC)
        );
    }


    private DisposableObserver<String> getAnimalsNameObserver(){
        return new DisposableObserver<String>(){

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

    private Observable<String> getAnimalsNameObservalbe(){
        return Observable.just("Cat","Dog","Bird","Snake","Tiger","Lion");
    }
}
