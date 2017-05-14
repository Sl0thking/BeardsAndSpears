package de.sloth.components.datatypes;

public class Edge implements Comparable<Edge>{
	
	private String edgeId;
	private Node startNode;
	private Node endNode;
	private double value;
	private EdgeType edgeType;

	public Edge(String edgeId, Node startNode, Node endNode, double value){
		this.edgeId = edgeId;
		this.startNode = startNode;
		this.endNode = endNode;
		this.value = value;
		this.edgeType = EdgeType.NONE;
	}
	
	public Edge(String edgeId, Node startNode, Node endNode, double value, EdgeType edgeType){
		this.edgeId = edgeId;
		this.startNode = startNode;
		this.endNode = endNode;
		this.value = value;
		this.edgeType = edgeType;
	}

	public EdgeType getEdgeType() {
		return edgeType;
	}

	public void setEdgeType(EdgeType edgeType) {
		this.edgeType = edgeType;
	}

	public String getEdgeId() {
		return edgeId;
	}

	public void setEdgeId(String edgeId) {
		this.edgeId = edgeId;
	}

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "   Edge [edgeId=" + edgeId +", "+ "edgeType=" + edgeType +", value=" + value+ ","
				+ "\n\t startNode=" + startNode + ",\n\t endNode=" + endNode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
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
