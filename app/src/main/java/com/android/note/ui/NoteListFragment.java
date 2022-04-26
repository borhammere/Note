package com.android.note.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.note.App;
import com.android.note.R;
import com.android.note.data.NoteRepoImpl;
import com.android.note.domain.NoteEntity;
import com.android.note.domain.NoteRepo;

public class NoteListFragment extends Fragment {
    private NoteRepo noteRepo;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        noteRepo = getApp().getNoteRepo();//вариант написания 2 (более читаемый код)
        adapter.setData(noteRepo.getNotes());
    }

    private void initViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.recycler_view);

        //чтобы соединить noteRepo и recyclerView (передать данные во view)
        //передаем способ как компановать экран
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//можно передать ориентацию и т.д.
        //обязательно нужно установить адаптер (это такая сущность котороя превращает noteEntity в
        adapter = new NoteAdapter(NoteRepoImpl.data, listener);
        recyclerView.setAdapter(adapter);
    }

    private final NoteAdapter.InteractionListener listener = new NoteAdapter.InteractionListener() {
        @Override
        public void onItemClickListener(NoteEntity noteEntity) {
            String sb = "onItemClickListener -"
                    + noteEntity.getTitle()
                    + noteEntity.getContent();
            Toast.makeText(getContext(), sb, Toast.LENGTH_SHORT).show();

            showNoteScreen(noteEntity);
        }
    };

    private void showNoteScreen(NoteEntity noteEntity) {
        getController().showNoteScreen(noteEntity);
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

    public void onDataChanged() {
        adapter.setData(noteRepo.getNotes());
    }

    interface Controller {
        void showNoteScreen(NoteEntity noteEntity);
    }
}
