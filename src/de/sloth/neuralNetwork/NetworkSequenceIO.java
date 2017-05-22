package de.sloth.neuralNetwork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.sloth.neuralNetwork.component.datatype.NetworkSequence;

/**
 * Collection of persistence functions for NetworkSequences.
 * 
 * @author Kevin Jolitz
 * @version 1.0.0
 * @date 18.05.2017
 *
 */
public abstract class NetworkSequenceIO {
	
	private static File archiveFile;
	
	/**
	 * Returns ArchiveFile
	 * @return File
	 */
	public static File getArchiveFile() {
		return NetworkSequenceIO.archiveFile;
	}

	/**
	 * Saves a snapshot of given population.
	 * 
	 * @param pop Population
	 * @throws IOException
	 */
	public static void savePopulationSnapshot(List<NetworkSequence> pop) throws IOException {
		NetworkSequenceIO.clearDir(NetworkSequenceIO.archiveFile + "\\teached_population");
		NetworkSequenceIO.saveSequences(NetworkSequenceIO.archiveFile + "\\teached_population", pop);
	}
	
	/**
	 * Prepare a learn archive for network sequences.
	 * 
	 * @param archivePath Path to archive
	 * @param learnArchiveID ID of archive
	 * @return archiveFile
	 */
	public static File prepareLearnArchive(String archivePath, int learnArchiveID) {
		File archiveFile = new File(archivePath + "\\learn_archive_" + String.valueOf(learnArchiveID));
		NetworkSequenceIO.archiveFile = archiveFile;
		if(!archiveFile.exists()) {
			archiveFile.mkdir();
			new File(archiveFile.getAbsolutePath() + "\\teached_population").mkdir();
			new File(archiveFile.getAbsolutePath() + "\\replay").mkdir();
		}
		return archiveFile;
	}
	
	/**
	 * Save given NetworkSequence.
	 * 
	 * @param path Path to target dir.
	 * @param filename Name of file
	 * @param sequence NetworkSequence to save
	 * @throws IOException
	 */
	public static void saveSequence(String path, String filename, NetworkSequence sequence) throws IOException {
		File targetNetworkFile = new File(path + File.separator + filename);
		targetNetworkFile.createNewFile();
		BufferedWriter bw =  new BufferedWriter(new FileWriter(targetNetworkFile));
		bw.write(sequence.getSequence());
		bw.flush();
		bw.close();
	}
	
	/**
	 * Load NetworkSequence of name
	 * @param path Path to saved sequence
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static NetworkSequence loadSequence(String path, String filename) throws IOException {
		File srcNNFile = new File(path + File.separator + filename);
		BufferedReader br = new BufferedReader(new FileReader(srcNNFile));
		NetworkSequence ns = new NetworkSequence("");
		ns.setSequence(br.readLine());
		br.close();
		return ns;
	}
	
	/**
	 * Load all sequences in a given directory.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
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
	
	/**
	 * Save a sequence.
	 * 
	 * @param path
	 * @param nseqs
	 * @throws IOException
	 */
	public static void saveSequences(String path, List<NetworkSequence> nseqs) throws IOException {
		int fid = 0;
		for(NetworkSequence ns : nseqs) {
			saveSequence(path, "ns_f" + ns.getFitnessLvl() + "_" + fid + ".nsq", ns);
			fid++;
		}
	}
	
	/**
	 * Deletes all sequences of a given directory.
	 * 
	 * @param path
	 */
	public static void clearDir(String path) {
		File[] files = (new File(path)).listFiles();
		for(File f : files) {
			if(!f.isDirectory() && f.getName().endsWith(".nsq")) {
				f.delete();
			}
		}
	}
	
	/**
	 * Returns the first network sequence in replay directory 
	 * @return
	 * @throws IOException
	 */
	public static NetworkSequence loadReplay() throws IOException {
		File replayDir = new File(archiveFile.getAbsolutePath() + File.separator + "replay");
		File[] replays = replayDir.listFiles();
		NetworkSequence ns = null;
		if(replays.length > 0) {
			ns = NetworkSequenceIO.loadSequence(replayDir.getAbsolutePath(), replays[0].getName());
		}
		return ns;
	}
}