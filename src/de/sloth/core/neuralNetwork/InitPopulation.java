package de.sloth.core.neuralNetwork;

import java.util.List;

import de.sloth.components.NetworkSequence;
import de.sloth.components.NeuralNetworkComp;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

public class InitPopulation implements IBehavior{

	@Override
	public void execute(GameSystem arg0) {}

	@Override
	public void execute(GameSystem system, GameEvent arg1) {
		System.out.println("TRIGGERED");
		NNEntityManager nnMan = (NNEntityManager) system.getEntityManager();
		NeuralNetworkComp nnComp = (NeuralNetworkComp) nnMan.getNNInformation().getComponent(NeuralNetworkComp.class);
		List<NetworkSequence> pop = nnComp.getPopulation();
		for(int i = 0; i < nnComp.getMaxPopSize(); i++) {
			System.out.println("CREATE SEQUENCE...");
			pop.add(generateRandomSequence());
		}
		nnComp.getNetwork().setSequence(pop.get(0));
	}
	
	private NetworkSequence generateRandomSequence() {
		return new NetworkSequence("11100010101010101000101010101010101010001010101010");
	}

}
