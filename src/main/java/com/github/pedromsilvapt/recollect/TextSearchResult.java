package com.github.pedromsilvapt.recollect;

import com.github.pedromsilvapt.recollect.Models.Views.SearchResultMatch;

import java.util.ArrayList;
import java.util.List;

public class TextSearchResult {
    private int score;

    private List<SearchResultMatch> matches;

    public TextSearchResult() {
        this.score = 0;
        this.matches = new ArrayList<>();
    }

    public TextSearchResult(int score, List<SearchResultMatch> matches) {
        this.score = score;
        this.matches = matches;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<SearchResultMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<SearchResultMatch> matches) {
        this.matches = matches;
    }
}
