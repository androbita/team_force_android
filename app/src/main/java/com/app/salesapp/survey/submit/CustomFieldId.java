package com.app.salesapp.survey.submit;

import java.io.Serializable;
import java.util.HashMap;

class CustomFieldId implements Serializable {
    private HashMap <String, String> hashMap;

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }
}
