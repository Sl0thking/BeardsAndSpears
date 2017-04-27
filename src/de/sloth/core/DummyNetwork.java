package de.sloth.core;

import de.sloth.components.NetworkSequence;

public class DummyNetwork implements INeuralNetwork {

	private NetworkSequence nSeq;
	
	@Override
	public double processInput() {
		return Math.random();
	}

	@Override
	public void setSequence(NetworkSequence nSeq) {
		this.nSeq = nSeq;
	}

	@Override
	public NetworkSequence getSequence() {
		return this.nSeq;
	}

	@Override
	public int getEdgeCount() {
		return 8;
	}
}