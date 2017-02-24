package de.sloth.components;

import de.sloth.component.Component;

public class NeuralNetworkComp extends Component {
	private int iterations;

	public NeuralNetworkComp(int iterations) {
		super();
		this.iterations = iterations;
	}

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
}