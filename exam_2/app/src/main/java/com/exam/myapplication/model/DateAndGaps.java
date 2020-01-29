package com.exam.myapplication.model;

import java.util.List;

public class DateAndGaps {
    private String date;
    private List<String> gaps;

    public DateAndGaps(String date, List<String> gaps) {
        this.date = date;
        this.gaps = gaps;
    }

    public String getDate() {
        return date;
    }

    public List<String> getGaps() {
        return gaps;
    }
}
