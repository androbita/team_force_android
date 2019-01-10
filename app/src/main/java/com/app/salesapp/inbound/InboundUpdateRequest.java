package com.app.salesapp.inbound;

public class InboundUpdateRequest {
    String token;
    String program_id;
    String outbound_id;
    String received_quantity;
    String return_quantity;
    String remark;
    String picture;


    public InboundUpdateRequest(String token, String program_id, String outbound_id, String received_quantity, String return_quantity, String remark, String picture) {
        this.token = token;
        this.program_id = program_id;
        this.outbound_id = outbound_id;
        this.received_quantity = received_quantity;
        this.return_quantity = return_quantity;
        this.remark = remark;
        this.picture = picture;
    }
}
