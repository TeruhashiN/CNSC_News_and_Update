package com.bee.cnscnewsandupdate;

import com.google.firebase.firestore.Exclude;

public class DocId {

    @Exclude
    public String docid;

    public <T extends DocId> T withId(final String docid) {
        this.docid = docid;
        return (T) this;
    }
}

