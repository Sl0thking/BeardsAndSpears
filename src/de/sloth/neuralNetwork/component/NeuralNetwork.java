package de.sloth.neuralNetwork.component;

import de.sloth.neuralNetwork.datatype.*;
import de.sloth.system.game.core.ConfigLoader;

import java.util.Set;

public class NeuralNetwork implements INeuralNetwork {

	private Graph graph;
	private NetworkSequence nnSeq;

    /**
     * Initialize the NeuralNetwork an create the Graph-object.
     */
	public NeuralNetwork() {
		this.createTestGraph();
	}

    /**
     * Create the Graph of the NeuralNetwork.
     * Initialize the Notes of each Layer and build the meshed graph.
     *
     * @return {@link Graph}
     */
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
		return graph;
	}

    /**
     * Returns the Graph.
     * @return {@link Graph}
     */
	public Graph getGraph() {
		return graph;
	}

	@Override
    /**
     * Runs the NeuralNetwork once.
     * Call {@code calculateInputsOfNode(nodeId)} for each {@link SigmoidNode} of the graph.
     *
     * @return int - output of the graph.
     */
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
    /**
     * Sets the sequence.
     * Calculate the bit-array to an integer between -127 und 127 and set the {@code edgeValue}.
     *
     * @param nnSeq {@link NeuralNetwork}
     */
	public void setSequence(NetworkSequence nnSeq) {
		this.nnSeq = nnSeq;
		Set<Edge> edges = this.getGraph().getEdges();
		String seq = nnSeq.getSequence();
		int i = 0;
		for (Edge edge : edges) {
            String subseq = (String) seq.subSequence(i * 8, (i + 1) * 8);
            boolean positive = true;
            if(subseq.charAt(0)=='0'){
                positive = false;
            }
            String calcseq = (String) subseq.subSequence(1,8);
            double edgeValue = (double) Integer.parseInt(calcseq, 2) / 127;
            if (!positive){
                edgeValue = -1*edgeValue;
            }
            edge.setValue(edgeValue);
            i++;
		}
	}

	@Override
    /**
     * @deprecated
     */
	public NetworkSequence getSequence() {
		return nnSeq;
	}

	@Override
    /**
     * Returns the count of the edges.
     *
     * @return int - count of edges
     */
	public int getEdgeCount() {
		return this.getGraph().getEdges().size();
	}

	@Override
    /**
     * Sets the input of the node.
     *
     * @param nodeId - the nodeId as String
     * @param input - value to set for the node
     */
	public void setInputOfNode(double input, String nodeID) throws Exception {
		this.getGraph().getNode(nodeID).setValue(input);
	}
}