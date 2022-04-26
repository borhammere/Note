package com.android.note.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.note.App;
import com.android.note.R;
import com.android.note.domain.NoteEntity;
import com.android.note.domain.NoteRepo;

public class NoteDetailFragment extends Fragment {

    private static final String NOTE_ENTITY_KEY = "sdasv";

    private Button saveButton = null;
    private Button cancelButton = null;
    private TextView idTv = null;
    private EditText headingTitleEt = null;
    private EditText contentEt = null;

    private int noteId;
    private int noteColor;

    private NoteEntity noteEntity;


    static NoteDetailFragment newInstance(NoteEntity noteEntity) {
        Bundle args = new Bundle();
        args.putParcelable(NOTE_ENTITY_KEY, noteEntity);

        NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setNoteEntity(NoteEntity noteEntity) {
        idTv.setText(String.valueOf(noteEntity.getId()));
        headingTitleEt.setText(noteEntity.getTitle());
        contentEt.setText(noteEntity.getContent());
        noteId = noteEntity.getId();
        noteColor = noteEntity.getColor();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setListeners();

        noteEntity = getArguments().getParcelable(NOTE_ENTITY_KEY);
        setNoteEntity(noteEntity);
    }


    private void initViews(@NonNull View view) {
        saveButton = view.findViewById(R.id.save_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        idTv = view.findViewById(R.id.id_text_view);
        headingTitleEt = view.findViewById(R.id.heading_title_edit_text);
        contentEt = view.findViewById(R.id.content_edit_text);
    }

    private void setListeners() {
        @SuppressLint("NonConstantResourceId") final View.OnClickListener OnClickListener = view -> {

            //проверяем нажатие кнопки и устанвливаем цвет экрана
            switch (view.getId()) {
                case R.id.save_button:
                    String changedTitle = headingTitleEt.getText().toString();//забрали изменение
                    String changedContent = contentEt.getText().toString();//забрали изменение

                    //собрали новую заметку
                    NoteEntity changedNote = new NoteEntity(
                            noteId,
                            changedTitle,
                            changedContent,
                            noteColor);//получаем новые измененные данные. new NoteEntity

                    NoteRepo noteRepo = getApp().getNoteRepo();//достали репозеторий

                    noteRepo.update(changedNote);//добавляем новые данные

                    getController().onDataChanged();
                    break;
                case R.id.cancel_button:
                    break;
                default:
            }
            getController().finishNoteDetailFragment();
        };

        saveButton.setOnClickListener(OnClickListener);
        cancelButton.setOnClickListener(OnClickListener);
    }

    private Controller getController() {
        return (Controller) getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getController();
    }

    private App getApp() {
        return (App) getContext().getApplicationContext();
    }

    interface Controller {
        void onDataChanged();

        void finishNoteDetailFragment();
    }

}
