/*
 * Copyright (c) 2015. JNV Games, All rights reserved.
 */

package com.jnv.betrayal.dungeon.utilities.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.jnv.betrayal.dungeon.managers.MonsterManager;
import com.jnv.betrayal.dungeon.utilities.constants.DungeonCoords;
import com.jnv.betrayal.gameobjects.Monster;
import com.jnv.betrayal.resources.BetrayalAssetManager;
import com.jnv.betrayal.resources.MonsterConstants;

public class DungeonMonster extends Entity {

	private Image image;
	private BetrayalAssetManager res;

	public DungeonMonster(BetrayalAssetManager res, int tier, int monsterID, Stage stage) {
		super(stage);
		Monster monster;
		switch (tier){
			case 0:
				monster = new Monster("monster-tier0-" + monsterID, res);
				image = new Image(monster.getMonsterTexture());
				image.layout();
				image.setBounds(
						MonsterConstants.tier0MonstersPosition[monsterID].x,
						MonsterConstants.tier0MonstersPosition[monsterID].y,
						MonsterConstants.tier0Monsters[monsterID].getWidth(),
						MonsterConstants.tier0Monsters[monsterID].getHeight());
				break;
			case 1:
				monster = new Monster("monster-tier1-" + monsterID, res);
				image = new Image(monster.getMonsterTexture());
				image.layout();
				image.setBounds(
						MonsterConstants.tier1MonstersPosition[monsterID].x,
						MonsterConstants.tier1MonstersPosition[monsterID].y,
						MonsterConstants.tier1Monsters[monsterID].getWidth(),
						MonsterConstants.tier1Monsters[monsterID].getHeight());
				break;
			case 2:
				monster = new Monster("monster-tier2-" + monsterID, res);
				image = new Image(monster.getMonsterTexture());
				image.layout();
				image.setBounds(
						MonsterConstants.tier2MonstersPosition[monsterID].x,
						MonsterConstants.tier2MonstersPosition[monsterID].y,
						MonsterConstants.tier2Monsters[monsterID].getWidth(),
						MonsterConstants.tier2Monsters[monsterID].getHeight());
				break;
			case 3:
				monster = new Monster("monster-tier3-" + monsterID, res);
				image = new Image(monster.getMonsterTexture());
				image.layout();
				image.setBounds(
						MonsterConstants.tier3MonstersPosition[monsterID].x,
						MonsterConstants.tier3MonstersPosition[monsterID].y,
						MonsterConstants.tier3Monsters[monsterID].getWidth(),
						MonsterConstants.tier3Monsters[monsterID].getHeight());
				break;
			case 4:
				monster = new Monster("monster-tier4-" + monsterID, res);
				image = new Image(monster.getMonsterTexture());
				image.layout();
				image.setBounds(
						MonsterConstants.tier4MonstersPosition[monsterID].x,
						MonsterConstants.tier4MonstersPosition[monsterID].y,
						MonsterConstants.tier4Monsters[monsterID].getWidth(),
						MonsterConstants.tier4Monsters[monsterID].getHeight());
				break;
			case 5:
				monster = new Monster("monster-tier5-" + monsterID, res);
				image = new Image(monster.getMonsterTexture());
				image.layout();
				image.setBounds(
						MonsterConstants.tier5MonstersPosition[monsterID].x,
						MonsterConstants.tier5MonstersPosition[monsterID].y,
						MonsterConstants.tier5Monsters[monsterID].getWidth(),
						MonsterConstants.tier5Monsters[monsterID].getHeight());
				break;
			default:
				break;
		}
		image.addAction(Actions.alpha(0));
		image.addAction(Actions.delay(1, Actions.fadeIn(2)));
		stage.addActor(image);
	}
}