package de.sloth.core.neuralNetwork;

import java.util.List;
import java.util.Random;

import de.sloth.components.NetworkSequence;
import de.sloth.components.NeuralNetworkComp;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

/**
 * Class for initializing a start population
 */
public class FillPopulation implements IBehavior{

	@Override
	public void execute(GameSystem arg0) {}

	@Override
	public void execute(GameSystem system, GameEvent arg1) {
		System.out.println("TRIGGERED");
		NNEntityManager nnMan = (NNEntityManager) system.getEntityManager();
		NeuralNetworkComp nnComp = (NeuralNetworkComp) nnMan.getNNInformation().getComponent(NeuralNetworkComp.class);
		List<NetworkSequence> pop = nnComp.getPopulation();
		while(nnComp.getMaxPopSize() - nnComp.getPopulation().size() != 0) {
			System.out.println("CREATE SEQUENCE...");
			pop.add(generateRandomSequence(nnComp.getNetwork().getEdgeCount()));
		}
		nnComp.getNetwork().setSequence(pop.get(0));
	}
	
	private NetworkSequence generateRandomSequence(int edges) {
		String sequence = "";
		Random rand = new Random();
		for(int i = 0; i < edges; i++) {
			String edgeSeq = Integer.toBinaryString(rand.nextInt(128));
			if(edgeSeq.length() < 7) {
				for(int x = 0; x < (7-edgeSeq.length()); x++) {
					edgeSeq = "0" + edgeSeq;
				}
			}
			sequence = sequence + edgeSeq;
		}
		return new NetworkSequence(sequence);
	}
}