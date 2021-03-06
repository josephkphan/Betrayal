package com.jnv.betrayal.dungeon;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.jnv.betrayal.character.Character;
import com.jnv.betrayal.dungeon.cards.Card;
import com.jnv.betrayal.dungeon.cards.MonsterCard;
import com.jnv.betrayal.dungeon.cards.PlayerCard;
import com.jnv.betrayal.dungeon.effects.Died;
import com.jnv.betrayal.dungeon.effects.Effect;
import com.jnv.betrayal.dungeon.effects.Event;
import com.jnv.betrayal.dungeon.effects.EventType;
import com.jnv.betrayal.dungeon.managers.AnimationManager;
import com.jnv.betrayal.dungeon.managers.NextTurnManager;
import com.jnv.betrayal.dungeon.managers.RoundManager;
import com.jnv.betrayal.dungeon.managers.UIManager;
import com.jnv.betrayal.dungeon.popup.EventLog;
import com.jnv.betrayal.gamestates.GameStateManager;
import com.jnv.betrayal.main.Betrayal;
import com.jnv.betrayal.resources.BetrayalAssetManager;
import com.jnv.betrayal.scene2d.InputListener;
import com.jnv.betrayal.scene2d.ui.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/*
 * Copyright (c) 2016. JNV Games.
 * Co-authors: Vincent Wang, Joseph Phan
 */

public class Field extends Group {

	public final RoundManager roundManager;
	public final UIManager uiManager;
	public final AnimationManager animationManager;
	public final GameStateManager gsm;
	public final BetrayalAssetManager res;
	public final Betrayal game;
	public final List<PlayerCard> playerZone;
	public final List<MonsterCard> monsterZone;
	public final List<Card> allCards;
	public final Socket socket;
	public int reward;
	private Image background, eventLogButton;
	private Character clientCharacter;
	private Group cardGroup = new Group();
	private NextTurnManager nextTurnManager;
	private boolean dungeonEnded = false;

	/**
	 * Creates an empty field that utilizes a stage for its actors
	 */
	public Field(GameStateManager gsm) {
		// Initialize card zones and instance variables
		playerZone = new ArrayList<PlayerCard>();
		monsterZone = new ArrayList<MonsterCard>();
		allCards = new ArrayList<Card>();
		this.gsm = gsm;
		game = gsm.game;
		res = gsm.game.res;
		socket = gsm.game.getCurrentCharacter().getRoom().getSocket();
		clientCharacter = gsm.game.getCurrentCharacter();
		if (socket != null && socket.connected()) configSocket();
		reward = 0;
		background = new Image(res.getTexture("map01"));
		// Add things to stage
		addActor(background);
		addActor(cardGroup);
		uiManager = new UIManager(this);
		animationManager = new AnimationManager(res, this);
		nextTurnManager = new NextTurnManager(this);
		roundManager = new RoundManager(animationManager);
		roundManager.setSocket(socket);
	}

	public void setup() {
		createEventLogButton();
		eventLogButton.toFront();
		nextTurnManager.setup();
	}

	public void setBackgroundForField(String s) {
		background.setDrawable(new TextureRegionDrawable(new TextureRegion(res.getTexture(s))));
	}

	public void adjustReward() {
		reward /= playerZone.size();
	}

	public void nextTurn() {
		System.out.println("----NEXT TURN----");
		refreshAllCards();
		//System.out.println(Arrays.asList(Thread.currentThread().getStackTrace()));

		// If your turn is ending, decrease skill cooldown counter
		if (!dungeonEnded) {
			nextTurnManager.nextTurn();
			roundManager.checkEvents(nextTurnManager.getCurrentCard());
			animationManager.animate();
			uiManager.nextTurn();
			System.out.println("CURRENT CARD: "  + getCurrentCard().getName());
			//System.out.println("Field.nextTurn(): Waiting for input...");
		}
	}

	public void dungeonEnded() {
		dungeonEnded = true;
		clearActions();
		uiManager.dungeonEnded();
	}

	private void createEventLogButton() {
		// Load event log button
		float scale = 0.5f;
		eventLogButton = new Image(res.getTexture("event-log-button"));
		eventLogButton.layout();
		eventLogButton.setBounds(10, Betrayal.HEIGHT - 30 - 144 * scale, 512 * scale, 144 * scale);
		eventLogButton.addListener(new InputListener(eventLogButton) {
			@Override
			public void doAction() {
				new EventLog(game, roundManager.eventHistory);
			}
		});
		addActor(eventLogButton);
	}

	public Group getCardGroup() {
		return cardGroup;
	}

	public void addCard(Card card) {
		if (card instanceof PlayerCard) playerZone.add((PlayerCard) card);
		else if (card instanceof MonsterCard) monsterZone.add((MonsterCard) card);
		else throw new AssertionError("Card is neither a PlayerCard or MonsterCard");
		cardGroup.addActor(card.getGroup());
		card.setField(this);
	}

	public void adjustMonsterHealth() {
		for (MonsterCard card : monsterZone) {
			card.multiplyHealth(playerZone.size());
		}
	}

	/**
	 * Activates select mode on all cards
	 */
	public void beginSelectMode(int numTargets) {
		for (Card card : playerZone) {
			card.beginSelectMode(numTargets);
		}
		for (Card card : monsterZone) {
			card.beginSelectMode(numTargets);
		}
	}

	public boolean isMonsterZoneEmpty() {
		return monsterZone.size() == 0;
	}

	public boolean checkCardsSelected(int cardsSelected) {
		return cardsSelected == getCardsSelected().size();
	}

	/**
	 * Ends select mode on all cards
	 */
	public void endSelectMode() {
		for (Card card : allCards) {
			card.endSelectMode();
		}
	}

	/**
	 * @return the array of cards on the field selected
	 */
	public List<Card> getCardsSelected() {
		List<Card> selectedCards = new ArrayList<Card>();
		for (Card card : allCards) {
			if (card.isSelected()) selectedCards.add(card);
		}
		return selectedCards;
	}

	public void unselectAll() {
		for (Card card : allCards) {
			card.unselect();
		}
	}

	public Card getCurrentCard() {
		return nextTurnManager.getCurrentCard();
	}

	// refreshed all cards incase a card died.
	public void refreshAllCards() {
		allCards.clear();
		allCards.addAll(playerZone);
		allCards.addAll(monsterZone);
	}

	public List<Card> getAllCards() {
		refreshAllCards();
		return allCards;
	}

	public List<Card> getAllPlayerCards() {
		return new ArrayList<Card>(playerZone);
	}

	public List<Card> getAllMonsterCards() {
		return new ArrayList<Card>(monsterZone);
	}

	/**
	 * Player card death event
	 */
	public void removePlayerCard(PlayerCard card) {
		card.getStatusIcon().setDrawable(null);
		playerZone.remove(card);
		card.getGroup().remove();
		refreshAllCards();
	}

	/**
	 * Monster card death event
	 */
	public void removeMonsterCard(MonsterCard card) {
		monsterZone.remove(card);
		refreshAllCards();
	}

	public void configSocket() {
		socket.on("newEvent", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];

				// Initializing values
				List<Integer> dstID = new ArrayList<Integer>();
				int srcID = -1;

				try {
					srcID = data.getJSONObject("effect").getInt("src");
					JSONArray destArray = data.getJSONObject("effect").getJSONArray("dest");
					for (int i = 0; i < destArray.length(); i++) {
						dstID.add(destArray.getInt(i));
					}

					// FInd Card corresponding to id
					Card src = findSrcCard(srcID);
					List<Card> dst = findDestCard(dstID);

					// Recreate event
					Class<?> clazz = Class.forName(data.getJSONObject("effect").getString("class"));
					Constructor<?> constructor = clazz.getConstructor(JSONObject.class, int.class, Card.class, List.class);
					Effect effect = (Effect) constructor.newInstance(data.getJSONObject("effect").getJSONObject("values"),
							data.getJSONObject("effect").getInt("turns"), src, dst);

					// Register recreated event with roundManager
					Event event = roundManager.addEventClient(effect, EventType.valueOf(data.getString("eventType")));
					System.out.println("RECEIVED EVENT: " + event);
					nextTurn();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).on("disconnectInDungeon", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				//takes in character of disconnected player
				System.out.println("DISCONNECT~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				JSONObject data = (JSONObject) args[0];
				int disconnectID = -1;
				try {
					disconnectID = data.getJSONObject("player").getInt("id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Card tmp = null;

				for (Card card : playerZone) {
					if (card.getID() == disconnectID) {
						//checks if it's that current person's turn

						tmp = card;
						//removes person from playerzone and perform animation
						break;
					}
				}
				final Card deleteThisCard = tmp;
				if (deleteThisCard != null) {
					System.out.println("DELETING DISCONNECT");
					playerZone.remove(deleteThisCard);
					roundManager.addEventClient(new Died(deleteThisCard), EventType.DIED);
					if (getCurrentCard() == deleteThisCard) nextTurn();
					Runnable r = new Runnable() {
						@Override
						public void run() {
							deleteThisCard.getCardName().setVisible(false);
							deleteThisCard.died();
							removePlayerCard((PlayerCard) deleteThisCard);
						}
					};
					deleteThisCard.getCardImage().addAction(Actions.delay(4f, Actions.run(r)));
					animationManager.getCardAnimation().fadeOut(deleteThisCard);
				} else {
					System.out.println("ALREADY DELETED");
				}
				refreshAllCards();

				// Remove from room
				getClientCharacter().getRoom().removeCharacter(disconnectID);
			}
		});
	}

	private Card findSrcCard(int srcID) {
		//check if there is a src or dest card in the playerzone
		for (Card card : allCards) {
			if (card.getID() == srcID) {
				return card;
			}
		}
		return null;
	}

	private List<Card> findDestCard(List<Integer> dstID) {
		List<Card> dst = new ArrayList<Card>();
		for (Card card : allCards) {
			for (int i : dstID) {
				if (i == card.getID()) {
					dst.add(card);
				}
			}
		}
		return dst;
	}

	public void adjustPlayerCardStatsBasedOnJobs() {
		boolean hasWarrior, hasThief, hasPriest, hasKnight;
		hasWarrior = hasThief = hasPriest = hasKnight = false;

		//Determines what jobs are in the party
		for (PlayerCard card : playerZone) {
			switch (card.getJob()) {

				case WARRIOR:
					hasWarrior = true;
					break;
				case THIEF:
					hasThief = true;
					break;
				case KNIGHT:
					hasKnight = true;
					break;
				case PRIEST:
					hasPriest = true;
					break;
			}
		}

		//adjusts all stats according to which Jobs are present
		for (PlayerCard card : playerZone) {
			if (hasWarrior) card.multiplyAttack();
			if (hasPriest) card.multiplyHealth();
			if (hasKnight) card.multiplyDefense();
		}

		if (hasThief) reward *= 1.5;
	}

	public Character getClientCharacter() {
		return clientCharacter;
	}
}
