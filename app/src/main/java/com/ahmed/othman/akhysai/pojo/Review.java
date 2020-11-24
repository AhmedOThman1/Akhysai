package com.ahmed.othman.akhysai.pojo;

public class Review {

    private String review_id;
    private String reviewBody;
    private float rate;
    private long date;
    private String reviewWriterName;

    public Review(String reviewBody, float rate, long date, String reviewWriterName) {
        this.reviewBody = reviewBody;
        this.rate = rate;
        this.date = date;
        this.reviewWriterName = reviewWriterName;
    }

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getReviewWriterName() {
        return reviewWriterName;
    }

    public void setReviewWriterName(String reviewWriterName) {
        this.reviewWriterName = reviewWriterName;
    }
}
