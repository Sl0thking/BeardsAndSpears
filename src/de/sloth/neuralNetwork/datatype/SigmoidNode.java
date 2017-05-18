package de.sloth.neuralNetwork.datatype;

public class SigmoidNode extends Node {
	
	private double sigmoidValue;

	/**
	 * Initialize the SigmoidNode.
	 * @param nodeId - nodeIt as a String
	 * @param nodeType - type of the Node as Nodetype
	 */
	public SigmoidNode(String nodeId, NodeType nodeType) {
		super(nodeId, nodeType);
	}

    /**
     * Calculate the value of node by using the sigmoid function.
     * @param input - input value of all edges combined
     * @param edgecount - count of inputedges of teh node
     * @return double
     */
	public double calculateSigmoid(double input, double edgecount) {
		return (1/( 1 + Math.pow(Math.E,(-1*input))));
		//return (1/( 1 + Math.pow(Math.E,(-(anzahl/3)*((input-(anzahl/2))/anzahl)))));
	}

}
