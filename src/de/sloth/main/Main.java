package de.sloth.main;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameCore;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.system.hmi.SpriteLoader;
import de.sloth.core.EntityGenerator;
import de.sloth.core.EntityManager;
import de.sloth.core.GameSystemGenerator;
import de.sloth.core.event.StartGameEvent;
import de.sloth.hmi.HMICore;
import de.sloth.hmi.PlayerStatusLayer;
import de.sloth.neuralNetwork.EntityManagerNN;
import de.sloth.neuralNetwork.NetworkSequenceIO;
import de.sloth.neuralNetwork.component.NetworkSequence;
import de.sloth.neuralNetwork.component.NeuralNetworkComp;
import de.sloth.neuralNetwork.event.GeneticalEvent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		ConfigLoader.getInstance("valHal.properties");
		boolean isKiControlled = Boolean.valueOf(ConfigLoader.getInstance().getConfig("isKi", "false"));
		boolean showGui = Boolean.valueOf(ConfigLoader.getInstance().getConfig("showGui", "false"));
		boolean isLearning = false;

		String archivePath = ConfigLoader.getInstance().getConfig("archivePath", ".");
		ConcurrentLinkedQueue<GameEvent> eventQueue = new ConcurrentLinkedQueue<GameEvent>();
		IEntityManagement entityManager = new EntityManager();
		GameCore core = new GameCore();
		HMICore gameHmi = null;
		if(isKiControlled) {
			entityManager = new EntityManagerNN();
			int learnArchiveID = Integer.valueOf(ConfigLoader.getInstance().getConfig("learnArchiveID", "1"));
			File archiveFile = new File(archivePath + "\\learn_archive_" + learnArchiveID);
			if(!archiveFile.exists()) {
				archiveFile.mkdir();
				new File(archiveFile.getAbsolutePath() + "\\teached_population").mkdir();
				new File(archiveFile.getAbsolutePath() + "\\replay").mkdir();
			}
			ConfigLoader.getInstance().setConfigFile(archiveFile.getAbsolutePath() + "\\learn.properties");
			isLearning = Boolean.valueOf(ConfigLoader.getInstance().getConfig("isLearning", "true"));
			entityManager.addEntity(EntityGenerator.getInstance().generateNNEntity());
			NeuralNetworkComp nnComp = (NeuralNetworkComp) IEntityManagement.filterEntitiesByComponent(entityManager.getAllEntities(), NeuralNetworkComp.class).get(0).getComponent(NeuralNetworkComp.class);
			
			if(isLearning) {
				try {
					nnComp.setPopulation(NetworkSequenceIO.loadSequences(archiveFile.getAbsolutePath() + "\\teached_population"));
				} catch (IOException e) {}
			} else {
				try {
					NetworkSequence nseq = NetworkSequenceIO.loadSequence(archiveFile.getAbsolutePath() + "\\replay", "ns0.nsq");
					nnComp.addPopulation(nseq);
					nnComp.getNetwork().setSequence(nseq);
				} catch (IOException e) {
					System.exit(0);
				}
			}
		}
		if(showGui) {
			int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
			int screenHeight = (int) Screen.getPrimary().getBounds().getHeight(); 
			int canvasWidth = 640;
			int canvasHeight = 480;
			int canvasLayers = 4;
			
			String[] aniPhaseNames = {"idle", "hit"};
			int aniPhases = 4;
			SpriteLoader spl = SpriteLoader.getInstance(2.0, 32, 32, aniPhases, aniPhaseNames);
			gameHmi = new HMICore(canvasWidth, canvasHeight, screenWidth, screenHeight, canvasLayers, spl);
			Scene scene = new Scene(gameHmi);
			gameHmi.registerGameInterfaceLayer(new PlayerStatusLayer(eventQueue));
	
			primaryStage.setScene(scene);
			primaryStage.setFullScreen(true);
			primaryStage.setFullScreenExitHint("");
			primaryStage.setAlwaysOnTop(true);
			primaryStage.show();
		}
		
		if(isKiControlled) {
			if(isLearning) {
				core.registerSystem(GameSystemGenerator.getInstance().generateGeneticalSystem(entityManager, eventQueue));
			}
			if(showGui) {
				core.registerSystem(GameSystemGenerator.getInstance().generateStartGameSystem(entityManager, eventQueue, gameHmi));
			} else {
				core.registerSystem(GameSystemGenerator.getInstance().generateStartGameSystemNN(entityManager, eventQueue));
			}
			core.registerSystem(GameSystemGenerator.getInstance().generateControllPlayerNNSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateEnemyControllSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateThrowSpearSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateFlyingSpearSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateMoveSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateCheckCollisionSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateCollisionSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateScoreSystem(entityManager, eventQueue));
			if(showGui) {
				core.registerSystem(GameSystemGenerator.getInstance().generateRenderSystem(entityManager, gameHmi, eventQueue));
			}
			core.registerSystem(GameSystemGenerator.getInstance().generateSystemActivationSystem(entityManager, core, eventQueue));
			if(isLearning) {
				core.registerSystem(GameSystemGenerator.getInstance().generateEndConditionNNSystem(entityManager, eventQueue));
			} else {
				core.registerSystem(GameSystemGenerator.getInstance().generateEndConditionSystem(entityManager, eventQueue));
			}
			core.start();
			if(isLearning) {
				eventQueue.add(new GeneticalEvent());
			} else {
				eventQueue.add(new StartGameEvent());
			}
			
		} else if(showGui) {
			core.registerSystem(GameSystemGenerator.getInstance().generateStartGameSystem(entityManager, eventQueue, gameHmi));
			gameHmi.getCanvas().setOnKeyPressed(GameSystemGenerator.getInstance().generateGameControllSystem(entityManager, eventQueue));
			gameHmi.getCanvas().requestFocus();
			core.registerSystem(GameSystemGenerator.getInstance().generateEnemyControllSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateThrowSpearSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateFlyingSpearSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateMoveSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateCheckCollisionSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateCollisionSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateScoreSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateRenderSystem(entityManager, gameHmi, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateBGMSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateSoundSystem(entityManager, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateSystemActivationSystem(entityManager, core, eventQueue));
			core.registerSystem(GameSystemGenerator.getInstance().generateEndConditionSystem(entityManager, eventQueue));
			core.start();
			eventQueue.add(new StartGameEvent());
		} else {
			System.out.println("Cannot start player controlled game without gui.");
			System.exit(1);
		}
		/*
		for(GameSystem gsys : core.getRegistredSystems()) {
			System.out.println("ACTIVE: " + gsys.getSystemID());
		}*/
	}

	public static void main(String[] args) {
		launch(args);
	}
}