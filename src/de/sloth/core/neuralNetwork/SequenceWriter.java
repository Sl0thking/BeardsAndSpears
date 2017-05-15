package de.sloth.core.neuralNetwork;

import java.util.ArrayList;
import java.util.List;

import de.sloth.components.NetworkSequence;

public abstract class SequenceWriter {
	
	public static void writeSequence(NetworkSequence nSeq, String path) {
		
	}
	
	public static List<NetworkSequence> loadSequences(String path) {
		return new ArrayList<NetworkSequence>();
	}
}
