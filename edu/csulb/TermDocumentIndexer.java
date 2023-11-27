package edu.csulb;

import cecs429.documents.DirectoryCorpus;
import cecs429.documents.Document;
import cecs429.documents.DocumentCorpus;
import cecs429.indexing.Index;
import cecs429.indexing.InvertedIndex;
import cecs429.indexing.Posting;
import cecs429.indexing.TermDocumentIndex;
import cecs429.text.BasicTokenProcessor;
import cecs429.text.EnglishTokenStream;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class TermDocumentIndexer {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		System.out.println("Enter a directory to retrieve files from: ");
		String path = input.next();

		System.out.println("Enter file type: ");
		String fileType = input.next();

		// Create a DocumentCorpus to load documents from the inputted project directory.
		DocumentCorpus corpus = DirectoryCorpus.loadTextDirectory(Paths.get(path).toAbsolutePath(), fileType);

		// Index the documents of the corpus.
		Index index = indexCorpus(corpus);
		while (true)
		{
			System.out.println("\n1. Search for Term\n2. Quit");
			System.out.println("Select an option:");

			// Input validator.
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

			// Break out of loop if 2 is inputted.
			if (choice == 2)
			{
				System.out.println("Goodbye!");
				break;
			}

			System.out.println("Enter a Term: ");
			String query = input.next();

			System.out.println("\nSearch Results:");

			List<Posting> indexPostings = index.getPostings(query);

			// Check if postings list is empty or not.
			if (indexPostings.isEmpty())
			{
				// Term was not found.
				System.out.println("No Results Found!");
			}
			else
			{
				// Print out document file names where term was found.
				for (Posting p : indexPostings)
				{
					System.out.println("Document " + corpus.getDocument(p.getDocumentId()).getTitle());
				}
			}
		}
	}
	
	private static Index indexCorpus(DocumentCorpus corpus) {
		BasicTokenProcessor processor = new BasicTokenProcessor();

		// Create a vocabulary x corpus sized matrix
		InvertedIndex invertIndexer = new InvertedIndex();

		for (Document d: corpus.getDocuments())
		{
			System.out.println("Found document " + d.getTitle());

			// Tokenize the text
			EnglishTokenStream tokenStream = new EnglishTokenStream(d.getContent());

			for(String token: tokenStream.getTokens())
			{
				// Process the token
				String term = processor.processToken(token);

				// Insert term into the invertedIndex.
				invertIndexer.addTerm(term, d.getId());
			}
		}

		return invertIndexer;
	}
}
