package de.sloth.components;

public class NetworkSequence implements Comparable<NetworkSequence>{
	private String sequence;
	private int fitnessLvl;
	
	public NetworkSequence(String sequence) {
		super();
		this.sequence = sequence;
		this.fitnessLvl = -1;
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
		return "NetworkSequence [fitnessLvl=" + fitnessLvl + "]";
	}
	@Override
	public int compareTo(NetworkSequence nSeq) {
		return new Integer(this.getFitnessLvl()).compareTo(new Integer(nSeq.getFitnessLvl()));
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fitnessLvl;
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.sequence.equals(((NetworkSequence) obj).getSequence()));
	}
	
	
	
	
}