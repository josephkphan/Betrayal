/*
 * Copyright (c) 2015. JNV Games, All rights reserved.
 */

package com.jnv.betrayal.entities;

public class Weapon extends Item {

    public Weapon(int id, String name) {
        super(id, name);
    }

    public Weapon(String name, int amount) {
        super(name, amount);
    }
}
