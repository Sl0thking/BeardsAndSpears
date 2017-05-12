package de.sloth.core.neuralNetwork;

import java.util.List;

import de.sloth.components.INeuralNetwork;
import de.sloth.components.NeuralNetworkComp;
import de.sloth.components.SlothEnemyComp;
import de.sloth.components.SpearBagComp;
import de.sloth.core.EntityToInputConverter;
import de.sloth.entity.Entity;
import de.sloth.spearSystems.ThrowSpearEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.core.IEntityManagement;
import de.sloth.system.game.flying.FlyingComp;
import de.sloth.system.game.moveSystem.PossibleMoveEvent;
import de.sloth.system.game.moveSystem.Direction;

public class ControllPlayerNN implements IBehavior {

	@Override
	public void execute(GameSystem system) {
		EntityManagerNN nnMan = (EntityManagerNN) system.getEntityManager();
		INeuralNetwork nn = ((NeuralNetworkComp) nnMan.getNNInformation().getComponent(NeuralNetworkComp.class)).getNetwork();
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		List<Entity> enemies = IEntityManagement.filterEntitiesByComponent(system.getEntityManager().getAllEntities(), SlothEnemyComp.class);
		List<Entity> spears = IEntityManagement.filterEntitiesByComponent(system.getEntityManager().getAllEntities(), FlyingComp.class);
		if(player != null) {
			SpearBagComp spComp = (SpearBagComp) player.getComponent(SpearBagComp.class);
			//check environ and give network input of entities
			double commandValue;
			try {
				nn.setInputOfNode(EntityToInputConverter.convertEntityToValue(player), "n_1");
				for(int i = 0; i < enemies.size(); i++) {
					nn.setInputOfNode(EntityToInputConverter.convertEntityToValue(enemies.get(i)), "n_" + i+2);
				}
				for(int i = 0; i < spears.size(); i++) {
					nn.setInputOfNode(EntityToInputConverter.convertEntityToValue(spears.get(i)), "n_" + i+5);
				}
				commandValue = nn.processInput();
			} catch (Exception e) {
				commandValue = 0.0;
			}
			if(commandValue <= 0.25) {
				system.getEventQueue().add(new PossibleMoveEvent(Direction.LEFT));
			} else if(commandValue > 0.25 && commandValue <= 0.50 && spComp.getSpears() > 0) {
				system.getEventQueue().add(new ThrowSpearEvent());
			} else if(commandValue > 0.50 && commandValue <= 0.75) {
				system.getEventQueue().add(new PossibleMoveEvent(Direction.RIGHT));
			}
		}
	}

	@Override
	public void execute(GameSystem arg0, GameEvent arg1) {}
}