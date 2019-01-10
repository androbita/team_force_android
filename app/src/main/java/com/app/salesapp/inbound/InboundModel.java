package com.app.salesapp.inbound;

import java.util.List;

public class InboundModel {

    public class OutboundList {
        public String outbound_id;
        public String material_name;
        public String quantity;
        public String date;
        public String status;
        public String description;
    }

    public int total_page;
    public List<OutboundList> outbound_list;

}
