package com.app.salesapp.training;

public class AudienceModel extends SalesModel{

    public  String token;
    public  String program_id;
    public  String organization_id;
    public  String full_name;
    public  String email;
    public  String mobile_phone;
    public  String position;
    public boolean isSelected;

    public AudienceModel() {
        
    }

    public AudienceModel(String token, String program_id, String organization_id, String full_name, String email, String mobile_phone, String position, boolean isSelected) {
        this.token = token;
        this.program_id = program_id;
        this.organization_id = organization_id;
        this.full_name = full_name;
        this.email = email;
        this.mobile_phone = mobile_phone;
        this.position = position;
        this.isSelected = isSelected;
    }
}
