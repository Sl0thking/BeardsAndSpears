package de.sloth.components.datatypes;

import java.util.*;

public class Graph {
	
	private TreeMap<Node, ArrayList<Edge>> nodeMap;
	private TreeSet<Edge> edgeSet;
	private TreeMap<String, ArrayList<Node>> hiddenLayer;

	public Graph(){
		this.nodeMap = new TreeMap<Node, ArrayList<Edge>>();
		this.edgeSet = new TreeSet<Edge>();
		this.hiddenLayer = new TreeMap<String, ArrayList<Node>>();
	}
	
	public void addNode(String nodeId, NodeType nodeType){
		Node node = new Node(nodeId, nodeType);
		this.addNodeToMap(node);
	}
	
	public void addSigmoidNode(String nodeId, NodeType nodeType){
		SigmoidNode node = new SigmoidNode(nodeId, nodeType);
		this.addNodeToMap(node);
	}

	private void addNodeToMap(Node node) {
		if(!this.nodeMap.containsKey(node)){
			this.nodeMap.put(node, new ArrayList<Edge>());
		}
	}
	
	public void addEdge(String start_node_id, String end_node_id, double value){
		Node startNode = null;
		Node endNode = null;
		for (Node node : this.nodeMap.keySet()) {
			if (node.getNodeId().equals(start_node_id)){
				startNode = node;
			}
			if (node.getNodeId().equals(end_node_id)){
				endNode = node;
			}
		}
		if (!this.edgeExist(startNode, endNode)){
			String edgeId = "e_"+(this.edgeSet.size()+1);
			Edge forwardEdge = new Edge(edgeId, startNode, endNode, value, EdgeType.FORWARD);
			this.edgeSet.add(forwardEdge);
			this.nodeMap.get(startNode).add(forwardEdge);
			edgeId = "e_"+(this.edgeSet.size()+1);
			Edge backwardEdge = new Edge(edgeId, endNode, startNode, value, EdgeType.BACKWARD);
			this.edgeSet.add(backwardEdge);
			this.nodeMap.get(endNode).add(backwardEdge);
		}
	}

	private boolean edgeExist(Node start_node, Node end_node) {
		List<Edge> start_edges = this.getEdgesOfNode(start_node.getNodeId());
		List<Edge> end_edges = this.getEdgesOfNode(end_node.getNodeId());
		
		boolean start_to_end_exists = false;
		boolean end_to_start_exists = false;
		
		for (Edge edge : start_edges) {
			if (edge.getStartNode() == start_node && edge.getEndNode() == end_node){
				start_to_end_exists = true;
			}
		}
		for (Edge edge : end_edges) {
			if (edge.getStartNode() == end_node && edge.getEndNode() == start_node) {
				end_to_start_exists = true;
			}
		}
        return start_to_end_exists && end_to_start_exists;
	}
	
	public void modifyEdgeValue(String start_node_id, String end_node_id, double value) {
		for (List<Edge> edgeList : this.nodeMap.values()) {
			for (Edge edge : edgeList) {
				if (edge.getStartNode().getNodeId() == start_node_id 
						&& edge.getEndNode().getNodeId() == end_node_id) {
					edge.setValue(value);
				}
			}
		}
	}
	
	public Node getNode(String nodeId) throws Exception {
	    Node node = new Node(nodeId);
		try {
		    if (this.getNodes().contains(node)){
		        Iterator<Node> itNode = this.getNodes().iterator();
		        while (itNode.hasNext()){
		            Node nowNode = itNode.next();
		            if(nowNode.getNodeId().equals(nodeId)){
		                return nowNode;
                    }
                }
            } else {
                throw new Exception("Node ["+nodeId+"] Not Found!");
            }
		} catch (Exception ex){
			throw new Exception("Node ["+nodeId+"] Not Found!");
		}
		return node;
	}
	
	public Set<Node> getNodes() {
		return this.getNodeMap().keySet();
	}

	public TreeMap getNodeMap(){
		return this.nodeMap;
	}
	
	public double getSumOfEdges() {
		double edge_sum = 0;
		for (Edge edge : this.getEdges()) {
			edge_sum += edge.getValue();
		}
		return edge_sum;
	}

	public Set<Edge> getEdges() {
		return this.edgeSet;
		/*ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Node node : this.getNodes()) {
			for (Edge edge : this.nodeMap.get(node)) {
				edges.add(edge);
			}
		}
		edges.sort(Edge::compareTo);
		return edges;*/
	}

	private List<Edge> getEdgesOfNode(String nodeId) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Node node : this.getNodes()) {
			if (node.getNodeId().equals(nodeId)) {
				for (Edge edge : this.nodeMap.get(node)) {
					edges.add(edge);
				}
			}
		}
		return edges;
	}
	
	private List<Edge> getInputEdgesOfNode(String nodeId) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Edge edge : this.getEdges()) {
			if(edge.getEndNode().getNodeId().equals(nodeId)){
				edges.add(edge);
			}
		}
		return edges;
	}

	@Override
	public String toString() {
		String str = "";
		for (Node node : this.getNodes()) {
			str += node.toString()+"\n";
			for (Edge edge : this.getEdgesOfNode(node.getNodeId())) {
				str += "  "+edge.getEdgeId()+" - "+edge.getEndNode().toString()+"\n";
			}
		}
		return str;
	}

	public String toStringNodeType(NodeType nodeType, boolean onlyForward){
		String str = "";
		for (Node node : this.getNodes()) {
			if (nodeType == NodeType.ALL) {
				str += node.toString()+"\n";
				/*for (Edge edge : getEdgesOfNode(node.getNodeId())) {
					str += getForwordEdge(edge, onlyForward);
				}*/
			} else if (node.getNodeType() == nodeType) {			
				str += node.toString()+"\n";
				/*for (Edge edge : getEdgesOfNode(node.getNodeId())) {
					str += getForwordEdge(edge, onlyForward);
				}*/
			}
		}
		return str;
	}

	private String getForwordEdge(Edge edge, boolean onlyForward) {
		if (onlyForward){
			if (edge.getEdgeType() == EdgeType.FORWARD) {							
				return edge.toString()+"\n";
			}
			return "";
		}
		return edge.toString()+"\n";
	}

	public void addInputNodes(int nodeCount) {
		for (int i = 0; i < nodeCount; i++) {
			this.addNode("n_"+(this.nodeMap.size()+1), NodeType.INPUT);
		}
	}
	
	public void addHiddenNodes(int nodeCount) {
		for (int i = 0; i < nodeCount; i++) {
			this.addSigmoidNode("n_"+(this.nodeMap.size()+1), NodeType.HIDDEN);
		}
	}
	public void addOutputNodes(int nodeCount) {
		for (int i = 0; i < nodeCount; i++) {
			this.addSigmoidNode("n_"+(this.nodeMap.size()+1), NodeType.OUTPUT);
		}
	}
	
	public Set<Node> getNodesOfType(NodeType nodeType){
		TreeSet<Node> nodes = new TreeSet<Node>();
		for (Node node : this.getNodes()) {
			if (node.getNodeType() == nodeType) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public void connectInputNodesToHiddenLayer() {
		for (Node node : this.getNodesOfType(NodeType.INPUT)) {
			for (Node othernode : hiddenLayer.firstEntry().getValue()) {
				this.addEdge(node.getNodeId(), othernode.getNodeId(), 1);
			}
		}
	}
	
	public void connectHiddenNodesToOutputNodes() {
		for (Node othernode : this.getNodesOfType(NodeType.OUTPUT)) {
			for (Node node : hiddenLayer.lastEntry().getValue()) {
				this.addEdge(node.getNodeId(), othernode.getNodeId(), 1);
			}
		}
	}
	
	public void buildHiddenLayers(int layerSize) throws Exception {
		int layerCount = (int) Math.ceil((double) getNodesOfType(NodeType.HIDDEN).size()/layerSize);
		Iterator<Node> tempNodes = getNodesOfType(NodeType.HIDDEN).iterator();
		int cur_layer = 1;
		while (tempNodes.hasNext()){
			ArrayList<Node> singleLayer = new ArrayList<Node>();
			for (int j = 1; j <= layerCount; j++) {
				if (tempNodes.hasNext()) {
					singleLayer.add(tempNodes.next());
				}
			}
			//System.out.println("PUT: layer_"+cur_layer+" "+singleLayer);
			hiddenLayer.put("layer_"+cur_layer, singleLayer);
			cur_layer++;
		}
		connectHiddenLayer();
		
	}

	private void connectHiddenLayer() throws Exception {
		Iterator<String> tempLayer = hiddenLayer.keySet().iterator();
		String cur_layer = null;
		ArrayList<Node> tempNodes = null;
		if (tempLayer.hasNext()) {
			cur_layer = tempLayer.next();
			tempNodes = hiddenLayer.get(cur_layer);
		} else {
			throw new Exception("No hiddenlayer");
		}
		for (int i = 1; i < hiddenLayer.keySet().size(); i++) {
			if (tempLayer.hasNext()) {
				cur_layer = tempLayer.next();
				ArrayList<Node> nextNodes = hiddenLayer.get(cur_layer);
				for (Node node : tempNodes){
					for (Node nextNode : nextNodes) {
						addEdge(node.getNodeId(), nextNode.getNodeId(), 1);
					}
				}
				tempNodes = hiddenLayer.get(cur_layer);
			}
		}
	}
	
	/**
	 * 
	 * @param nodeId
	 * @return sum of edges or 0 if the node have no inputs
	 * @throws Exception
	 */
	public double calculateInputsOfNode(String nodeId) throws Exception{
		//System.out.println("LOOK FOR INPUTS OF NODE: "+nodeId);
		List<Edge> edges = this.getInputEdgesOfNode(nodeId);
		double edgeSum = 0;
		for (Edge edge : edges) {
			if (edge.getEdgeType() == EdgeType.FORWARD) {
				//System.out.println(" "+edge.toString());
				edgeSum += edge.getStartNode().getValue()*edge.getValue();
			}
		}
		if (getNode(nodeId).getClass().equals(SigmoidNode.class)){
			return (double) ((SigmoidNode) getNode(nodeId)).calculateSigmoid(edgeSum);
		} else {
		    System.out.println("################## HALP");
        }
		return 0;
	}
	
	public List<Double> getOutput() throws Exception {
		Set<Node> nodes = getNodesOfType(NodeType.OUTPUT);
		Iterator<Node> itNodes = nodes.iterator();
		ArrayList<Double> output = new ArrayList<Double>();
		while (itNodes.hasNext()) {
			output.add(itNodes.next().getValue());
		}
		return output;
	}

	public Set<Node> getSigmoidNodes() {
		Set<Node> nodes = this.getNodesOfType(NodeType.HIDDEN);
		nodes.addAll(this.getNodesOfType(NodeType.OUTPUT));
		return nodes;
	}
	
}
