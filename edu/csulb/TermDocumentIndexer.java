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
import java.util.Scanner;

public class TermDocumentIndexer {
	public static void main(String[] args) {
		// Create a DocumentCorpus to load .txt documents from the project directory.
		DocumentCorpus corpus = DirectoryCorpus.loadTextDirectory(Paths.get("MobyDick10Chapters").toAbsolutePath(), ".txt");

		// Index the documents of the corpus.
		Index index = indexCorpus(corpus) ;

		Scanner input = new Scanner(System.in);
		while (true)
		{
			System.out.println("\n1. Search for Term\n2. Quit");
			System.out.println("Select an option:");

			int choice = 0;
			boolean valid = false;
			while (!valid)
			{
				if (input.hasNextInt())
				{
					choice = input.nextInt();
					if (choice == 1 || choice == 2)
					{
						valid = true;
					}
					else
					{
						System.out.println( "Invalid Range." );
					}
				}
				else
				{
					input.next(); //clear invalid string
					System.out.println( "Invalid Input." );
				}
			}

			if (choice == 2)
			{
				System.out.println("Goodbye!");
				break;
			}

			System.out.println("Enter a Term: ");
			String query = input.next();

			System.out.println("\nSearch Results:");

			for (Posting p : index.getPostings(query)) {
				System.out.println("Document " + corpus.getDocument(p.getDocumentId()).getTitle());
			}
		}
	}
	
	private static Index indexCorpus(DocumentCorpus corpus) {
		HashSet<String> vocabulary = new HashSet<>();
		BasicTokenProcessor processor = new BasicTokenProcessor();
		
		// First, build the vocabulary hash set.
		for (Document d : corpus.getDocuments()) {
			System.out.println("Found document " + d.getTitle());

			// Tokenize text
			EnglishTokenStream tokenStream = new EnglishTokenStream(d.getContent());

			for(String token: tokenStream.getTokens())
			{
				// Process token
				String term = processor.processToken(token);

				// Add processed token to HashSet of vocabulary
				vocabulary.add(term);
			}
		}

		// Create a vocabulary x corpus sized matrix
		TermDocumentIndex tdi = new TermDocumentIndex(vocabulary, corpus.getCorpusSize());

		for(Document d: corpus.getDocuments())
		{
			// Tokenize the text
			EnglishTokenStream tokenStream = new EnglishTokenStream(d.getContent());

			for(String token: tokenStream.getTokens())
			{
				// Process the token
				String term = processor.processToken(token);

				// Put a "1" in the matrix for that document ID and term
				tdi.addTerm(term, d.getId());
			}
		}
		return tdi;
	}
}
