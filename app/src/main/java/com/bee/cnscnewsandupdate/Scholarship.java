package com.bee.cnscnewsandupdate;

import java.util.Date;
import java.util.List;

public class Scholarship extends DocId {
    private String title, createdBy;
    private int award, awarded, budget, payment;
    private Date createdAt, deadline;
    private List<String> applicants, accepted, declined, released;

    public Scholarship() {
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public List<String> getReleased() {
        return released;
    }

    public void setReleased(List<String> released) {
        this.released = released;
    }

    public List<String> getAccepted() {
        return accepted;
    }

    public void setAccepted(List<String> accepted) {
        this.accepted = accepted;
    }

    public List<String> getDeclined() {
        return declined;
    }

    public void setDeclined(List<String> declined) {
        this.declined = declined;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getAward() {
        return award;
    }

    public void setAward(int award) {
        this.award = award;
    }

    public int getAwarded() {
        return awarded;
    }

    public void setAwarded(int awarded) {
        this.awarded = awarded;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<String> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<String> applicants) {
        this.applicants = applicants;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
