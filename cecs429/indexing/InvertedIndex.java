package cecs429.indexing;

import java.util.*;

/**
 * Implements an Index using a term-document matrix. Requires knowing the full corpus vocabulary and number of documents
 * prior to construction.
 */
public class InvertedIndex implements Index
{
    private final HashMap<String, List<Posting>> termsMap;

    /**
     * Constructs an empty index with given vocabulary set and corpus size.
     */
    public InvertedIndex()
    {
        termsMap = new HashMap<String, List<Posting>>();
    }

    /**
     * Associates the given documentId with the given term in the index.
     * Format is: {Posting(ID1), Posting(ID2),...Posting(IDk)}
     */
    public void addTerm(String term, int documentId)
    {
        // TODO: Implement addTerm method.

        // Check if term doesn't exist in HashMap.
        if (!termsMap.containsKey(term))
        {
            // Initialize list.
            List<Posting> postingList = new ArrayList<Posting>();

            // Add documentId to list. We know that it's a distinct documentId since the term didn't exist prior.
            postingList.add(new Posting(documentId));

            // Create a key-value pair for the term.
            termsMap.put(term, postingList);
        }
        else
        {
            // Term does exist. We check if the documentId is already in the list of postings.

            // Get postings list of given term.
            List<Posting> postingList = termsMap.get(term);

            // Check if documentId is not in list of postings.
            if (documentId != postingList.get(postingList.size() - 1).getDocumentId())
            {
                // Add document ID to postings list.
                postingList.add(new Posting(documentId));

                // Replace postings list in HashMap with new postings list.
                termsMap.put(term, postingList);
            }
        }
    }

    @Override
    public List<Posting> getPostings(String term)
    {
        // Do an O(1) lookup to find the string in the dictionary.
        // Return the list associated with the string.
        if (termsMap.get(term) == null)
        {
            return new ArrayList<Posting>();
        }

        return termsMap.get(term);
    }

    public List<String> getVocabulary() {
        return Collections.unmodifiableList(new ArrayList<String>(termsMap.keySet()));
    }
}

