package de.sloth.components;

public class NetworkSequence implements Comparable<NetworkSequence>{
	private String sequence;
	private int fitnessLvl;
	
	public NetworkSequence(String sequence) {
		super();
		this.sequence = sequence;
		this.fitnessLvl = 0;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public int getFitnessLvl() {
		return fitnessLvl;
	}
	public void setFitnessLvl(int fitnessLvl) {
		this.fitnessLvl = fitnessLvl;
	}
	@Override
	public String toString() {
		return "NetworkSequence [sequence=" + sequence + ", fitnessLvl=" + fitnessLvl + "]";
	}
	@Override
	public int compareTo(NetworkSequence nSeq) {
		return new Integer(this.getFitnessLvl()).compareTo(new Integer(nSeq.getFitnessLvl()));
	}
	
	
}