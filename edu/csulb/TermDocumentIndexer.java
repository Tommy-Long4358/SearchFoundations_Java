package edu.csulb;

import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.indexing.Index;
import cecs429.indexing.Posting;
import cecs429.indexing.TermDocumentIndex;
import cecs429.text.BasicTokenProcessor;
import cecs429.text.EnglishTokenStream;

import java.nio.file.Paths;
import java.util.HashSet;

public class TermDocumentIndexer {
	public static void main(String[] args) {
		// Create a DocumentCorpus to load .txt documents from the project directory.
		DocumentCorpus corpus = DirectoryCorpus.loadTextDirectory(Paths.get("MobyDick10Chapters").toAbsolutePath(), ".txt");

		// Index the documents of the corpus.
		Index index = indexCorpus(corpus) ;

		// We aren't ready to use a full query parser; for now, we'll only support single-term queries.
		String query = "whale"; // hard-coded search for "whale"

		System.out.println("Search Results:");

		for (Posting p : index.getPostings(query)) {
			System.out.println("Document " + corpus.getDocument(p.getDocumentId()).getTitle());
		}

		// TODO: fix this application so the user is asked for a term to search.
	}
	
	private static Index indexCorpus(DocumentCorpus corpus) {
		HashSet<String> vocabulary = new HashSet<>();
		BasicTokenProcessor processor = new BasicTokenProcessor();
		
		// First, build the vocabulary hash set.
		for (Document d : corpus.getDocuments()) {
			System.out.println("Found document " + d.getTitle());

			EnglishTokenStream tokenStream = new EnglishTokenStream(d.getContent());

			for(String token: tokenStream.getTokens())
			{
				String term = processor.processToken(token);
				vocabulary.add(term);
			}
		}

		TermDocumentIndex tdi = new TermDocumentIndex(vocabulary, corpus.getCorpusSize());

		for(Document d: corpus.getDocuments())
		{
			EnglishTokenStream tokenStream = new EnglishTokenStream(d.getContent());

			for(String token: tokenStream.getTokens())
			{
				String term = processor.processToken(token);
				tdi.addTerm(term, d.getId());
			}
		}
		return tdi;
	}
}
