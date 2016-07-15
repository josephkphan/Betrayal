package com.jnv.betrayal.dungeon.effects;

import com.jnv.betrayal.dungeon.actions.EventType;
import com.jnv.betrayal.dungeon.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Cloak extends Effect {

	public Cloak(Card src) {
		super(EventType.ATTACK);
		isHostile = false;
		this.src = src;
		dest = new ArrayList<Card>();
		dest.add(src);
	}

	@Override
	public void startEffect(Card card) {

	}

	@Override
	public void endEffect(Card card) {

	}

	@Override
	public void consistentEffect(Card card) {

	}
}