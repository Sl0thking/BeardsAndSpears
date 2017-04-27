package de.sloth.core;

import de.sloth.components.NetworkSequence;

public interface INeuralNetwork {

	public double processInput();
	public void setSequence(NetworkSequence nnSeq);
	public NetworkSequence getSequence();
	public int getEdgeCount();

}
