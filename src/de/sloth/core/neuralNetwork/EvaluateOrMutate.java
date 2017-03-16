package de.sloth.core.neuralNetwork;

import java.util.Arrays;
import java.util.List;

import de.sloth.components.NetworkSequence;
import de.sloth.components.NeuralNetworkComp;
import de.sloth.core.StartGameEvent;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

public class EvaluateOrMutate implements IBehavior {

	@Override
	public void execute(GameSystem system) {}
	
	private NetworkSequence getUnratedSequence(List<NetworkSequence> pop) {
		for(NetworkSequence nseq : pop) {
			if(nseq.getFitnessLvl() == 0) {
				return nseq;
			}
		}
		return null;
	}
	
	@Override
	public void execute(GameSystem system, GameEvent event) {
		NNEntityManager nnMan = (NNEntityManager) system.getEntityManager();
		NeuralNetworkComp nnComp = (NeuralNetworkComp) nnMan.getNNInformation().getComponent(NeuralNetworkComp.class);
		List<NetworkSequence> pop = nnComp.getPopulation();
		System.out.println(pop);
		NetworkSequence nnSeq = getUnratedSequence(pop);
		if(nnSeq == null) {
			Object[] generation = pop.toArray();
			Arrays.sort(generation);
			for(Object o : generation) {
				System.out.println((NetworkSequence) o);
			}
			System.exit(0);
		} else {
			nnComp.getNetwork().setSequence(nnSeq);
			system.getEventQueue().add(new StartGameEvent());
		}
	}
}