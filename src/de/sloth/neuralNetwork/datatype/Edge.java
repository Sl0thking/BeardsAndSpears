package de.sloth.neuralNetwork.datatype;

public class Edge implements Comparable<Edge>{
	
	private String edgeId;
	private Node startNode;
	private Node endNode;
	private double value;
	private EdgeType edgeType;

	/**
	 * Initialize the edge.
	 * @param edgeId - as a String e.g. e_#
	 * @param startNode - {@link Node}
	 * @param endNode - {@link Node}
	 * @param value - value of the edge
	 */
	public Edge(String edgeId, Node startNode, Node endNode, double value){
		this.edgeId = edgeId;
		this.startNode = startNode;
		this.endNode = endNode;
		this.value = value;
		this.edgeType = EdgeType.NONE;
	}

    /**
     * Initialize the edge.
     * @param edgeId - as a String e.g. e_#
     * @param startNode - {@link Node}
     * @param endNode - {@link Node}
     * @param value - value of the edge
     * @param edgeType - type of the Edge as {@link EdgeType}
     */
	public Edge(String edgeId, Node startNode, Node endNode, double value, EdgeType edgeType){
		this.edgeId = edgeId;
		this.startNode = startNode;
		this.endNode = endNode;
		this.value = value;
		this.edgeType = edgeType;
	}

    /**
     * Returns the {@link EdgeType}
     * @return {@link EdgeType}
     */
	public EdgeType getEdgeType() {
		return edgeType;
	}

    /**
     * Sets the {@link EdgeType}
     * @param edgeType
     */
	public void setEdgeType(EdgeType edgeType) {
		this.edgeType = edgeType;
	}

    /**
     * Returns the edgeId.
     * @return edgeId as String
     */
	public String getEdgeId() {
		return edgeId;
	}

    /**
     * Sets the edgeID.
     * @param edgeId - as a String e.g. e_#
     */
	public void setEdgeId(String edgeId) {
		this.edgeId = edgeId;
	}

    /**
     * Returns the startNode.
     * @return {@link Node}
     */
	public Node getStartNode() {
		return startNode;
	}

    /**
     * Sets the startNode.
     * @param startNode - {@link Node}
     */
	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

    /**
     * Returns the endNode.
     * @return {@link Node}
     */
	public Node getEndNode() {
		return endNode;
	}

    /**
     * Sets the endNode.
     * @param endNode - {@link Node}
     */
	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

    /**
     * Returns the value of the Edge.
     * @return value as double
     */
	public double getValue() {
		return value;
	}

    /**
     * Sets the value of the Edge.
     * @param value - value of the Edge as double
     */
	public void setValue(double value) {
		this.value = value;
	}

	@Override
    /**
     * Returns the Edge as a Stringrepresentaion.
     * @return String
     */
	public String toString() {
		return "   Edge [edgeId=" + edgeId +", "+ "edgeType=" + edgeType +", value=" + value+ ","
				+ "\n\t startNode=" + startNode + ",\n\t endNode=" + endNode + "]";
	}

	@Override
    /**
     * Creates a hashcode of the edge.
     * @return int
     */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
    /**
     * Checks the equality of this Edge and the given object.
     * @return boolean
     */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (edgeId == null) {
			if (other.edgeId != null)
				return false;
		} else if (!edgeId.equals(other.edgeId))
			return false;
		if (edgeType != other.edgeType)
			return false;
		if (endNode == null) {
			if (other.endNode != null)
				return false;
		} else if (!endNode.equals(other.endNode))
			return false;
		if (startNode == null) {
			if (other.startNode != null)
				return false;
		} else if (!startNode.equals(other.startNode))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return true;
	}

	@Override
    /**
     * Compare this Edge to the given Edge.
     */
	public int compareTo(Edge o) {
		int ownId = Integer.valueOf(this.getEdgeId().split("_")[1]);
		int otherId = Integer.valueOf(o.getEdgeId().split("_")[1]);
		if (ownId<otherId) {
			return -1;
		} else if (ownId==otherId) {
			return 0;
		}
		return 1;
	}

}
