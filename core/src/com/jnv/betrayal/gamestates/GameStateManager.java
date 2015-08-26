/*
 * Copyright (c) 2015. JNV Games, All rights reserved.
 */

package com.jnv.betrayal.gamestates;

import com.badlogic.gdx.Gdx;
import com.jnv.betrayal.gamestates.CharacterSelection;
import com.jnv.betrayal.gamestates.Dungeon;
import com.jnv.betrayal.gamestates.GameState;
import com.jnv.betrayal.gamestates.HallOfFame;
import com.jnv.betrayal.gamestates.LoadGame;
import com.jnv.betrayal.gamestates.Lobby;
import com.jnv.betrayal.gamestates.Menu;
import com.jnv.betrayal.gamestates.SplashScreen;
import com.jnv.betrayal.main.Betrayal;

import java.util.Stack;

public class GameStateManager {

    private Betrayal game;
    private Stack<GameState> gameStates;
    public State currentState;
    public static GameState pauseState;

    public enum State {
        SPLASH,
        MENU,
        CHARACTER_SELECTION,
        LOAD_GAME,
        HALL_OF_FAME,
        LOBBY,
        DUNGEON
    }

    public GameStateManager(Betrayal game) {
        this.game = game;
        gameStates = new Stack<GameState>();
    }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }
    public void render() {
        gameStates.peek().render();
    }
    public void pause() {
        pauseState = gameStates.peek();
    }
    public void resume() {
        gameStates = new Stack<GameState>();
        gameStates.push(pauseState);
        pauseState = null;
    }

    // Helpers
    public void pushState(State state) {
        gameStates.push(getState(state));
    }
    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

    // Getters
    public Betrayal getGame() { return game; }
    private GameState getState(State state) {
        currentState = state;
        switch (state) {
            case SPLASH:
                return new SplashScreen(this);
            case MENU:
                return new Menu(this);
            case CHARACTER_SELECTION:
                return new CharacterSelection(this);
            case LOAD_GAME:
                return new LoadGame(this);
            case HALL_OF_FAME:
                return new HallOfFame(this);
            case LOBBY:
                return new Lobby(this);
            case DUNGEON:
                return new Dungeon(this);

            default:
                Gdx.app.log("GameStateManager", "getState() returns null: ERROR");
                return null;
        }
    }
    public GameState state() {
        return gameStates.peek();
    }

    // Setters
    public void setState(State state) {
        if (gameStates.isEmpty()) pushState(state);
        else {
            popState();
            pushState(state);
        }
    }

}
