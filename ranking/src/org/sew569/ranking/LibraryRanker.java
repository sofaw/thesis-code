package org.sew569.ranking;

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

	public LibraryRanker() { // Require default constructor for plugin to work
	}
	
	public void setFilesToIndex(List<String> docs) {
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

            FileInputStream inputStream = 
                new FileInputStream(path);

            int nRead = 0;
            while((nRead = inputStream.read(buffer)) != -1) {
            	String bufferAsString = new String(buffer);
                result = result + bufferAsString;
                buffer = new byte[1000];
            }   

            inputStream.close();        
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                path + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + path + "'");                  
        }
		
		return result;
    }

	private void addTermToIndex(String term) throws Exception {
		if(docIds == null) {
			throw new Exception("Need to set files to index first");
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
		if(docIds == null) {
			throw new Exception("Need to set files to index first");
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
		if(docIds == null) {
			throw new Exception("Need to set files to index first");
		}
		return n;
	}

	private int getDocFrequency(String term) throws Exception {
		if(docIds == null) {
			throw new Exception("Need to set files to index first");
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
			if(tf == 0) {
				return 0;
			}
			n = getNumDocs();
			df = getDocFrequency(term);
		} catch(Exception e) {
			throw new Exception("The index has not been initialised correctly");
		}
		
		return (1 + Math.log(tf)) * Math.log(n / df);
	}
}
