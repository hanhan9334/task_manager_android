package com.example.myapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities=Note.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static final String TAG = "Note Database";
    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            populateDbAsync();
            super.onCreate(db);

        }
    };

    private static void populateDbAsync() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                NoteDao noteDao = instance.noteDao();
                noteDao.insert(new Note("Title1","Description 1",1));
                noteDao.insert(new Note("Title2","Description 2",2));
                noteDao.insert(new Note("Title3","Description 3 ",3));
                Log.d(TAG, "populateDbAsync: ");
            }
        };
        new Thread(runnable).start();
        /**
         *         new Thread(() -> {
         *             try {
         *                 NoteDao noteDao = instance.noteDao();
         *                 noteDao.insert(new Note("Title1","Description 1",1));
         *                 noteDao.insert(new Note("Title2","Description 2",2));
         *                 noteDao.insert(new Note("Title3","Description 3 ",3));
         *                 Log.d(TAG, "populateDbAsync: ");
         *             } catch (Exception ignored) {
         *             }
         *         }).start();
         *
         * */

    }

}
