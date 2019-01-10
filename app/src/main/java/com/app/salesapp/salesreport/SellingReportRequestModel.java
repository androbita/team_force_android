package com.app.salesapp.salesreport;

public class SellingReportRequestModel {
    private final String datetime;
    String token;
    String program_id;
    String users_organization_id;
    String selling_type_id;
    String program_product_id;

    public String getInvoice_date() {
        return invoice_date;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public String getUnique_code() {
        return unique_code;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getToken() {
        return token;
    }

    String invoice_number;
    String invoice_date;
    String quantity;
    String revenue;
    String unique_code;
    String picture;
    String remark;

    public SellingReportRequestModel(String token, String program_id, String users_organization_id, String selling_type_id, String program_product_id, String invoice_number, String invoice_date, String quantity, String revenue, String unique_code, String picture, String remark, String datetime) {
        this.token = token;
        this.program_id = program_id;
        this.users_organization_id = users_organization_id;
        this.selling_type_id = selling_type_id;
        this.program_product_id = program_product_id;
        this.invoice_number = invoice_number;
        this.invoice_date = invoice_date;
        this.quantity = quantity;
        this.revenue = revenue;
        this.unique_code = unique_code;
        this.picture = picture;
        this.remark = remark;
        this.datetime = datetime;
    }
}
