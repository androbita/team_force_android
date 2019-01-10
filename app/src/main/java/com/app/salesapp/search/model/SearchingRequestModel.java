package com.app.salesapp.search.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zcky on 3/11/18.
 */

public class SearchingRequestModel {

    @SerializedName("search_type")
    public String searchType;
    @SerializedName("search_value")
    public String searchValue;
    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;

    public SearchingRequestModel(String token, String programId, String searchType, String searchValue) {
        this.token = token;
        this.programId = programId;
        this.searchType = searchType;
        this.searchValue = searchValue;
    }
}
