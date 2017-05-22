package de.sloth.core;

import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import de.sloth.component.FocusComp;
import de.sloth.core.behavior.BCheckForDeath;
import de.sloth.core.behavior.BControllEnemy;
import de.sloth.core.behavior.BKillEnemy;
import de.sloth.core.behavior.BRender;
import de.sloth.core.behavior.BStartGame;
import de.sloth.core.component.VikingEnemyComp;
import de.sloth.core.event.StartGameEvent;
import de.sloth.hmi.HMICore;
import de.sloth.neuralNetwork.behavior.BCheckForDeathNN;
import de.sloth.neuralNetwork.behavior.BControllPlayerNN;
import de.sloth.neuralNetwork.behavior.BProcessEvoAlgorithmNN;
import de.sloth.neuralNetwork.behavior.BStartGameWithoutGUI;
import de.sloth.neuralNetwork.event.GeneticalEvent;
import de.sloth.score.behavior.BCalcScore;
import de.sloth.score.event.CalcScoreEvent;
import de.sloth.spears.behavior.BCollectSpearPoints;
import de.sloth.spears.behavior.BControllSpear;
import de.sloth.spears.behavior.BThrowSpear;
import de.sloth.spears.event.ThrowSpearEvent;
import de.sloth.system.game.collision.CheckCollision;
import de.sloth.system.game.collision.CollisionEvent;
import de.sloth.system.game.collision.CollisionHandleSystem;
import de.sloth.system.game.collision.commonBehavior.DamagePlayer;
import de.sloth.system.game.collision.commonBehavior.Deglitch;
import de.sloth.system.game.collision.commonBehavior.Despawn;
import de.sloth.system.game.controlls.ControllSystem;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameCore;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.system.game.flying.FlyingComp;
import de.sloth.system.game.moveSystem.Direction;
import de.sloth.system.game.moveSystem.Move;
import de.sloth.system.game.moveSystem.MoveEvent;
import de.sloth.system.game.moveSystem.PossibleMoveEvent;
import de.sloth.system.game.soundSystem.PlayBgm;
import de.sloth.system.game.soundSystem.PlaySound;
import de.sloth.system.game.soundSystem.PlaySoundEvent;
import de.sloth.system.game.systemActivation.ActivateAllSystems;
import de.sloth.system.game.systemActivation.ActivateSystem;
import de.sloth.system.game.systemActivation.GameCoreSystem;
import de.sloth.system.game.systemActivation.SystemActivationEvent;
import de.sloth.system.hmi.HMIGameSystem;

/**
 * Generator for Systems
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 17.05.2017
 *
 */
public class GameSystemGenerator {

	private static GameSystemGenerator instance;
	private static int maxX;
	private static int maxY;
	private static double scaling;
	private static int spriteWidth;
	private static int spriteHeight;
	
	public static GameSystemGenerator getInstance() {
		if(instance == null) {
			instance = new GameSystemGenerator();
		}
		return instance;
	}
	
	private GameSystemGenerator() {
		GameSystemGenerator.scaling = 2.0;
		GameSystemGenerator.spriteWidth = 32;
		GameSystemGenerator.spriteHeight = 32;
		GameSystemGenerator.maxY = 480; 
		GameSystemGenerator.maxX = 640; 
	}

	public GameSystem generateStartGameSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue, HMICore gameHMI) {
		HMIGameSystem startGameSystem = new HMIGameSystem(gameHMI, "startSys", StartGameEvent.class, entityManager, eventQueue);
		startGameSystem.registerBehavior("Any", new BStartGame());
		return startGameSystem;
	}

	public GameSystem generateMoveSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem moveSystem = new GameSystem("moveSys", MoveEvent.class, entityManager, eventQueue);
		moveSystem.registerBehavior("Any", new Move((GameSystemGenerator.maxX-((int) GameSystemGenerator.scaling*GameSystemGenerator.spriteWidth)), (GameSystemGenerator.maxY-((int) GameSystemGenerator.scaling*GameSystemGenerator.spriteHeight))));
		return moveSystem;
	}
	
	public GameSystem generateCheckCollisionSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem collSystem = new GameSystem("checkCollSys", PossibleMoveEvent.class, entityManager, eventQueue);
		collSystem.registerBehavior("Any", new CheckCollision());
		return collSystem;
	}

	public GameSystem generateCollisionSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		CollisionHandleSystem collSystem = new CollisionHandleSystem("collSys", CollisionEvent.class, entityManager, eventQueue);
		collSystem.registerCollisionBehavior(FocusComp.class, FlyingComp.class, new BCollectSpearPoints());
		collSystem.registerCollisionBehavior(FlyingComp.class, FocusComp.class, new DamagePlayer());
		collSystem.registerCollisionBehavior(VikingEnemyComp.class, VikingEnemyComp.class, new Deglitch(32));
		collSystem.registerCollisionBehavior(FlyingComp.class, VikingEnemyComp.class, new BKillEnemy());
		collSystem.registerCollisionBehavior(FlyingComp.class, FlyingComp.class, new Despawn());
		return collSystem;
	}
	
	public GameSystem generateRenderSystem(IEntityManagement entityManager, HMICore gameHMI, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		HMIGameSystem renderSystem = new HMIGameSystem(gameHMI, "renderSys", null, entityManager, eventQueue);
		renderSystem.setActive(true);
		renderSystem.registerBehavior("Any", new BRender(maxX, maxY));
		return renderSystem;
	}

	public GameSystem generateSystemActivationSystem(IEntityManagement entityManager, GameCore core, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameCoreSystem systemActivationSystem = new GameCoreSystem("sysActiveSys", SystemActivationEvent.class, entityManager, eventQueue, core);
		systemActivationSystem.registerBehavior("single", new ActivateSystem());
		systemActivationSystem.registerBehavior("all", new ActivateAllSystems());
		return systemActivationSystem;
	}
	
	public GameSystem generateBGMSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem bgmSystem = new GameSystem("bgmSys", null, entityManager, eventQueue);
		double bgmVolume = Double.parseDouble(ConfigLoader.getInstance().getConfig("bgmVolume", "0.5"));
		bgmSystem.registerBehavior("Any", new PlayBgm(bgmVolume));
		boolean musicOn = Boolean.valueOf(ConfigLoader.getInstance().getConfig("bgm", "true"));
		bgmSystem.setActive(musicOn);
		return bgmSystem;
	}

	public EventHandler<? super KeyEvent> generateGameControllSystem(
			IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		ControllSystem cSys = new ControllSystem("cSys", entityManager, eventQueue);
		cSys.registerKey(KeyCode.A, new PossibleMoveEvent(Direction.LEFT));
		cSys.registerKey(KeyCode.D, new PossibleMoveEvent(Direction.RIGHT));
		cSys.registerKey(KeyCode.F, new ThrowSpearEvent());
		return cSys;
	}
	
	public GameSystem generateControllPlayerNNSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem enemyControllSystem = new GameSystem("ctrlPlayerNN", null, entityManager, eventQueue);
		enemyControllSystem.registerBehavior("Any", new BControllPlayerNN());
		return enemyControllSystem;
	}
	
	public GameSystem generateEnemyControllSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem enemyControllSystem = new GameSystem("enemyCtrl", null, entityManager, eventQueue);
		enemyControllSystem.registerBehavior("Any", new BControllEnemy());
		return enemyControllSystem;
	}
	
	public GameSystem generateFlyingSpearSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem flyingSpearSystem = new GameSystem("spearCtrl", null, entityManager, eventQueue);
		flyingSpearSystem.registerBehavior("Any", new BControllSpear());
		return flyingSpearSystem;
	}

	public GameSystem generateThrowSpearSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem flyingSpearSystem = new GameSystem("throwSpearCtrl", ThrowSpearEvent.class, entityManager, eventQueue);
		flyingSpearSystem.registerBehavior("Any", new BThrowSpear());
		return flyingSpearSystem;
	}

	public GameSystem generateEndConditionSystem(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem endConditionSystem = new GameSystem("endCondition", null, entityManager, eventQueue);
		endConditionSystem.registerBehavior("Any", new BCheckForDeath());
		endConditionSystem.setActive(false);
		return endConditionSystem;
	}
	
	public GameSystem generateEndConditionNNSystem(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem endConditionSystem = new GameSystem("endCondition", null, entityManager, eventQueue);
		endConditionSystem.registerBehavior("Any", new BCheckForDeathNN());
		endConditionSystem.setQuiet(Boolean.parseBoolean(ConfigLoader.getInstance().getConfig("isQuiet", "true")));
		endConditionSystem.setActive(false);
		return endConditionSystem;
	}

	public GameSystem generateStartGameSystemNN(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem startGameSystem = new GameSystem("startGame", StartGameEvent.class, entityManager, eventQueue);
		startGameSystem.setQuiet(Boolean.parseBoolean(ConfigLoader.getInstance().getConfig("isQuiet", "true")));
		startGameSystem.registerBehavior("Any", new BStartGameWithoutGUI());
		return startGameSystem;
	}
	
	public GameSystem generateGeneticalSystem(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem genSystem = new GameSystem("genSys", GeneticalEvent.class, entityManager, eventQueue);
		genSystem.setQuiet(Boolean.parseBoolean(ConfigLoader.getInstance().getConfig("isQuiet", "true")));
		genSystem.registerBehavior("Any", new BProcessEvoAlgorithmNN());
		return genSystem;
	}
	
	public GameSystem generateScoreSystem(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem scoreSystem = new GameSystem("scoreSys", CalcScoreEvent.class, entityManager, eventQueue);
		scoreSystem.registerBehavior("CalcScore", new BCalcScore());
		return scoreSystem;
	}

	public GameSystem generateSoundSystem(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem seSystem = new GameSystem("seSys", PlaySoundEvent.class, entityManager, eventQueue);
		double seVolume = Double.parseDouble(ConfigLoader.getInstance().getConfig("seVolume", "0.5"));
		seSystem.registerBehavior("Any", new PlaySound(seVolume));
		seSystem.setActive(Boolean.parseBoolean(ConfigLoader.getInstance().getConfig("playSE", "true")));
		return seSystem;
	}
}