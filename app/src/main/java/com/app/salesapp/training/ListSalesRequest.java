package com.app.salesapp.training;

public class ListSalesRequest {
    public String token;
    public String program_id;
    public String organization_id;

    public ListSalesRequest(String token, String program_id, String organization_id) {
        this.token = token;
        this.program_id = program_id;
        this.organization_id = organization_id;
    }
}

