package com.bee.cnscnewsandupdate;

public class DataClass {

    private String dataTitle;
    private String dataDesc;
    private String dataDate;
    private String dataImage;

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataDate() {
        return dataDate;
    }

    public String getDataImage() {
        return dataImage;
    }

    public DataClass(String dataTitle, String dataDesc, String dataDate, String dataImage) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataDate = dataDate;
        this.dataImage = dataImage;
    }

    public DataClass() {

    }


}
