package de.sloth.components.datatypes;

public class Node implements Comparable<Node>{
	
	private String nodeId;
	private NodeType nodeType;
	private double value;

	public Node(String nodeId, NodeType nodeType){
		this.nodeId = nodeId;
		this.nodeType = nodeType;
		this.value = 0;
	}
    public Node(String nodeId){
        this.nodeId = nodeId;
        this.nodeType = null;
        this.value = 0;
    }

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	@Override
	public String toString() {
		return "Node ["+this.getClass().getSimpleName()+" nodeId=" + nodeId + ", nodeType=" + nodeType + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		return result;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (nodeId != null ? !nodeId.equals(node.nodeId) : node.nodeId != null) return false;
        return nodeType == node.nodeType;
    }

    @Override
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
