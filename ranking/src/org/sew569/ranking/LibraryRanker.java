package org.sew569.ranking;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class LibraryRanker {
	private int n;
	private BiMap<String, Integer> docIds;
	private Set<String> indexedTerms;
	private Map<String, ArrayList<Integer>> termIndex; // Map each term to pairs
														// of <docIndex,
														// termFreq>
	private List<String> headerFiles;
	private boolean initialised = false;

	public LibraryRanker() { // Require default constructor for plugin to work
	}

	public List<String> getHeaderFiles() throws Exception {
		if(!initialised) {
			throw new Exception("Index must be initialised.");
		}
		return headerFiles;
	}
	
	public void init(String librariesRoot) {
		// get all header files from libraries' root directory and init the index
		headerFiles = findHeaderFiles(librariesRoot);
		setFilesToIndex(headerFiles);
		initialised = true;
	}

	private List<String> findHeaderFiles(String librariesRoot) {
		// check not null
		// add all *.h files to result
		// dfs all subdirs and add to result
		// return result

		File rootDir = new File(librariesRoot);

		List<String> headerFiles = new ArrayList<String>();
		for (String l : rootDir.list()) {
			File fileOrDir = new File(rootDir.getAbsolutePath() + "/" + l);
			String path = fileOrDir.getAbsolutePath();

			if (fileOrDir.isFile()) { // If header file, add to result
				String fileName = fileOrDir.getName();
				int i = fileName.lastIndexOf('.');

				if (i > 0) {
					String extension = fileName.substring(i + 1);
					if (extension.equals("h") && !(path.contains("extras") || path.contains("utility"))) {
						headerFiles.add(path);
					}
				}
			} else { // Dfs dir and add to results
				headerFiles.addAll(findHeaderFiles(path));
			}
		}

		return headerFiles;
	}

	private void setFilesToIndex(List<String> docs) {
		// Map docs to ids to reduce space in termIndex
		docIds = HashBiMap.create();
		for (int i = 0; i < docs.size(); i++) {
			docIds.put(docs.get(i), i);
		}

		n = docs.size();

		indexedTerms = new HashSet<String>();

		termIndex = new HashMap<String, ArrayList<Integer>>();
	}

	private String readFile(String path) {
		String result = "";
		try {
			byte[] buffer = new byte[1000];

			FileInputStream inputStream = new FileInputStream(path);

			int nRead = 0;
			while ((nRead = inputStream.read(buffer)) != -1) {
				String bufferAsString = new String(buffer);
				result = result + bufferAsString;
				buffer = new byte[1000];
			}

			inputStream.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + path + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + path + "'");
		}

		return result;
	}

	private void addTermToIndex(String term) throws Exception {
		if (!initialised) {
			throw new Exception("Index must be initialised.");
		}
		ArrayList<Integer> termFreqs = new ArrayList<Integer>(n);

		// For each doc, calculate the term frequency and add to the term index
		for (int i = 0; i < n; i++) {
			String doc = docIds.inverse().get(i);
			String docStr = readFile(doc);
			int termFreq = StringUtils.countMatches(docStr.toLowerCase(), term.toLowerCase());
			termFreqs.add(i, termFreq);
		}
		termIndex.put(term.toLowerCase(), termFreqs);
		indexedTerms.add(term.toLowerCase());
	}

	private int getTermFrequency(String term, String doc) throws Exception {
		if (!initialised) {
			throw new Exception("Index must be initialised.");
		}
		if (!indexedTerms.contains(term.toLowerCase())) {
			addTermToIndex(term);
		}

		// Note that docId corresponds to position in list of relevant term
		// frequency
		ArrayList<Integer> termFreqs = termIndex.get(term.toLowerCase());
		int docId = docIds.get(doc);
		return termFreqs.get(docId);
	}

	private int getNumDocs() throws Exception {
		if (!initialised) {
			throw new Exception("Index must be initialised.");
		}
		return n;
	}

	private int getDocFrequency(String term) throws Exception {
		if (!initialised) {
			throw new Exception("Index must be initialised.");
		}
		if (!indexedTerms.contains(term.toLowerCase())) {
			addTermToIndex(term);
		}

		ArrayList<Integer> termFreqs = termIndex.get(term.toLowerCase());
		int docFreq = 0;
		for (int i : termFreqs) {
			if (i != 0) {
				docFreq++;
			}
		}
		return docFreq;
	}

	public double calculateTfIdfWeight(String term, String doc) throws Exception {
		double tf;
		double n;
		double df;
		try {
			tf = getTermFrequency(term, doc);
			if (tf == 0) {
				return 0;
			}
			n = getNumDocs();
			df = getDocFrequency(term);
		} catch (Exception e) {
			throw new Exception("The index has not been initialised correctly");
		}

		return (1 + Math.log(tf)) * Math.log(n / df);
	}
}
