package de.sloth.core.neuralNetwork;

import de.sloth.components.NeuralNetworkComp;
import de.sloth.components.SpearBagComp;
import de.sloth.core.INeuralNetwork;
import de.sloth.entity.Entity;
import de.sloth.spearSystems.ThrowSpearEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;
import de.sloth.system.game.moveSystem.PossibleMoveEvent;
import de.sloth.system.game.moveSystem.Direction;

public class ControllPlayerNN implements IBehavior {

	@Override
	public void execute(GameSystem system) {
		NNEntityManager nnMan = (NNEntityManager) system.getEntityManager();
		INeuralNetwork nn = ((NeuralNetworkComp) nnMan.getNNInformation().getComponent(NeuralNetworkComp.class)).getNetwork();
		Entity player = system.getEntityManager().getActivePlayabaleEntity();
		if(player != null) {
			SpearBagComp spComp = (SpearBagComp) player.getComponent(SpearBagComp.class);
			double commandValue = nn.processInput();
			if(commandValue < 0.29) {
				system.getEventQueue().add(new PossibleMoveEvent(Direction.LEFT));
			} else if(commandValue > 0.29 && commandValue < 0.65 && spComp.getSpears() > 0) {
				system.getEventQueue().add(new ThrowSpearEvent());
			} else {
				system.getEventQueue().add(new PossibleMoveEvent(Direction.RIGHT));
			}
		}
		//System.out.println(player);
	}

	@Override
	public void execute(GameSystem arg0, GameEvent arg1) {}
}