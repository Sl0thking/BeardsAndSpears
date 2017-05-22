package de.sloth.neuralNetwork.component.datatype;

/**
 * Interface for a neural network implementation.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 22.05.2017
 *
 */
public interface INeuralNetwork {
	public double processInput() throws Exception;
	public void setSequence(NetworkSequence nnSeq);
	public NetworkSequence getSequence();
	public int getEdgeCount();
	public void setInputOfNode(double input, String nodeId) throws Exception;
}