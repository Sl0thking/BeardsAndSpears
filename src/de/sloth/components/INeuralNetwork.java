package de.sloth.components;


public interface INeuralNetwork {

	public double processInput();
	public void setSequence(NetworkSequence nnSeq);
	public NetworkSequence getSequence();
	public int getEdgeCount();

}