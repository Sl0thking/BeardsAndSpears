package de.sloth.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.sloth.components.NetworkSequence;

public abstract class NetworkSequenceIO {
	public static void saveSequence(String path, String filename, NetworkSequence sequence) throws IOException {
		File targetNetworkFile = new File(path + File.separator + filename);
		targetNetworkFile.createNewFile();
		BufferedWriter bw =  new BufferedWriter(new FileWriter(targetNetworkFile));
		bw.write(sequence.getSequence());
		bw.flush();
		bw.newLine();
		bw.write(String.valueOf(sequence.getFitnessLvl()));
		bw.flush();
		bw.close();
	}
	
	public static NetworkSequence loadSequence(String path, String filename) throws IOException {
		File srcNNFile = new File(path + File.separator + filename);
		BufferedReader br = new BufferedReader(new FileReader(srcNNFile));
		NetworkSequence ns = new NetworkSequence("");
		ns.setSequence(br.readLine());
		ns.setFitnessLvl(Integer.parseInt(br.readLine()));
		br.close();
		return ns;
	}
	
	public static List<NetworkSequence> loadSequences(String path) throws IOException {
		List<NetworkSequence> nseqs = new LinkedList<NetworkSequence>();
		File[] files = (new File(path)).listFiles();
		for(File f : files) {
			if(!f.isDirectory() && f.getName().endsWith(".nsq")) {
				nseqs.add(loadSequence(path, f.getName()));
			}
		}
		return nseqs;
	}
	
	public static void saveSequences(String path, List<NetworkSequence> nseqs) throws IOException {
		int fid = 0;
		for(NetworkSequence ns : nseqs) {
			System.out.println(ns.toString());
			saveSequence(path, "ns" + fid + ".nsq", ns);
			fid++;
		}
	}
	
	public static void clearDir(String path) {
		File[] files = (new File(path)).listFiles();
		for(File f : files) {
			if(!f.isDirectory() && f.getName().endsWith(".nsq")) {
				f.delete();
			}
		}
	}
}
