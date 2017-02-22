package de.sloth.testgame.main;

import java.util.concurrent.ConcurrentLinkedQueue;


import de.sloth.system.game.core.GameCore;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.hmi.HMICore;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
		int screenHeight = (int) Screen.getPrimary().getBounds().getHeight(); 
		int canvasWidth = 640;
		int canvasHeight = 480;
		int canvasLayers = 4;
		IEntityManagement entityManager = new EntityManager();
		HMICore gameHmi = new HMICore(canvasWidth, canvasHeight, screenWidth, screenHeight, canvasLayers);
		
		Scene scene = new Scene(gameHmi);
		primaryStage.setScene(scene);
		ConcurrentLinkedQueue<GameEvent> eventQueue = new ConcurrentLinkedQueue<GameEvent>();
		//Register hmi layers
		gameHmi.getCanvas().setOnKeyPressed(GameSystemGenerator.getInstance().generateGameControllSystem(entityManager, eventQueue));
		gameHmi.getCanvas().requestFocus();
		//initate controlls
		
		GameCore core = new GameCore();
		core.registerSystem(GameSystemGenerator.getInstance().generateSystemActivationSystem(entityManager, core, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateCheckCollisionSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateCollisionSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateMoveSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateBGMSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateStartGameSystem(entityManager, eventQueue, gameHmi));
		core.registerSystem(GameSystemGenerator.getInstance().generateRenderSystem(entityManager, gameHmi, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateEnemyControllSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateFlyingSpearSystem(entityManager, eventQueue));
		core.registerSystem(GameSystemGenerator.getInstance().generateThrowSpearSystem(entityManager, eventQueue));
		gameHmi.registerGameInterfaceLayer(new PlayerStatusLayer(eventQueue));
		core.start();
		eventQueue.add(new StartGameEvent());
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitHint("");
		primaryStage.setAlwaysOnTop(true);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}