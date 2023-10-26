package com.github.pedromsilvapt.recollect;

import com.github.pedromsilvapt.recollect.Models.RecordingLine;

/**
 * Class used as a tuple to hold a term/token/word (of type string) and the subtitle line object that it belongs to
 */
public class TextSearchCursor {
    private String term;

    private RecordingLine line;

    /**
     * Initializes all values as null, since this class is meant to be instantiated once and then be reused
     */
    public TextSearchCursor() {
        this.term = null;
        this.line = null;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public RecordingLine getLine() {
        return line;
    }

    public void setLine(RecordingLine line) {
        this.line = line;
    }
}
