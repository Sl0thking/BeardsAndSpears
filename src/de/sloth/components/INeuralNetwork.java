package de.sloth.components;


public interface INeuralNetwork {

	public double processInput() throws Exception;
	public void setSequence(NetworkSequence nnSeq);
	public NetworkSequence getSequence();
	public int getEdgeCount();
	public void setInputOfNode(double input, String nodeId) throws Exception;

}
