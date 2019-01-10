package com.app.salesapp.training;

import java.util.ArrayList;
import java.util.List;

public class PostTrainingRequest {
    public String token;
    public String program_id;
    public String training_type;
    public String venue;
    public List<SalesModel> audience_name = new ArrayList<>();
    public List<String> module_name = new ArrayList<>();
    public String total_audience;
    public String remark;
    public String picture;
}
