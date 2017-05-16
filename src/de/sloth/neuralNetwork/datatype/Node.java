package de.sloth.neuralNetwork.datatype;

public class Node implements Comparable<Node>{
	
	private String nodeId;
	private NodeType nodeType;
	private double value;

    /**
     * Initilize Node
     * @param nodeId - id of the node as String - n_#
     * @param nodeType - type of the Node as {@link NodeType}
     */
	public Node(String nodeId, NodeType nodeType){
		this.nodeId = nodeId;
		this.nodeType = nodeType;
		this.value = 0;
		if (nodeType == NodeType.INPUT){
			this.value = 1;
		}
	}

    /**
     * Initilize Node
     * @param nodeId - id of the node as String - n_#
     */
    public Node(String nodeId){
        this.nodeId = nodeId;
        this.nodeType = null;
        this.value = 0;
    }

    /**
     * Returns the value of the node.
     * @return double
     */
	public double getValue() {
		return value;
	}

    /**
     * Sets the value of the node.
     * @param value double
     */
	public void setValue(double value) {
		this.value = value;
	}

    /**
     * Returns the nodeId.
     * @return String
     */
	public String getNodeId() {
		return nodeId;
	}

    /**
     * Sets the nodeId.
     * @param nodeId String
     */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

    /**
     * Retruns the {@link NodeType}
     * @return {@link NodeType}
     */
	public NodeType getNodeType() {
		return nodeType;
	}

    /**
     * Sets the {@link NodeType}
     * @param nodeType
     */
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	@Override
    /**
     * Create a stringrepresentation of the Node.
     * @return String
     */
	public String toString() {
		return "Node ["+this.getClass().getSimpleName()+" nodeId=" + nodeId + ", nodeType=" + nodeType + ", value=" + value + "]";
	}

	@Override
    /**
     * Create a hash of the node.
     * @return int
     */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		return result;
	}

    @Override
    /**
     * Check the equality of this and the given object.
     * @return boolean
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (nodeId != null ? !nodeId.equals(node.nodeId) : node.nodeId != null) return false;
        return nodeType == node.nodeType;
    }

    @Override
    /**
     * Compare this Node to the given Node.
     */
	public int compareTo(Node o) {
		int ownId = Integer.valueOf(this.getNodeId().split("_")[1]);
		int otherId = Integer.valueOf(o.getNodeId().split("_")[1]);
		if (ownId<otherId) {
			return -1;
		} else if (ownId==otherId) {
			return 0;
		}
		return 1;
	}
	
}
