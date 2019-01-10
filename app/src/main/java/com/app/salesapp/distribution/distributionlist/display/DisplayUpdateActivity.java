package com.app.salesapp.distribution.distributionlist.display;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.databinding.ActivityDisplayUpdateBinding;
import com.app.salesapp.distribution.model.DistributionPostResponseModel;
import com.app.salesapp.distribution.model.DistributionResponse;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindorks.paracamera.Camera;

import java.util.List;

import javax.inject.Inject;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class DisplayUpdateActivity extends BaseActivity implements DisplayUpdateView {

    @Inject
    SalesAppService salesAppService;
    @Inject
    UserService userService;

    private String photos = "";
    private Camera camera;
    private ActivityDisplayUpdateBinding binding;
    private DisplayUpdatePresenter presenter;
    private DisplayUpdateViewModel viewModel;
    private ListChannelResponseModel.ChannelList selectedChannel;
    private List<DistributionPostResponseModel.DistributionList> materialData;
    private String[] trainingItem;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        viewModel = new DisplayUpdateViewModel();
        presenter = new DisplayUpdatePresenter(this, userService, salesAppService, viewModel);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_display_update);
        binding.setPresenter(presenter);
        binding.setViewModel(viewModel);

        setSupportActionBar(binding.layoutToolbarUpdate.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null));
        TextView title = binding.layoutToolbarUpdate.textView2;
        title.setText("Maintenance");

        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("Teamforce")
                .setName("Teamforce_Display_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000).build(this);

        selectedChannel = userService.getPostAttendanceChannel();
        if(selectedChannel != null){
            if(!selectedChannel.name.equals("")){
                binding.contentDisplayUpdate.currLocation.setText(selectedChannel.name);
            }else{
                binding.contentDisplayUpdate.currLocation.setText("Please Check in");
            }
        }else{
            binding.contentDisplayUpdate.currLocation.setText("Please Check in");
        }

        binding.contentDisplayUpdate.layoutPickPhotoBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    camera.takePicture();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.layoutToolbarUpdate.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int display = binding.contentDisplayUpdate.displayedSwitch.isChecked() ? 1 : 0;
                int active = binding.contentDisplayUpdate.usedActiveSwitch.isChecked() ? 1 : 0;
                String token = userService.getAccessToken();
                String programId = userService.getCurrentProgram();
                String usersOrganizationId = selectedChannel.users_organization_id;
                String merchandiseDistributionId = getSelectedMaterial();
                String remarks = String.valueOf(binding.contentDisplayUpdate.inputRemark.getText());
                presenter.postDisplayed(display, active, token, programId, usersOrganizationId, merchandiseDistributionId, remarks, photos);
            }
        });

        trainingItem = new String[]{
                "Choose Material Name"
        };
        setDropdownAdapter();

        binding.contentDisplayUpdate.resetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.contentDisplayUpdate.imgPicFrame.setImageBitmap(null);
                binding.contentDisplayUpdate.resetImage.setVisibility(View.GONE);
                photos = null;
            }
        });

        if(selectedChannel != null){
            presenter.getDistribution(selectedChannel.users_organization_id);
        }
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
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                Util.movePhotoToGallery(getApplicationContext(), camera.getCameraBitmapPath(),"Teamforce_Display_"+Util.getCurrentDateTime()+"_" + System.currentTimeMillis()+".jpg");
                photos = Util.encodeToBase64(BitmapFactory.decodeFile(camera.getCameraBitmapPath()), Bitmap.CompressFormat.JPEG, 50);
                binding.contentDisplayUpdate.imgPicFrame.setImageBitmap(BitmapFactory.decodeFile(camera.getCameraBitmapPath()));
                binding.contentDisplayUpdate.resetImage.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void setMaterialData(List<DistributionPostResponseModel.DistributionList> data){
        materialData = data;
        setArrayData(data);
    }

    public void setArrayData(List<DistributionPostResponseModel.DistributionList> data){
        trainingItem = new String[data.size()+1];
        trainingItem[0] = "Choose Material Name";
        for(int i = 0 ; i < data.size(); i++){
            trainingItem[i+1] = data.get(i).materialname;
        }
        setDropdownAdapter();
    }

    public String getSelectedMaterial(){
        int pos = binding.contentDisplayUpdate.inputMaterialName.getSelectedItemPosition();
        return materialData.get(pos-1).merchantdiseDistributionId;
    }

    public List<DistributionPostResponseModel.DistributionList> getMaterialData(){
        return materialData;
    }

    public void setDropdownAdapter(){
        adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_view_resource, trainingItem){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    return false;
                }
                else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        binding.contentDisplayUpdate.inputMaterialName.setAdapter(adapter);
        binding.contentDisplayUpdate.inputMaterialName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.contentDisplayUpdate.inputMaterialName.setSelection(0);
    }
}
