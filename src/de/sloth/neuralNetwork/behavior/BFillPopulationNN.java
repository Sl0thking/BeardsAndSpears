package de.sloth.neuralNetwork.behavior;

import java.util.List;
import java.util.Random;

import de.sloth.neuralNetwork.EntityManagerNN;
import de.sloth.neuralNetwork.component.NetworkSequence;
import de.sloth.neuralNetwork.component.NeuralNetworkComp;
import de.sloth.neuralNetwork.event.GeneticalEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

/**
 * Class for initializing a start population
 */
public class BFillPopulationNN implements IBehavior{

	private static final int BIT_RANGE = 8;
	private static final int MAX_BIT_NR = 256;
	
	@Override
	public void execute(GameSystem arg0) {}

	@Override
	public void execute(GameSystem system, GameEvent arg1) {
		System.out.println("TRIGGERED");
		NetworkSequence lastSeq = null;
		EntityManagerNN nnMan = (EntityManagerNN) system.getEntityManager();
		NeuralNetworkComp nnComp = (NeuralNetworkComp) nnMan.getNNInformation().getComponent(NeuralNetworkComp.class);
		List<NetworkSequence> pop = nnComp.getPopulation();
		while(nnComp.getMaxPopSize() - nnComp.getPopulation().size() != 0) {
			System.out.println("CREATE SEQUENCE...");
			lastSeq = generateRandomSequence(nnComp.getNetwork().getEdgeCount());
			pop.add(lastSeq);
		}
		if(lastSeq != null) {
			nnComp.getNetwork().setSequence(lastSeq);
		} else {
			system.getEventQueue().add(new GeneticalEvent("CheckOrMutate"));
		}
	}
	
	private NetworkSequence generateRandomSequence(int edges) {
		String sequence = "";
		Random rand = new Random();
		for(int i = 0; i < edges; i++) {
			String edgeSeq = Integer.toBinaryString(rand.nextInt(MAX_BIT_NR));
			if(edgeSeq.length() < BIT_RANGE) {
				int zeroFillCount = BIT_RANGE-edgeSeq.length();
				for(int x = 0; x < zeroFillCount; x++) {
					edgeSeq = "0" + edgeSeq;
				}
			}
			sequence = sequence + edgeSeq;
		}
		return new NetworkSequence(sequence);
	}
}