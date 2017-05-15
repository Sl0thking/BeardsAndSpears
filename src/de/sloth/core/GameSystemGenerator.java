package de.sloth.core;

import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import de.sloth.component.FocusComp;
import de.sloth.components.SlothEnemyComp;
import de.sloth.core.neuralNetwork.CheckForDeathNN;
import de.sloth.core.neuralNetwork.ControllPlayerNN;
import de.sloth.core.neuralNetwork.EvaluateOrMutate;
import de.sloth.core.neuralNetwork.GeneticalEvent;
import de.sloth.core.neuralNetwork.FillPopulation;
import de.sloth.core.neuralNetwork.StartGameNN;
import de.sloth.hmi.HMICore;
import de.sloth.spearSystems.CollectSpear;
import de.sloth.spearSystems.ControllSpear;
import de.sloth.spearSystems.ThrowSpear;
import de.sloth.spearSystems.ThrowSpearEvent;
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
import de.sloth.system.game.systemActivation.ActivateAllSystems;
import de.sloth.system.game.systemActivation.ActivateSystem;
import de.sloth.system.game.systemActivation.GameCoreSystem;
import de.sloth.system.game.systemActivation.SystemActivationEvent;
import de.sloth.system.hmi.HMIGameSystem;


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
		//GameSystemGenerator.maxX = (int) Screen.getPrimary().getBounds().getWidth();
		//GameSystemGenerator.maxY = (int) Screen.getPrimary().getBounds().getHeight(); 
		GameSystemGenerator.scaling = 2.0;
		GameSystemGenerator.spriteWidth = 32;
		GameSystemGenerator.spriteHeight = 32;
		GameSystemGenerator.maxY = 480; 
		GameSystemGenerator.maxX = 640; 
		
	}

	public GameSystem generateStartGameSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue, HMICore gameHMI) {
		HMIGameSystem startGameSystem = new HMIGameSystem(gameHMI, "startSys", StartGameEvent.class, entityManager, eventQueue);
		startGameSystem.registerBehavior("Any", new StartGame());
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
		collSystem.registerCollisionBehavior(FocusComp.class, FlyingComp.class, new CollectSpearPoints());
		collSystem.registerCollisionBehavior(FlyingComp.class, FocusComp.class, new DamagePlayer());
		collSystem.registerCollisionBehavior(SlothEnemyComp.class, SlothEnemyComp.class, new Deglitch());
		collSystem.registerCollisionBehavior(FlyingComp.class, SlothEnemyComp.class, new KillEnemy());
		collSystem.registerCollisionBehavior(FlyingComp.class, FlyingComp.class, new Despawn());
		return collSystem;
	}
	
	public GameSystem generateRenderSystem(IEntityManagement entityManager, HMICore gameHMI, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		HMIGameSystem renderSystem = new HMIGameSystem(gameHMI, "renderSys", null, entityManager, eventQueue);
		renderSystem.setActive(true);
		renderSystem.registerBehavior("Any", new Render(maxX, maxY));
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
		bgmSystem.registerBehavior("Any", new PlayBgm());
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
		enemyControllSystem.registerBehavior("Any", new ControllPlayerNN());
		return enemyControllSystem;
	}
	
	public GameSystem generateEnemyControllSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem enemyControllSystem = new GameSystem("enemyCtrl", null, entityManager, eventQueue);
		enemyControllSystem.registerBehavior("Any", new ControllEnemy());
		return enemyControllSystem;
	}
	
	public GameSystem generateFlyingSpearSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem flyingSpearSystem = new GameSystem("spearCtrl", null, entityManager, eventQueue);
		flyingSpearSystem.registerBehavior("Any", new ControllSpear());
		return flyingSpearSystem;
	}

	public GameSystem generateThrowSpearSystem(IEntityManagement entityManager, ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem flyingSpearSystem = new GameSystem("throwSpearCtrl", ThrowSpearEvent.class, entityManager, eventQueue);
		flyingSpearSystem.registerBehavior("Any", new ThrowSpear());
		return flyingSpearSystem;
	}

	public GameSystem generateEndConditionSystem(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem endConditionSystem = new GameSystem("endCondition", null, entityManager, eventQueue);
		endConditionSystem.registerBehavior("Any", new CheckForDeath());
		endConditionSystem.setActive(false);
		return endConditionSystem;
	}
	
	public GameSystem generateEndConditionNNSystem(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem endConditionSystem = new GameSystem("endCondition", null, entityManager, eventQueue);
		endConditionSystem.registerBehavior("Any", new CheckForDeathNN());
		endConditionSystem.setActive(false);
		return endConditionSystem;
	}

	public GameSystem generateStartGameSystemNN(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem startGameSystem = new GameSystem("startGame", StartGameEvent.class, entityManager, eventQueue);
		startGameSystem.registerBehavior("Any", new StartGameNN());
		return startGameSystem;
	}
	
	public GameSystem generateGeneticalSystem(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem genSystem = new GameSystem("genSys", GeneticalEvent.class, entityManager, eventQueue);
		genSystem.registerBehavior("Init", new FillPopulation());
		genSystem.registerBehavior("CheckOrMutate", new EvaluateOrMutate());
		return genSystem;
	}
	
	public GameSystem generateScoreSystem(IEntityManagement entityManager,
			ConcurrentLinkedQueue<GameEvent> eventQueue) {
		GameSystem scoreSystem = new GameSystem("scoreSys", CalcScoreEvent.class, entityManager, eventQueue);
		scoreSystem.registerBehavior("CalcScore", new CalcScore());
		return scoreSystem;
	}
}