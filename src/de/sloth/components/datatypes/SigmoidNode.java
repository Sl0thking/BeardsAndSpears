package de.sloth.components.datatypes;

public class SigmoidNode extends Node {
	
	private double sigmoidValue;

	public SigmoidNode(String nodeId, NodeType nodeType) {
		super(nodeId, nodeType);
	}

	public double calculateSigmoid(double input, double anzahl) {
		return (1/( 1 + Math.pow(Math.E,(-(anzahl/3)*((input-(anzahl/2))/anzahl)))));
	}

}
