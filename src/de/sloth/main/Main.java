package de.sloth.main;

import java.util.concurrent.ConcurrentLinkedQueue;

import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameCore;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.system.hmi.SpriteLoader;
import de.sloth.core.EntityGenerator;
import de.sloth.core.EntityManager;
import de.sloth.core.GameSystemGenerator;
import de.sloth.core.StartGameEvent;
import de.sloth.core.neuralNetwork.GeneticalEvent;
import de.sloth.core.neuralNetwork.EntityManagerNN;
import de.sloth.hmi.HMICore;
import de.sloth.hmi.PlayerStatusLayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		//trick for using special config name
		ConfigLoader.getInstance("valHal.properties");
		ConcurrentLinkedQueue<GameEvent> eventQueue = new ConcurrentLinkedQueue<GameEvent>();
		boolean isKiControlled = Boolean.valueOf(ConfigLoader.getInstance().getConfig("isKi", "false"));
		boolean showGui = Boolean.valueOf(ConfigLoader.getInstance().getConfig("showGui", "false"));
		IEntityManagement entityManager = new EntityManager();
		GameCore core = new GameCore();
		if(isKiControlled) {
			entityManager = new EntityManagerNN();
			//check if is learning mode
			entityManager.addEntity(EntityGenerator.getInstance().generateNNEntity());
			core.registerSystem(GameSystemGenerator.getInstance().generateGeneticalSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateControllPlayerNNSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateEndConditionNNSystem(entityManager, eventQueue));
		} 
		
		if(showGui) {
			int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
			int screenHeight = (int) Screen.getPrimary().getBounds().getHeight(); 
			int canvasWidth = 640;
			int canvasHeight = 480;
			int canvasLayers = 4;
			
			String[] aniPhaseNames = {"idle"};
			int aniPhases = 4;
			SpriteLoader spl = SpriteLoader.getInstance(2.0, 32, 32, aniPhases, aniPhaseNames);
			HMICore gameHmi = new HMICore(canvasWidth, canvasHeight, screenWidth, screenHeight, canvasLayers, spl);
			Scene scene = new Scene(gameHmi);
			
			gameHmi.registerGameInterfaceLayer(new PlayerStatusLayer(eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateRenderSystem(entityManager, gameHmi, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateStartGameSystem(entityManager, eventQueue, gameHmi));
			if(!isKiControlled) {
				//initate controlls
				gameHmi.getCanvas().setOnKeyPressed(GameSystemGenerator.getInstance().generateGameControllSystem(entityManager, eventQueue));
				gameHmi.getCanvas().requestFocus();
				core.registerSystem(GameSystemGenerator.getInstance().generateEndConditionSystem(entityManager, eventQueue));
				core.registerSystem(GameSystemGenerator.getInstance().generateBGMSystem(entityManager, eventQueue));
			}
			primaryStage.setScene(scene);
			primaryStage.setFullScreen(true);
			primaryStage.setFullScreenExitHint("");
			primaryStage.setAlwaysOnTop(true);
			primaryStage.show();
		}
		
		core.registerSystem(GameSystemGenerator.getInstance().generateSystemActivationSystem(entityManager, core, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateCheckCollisionSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateCollisionSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateMoveSystem(entityManager, eventQueue));
		
		core.registerSystem(GameSystemGenerator.getInstance().generateEnemyControllSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateFlyingSpearSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateThrowSpearSystem(entityManager, eventQueue));
		
		core.start();
		eventQueue.add(new GeneticalEvent("Init"));
		eventQueue.add(new StartGameEvent());
	}

	public static void main(String[] args) {
		launch(args);
	}
}