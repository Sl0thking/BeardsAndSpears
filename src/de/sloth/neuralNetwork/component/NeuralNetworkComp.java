package de.sloth.neuralNetwork.component;

import java.util.ArrayList;
import java.util.List;

import de.sloth.component.Component;
import de.sloth.neuralNetwork.component.datatype.INeuralNetwork;
import de.sloth.neuralNetwork.component.datatype.NetworkSequence;

/**
 * Component which contains the neural network and settings for it
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 22.05.2017
 *
 */
public class NeuralNetworkComp extends Component {
	private int generations;
	private int currGen;
	private INeuralNetwork network;
	private List<NetworkSequence> population;
	private int maxPopSize;
	private int sizeOfElite;
	
 
	public NeuralNetworkComp(int generations, INeuralNetwork network, int maxPopSize, int sizeOfElite) {
		super();
		this.generations = generations;
		this.currGen = 0;
		this.network = network;
		this.setSizeOfElite(sizeOfElite);
		this.setPopulation(new ArrayList<NetworkSequence>());
		this.setMaxPopSize(maxPopSize);
	}

	public int getGenerations() {
		return generations;
	}

	public void setGenerations(int generations) {
		this.generations = generations;
	}

	public INeuralNetwork getNetwork() {
		return network;
	}

	public void setNetwork(INeuralNetwork network) {
		this.network = network;
	}
	
	public int getMaxPopSize() {
		return maxPopSize;
	}

	public void setMaxPopSize(int maxPopSize) {
		this.maxPopSize = maxPopSize;
	}

	public List<NetworkSequence> getPopulation() {
		return population;
	}

	public void setPopulation(List<NetworkSequence> list) {
		this.population = list;
	}
	
	public void addPopulation(NetworkSequence sequence) {
		this.population.add(sequence);
	}

	public int getCurrGen() {
		return currGen;
	}

	public void setCurrGen(int currGen) {
		this.currGen = currGen;
	}

	public int getSizeOfElite() {
		return sizeOfElite;
	}

	public void setSizeOfElite(int sizeOfElite) {
		this.sizeOfElite = sizeOfElite;
	}
}