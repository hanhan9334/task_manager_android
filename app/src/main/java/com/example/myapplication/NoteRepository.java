package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public NoteRepository(Application application){
        NoteDatabase database = NoteDatabase.getInstance(application);

        noteDao = database.noteDao();
        allNotes = noteDao.getAllNote();
    }

    public void insert(Note note){
        insertAsync(note);
    }

    public void update(Note note){
        updateAsync(note);
    }

    public void delete(Note note){
        deleteAsync(note);
    }

    public void deleteAllNotes(){
        deleteAllAsync();
    }

    public LiveData<Note> getNoteByTitle(String title){
        return getNoteByTitleAsync(title);
    }

    private void insertAsync(final Note note) {

        new Thread(() -> {
            try {
                noteDao.insert(note);
            } catch (Exception ignored) {
            }
        }).start();
    }
    private void updateAsync(final Note note) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    noteDao.update(note);
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    private void deleteAsync(final Note note) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    noteDao.delete(note);
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    private void deleteAllAsync() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    noteDao.deleteAll();
                } catch (Exception ignored) {
                }
            }
        }).start();
    }

    private LiveData<Note> getNoteByTitleAsync(String title) {

        final LiveData<Note>[] resultNote = new LiveData[]{new MutableLiveData<Note>()};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    resultNote[0] =noteDao.getNoteByTitle(title);
                } catch (Exception ignored) {
                }
            }
        }).start();
        return resultNote[0];
    }

}
