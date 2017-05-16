package de.sloth.neuralNetwork.behavior;

import java.util.List;

import de.sloth.components.INeuralNetwork;
import de.sloth.components.NeuralNetworkComp;
import de.sloth.components.SlothEnemyComp;
import de.sloth.components.SpearBagComp;
import de.sloth.entity.Entity;
import de.sloth.neuralNetwork.EntityManagerNN;
import de.sloth.neuralNetwork.EntityNNInputConverter;
import de.sloth.spearSystems.ThrowSpearEvent;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.system.game.flying.FlyingComp;
import de.sloth.system.game.moveSystem.PossibleMoveEvent;
import de.sloth.system.game.moveSystem.Direction;

public class BControllPlayerNN implements IBehavior {

	@Override
	public void execute(GameSystem system) {
		EntityManagerNN nnMan = (EntityManagerNN) system.getEntityManager();
		INeuralNetwork nn = ((NeuralNetworkComp) nnMan.getNNInformation().getComponent(NeuralNetworkComp.class)).getNetwork();
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		int maxEnemies = Integer.valueOf(ConfigLoader.getInstance().getConfig("maxEnemies", "7"));
		int maxSpears = Integer.valueOf(ConfigLoader.getInstance().getConfig("maxSpears", "7"));
		List<Entity> enemies = IEntityManagement.filterEntitiesByComponent(system.getEntityManager().getAllEntities(), SlothEnemyComp.class);
		List<Entity> spears = IEntityManagement.filterEntitiesByComponent(system.getEntityManager().getAllEntities(), FlyingComp.class);
		if(player != null) {
			SpearBagComp spComp = (SpearBagComp) player.getComponent(SpearBagComp.class);
			//check environ and give network input of entities
			double commandValue;
			try {
				//System.out.println(((NeuralNetwork) nn).getGraph().toStringNodeType(NodeType.INPUT, false));
				nn.setInputOfNode(EntityNNInputConverter.convertEntityToValue(player), "n_1");
				for(int i = 0; i < maxEnemies; i++) {
					if(i < enemies.size()) {
						nn.setInputOfNode(EntityNNInputConverter.convertEntityToValue(enemies.get(i)), "n_" + (i+2));
					} else {
						nn.setInputOfNode(0.0, "n_" + (i+2));
					}
				}
				for(int i = 0; i < maxSpears; i++) {
					if(i < spears.size()) {
						nn.setInputOfNode(EntityNNInputConverter.convertEntityToValue(spears.get(i)), "n_" + (i+5));
					} else {
						nn.setInputOfNode(0.0, "n_" + (i+5));
					}
				}
				commandValue = nn.processInput();
				//System.out.println("cValue: " + commandValue);
			} catch (Exception e) {
				e.printStackTrace();
				commandValue = 1.0;
				//System.out.println("cValue: " + commandValue);
			}
			if(commandValue <= 0.35) {
				system.getEventQueue().add(new PossibleMoveEvent(Direction.LEFT));
			} else if(commandValue > 0.25 && commandValue <= 0.35 && spComp.getSpears() > 0) {
				system.getEventQueue().add(new ThrowSpearEvent());
			} else if(commandValue > 0.65) {
				system.getEventQueue().add(new PossibleMoveEvent(Direction.RIGHT));
			}
		}
	}

	@Override
	public void execute(GameSystem arg0, GameEvent arg1) {}
}