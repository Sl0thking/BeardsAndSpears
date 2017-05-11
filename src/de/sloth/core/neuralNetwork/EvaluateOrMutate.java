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
		Object[] eval_gen = evaluate(system, nnComp, pop);
		if(eval_gen != null) {
			nnComp.setCurrGen(nnComp.getCurrGen() + 1);
			System.out.println("Curr Gen: " + nnComp.getCurrGen());
			if(nnComp.getCurrGen() < nnComp.getGenerations()) {
				Object[] cleaned_eval_gen = new NetworkSequence[nnComp.getMaxPopSize() / 2];
				for(int i = 0; i < 2; i++) {
					cleaned_eval_gen[i] = eval_gen[i];
				}
				NetworkSequence[] comb_gen = combine(Arrays.copyOf(cleaned_eval_gen, cleaned_eval_gen.length, NetworkSequence[].class));
				NetworkSequence[] mutate_gen = mutate(comb_gen);
				List<NetworkSequence> newPop = new LinkedList<NetworkSequence>(Arrays.asList(mutate_gen));
				nnComp.setPopulation(newPop);
				system.getEventQueue().add(new GeneticalEvent("Init"));
			} else {
				System.exit(0);
			}
		}
	}

	private NetworkSequence[] mutate(NetworkSequence[] comb_gen) {
		double mutateChance = 0.1f;
		for(NetworkSequence n : comb_gen) {
			for(int i = 0; i < n.getSequence().toCharArray().length; i++) {
				if(Math.random() < mutateChance) {
					if(bit == '0') {
						n.setSequence(n.getSequence().substring(0, n.getSequence().indexOf(ch)));
					} else {
					
					}
				}
			}
		}
		return comb_gen;
	}

	private NetworkSequence[] combine(NetworkSequence[] eval_gen) {
		NetworkSequence n_seq_1 = new NetworkSequence("");
		NetworkSequence n_seq_2 = new NetworkSequence("");
		
		n_seq_1.setSequence(eval_gen[0].getSequence().substring(0, eval_gen[0].getSequence().length()/2) + 
						  eval_gen[1].getSequence().substring(eval_gen[1].getSequence().length()/2));
		n_seq_2.setSequence(eval_gen[1].getSequence().substring(0, eval_gen[1].getSequence().length()/2) + 
				  eval_gen[0].getSequence().substring(eval_gen[0].getSequence().length()/2));
		NetworkSequence[] c_ns = {n_seq_1, n_seq_2};
		return c_ns;
	}

	/*
	 * Evaluate given population
	 */
	private Object[] evaluate(GameSystem system, NeuralNetworkComp nnComp, List<NetworkSequence> pop) {
		NetworkSequence nnSeq = getUnratedSequence(pop);
		if(nnSeq != null) {
			nnComp.getNetwork().setSequence(nnSeq);
			system.getEventQueue().add(new StartGameEvent());
			return null;
		}
		Object[] generation = pop.toArray();
		Arrays.sort(generation);
		return generation;
	}
}