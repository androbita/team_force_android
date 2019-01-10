package com.app.salesapp.training.model;

/**
 * Created by soeltanzaki_r on 4/5/18.
 */

public class ModuleSelectedModel {
    public String training_module_id;
    public String name;
    public boolean isSelected;

    public ModuleSelectedModel(){}

    public ModuleSelectedModel(String trainingModuleId, String name, boolean isSelected) {
        this.training_module_id = trainingModuleId;
        this.name = name;
        this.isSelected = isSelected;
    }
}
