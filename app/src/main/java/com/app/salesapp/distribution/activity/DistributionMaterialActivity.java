package com.app.salesapp.distribution.activity;

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

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.distribution.activity.adapter.DistributionMaterialRecyclerAdapter;
import com.app.salesapp.distribution.createdistribution.CreateDistributionActivity;
import com.app.salesapp.distribution.model.MaterialSelected;
import com.app.salesapp.distribution.model.ReceivedResponse;
import com.app.salesapp.training.model.ModuleSelectedModel;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DistributionMaterialActivity extends BaseActivity {

    private ArrayList<MaterialSelected> material;
    private EditText searchMaterial;
    private DistributionMaterialRecyclerAdapter materialRecyclerAdapter;
    @Inject
    protected SalesAppService salesAppService;
    @Inject
    protected UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);
        setContentView(R.layout.activity_material2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Select Material");
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        searchMaterial = (EditText) findViewById(R.id.searchMaterial);
        searchMaterial.setHint("Select Material here...");

        material = new ArrayList<>();
        getMaterialSelectedList();
        materialRecyclerAdapter = new DistributionMaterialRecyclerAdapter(getApplicationContext(), material, userService);
        RecyclerView listAudienceRecycler = (RecyclerView) findViewById(R.id.list_audience);
        listAudienceRecycler.setLayoutManager(new LinearLayoutManager(this));
        listAudienceRecycler.setAdapter(materialRecyclerAdapter);

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

    void getMaterialSelectedList(){
        material = userService.getMaterialSelected();
    }

    void filter(String text){
        ArrayList<MaterialSelected> temp = new ArrayList();
        for(MaterialSelected d: material){
            if(d.materialName.toLowerCase().contains(text)){
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
        return true;
    }
}
