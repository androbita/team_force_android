package com.app.salesapp.draft;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import javax.inject.Inject;

public class DraftActivity extends BaseActivity implements MyDraftRecyclerAdapter.OnDraftListener{

    @Inject
    public UserService userService;

    MyDraftRecyclerAdapter adapter;
    RecyclerView listDraft;
    ArrayList<DraftModel> draftModelArrayList;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

       /* ImageView logo = (ImageView) findViewById(R.id.app_logo);
        Glide.with(getApplicationContext()).load(userService.getUserLogo())
                .error(R.drawable.sales_club_logo)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(logo);
        */
        draftModelArrayList = userService.getListDraft();
        adapter = new MyDraftRecyclerAdapter(getApplicationContext(),draftModelArrayList);
        adapter.setListener(this);
        listDraft = (RecyclerView)findViewById(R.id.listDraft);
        listDraft.setLayoutManager(new LinearLayoutManager(this));
        listDraft.setAdapter(adapter);
        dialog = new ProgressDialog(DraftActivity.this);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);

    }

    @Override
    public void showLoading(boolean b) {
        if(b)
            dialog.show();
            else
            dialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSuccessPost(String response) {
        AlertDialog dialog = new AlertDialog.Builder(DraftActivity.this)
                .setTitle("Post Draft")
                .setMessage("Success, "+response)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(draftModelArrayList.size()==0){
                            finish();
                        }
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void onErrorPost(String message) {
        AlertDialog dialog = new AlertDialog.Builder(DraftActivity.this)
                .setTitle("Post Draft")
                .setMessage("Error, "+message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void onRemoveDraft(int position) {
        draftModelArrayList.remove(position);
        adapter.notifyDataSetChanged();
        userService.saveDraftListAfterRemoved(draftModelArrayList);
    }
}
