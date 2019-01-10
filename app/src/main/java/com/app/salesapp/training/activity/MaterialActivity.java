package com.app.salesapp.training.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.training.AudienceModel;
import com.app.salesapp.training.AudienceRecyclerAdapter;
import com.app.salesapp.training.CreateTrainingActivity;
import com.app.salesapp.training.MaterialRecyclerAdapter;
import com.app.salesapp.training.model.ModuleSelectedModel;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import javax.inject.Inject;

public class MaterialActivity extends BaseActivity {
    private ArrayList<ModuleSelectedModel> module;
    private MaterialRecyclerAdapter materialRecyclerAdapter;
    private EditText searchMaterial;
    private TextView tableColumnName;

    @Inject
    public UserService userService;
    @Inject
    public SalesAppService salesAppService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        module = userService.getModuleTraining();
        materialRecyclerAdapter = new MaterialRecyclerAdapter(getApplicationContext(), module);
        RecyclerView listAudienceRecycler = (RecyclerView) findViewById(R.id.list_audience);
        listAudienceRecycler.setLayoutManager(new LinearLayoutManager(this));
        listAudienceRecycler.setAdapter(materialRecyclerAdapter);

        searchMaterial = (EditText) findViewById(R.id.searchAudiance);
        searchMaterial.setHint("Select Material here...");
        tableColumnName = (TextView) findViewById(R.id.titleName);
        tableColumnName.setText("Material Name");

        searchMaterial.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    void filter(String text){
        ArrayList<ModuleSelectedModel> temp = new ArrayList();
        for(ModuleSelectedModel d: module){
            if(d.name.toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        if(text == null || text.equalsIgnoreCase("")){
            materialRecyclerAdapter.search(temp);
        }else{
            materialRecyclerAdapter.search(temp);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        userService.setModuleTraining(module);
        return true;
    }
}
