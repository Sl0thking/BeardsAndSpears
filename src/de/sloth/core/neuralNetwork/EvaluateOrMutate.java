package de.sloth.core.neuralNetwork;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

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
		NetworkSequence[] eval_gen = evaluate(system, nnComp, pop);
		if(eval_gen != null) {
			nnComp.setCurrGen(nnComp.getCurrGen() + 1);
			if(nnComp.getCurrGen() < nnComp.getGenerations()) {
				NetworkSequence[] comb_gen = combine(eval_gen);
				NetworkSequence[] mutate_gen = mutate(comb_gen);
				NetworkSequence[] next_gen = fillGeneration(mutate_gen);
				nnComp.setPopulation(Arrays.asList(next_gen));
				system.getEventQueue().add(new GeneticalEvent("Init"));
			}
		}
	}
	
	private NetworkSequence[] fillGeneration(NetworkSequence[] mutate_gen) {
		// TODO Auto-generated method stub
		return null;
	}

	private NetworkSequence[] mutate(NetworkSequence[] comb_gen) {
		// TODO Auto-generated method stub
		return null;
	}

	private NetworkSequence[] combine(NetworkSequence[] eval_gen) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Evaluate given population
	 */
	private NetworkSequence[] evaluate(GameSystem system, NeuralNetworkComp nnComp, List<NetworkSequence> pop) {
		NetworkSequence nnSeq = getUnratedSequence(pop);
		if(nnSeq != null) {
			nnComp.getNetwork().setSequence(nnSeq);
			system.getEventQueue().add(new StartGameEvent());
			return null;
		}
		NetworkSequence[] generation = (NetworkSequence[]) pop.toArray();
		Arrays.sort(generation);
		return generation;
	}
}