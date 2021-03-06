package com.project.comember.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.project.comember.R;
import com.project.comember.game.GameColor;
import com.project.comember.game.GameEngine;
import com.project.comember.game.GameMode;
import com.project.comember.game.GameStatistics;
import com.project.comember.misc.ClickableGroup;
import com.project.comember.misc.HighlightButtonRunnable;
import com.project.comember.ui.widgets.GameButton;
import com.project.comember.ui.widgets.GameLayout;
import com.project.comember.ui.widgets.GameScoreCounter;
import com.project.comember.util.FutureCallback;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameFragment extends Fragment {

    private GameEngine gameEngine;

    private GameLayout gameLayout;
    private GameButton[] gameButtons;
    private GameScoreCounter gameScoreCounter;

    private ExecutorService executor;

    protected boolean gameLost = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        gameEngine = getGameEngine(this);

        ClickableGroup clickToStartGroup = view.findViewById(R.id.click_to_start_group);
        clickToStartGroup.setOnClickListener(clickedView -> {
            clickToStartGroup.setVisibility(View.INVISIBLE);
            gameEngine.start();
        });

        gameLayout = view.findViewById(R.id.game_button_layout);
        gameButtons = gameLayout.getGameButtons();
        gameScoreCounter = view.findViewById(R.id.game_score_counter);

        initializeGameButtons();
    }

    protected GameEngine getGameEngine(GameFragment gameController) {
        return new GameEngine(gameController);
    }

    private void initializeGameButtons() {
        for (int index = 0; index < 4; index++) {
            final GameColor buttonColor = GameColor.valueOf(index);

            gameButtons[index].setGameColor(buttonColor);
            gameButtons[index].setOnClickListener(view -> gameEngine.checkColorClicked(buttonColor));
        }
    }

    public void highlightColorSequence(List<GameColor> gameColorSequence, int highlightMillis, int highlightPauseMillis) {
        //This check is needed for games canceled via back button
        if (gameLost)
            return;

        Future<?> waitForExecution = null;
        executor = Executors.newSingleThreadExecutor();
        for (GameColor gameColor : gameColorSequence) {
            waitForExecution = executor.submit(new HighlightButtonRunnable(
                    gameButtons[gameColor.getValue()],
                    highlightMillis,
                    highlightPauseMillis
            ));
        }
        new FutureCallback(waitForExecution) {
            @Override
            public void futureFinished() {
                gameEngine.highlightingFinished();
            }
        };
    }

    public void unhighlightAll() {
        for (GameButton gb : gameButtons) {
            gb.unhighlight();
        }
    }

    public void setClickable(boolean clickable) {
        gameLayout.setTouchable(clickable);
    }

    public void setGameTimer(int percentRemaining) {
        gameScoreCounter.setProgress(100 - percentRemaining);
    }

    public void incrementGameScore() {
        gameScoreCounter.increment();
    }

    public void gameLost(int gameScore) {
        if (gameLost) return;
        gameLost = true;

        GameStatistics.setNewScore(getContext(), GameMode.CLASSIC, gameScore);

        GameFragmentDirections.GameFragmentToGameOverFragment action = GameFragmentDirections.gameFragmentToGameOverFragment();
        action.setGameScore(gameScore);
        action.setPlayAgainActionId(R.id.gameOverFragment_to_gameFragment);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (executor != null)
            executor.shutdownNow();
        gameLost = true;
    }
}