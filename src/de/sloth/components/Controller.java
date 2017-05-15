package de.sloth.components;

import de.sloth.components.datatypes.*;

import java.util.List;
import java.util.Set;

public class Controller implements INeuralNetwork {

	private Graph graph;

	public Controller() {
	}

	public Graph createTestGraph() {
		graph = new Graph();
		graph.addInputNodes(14); //14
		graph.addHiddenNodes(45); //45
		graph.addOutputNodes(1);
		try {
			graph.buildHiddenLayers(2);
			graph.connectInputNodesToHiddenLayer();
			graph.connectHiddenNodesToOutputNodes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<Node> inputs = this.getGraph().getNodesOfType(NodeType.INPUT);
		for (Node node : inputs) {
			node.setValue(1);
		}
		/*
		for (Node node : this.getGraph().getNodes()) {
			System.out.println(node.toString());
		}
		System.out.println("-----------TEST----------------");
		//System.out.println(this.getGraph().toStringNodeType(NodeType.ALL, false));
		System.out.println("-------------------------------");

		PROCESSINPUT
		Set<Node> sigmoids = this.graph.getSigmoidNodes();
		for (Node node : sigmoids) {
			try {
				double sigmoidCalculation = this.getGraph().calculateInputsOfNode(node.getNodeId());
				((SigmoidNode) node).setValue(sigmoidCalculation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		return graph;
	}

	public Graph getGraph() {
		return graph;
	}

	@Override
	public double processInput() throws Exception {
		Set<Node> sigmoids = this.getGraph().getSigmoidNodes();
		for (Node node : sigmoids) {
		    //System.out.println(node.toString());
			try {
				double sigmoidCalculation = this.getGraph().calculateInputsOfNode(node.getNodeId());
				((SigmoidNode) node).setValue(sigmoidCalculation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.getGraph().getOutput().get(0);
	}

	@Override
	public void setSequence(NetworkSequence nnSeq) {
		Set<Edge> edges = this.getGraph().getEdges();
		String seq = nnSeq.getSequence();
		int i = 0;
		for (Edge edge : edges) {
			double edgeValue = (double) Integer.parseInt((String) seq.subSequence(i * 7, (i + 1) * 7), 2) / 127;
			edge.setValue(edgeValue);
			i++;
		}
	}

	@Override
	public NetworkSequence getSequence() {
		String seq = "";
		Set<Edge> edges = this.getGraph().getEdges();
		for (Edge edge : edges) {
			/*System.out.println(edge.getValue());
			System.out.println((int)(edge.getValue()*127));
			System.out.println(Integer.toBinaryString((int)(edge.getValue()*127)));*/
			seq += Integer.toBinaryString((int)(edge.getValue()*127));
		}
		NetworkSequence netSeq = new NetworkSequence(seq);
		return netSeq;
	}

	@Override
	public int getEdgeCount() {
		return this.getGraph().getEdges().size();
	}

	@Override
	public void setInputOfNode(double input, String nodeID) throws Exception {
		this.getGraph().getNode(nodeID).setValue(input);
	}

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.createTestGraph();
		System.out.println(controller.getEdgeCount());
		System.out.println("-------------ALL NODES--------------");
		System.out.println(controller.getGraph().toStringNodeType(NodeType.ALL, true));
		System.out.println("------------------------------------");
		try {
			double v = controller.processInput();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*System.out.println("---------------------------");
		for (Edge edge : controller.getGraph().getEdges()) {
			System.out.println(edge);
		}*/

		System.out.println("-----------SEQUENCE---------");
		NetworkSequence nnSeq = controller.getSequence();
		System.out.println(nnSeq);
		controller.setSequence(nnSeq);

		System.out.println("-----------TEST CALCULATION FOR SEQUENCE CALCULATION---------");
		String seq = "100000011111111111000";
		System.out.println("SEQUENCE: "+seq);
		int i = 0;
		System.out.println("-------------------------------------------------------------");

		System.out.println(seq.subSequence(i * 7, (i + 1) * 7));
		System.out.println(Integer.parseInt((String) seq.subSequence(i * 7, (i + 1) * 7), 2));
		double edgevalue = (double) Integer.parseInt((String) seq.subSequence(i * 7, (i + 1) * 7), 2) / 127;
		System.out.println(edgevalue);
		System.out.println(Integer.toBinaryString((int)(edgevalue*127)));
		System.out.println("_____");

		i++;
		System.out.println(seq.subSequence(i * 7, (i + 1) * 7));
		System.out.println(Integer.parseInt((String) seq.subSequence(i * 7, (i + 1) * 7), 2));
		edgevalue = (double) Integer.parseInt((String) seq.subSequence(i * 7, (i + 1) * 7), 2) / 127;
		System.out.println(edgevalue);
		System.out.println(Integer.toBinaryString((int)(edgevalue*127)));
		System.out.println("_____");

		i++;
		System.out.println(seq.subSequence(i * 7, (i + 1) * 7));
		System.out.println(Integer.parseInt((String) seq.subSequence(i * 7, (i + 1) * 7), 2));
		edgevalue = (double) Integer.parseInt((String) seq.subSequence(i * 7, (i + 1) * 7), 2) / 127;
		System.out.println(edgevalue);
		System.out.println(Integer.toBinaryString((int)(edgevalue*127)));
		System.out.println("-------------------------------------------------------------");
	}
}