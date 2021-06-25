package com.project.comember.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.comember.R;
import com.project.comember.game.GameMode;
import com.project.comember.misc.GameModeAdapter;

import java.util.ArrayList;
import java.util.List;

public class GameModeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_mode_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GameModeAdapter adapter = new GameModeAdapter(getGameModeList(), getContext());

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);


    }

    private List<GameMode> getGameModeList() {
        List<GameMode> gameModeList = new ArrayList<>();
        gameModeList.add(new GameMode(R.drawable.ic_game_button, "Classic", "Repeat the highlighted sequence of colors.", 15, 5, 3, R.id.gameModeFragment_to_gameFragment));
        gameModeList.add(new GameMode(R.drawable.ic_game_button_spinning, "Spinning", "The playing field starts spinning.", 18, 5, 2, R.id.gameModeFragment_to_spinningGameFragment));
        gameModeList.add(new GameMode(R.drawable.ic_game_button_no_repeat, "No Repeat", "The whole sequence is never repeated, only the new color will be shown.", 16, 3, 1, R.id.gameModeFragment_to_noRepeatGameFragment));

        return gameModeList;
    }
}