package com.fastie4.testnazk.pojo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Declaration {
    @SerializedName("page")
    public Page page;

    @SerializedName("items")
    public List<Item> items = null;
}
