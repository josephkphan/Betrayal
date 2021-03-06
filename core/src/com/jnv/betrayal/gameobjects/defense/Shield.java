package com.jnv.betrayal.gameobjects.defense;

import com.badlogic.gdx.graphics.Texture;
import com.jnv.betrayal.gameobjects.DualWieldable;
import com.jnv.betrayal.gameobjects.Equip;
import com.jnv.betrayal.gameobjects.ItemNaming;
import com.jnv.betrayal.resources.BetrayalAssetManager;

/*
 * Copyright (c) 2016. JNV Games.
 * Co-authors: Vincent Wang, Joseph Phan
 */

public class Shield extends Equip implements DualWieldable {

	private Texture leftPreview, rightPreview;

	public Shield(String name, BetrayalAssetManager res) {
		super(name, res);
		leftPreview = res.getTexture(ItemNaming.toPreview(name, true));
		rightPreview = res.getTexture(ItemNaming.toPreview(name, false));
	}

	public Shield(int id, String name, BetrayalAssetManager res, int buyCost, int health, int attack, int defense, String description) {
		super(id, name, res, buyCost, health, attack, defense, description);
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
		private String description;

		public ShieldFactory(BetrayalAssetManager res) {
			this.res = res;
		}

		public ShieldFactory id(int id) {
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

		public ShieldFactory description(String description) {
			this.description = description;
			return this;
		}

		public Shield build() {
			return new Shield(id, name, res, costBuy, 0, 0, defense, description);
		}

	}
}
