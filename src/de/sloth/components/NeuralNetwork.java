package de.sloth.components;

import de.sloth.components.datatypes.*;
import de.sloth.system.game.core.ConfigLoader;

import java.util.Set;

public class NeuralNetwork implements INeuralNetwork {

	private Graph graph;
	private NetworkSequence nnSeq;

	public NeuralNetwork() {
		this.createTestGraph();
	}

	public Graph createTestGraph() {
		graph = new Graph();
		int maxEnemies = Integer.valueOf(ConfigLoader.getInstance().getConfig("maxEnemies", "6"));
		int maxSpears = Integer.valueOf(ConfigLoader.getInstance().getConfig("maxSpears", "18"));
		int inputNodes = 1 + maxEnemies + maxSpears;
		graph.addInputNodes(inputNodes); //14
		graph.addHiddenNodes(inputNodes*2); //45
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
		Set<Node> sigmoids = this.graph.getSigmoidNodes();
		for (Node node : sigmoids) {
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
		this.nnSeq = nnSeq;
		Set<Edge> edges = this.getGraph().getEdges();
		String seq = nnSeq.getSequence();
		int i = 0;
		for (Edge edge : edges) {
            String subseq = (String) seq.subSequence(i * 8, (i + 1) * 8);
            //System.out.println("SUB: " + subseq);
            boolean positive = true;
            if(subseq.charAt(0)=='0'){
                positive = false;
            }
            String calcseq = (String) subseq.subSequence(1,8);
            //System.out.println("CALC: "+calcseq);
            //System.out.println("INT: "+Integer.parseInt(calcseq, 2));
            double edgeValue = (double) Integer.parseInt(calcseq, 2) / 127;
            if (!positive){
                edgeValue = -1*edgeValue;
            }
            System.out.println("EV: " + edgeValue);
            edge.setValue(edgeValue);
            i++;

		    /*------------------
            System.out.println((double) Integer.parseInt((String) seq.subSequence(i * 8, (i + 1) * 8), 2));
			double edgeValue = (((double) Integer.parseInt((String) seq.subSequence(i * 8, (i + 1) * 8), 2) - 127) / 254);
			edge.setValue(edgeValue);
			i++;*/
		}
	}

	@Override
	public NetworkSequence getSequence() {
		return nnSeq;
	}

	@Override
	public int getEdgeCount() {
		return this.getGraph().getEdges().size();
	}

	@Override
	public void setInputOfNode(double input, String nodeID) throws Exception {
		this.getGraph().getNode(nodeID).setValue(input);
	}

	/*public static void main(String[] args) {
		NeuralNetwork controller = new NeuralNetwork();
		controller.createTestGraph();
		System.out.println(controller.getEdgeCount());
		System.out.println("-------------ALL NODES--------------");
		System.out.println(controller.getGraph().toStringNodeType(NodeType.ALL, true));
		try {
			double v = controller.processInput();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("---------------------------");
		for (Edge edge : controller.getGraph().getEdges()) {
			System.out.println(edge);
		}


		System.out.println("-----------SEQUENCE---------");
		NetworkSequence nnSeq = controller.getSequence();
		System.out.println(nnSeq);
		controller.setSequence(nnSeq);

		System.out.println("-----------TEST CALCULATION FOR SEQUENCE CALCULATION---------");
		String seq = "100000000111111101111000";
		System.out.println("SEQUENCE: "+seq);
		int i = 0;
		System.out.println("-------------------------------------------------------------");

		i = controller.calc(i, seq);
        i = controller.calc(i, seq);
		i = controller.calc(i, seq);
        String subseq = (String) seq.subSequence(i * 8, (i + 1) * 8);
        System.out.println("SUB: "+subseq);
		boolean positive = true;
		if(subseq.charAt(0)=='1'){
		    positive = true;
        } else {
		    positive = false;
        }
        String calcseq = (String) subseq.subSequence(1,8);
        System.out.println("CALC: "+calcseq);
        System.out.println(Integer.parseInt(calcseq, 2));
        edgevalue = (double) Integer.parseInt(calcseq, 2) / 127;
        System.out.println("PRE+-: "+edgevalue);
        if (!positive){
            edgevalue = -1*edgevalue;
        }
        /*System.out.println("POST+-: "+edgevalue);
        System.out.println(Integer.toBinaryString((int)(edgevalue*127)));
        System.out.println("_____");
		i++;
		System.out.println("-------------------------------------------------------------");
	}

	private int calc(int i, String seq){
        String subseq = (String) seq.subSequence(i * 8, (i + 1) * 8);
        System.out.println("SUB: "+subseq);
        boolean positive = true;
        if(subseq.charAt(0)=='1'){
            positive = true;
        } else {
            positive = false;
        }
        String calcseq = (String) subseq.subSequence(1,8);
        System.out.println("CALC: "+calcseq);
        System.out.println("INT: "+Integer.parseInt(calcseq, 2));
        double edgevalue = (double) Integer.parseInt(calcseq, 2) / 127;
        System.out.println("PRE+-: "+edgevalue);
        if (!positive){
            edgevalue = -1*edgevalue;
        }
        System.out.println("POST+-: "+edgevalue);
        /*if(edgevalue<0){
            edgevalue=edgevalue*-1;
        }
        System.out.println(Integer.toBinaryString((int)(edgevalue*127)));
        System.out.println("_____");
        i++;
        return i;
    }*/
}