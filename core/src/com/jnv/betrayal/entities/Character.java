package com.jnv.betrayal.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jnv.betrayal.main.Betrayal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Holds information regarding a game character's traits
 * @author Vincent Wang
 */
public class Character {

    /** Contains rotation value for character preview
     * front = 0, right side = 1, back = 2, left side = 3 */
    private int rotation = 0;

    /** Holds character trait values */
    private int hair_male, hair_female, hairColor, skinTone;

    public enum Trait {
        GENDER,
        HAIR_STYLE,
        HAIR_COLOR,
        JOB
    }

    /** Holds gender trait values, male or female */
    private enum Gender {
        MALE, FEMALE
    }
    private Gender gender;

    public enum Jobs {
        WARRIOR, THIEF, KNIGHT, PRIEST
    }

    /** Textures for character head, format: head_side_walkAnimation */
    private TextureRegion head_front_left, head_front_still, head_front_right;
    private TextureRegion head_right_left, head_right_still, head_right_right;
    private TextureRegion head_left_left, head_left_still, head_left_right;
    private TextureRegion head_back_left, head_back_still, head_back_right;
    private TextureRegion armor_front_left, armor_front_still, armor_front_right;
    private TextureRegion armor_right_left, armor_right_still, armor_right_right;
    private TextureRegion armor_left_left, armor_left_still, armor_left_right;
    private TextureRegion armor_back_left, armor_back_still, armor_back_right;

    private Job job;
    private Equips equips;
    private Inventory inventory;
    private Stats stats;

    /** Creates character with default values */
    public Character() {
        equips = new Equips();
        job = new Job();
        inventory = new Inventory();
        stats = new Stats();

        gender = Gender.MALE;
        hair_male = 1;
        hair_female = 1;
        hairColor = 1;
        skinTone = 1;
        job.setJob(Jobs.WARRIOR);
        update();
    }

    public void createCharacter(Jobs job) {
        this.job.setJob(job);
    }
    private void update() {
        updateHeadSprites();
        updateArmorSprites();
    }
    public void saveInfo() {

    }

    // Helpers
    /** Update and split the sprite sheet into appropriate sprites */
    private void updateHeadSprites() {
        Texture head_all;
        if (gender == Gender.MALE) {
            head_all = Betrayal.res.getTexture("hair-male-"
                    + hair_male + "-" + hairColor + "-all");
        } else {
            head_all = Betrayal.res.getTexture("hair-female-"
                    + hair_female + "-" + hairColor + "-all");
        }
        TextureRegion[][] head_split = TextureRegion.split(head_all, 32, 48);
        head_front_left = head_split[0][0];
        head_front_still = head_split[0][1];
        head_front_right = head_split[0][2];
        head_right_left = head_split[1][0];
        head_right_still = head_split[1][1];
        head_right_right = head_split[1][2];
        head_left_left = head_split[2][0];
        head_left_still = head_split[2][1];
        head_left_right = head_split[2][2];
        head_back_left = head_split[3][0];
        head_back_still = head_split[3][1];
        head_back_right = head_split[3][2];
    }
    private void updateArmorSprites() {
        Texture armor_all = Betrayal.res.getTexture("char-armor-peasant");
        TextureRegion[][] armor_split = TextureRegion.split(armor_all, 32, 48);
        armor_front_left = armor_split[0][0];
        armor_front_still = armor_split[0][1];
        armor_front_right = armor_split[0][2];
        armor_right_left = armor_split[1][0];
        armor_right_still = armor_split[1][1];
        armor_right_right = armor_split[1][2];
        armor_left_left = armor_split[2][0];
        armor_left_still = armor_split[2][1];
        armor_left_right = armor_split[2][2];
        armor_back_left = armor_split[3][0];
        armor_back_still = armor_split[3][1];
        armor_back_right = armor_split[3][2];
    }
    /** Called when character trait is changed */
    private void spriteChanged() {
        update();
    }

    // Getters
    public TextureRegion getHeadPreview() {
        switch (rotation) {
            case 0:
                return head_front_still;
            case 1:
                return head_right_still;
            case 2:
                return head_back_still;
            case 3:
                return head_left_still;
            default:
                return null;
        }
    }
    public TextureRegion getArmorPreview() {
        switch (rotation) {
            case 0:
                return armor_front_still;
            case 1:
                return armor_right_still;
            case 2:
                return armor_back_still;
            case 3:
                return armor_left_still;
            default:
                return null;
        }
    }
    public String getTrait(Trait trait) {
        switch (trait) {
            case GENDER:
                if (gender == Gender.MALE) return "M";
                else return "F";
            case HAIR_STYLE:
                if (gender == Gender.MALE) return Integer.toString(hair_male);
                else return Integer.toString(hair_female);
            case HAIR_COLOR:
                if (gender == Gender.MALE) return Integer.toString(hairColor);
                else return Integer.toString(hairColor);
            case JOB:
                return getJob(job.getJob());
            default:
                return null;
        }
    }
    private String getJob(Jobs job) {
        switch (job) {
            case WARRIOR:
                return "W";
            case KNIGHT:
                return "K";
            case PRIEST:
                return "P";
            case THIEF:
                return "T";
            default:
                return null;
        }
    }
    public Job getJobClass() { return job; }
    public Equips getEquipsClass() { return equips; }
    public Inventory getInventoryClass() { return inventory; }
    public Stats getStatsClass() { return stats; }

    // Setters
    /** Functions for rotating character preview image */
    public void rotateLeft() {
        if (rotation == 0) rotation = 3;
        else rotation--;
    }
    public void rotateRight() {
        rotation++;
        rotation &= 3;
    }
    public void setPreviousTrait(Trait trait) {
        switch (trait) {
            case GENDER:
                if (gender == Gender.MALE) gender = Gender.FEMALE;
                else gender = Gender.MALE;
                spriteChanged();
                break;
            case HAIR_STYLE:
                if (gender == Gender.MALE) {
                    if (hair_male == 1) {
                        hair_male = 5;
                    } else hair_male--;
                } else {
                    if (hair_female == 1) {
                        hair_female = 4;
                    } else hair_female--;
                }
                spriteChanged();
                break;
            case HAIR_COLOR:
                if (hairColor == 1) hairColor = 7;
                else hairColor--;
                spriteChanged();
                break;
            case JOB:
                job.setPreviousJob();
            default:
                break;
        }
    }
    public void setNextTrait(Trait trait) {
        switch (trait) {
            case GENDER:
                if (gender == Gender.MALE) gender = Gender.FEMALE;
                else gender = Gender.MALE;
                spriteChanged();
                break;
            case HAIR_STYLE:
                if (gender == Gender.MALE) {
                    if (hair_male == 5) {
                        hair_male = 1;
                    } else hair_male++;
                } else {
                    if (hair_female == 4) {
                        hair_female = 1;
                    } else hair_female++;
                }
                spriteChanged();
                break;
            case HAIR_COLOR:
                if (hairColor == 7) hairColor = 1;
                else hairColor++;
                spriteChanged();
                break;
            case JOB:
                job.setNextJob();
            default:
                break;
        }
    }

    // Classes
    public class Equips {

        public Equips() {}

        public void equipWeapon(Weapon weapon) {}
        public void equipHeadArmor() {}
        public void equipBodyArmor() {}
        public void equipShield() {}

        public void unequipWeapon() {}
        public void unequipHeadArmor() {}
        public void unequipBodyArmor() {}
        public void unequipShield() {}

    }
    public class Inventory {

        private int gold, items_max;
        private Map<Item, Integer> items;

        public Inventory() {
            gold = 0;
            items_max = 20;

            items = new LinkedHashMap<Item, Integer>();
        }

        // Getters
        public int getGold() { return gold; }

        // Setters
        /** Adds an item to inventory and return true, if inventory is filled,
         * do nothing and return false.
         * @param item item to be added
         * @return true if item was successfully added. false if inv was full */
        public boolean addItem(Item item) {
            return addItem(item, 1);
        }
        /** Adds the specific amount of items to inventory and return true.
         * If inventory is filled, do nothing and return false.
         * @param i item to be added
         * @param amount how many items to be added
         * @return true if item was successfully added. false if inv was full */
        public boolean addItem(Item i, int amount) {
            if (items.size() < items_max) {
                if (items.containsKey(i)) {
                    items.put(i, items.get(i) + amount);
                } else items.put(i, amount);
                return true;
            } else return false;
        }
        /** Sorts the inventory */
        public void sortItems() {
            if (items != null) {
                Map<Item, Integer> items_sorted =
                        new TreeMap<Item, Integer>(new Item.ItemComparator());
                items_sorted.putAll(items);
                items = new LinkedHashMap<Item, Integer>(items_sorted);
            }
        }

    }
    public class Stats {

        private int health, defense, attack, agility;

        public Stats() {
            health = 25;
            defense = 5;
            attack = 5;
            agility = 5;
        }
    }
    public class Job {

        private Jobs job;

        public Job() {}
        public Job(Jobs job) {
            this.job = job;
        }

        // Getters
        public Jobs getJob() { return job; }

        // Setters
        public void setJob(Jobs job) {
            this.job = job;
        }
        public void setPreviousJob() {
            switch (job) {
                case WARRIOR:
                    this.job = Jobs.THIEF;
                    break;
                case KNIGHT:
                    this.job = Jobs.WARRIOR;
                    break;
                case PRIEST:
                    this.job = Jobs.KNIGHT;
                    break;
                case THIEF:
                    this.job = Jobs.PRIEST;
                    break;
                default:
                    break;
            }
        }
        public void setNextJob() {
            switch (job) {
                case WARRIOR:
                    this.job = Jobs.KNIGHT;
                    break;
                case KNIGHT:
                    this.job = Jobs.PRIEST;
                    break;
                case PRIEST:
                    this.job = Jobs.THIEF;
                    break;
                case THIEF:
                    this.job = Jobs.WARRIOR;
                    break;
                default:
                    break;
            }
        }
    }
}