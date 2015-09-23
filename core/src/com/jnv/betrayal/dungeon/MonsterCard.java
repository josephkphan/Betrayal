/*
 * Copyright (c) 2015. JNV Games, All rights reserved.
 */

package com.jnv.betrayal.dungeon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.jnv.betrayal.gameobjects.Monster;
import com.jnv.betrayal.resources.BetrayalAssetManager;
import com.jnv.betrayal.scene2d.Actor;
import com.jnv.betrayal.scene2d.Dimension;

public class MonsterCard extends Card {

	public MonsterCard(Dimension dimension, Monster monster, BetrayalAssetManager res) {
		this(dimension.getX(), dimension.getY(), dimension.getWidth(), dimension.getHeight(),
				monster, res);
	}

	public MonsterCard(final float x, final float y, final float width, final float height,
					   final Monster monster, BetrayalAssetManager res) {
		super(x, y, width, height, res);
		cardImage = new Actor() {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
				batch.draw(monster.getMonsterTexture(), 0, 0, width, height);
			}
		};
		cardImage.setBounds(0, 0, width, height);
		addActor(cardImage);
		cardImage.toBack();
	}
}
