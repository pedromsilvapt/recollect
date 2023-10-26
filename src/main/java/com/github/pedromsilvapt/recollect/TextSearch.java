package com.github.pedromsilvapt.recollect;

import com.github.pedromsilvapt.recollect.Models.RecordingLine;
import com.github.pedromsilvapt.recollect.Models.Views.SearchResultMatch;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class TextSearch {
    private static final Pattern TOKENS_SEPARATOR = Pattern.compile("[\\s\\n\\r;:.!?,]+");

    private String[] searchTerms;

    public TextSearch(String text) {
        searchTerms = TOKENS_SEPARATOR.split(text.trim());
    }

    /**
     * Calculates a numeric score for a recording. Higher numbers mean closer matches
     * The score is calculated by first counting the number of matching terms. Terms are matches using the
     * Leventshtein distance algorithm, with a max distance cutoff of Q. Exact matches are counted towards the
     * final score as Q. Matches with 1 distance are calculated as Q - 1, and so on.
     * <p>
     * After all matches have been found, sequences of matches spatially close to each other are also calculated.
     * Each pair of matches within N < D tokens distance boosts the score by D - N.
     * <p>
     * The values of Q and D are configurable to adjust the results.
     *
     * @param lines
     * @param maxTermDifference How many differences between a term in the line and a search term can occur with the term
     *                          still being considered a match
     * @param maxTermDistance   How many unmatched terms in between two sequential matches can occur, before no score
     *                          boosting occurs
     * @return
     */
    public TextSearchResult calculateSearchResultScore(List<RecordingLine> lines, int maxTermDifference, int maxTermDistance) {
        int score = 0;
        int distanceToLastMatch = 0;

        // Create the list to hold the matches
        var matches = new ArrayList<SearchResultMatch>();
        RecordingLine lastMatchedLine = null;

        // Create an instance of the Levenshtein distance calculator, with the max threshold
        var levenshtein = new LevenshteinDistance(maxTermDifference);

        for (var cursor : tokenize(lines)) {
            // Returns an integer representing the distance or difference between the search terms and this term
            // A higher number means higher difference. Negative one (-1) means no match (difference exceeded the threshold)
            int termDifference = minDistance(levenshtein, cursor.getTerm());

            // If this term matches the search (is within the max term difference)
            if (termDifference > -1) {
                if (lastMatchedLine == null || lastMatchedLine != cursor.getLine()) {
                    // If the index difference between both line's indexes is greater than one, there is a gap between them
                    boolean hasGap = lastMatchedLine != null && cursor.getLine().getIndex() - lastMatchedLine.getIndex() > 1;

                    lastMatchedLine = cursor.getLine();

                    matches.add(new SearchResultMatch(
                            lastMatchedLine.getIndex() + 1,
                            Utilities.formatDuration(lastMatchedLine.getStartTime()),
                            Utilities.formatDuration(lastMatchedLine.getEndTime()),
                            lastMatchedLine.getText(),
                            hasGap
                    ));
                }

                // If this is not the first match, and the distance to the last match is within the max term distance,
                // boost the score
                if (score > 0 && distanceToLastMatch <= maxTermDistance) {
                    score += maxTermDistance - distanceToLastMatch + 1;
                }

                score += maxTermDifference - termDifference + 1;

                distanceToLastMatch = 0;
            } else {
                distanceToLastMatch += 1;
            }
        }

        return new TextSearchResult(score, matches);
    }

    public int minDistance(LevenshteinDistance levenshtein, String term) {
        int minDistance = -1;

        for (int i = 0; i < searchTerms.length; i++) {
            var termDistance = levenshtein.apply(searchTerms[i], term);

            if (termDistance != -1 && (minDistance == -1 || termDistance < minDistance)) {
                minDistance = termDistance;
            }
        }

        return minDistance;
    }

    /**
     * Returns an iterator that goes through each individual word inside the subtitle lines.
     * Each word is considered any sequence of characters separated by white spaces (spaces, tabs, new lines, etc...)
     * and/or punctuation (period, comma, semi-colon, colon, question mark, exclamation mark).
     *
     * @param lines
     * @return
     */
    public static Iterable<TextSearchCursor> tokenize(List<RecordingLine> lines) {
        // Return an anonymous iterator object to loop over each individual word in the list of subtitle lines
        return (Iterable<TextSearchCursor>) () -> {
            return new Iterator<TextSearchCursor>() {
                /**
                 * Index of the next subtitle line to tokenize. Whenever `lineTokens` is null, this index is used to
                 * split the next line, and is then increased,
                 */
                private int nextLineIndex = 0;

                /**
                 * Cached array of individually separated tokens. When null, or when the `nextTokenIndex` is greater
                 * than or equal to this array's length, the next line (identified by `nextLineIndex`) is divided and
                 * placed in here.
                 */
                private String[] lineTokens = null;

                /**
                 * Reusable object to be returned by the `next` method. To avoid heap allocations, we just mutate its
                 * internal state with the next token, instead of creating a whole new object
                 */
                private TextSearchCursor cursor = new TextSearchCursor();

                /**
                 * Index of the next token inside `lineTokens` to be returned by the iterator.
                 */
                private int nextTokenIndex = 0;

                @Override
                public boolean hasNext() {
                    // Check if the current line has any more tokens left
                    if (lineTokens != null && nextTokenIndex < lineTokens.length) {
                        return true;
                    }

                    // Check if there is a next line
                    while (lines.size() > nextLineIndex) {
                        // Skip over empty lines
                        if (Objects.equals(lines.get(nextLineIndex).getText(), "")) {
                            nextLineIndex++;
                            continue;
                        }

                        return true;
                    }

                    return false;
                }

                @Override
                public TextSearchCursor next() {
                    // Return the next token in the current line, if there is one
                    if (lineTokens != null && nextTokenIndex < lineTokens.length) {
                        cursor.setTerm(lineTokens[nextTokenIndex++]);
                        return cursor;
                    }

                    // Search for the next non-empty line
                    while (lines.size() > nextLineIndex) {
                        var text = lines.get(nextLineIndex).getText();
                        // Skip over empty lines
                        if (Objects.equals(text, "")) {
                            nextLineIndex++;
                            continue;
                        }

                        lineTokens = TOKENS_SEPARATOR.split(text);
                        // The next token will always be the second (index 1) since we will already return the first one
                        // right now
                        nextTokenIndex = 1;

                        cursor.setLine(lines.get(nextLineIndex));
                        cursor.setTerm(lineTokens[0]);

                        nextLineIndex++;

                        return cursor;
                    }

                    // Should never reach here, if the calling code always calls `hasNext()` first
                    return null;
                }
            };
        };
    }
}
