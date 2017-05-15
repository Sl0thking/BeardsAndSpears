package de.sloth.core.neuralNetwork;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.sloth.components.NetworkSequence;
import de.sloth.components.NeuralNetworkComp;
import de.sloth.core.NetworkSequenceIO;
import de.sloth.core.StartGameEvent;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

public class EvaluateOrMutate implements IBehavior {

	@Override
	public void execute(GameSystem system) {}
	
	private NetworkSequence getUnratedSequence(List<NetworkSequence> pop) {
		for(NetworkSequence nseq : pop) {
			if(nseq.getFitnessLvl() == -1) {
				return nseq;
			}
		}
		return null;
	}
	
	@Override
	public void execute(GameSystem system, GameEvent event) {
		EntityManagerNN nnMan = (EntityManagerNN) system.getEntityManager();
		NeuralNetworkComp nnComp = (NeuralNetworkComp) nnMan.getNNInformation().getComponent(NeuralNetworkComp.class);
		List<NetworkSequence> pop = nnComp.getPopulation();
		Object[] eval_gen = evaluate(system, nnComp, pop);
		if(eval_gen != null) {
			nnComp.setCurrGen(nnComp.getCurrGen() + 1);
			if(nnComp.getCurrGen() < nnComp.getGenerations()) {
				System.out.println("Combine and mutate...");
				Object[] cleaned_eval_gen = new NetworkSequence[nnComp.getSizeOfElite()];
				for(int i = nnComp.getMaxPopSize()-nnComp.getSizeOfElite(); i < nnComp.getMaxPopSize(); i++) {
					cleaned_eval_gen[i-(nnComp.getMaxPopSize()-nnComp.getSizeOfElite())] = eval_gen[i];
				}
				NetworkSequence[] strongest_gen = {(NetworkSequence) cleaned_eval_gen[0], (NetworkSequence) cleaned_eval_gen[1]};
				NetworkSequence[] other_gen = new NetworkSequence[cleaned_eval_gen.length-2];
				for(int i = 2; i < cleaned_eval_gen.length; i++) {
					other_gen[i-2] = (NetworkSequence) cleaned_eval_gen[i];
				}
				NetworkSequence[] mutate_gen = mutate(combine(strongest_gen));
				List<NetworkSequence> newPop = new LinkedList<NetworkSequence>();
				newPop.addAll(Arrays.asList(mutate_gen));
				newPop.addAll(Arrays.asList(other_gen));
				newPop.addAll(Arrays.asList(strongest_gen));
				nnComp.setPopulation(newPop);
				System.out.println("Population of Generation: ");
				for(NetworkSequence nseq : nnComp.getPopulation()) {
					System.out.println(nseq);
				}
				system.getEventQueue().add(new GeneticalEvent("Init"));
			} else {
				int learnArchiveID = Integer.valueOf(ConfigLoader.getInstance().getConfig("learnArchiveID", "1"));
				NetworkSequenceIO.clearDir(".\\learn_archive_" + learnArchiveID + "\\teached_population");
				try {
					NetworkSequenceIO.saveSequences(".\\learn_archive_" + learnArchiveID + "\\teached_population", nnComp.getPopulation());
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		}
	}

	private NetworkSequence[] mutate(NetworkSequence[] comb_gen) {
		double mutateChance = 0.1f;
		for(NetworkSequence n : comb_gen) {
			for(int i = 0; i < n.getSequence().toCharArray().length; i++) {
				if(Math.random() < mutateChance) {
					if(n.getSequence().toCharArray()[i] == '0') {
						n.setSequence(n.getSequence().substring(0, i) + "1" + n.getSequence().substring(i+1, n.getSequence().toCharArray().length));
					} else {
						n.setSequence(n.getSequence().substring(0, i) + "0" + n.getSequence().substring(i+1, n.getSequence().toCharArray().length));
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
			nnSeq.setFitnessLvl(0);
			nnComp.getNetwork().setSequence(nnSeq);
			system.getEventQueue().add(new StartGameEvent());
			return null;
		}
		Object[] generation = pop.toArray();
		Arrays.sort(generation);
		return generation;
	}
}