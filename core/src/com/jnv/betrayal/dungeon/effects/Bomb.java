package com.jnv.betrayal.dungeon.effects;


import com.jnv.betrayal.dungeon.actions.EventType;
import com.jnv.betrayal.dungeon.cards.Card;

public class Bomb extends Effect {
	private int attack;

	public Bomb(int attack, int turns) {
		super(EventType.BOMB, turns);
		this.attack = attack;
		isHostile = true;
	}

	@Override
	public void startEffect(Card card) {

	}

	@Override
	public void endEffect(Card card) {
		card.takeDamage(attack);
	}

	@Override
	public void consistentEffect(Card card) {

	}
}
