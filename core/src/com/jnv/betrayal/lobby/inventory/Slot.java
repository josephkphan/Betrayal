/*
 * Copyright (c) 2015. JNV Games, All rights reserved.
 */

package com.jnv.betrayal.lobby.inventory;

import com.jnv.betrayal.scene2d.Dimension;

final class Slot {

	static final int SLOTS = 7;
	static final float LENGTH = 92;

	static final Dimension HEAD = new Dimension(212, 525, LENGTH, LENGTH);
	static final Dimension BODY = new Dimension(212, 425, LENGTH, LENGTH);
	static final Dimension LEFT_WEAPON = new Dimension(112, 325, LENGTH, LENGTH);
	static final Dimension RIGHT_WEAPON = new Dimension(312, 325, LENGTH, LENGTH);
	static final Dimension RING_1 = new Dimension(518, 325, LENGTH, LENGTH);
	static final Dimension RING_2 = new Dimension(518, 225, LENGTH, LENGTH);
	static final Dimension CLOAK = new Dimension(518, 525, LENGTH, LENGTH);

	static final Dimension[] ALL_DIMENS = {
			HEAD, BODY, CLOAK, LEFT_WEAPON, RIGHT_WEAPON, RING_1, RING_2
	};
}
