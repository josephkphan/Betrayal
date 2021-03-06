package com.jnv.betrayal.dungeon.turns;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;
import com.jnv.betrayal.dungeon.Field;
import com.jnv.betrayal.dungeon.cards.Card;
import com.jnv.betrayal.dungeon.cards.MonsterCard;
import com.jnv.betrayal.dungeon.effects.Effect;
import com.jnv.betrayal.dungeon.effects.Event;
import com.jnv.betrayal.dungeon.effects.actions.Attack;
import com.jnv.betrayal.dungeon.utils.Panel;
import com.jnv.betrayal.main.Betrayal;
import com.jnv.betrayal.resources.FontManager;
import com.jnv.betrayal.scene2d.ui.Button;
import com.jnv.betrayal.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/*
 * Copyright (c) 2016. JNV Games.
 * Co-authors: Vincent Wang, Joseph Phan
 */

public class MonsterTurn extends Turn {

	private Card currentCard;

	public MonsterTurn(Field field, Pool<Label> panelPool, Pool<Button> buttonPool, Group panels, Betrayal game) {
		super(field, panelPool, buttonPool, panels, game);
	}

	@Override
	public void draw() {
		panels.clear();
		System.out.println("DRAWING MONSTEr PANEL");
		createGrayPanel("Monster's Turn", FontManager.getFont80(), Panel.full);
		currentCard = field.getCurrentCard();
		System.out.println("!!!!!!!Monster's turn current card : " + currentCard.getName());

		currentCard.getField().addAction(Actions.delay(3, Actions.run(new Runnable() {
			@Override
			public void run() {
				if (!(currentCard instanceof MonsterCard)
						&& Betrayal.DEBUG
						&& currentCard != null) {
					throw new AssertionError("Current card should be a MonsterCard, " +
							"instead it is this player's turn: " + field.getCurrentCard());
				}

				if (field.playerZone.size() != 0
						&& field.gsm.game.getCurrentCharacter().getId() == field.playerZone.get(0).getID()
						&& currentCard instanceof MonsterCard
						&& field.roundManager.checkSrcAlive(currentCard)) {
					monsterAttack((MonsterCard) currentCard);
				} else {
					field.nextTurn();
				}
			}
		})));
	}

	private void monsterAttack(MonsterCard currentCard) {
		int numTargets = currentCard.getNumAttackTargets();
		int max = field.playerZone.size();
		int x;

		ArrayList<Integer> target = new ArrayList<Integer>();
		ArrayList<Card> dst = new ArrayList<Card>();
		Random randomNumberGenerator = new Random();
		while (target.size() < max && target.size() < numTargets) {
			x = randomNumberGenerator.nextInt(max);
			if (!target.contains(x)) {
				target.add(x);
			}
		}
		for (Integer i : target) {
			dst.add(field.playerZone.get(i));
		}

		if (currentCard.hasEffect() && currentCard.effectCounter == currentCard.getMonster().getEffectCD()) {
			currentCard.effectCounter = 1;
			// Make monster do effect
			Event event = new Event(currentCard.getEffect(), currentCard.getEffect().getStartType());
			event.getEffect().setSrc(currentCard);
			if (!event.getEffect().isHostile()) {
				dst.clear();
				dst.add(currentCard);
			}
			event.getEffect().setDest(dst);
			currentCard.getField().roundManager.addEvent(event);
			field.nextTurn();
			System.out.println("MONSTERSPECIAL");
		} else {
			System.out.println(Arrays.asList(Thread.currentThread().getStackTrace()));

			// Make monster do normal attack
			currentCard.effectCounter++;
			Effect attack = new Attack(currentCard, dst);
			field.roundManager.addEvent(attack, attack.getStartType());
			field.nextTurn();
			System.out.println("MONSTERNORMALATTACK");
		}
	}
}
