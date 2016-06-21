/*
 * Copyright (c) 2015. JNV Games, All rights reserved.
 */

package com.jnv.betrayal.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.jnv.betrayal.resources.BetrayalAssetManager;

public class Shield extends Equip implements DualWieldable {

	private int defense;
	private Texture leftPreview, rightPreview;

	public int getDefense() { return defense; }

	public Shield(String name, BetrayalAssetManager res) {
		super(name, res);
		leftPreview = res.getTexture(ItemNaming.toPreview(name, true));
		rightPreview = res.getTexture(ItemNaming.toPreview(name, false));
	}

	public Shield(int id, String name, BetrayalAssetManager res, int cost_buy, int defense) {
		super(id, name, res, cost_buy);
		this.defense = defense;
	}

	public Texture getLeftPreview() {
		return leftPreview;
	}

	public Texture getRightPreview() {
		return rightPreview;
	}

	public static class ShieldFactory {
		private int id = -1;
		private String name = "";
		private int costBuy = -1;
		private int defense = -1;
		private BetrayalAssetManager res;

		public ShieldFactory(BetrayalAssetManager res) {
			this.res = res;
		}

		public ShieldFactory id(int id){
			this.id = id;
			return this;
		}

		public ShieldFactory name(String name) {
			this.name = name;
			return this;
		}

		public ShieldFactory costBuy(int costBuy) {
			this.costBuy = costBuy;
			return this;
		}

		public ShieldFactory defense(int defense) {
			this.defense = defense;
			return this;
		}

		public Shield build() {
			return new Shield(id, name, res, costBuy, defense);
		}

	}
}
