package de.sloth.neuralNetwork.component.datatype;

/**
 * Dummy for a neural network
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 22.05.2017
 *
 */
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

	@Override
	public void setInputOfNode(double input, String nodeId) throws Exception {}
}