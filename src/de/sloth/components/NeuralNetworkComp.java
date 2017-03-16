package de.sloth.components;

import java.util.ArrayList;
import java.util.List;

import de.sloth.component.Component;
import de.sloth.core.INeuralNetwork;

public class NeuralNetworkComp extends Component {
	private int generations;
	private INeuralNetwork network;
	private List<NetworkSequence> population;
	private int maxPopSize;
	
 
	public NeuralNetworkComp(int generations, INeuralNetwork network, int maxPopSize) {
		super();
		this.generations = generations;
		this.network = network;
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
}