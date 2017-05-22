package de.sloth.neuralNetwork.behavior;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.sloth.core.event.StartGameEvent;
import de.sloth.neuralNetwork.EntityManagerNN;
import de.sloth.neuralNetwork.NetworkSequenceIO;
import de.sloth.neuralNetwork.component.NeuralNetworkComp;
import de.sloth.neuralNetwork.component.datatype.NetworkSequence;
import de.sloth.system.game.core.ConfigLoader;
import de.sloth.system.game.core.GameEvent;
import de.sloth.system.game.core.GameSystem;
import de.sloth.system.game.core.IBehavior;

/**
 * Behavior for genetical system. 
 * Rates NetworkSequences and executes a evolutionary algorithm 
 * to create new generations.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 22.05.2017
 *
 */
public class BProcessEvoAlgorithmNN implements IBehavior {

	private static final int BIT_RANGE = 8;
	private static final int MAX_BIT_NR = 256;
	private static Date b4_timestamp;
	private static Date after_timestamp;
	private static long lastRemainingTime = -1;
	
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
		if(pop.size() == 0) {
			fillPopulation(system, nnComp);
		}
		Object[] eval_gen = evaluate(system, nnComp, pop);
		if(eval_gen != null) {
			nnComp.setCurrGen(nnComp.getCurrGen() + 1);
			BProcessEvoAlgorithmNN.after_timestamp = new Date();
			System.out.println("[GeneticalSysNN::ProcessEvoAlgorithm] " + nnComp.getCurrGen() + " gens / " + nnComp.getGenerations() + " gens");
			if(!system.isQuiet()) {
				System.out.println("[GeneticalSysNN::ProcessEvoAlgorithm] Process next generation: " + nnComp.getCurrGen());
			}
			if(BProcessEvoAlgorithmNN.b4_timestamp != null) {
				long millSec = after_timestamp.getTime() - b4_timestamp.getTime();
				long millSecOfEntireProcess = millSec * (nnComp.getGenerations() - nnComp.getCurrGen());
				long sec = millSecOfEntireProcess/1000;
				//calculate average between new and last value
				//for a more realistic time value
				if(BProcessEvoAlgorithmNN.lastRemainingTime >= 0) {
					sec = (BProcessEvoAlgorithmNN.lastRemainingTime + sec) / 2;
				}
				BProcessEvoAlgorithmNN.lastRemainingTime = sec;
				long hours = sec / 3600;
				long min = (sec % 3600) / 60;
				
				System.out.println("[GeneticalSysNN::ProcessEvoAlgorithm] Remaining time " + hours + "h " + min + "min");
			} else {
				System.out.println("[GeneticalSysNN::ProcessEvoAlgorithm] Beginn calculation of remaining time...");
			}
			if(nnComp.getCurrGen() < nnComp.getGenerations()) {
				if(!system.isQuiet()) {
					System.out.println("[GeneticalSysNN::ProcessEvoAlgorithm] Combine and mutate strongest candidates...");
				}
				
				//kill weaklings
				Object[] elite_gen = new NetworkSequence[nnComp.getSizeOfElite()];
				//set start index to greater than 0 to ignore the weakest
				//sequences (evaluate sorts NetworkSequence arrays in ascending order)
				int startIndex = nnComp.getMaxPopSize()-nnComp.getSizeOfElite();
				for(int i = startIndex; i < nnComp.getMaxPopSize(); i++) {
					elite_gen[i-startIndex] = eval_gen[i];
				}
				//save the two strongest sequences
				NetworkSequence[] strongest_gen = {(NetworkSequence) elite_gen[elite_gen.length-1], (NetworkSequence) elite_gen[elite_gen.length-2]};
				//other elite sequences are saved
				NetworkSequence[] other_gen = new NetworkSequence[elite_gen.length-2];
				for(int i = 0; i < elite_gen.length-2; i++) {
					other_gen[i] = (NetworkSequence) elite_gen[i];
				}
				NetworkSequence[] mutated_gen = mutate(system, breed(strongest_gen));
				List<NetworkSequence> newPop = new LinkedList<NetworkSequence>();
				newPop.addAll(Arrays.asList(mutated_gen));
				newPop.addAll(Arrays.asList(other_gen));
				newPop.addAll(Arrays.asList(strongest_gen));
				try {
					NetworkSequenceIO.savePopulationSnapshot(newPop);
					if(!system.isQuiet()) {
						System.out.println("[GeneticalSysNN::ProcessEvoAlgorithm] Successfull saved population snapshot under " + NetworkSequenceIO.getArchiveFile());
					}
				} catch (IOException e) {
					System.out.println("[GeneticalSysNN::ProcessEvoAlgorithm] Error with saving a population snapshot. Quit execution.");
					System.exit(2);
				}
				nnComp.setPopulation(newPop);
				resetFitness(newPop);
				fillPopulation(system, nnComp);
				evaluate(system, nnComp, newPop);
				BProcessEvoAlgorithmNN.b4_timestamp = new Date();
				system.getEventQueue().add(new StartGameEvent());
			} else {
				System.exit(0);
			}
		} else {
			system.getEventQueue().add(new StartGameEvent());
		}
	}

	/**
	 * Resets fitness levels of a given population
	 * @param newPop source population
	 */
	private void resetFitness(List<NetworkSequence> newPop) {
		for(NetworkSequence ns : newPop) {
			ns.setFitnessLvl(-1);
		}
	}
	
	/**
	 * Fills up a given population 
	 * @param sys reference to executing system
	 * @param nnComp Component which contains the neural network
	 */
	private void fillPopulation(GameSystem sys, NeuralNetworkComp nnComp) {
		NetworkSequence lastSeq = null;
		List<NetworkSequence> pop = nnComp.getPopulation();
		while(nnComp.getMaxPopSize() - nnComp.getPopulation().size() != 0) {
			if(!sys.isQuiet()) {
				System.out.println("[GeneticalSysNN::ProcessEvoAlgorithm] Create random candidate...");
			}
			lastSeq = generateRandomSequence(nnComp.getNetwork().getEdgeCount());
			pop.add(lastSeq);
		}
	}
	
	/**
	 * Generates a random NetworkSequence
	 * @param edges number of edges
	 * @return new random sequence
	 */
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

	/**
	 * Randomly mutates given sequences
	 * @param sys reference to executing system
	 * @param comb_gen array of source sequences
	 * @return mutated sequences
	 */
	private NetworkSequence[] mutate(GameSystem sys, NetworkSequence[] comb_gen) {
		double mutateChance = Double.valueOf(ConfigLoader.getInstance().getConfig("mutateChance", "0.1"));
		int mutationCount = 0;
		int sequenceNr = 1;
		for(NetworkSequence n : comb_gen) {
			mutationCount = 0;
			for(int i = 0; i < n.getSequence().toCharArray().length; i++) {
				if(Math.random() < mutateChance) {
					mutationCount++;
					if(n.getSequence().toCharArray()[i] == '0') {
						n.setSequence(n.getSequence().substring(0, i) + "1" + n.getSequence().substring(i+1, n.getSequence().toCharArray().length));
					} else {
						n.setSequence(n.getSequence().substring(0, i) + "0" + n.getSequence().substring(i+1, n.getSequence().toCharArray().length));
					}
				}
			}
			if(!sys.isQuiet()) {
				System.out.println("[GeneticalSysNN::ProcessEvoAlgorithm] " + mutationCount + " Mutations happened in sequence " + sequenceNr + "...");
			}
			sequenceNr++;
		}
		return comb_gen;
	}
	
	/**
	 * Breeding of two sequences in a given array
	 * @param eval_gen src array
	 * @return array of children
	 */
	private NetworkSequence[] breed(NetworkSequence[] eval_gen) {
		NetworkSequence n_seq_1 = new NetworkSequence("");
		NetworkSequence n_seq_2 = new NetworkSequence("");
		
		n_seq_1.setSequence(eval_gen[0].getSequence().substring(0, eval_gen[0].getSequence().length()/2) + 
						  eval_gen[1].getSequence().substring(eval_gen[1].getSequence().length()/2));
		n_seq_2.setSequence(eval_gen[1].getSequence().substring(0, eval_gen[1].getSequence().length()/2) + 
				  eval_gen[0].getSequence().substring(eval_gen[0].getSequence().length()/2));
		NetworkSequence[] c_ns = {n_seq_1, n_seq_2};
		return c_ns;
	}

	/**
	 * Evaluation of a given population
	 * @param system reference to running system
	 * @param nnComp component which contains the neural network
	 * @param pop given population
	 * @return null or a sorted array 
	 */
	private Object[] evaluate(GameSystem system, NeuralNetworkComp nnComp, List<NetworkSequence> pop) {
		NetworkSequence nnSeq = getUnratedSequence(pop);
		if(nnSeq != null) {
			nnSeq.setFitnessLvl(0);
			nnComp.getNetwork().setSequence(nnSeq);
			return null;
		}
		Object[] generation = pop.toArray();
		Arrays.sort(generation);
		return generation;
	}
}