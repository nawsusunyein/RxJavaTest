package com.example.rxjavaandroidtest;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

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

import java.util.ArrayList;
import java.util.List;

public class CustomizeDataTypeTest extends AppCompatActivity {


    private static final String TAG = CustomizeDataTypeTest.class.getSimpleName();
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_data_type_test);
        Observable<Note> notesObservable = getNotesObservable();
        DisposableObserver<Note> notesObserver = getNotesObserver();
        compositeDisposable.add(
           notesObservable
           .observeOn(Schedulers.io())
           .subscribeOn(AndroidSchedulers.mainThread())
           .map(new Function<Note,Note>(){
               @Override
               public Note apply(Note note) throws Exception{
                    note.setNote(note.getNote().toUpperCase());
                    return note;
               }
           })
           .subscribeWith(notesObserver)
        );


    }

    private DisposableObserver<Note> getNotesObserver(){
      return new DisposableObserver<Note>() {
          @Override
          public void onNext(Note note) {
              Log.d(TAG,"Note name : " + note.getNote());
          }

          @Override
          public void onError(Throwable e) {
            Log.d(TAG,"Error : " + e.getMessage());
          }

          @Override
          public void onComplete() {
            Log.d(TAG , "All items are emitted");
          }
      };
    }

    private Observable<Note> getNotesObservable(){
        final List<Note> notes = prepareNotesData();
        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
                for (Note note : notes) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(note);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

    private List<Note> prepareNotesData(){
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1,"To study CN"));
        notes.add(new Note(2,"To call "));
        notes.add(new Note(3,"To buy foods"));
        notes.add(new Note(4,"To extract notes"));
        notes.add(new Note(5,"To make cake"));
        return notes;
    }

    class Note{
        int id;
        String note;

        public Note(int id,String note){
            this.id = id;
            this.note = note;
        }

        public int getId(){
            return id;
        }

        public String getNote(){
            return note;
        }

        public void setId(int id){
            this.id = id;
        }

        public void setNote(String note){
            this.note = note;
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
