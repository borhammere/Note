package com.android.note.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.note.R;
import com.android.note.domain.NoteEntity;

public class RootActivity extends AppCompatActivity implements NoteListFragment.Controller, NoteDetailFragment.Controller {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        Fragment noteListFragment = new NoteListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container_layout, noteListFragment, "list")
                .commit();
    }

    @Override
    public void showNoteScreen(NoteEntity noteEntity) {
        Fragment noteDetailFragment = NoteDetailFragment.newInstance(noteEntity);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.slave_container_layout, noteDetailFragment, "details") // todo константы бы
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDataChanged() {
        NoteListFragment fragment = (NoteListFragment) getSupportFragmentManager().findFragmentByTag("list");
        fragment.onDataChanged();
    }

    @Override
    public void finishNoteDetailFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("details");
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }
}