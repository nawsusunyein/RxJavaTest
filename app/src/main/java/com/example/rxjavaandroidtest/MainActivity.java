package com.example.rxjavaandroidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    Button btnDisposable;
    Button btnOperator;
    Button btnCompositeDisposable;
    Button btnCustomizeDataType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDisposable = (Button) findViewById(R.id.btnDisposable);
        btnOperator = (Button) findViewById(R.id.btnOperator);
        btnCompositeDisposable = (Button) findViewById(R.id.btnCompositeDisposible);
        btnCustomizeDataType = (Button) findViewById(R.id.btnCustomizeType);

        Observable<String> animalObservable = getAnimalsObservable();
        Observer<String> animalObserver = getAnimalsObserver();

        animalObservable
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(animalObserver);

        btnDisposable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent disposableIntent = new Intent(MainActivity.this,DisposableTest.class);
                startActivity(disposableIntent);
            }
        });

        btnOperator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent operatorIntent = new Intent(MainActivity.this,OperatorTest.class);
                startActivity(operatorIntent);
            }
        });

        btnCompositeDisposable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compositeDisposableIntent = new Intent(MainActivity.this,CompositeDisposableTest.class);
                startActivity(compositeDisposableIntent);
            }
        });

        btnCustomizeDataType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customizeDataTypeIntent = new Intent(MainActivity.this,CustomizeDataTypeTest.class);
                startActivity(customizeDataTypeIntent);
            }
        });
    }

    private Observer<String> getAnimalsObserver(){
        return new Observer<String>(){

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG,"On Subscribe");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG,"Name : " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"Error : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"All items are emitted");
            }
        };
    }

    private Observable<String> getAnimalsObservable(){
        return Observable.just("monkey","cat","dog","snake","tiger","lion");
    }
}
