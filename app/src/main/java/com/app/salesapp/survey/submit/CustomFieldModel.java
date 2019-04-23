package com.app.salesapp.survey.submit;

import java.io.Serializable;
import java.util.List;

public class CustomFieldModel implements Serializable {
    private List<String> customfieldid;
    private List<String> fieldvalue;
    private String programid;
    private String token;
    private String photo;

    public List<String> getCustomfieldid() {
        return customfieldid;
    }

    public void setCustomfieldid(List<String> customfieldid) {
        this.customfieldid = customfieldid;
    }

    public List<String> getFieldvalue() {
        return fieldvalue;
    }

    public void setFieldvalue(List<String> fieldvalue) {
        this.fieldvalue = fieldvalue;
    }

    public String getProgramid() {
        return programid;
    }

    public void setProgramid(String programid) {
        this.programid = programid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
