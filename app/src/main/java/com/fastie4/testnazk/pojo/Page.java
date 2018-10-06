package com.fastie4.testnazk.pojo;
import com.google.gson.annotations.SerializedName;

public class Page {
    @SerializedName("batchSize")
    public int batchSize;

    @SerializedName("totalItems")
    public int totalItems;
}
