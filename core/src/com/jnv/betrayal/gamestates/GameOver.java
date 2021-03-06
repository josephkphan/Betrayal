package com.jnv.betrayal.gamestates;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.jnv.betrayal.main.Betrayal;
import com.jnv.betrayal.resources.BetrayalAssetManager;
import com.jnv.betrayal.resources.FontManager;
import com.jnv.betrayal.scene2d.InputListener;

/*
 * Copyright (c) 2016. JNV Games.
 * Co-authors: Vincent Wang, Joseph Phan
 */

public class GameOver extends GameState {

	private BetrayalAssetManager res;
	private Image background;
	private Actor mask;
	private Label.LabelStyle labelStyle;
	private Label rip, touchAnywhere;

	public GameOver(GameStateManager gsm) {
		super(gsm);
		this.res = gsm.game.res;
		loadFont();
		loadButtons();
	}

	public void update(float dt) {
		stage.act(dt);
	}

	public void render() {
		stage.draw();
	}

	public void dispose() {

	}

	private void loadFont() {
		labelStyle = FontManager.getFont60();
	}

	private void loadButtons() {
		loadRip();
		loadTouchAnywhere();
		loadBackground();
		loadContent();
		loadMask();
	}

	private void loadMask() {
		mask = new Actor();
		mask.setBounds(0, 0, Betrayal.WIDTH, Betrayal.HEIGHT);
		stage.addActor(mask);
		mask.addListener(new InputListener(mask) {
			@Override
			public void doAction() {
				gsm.setState(GameStateManager.State.MENU);
			}
		});

	}

	private void loadRip() {
		rip = new Label("R.I.P.", labelStyle);
		rip.setX(100);
		rip.setY(Betrayal.HEIGHT - 200);
		stage.addActor(rip);
	}

	private void loadTouchAnywhere() {
		touchAnywhere = new Label("[touch anywhere to return to main menu]", labelStyle);
		touchAnywhere.setX((Betrayal.WIDTH - touchAnywhere.getWidth()) / 2);
		touchAnywhere.setY(0);
		stage.addActor(touchAnywhere);
	}

	private void loadBackground() {
		background = new Image(res.getTexture("game-over-background"));
		background.layout();
		background.setBounds(0, 0, Betrayal.WIDTH, Betrayal.HEIGHT);
		stage.addActor(background);
	}

	private void loadContent() {
	}

	private void removeGameOver() {
		mask.remove();
		touchAnywhere.remove();
		rip.remove();
		background.remove();
	}
}
