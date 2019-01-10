package com.app.salesapp.distribution.createdistribution;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.databinding.ActivityCreateDistributionBinding;
import com.app.salesapp.distribution.activity.DistributionMaterialActivity;
import com.app.salesapp.distribution.model.DistributionPostRequest;
import com.app.salesapp.distribution.model.MaterialRequestModel;
import com.app.salesapp.distribution.model.MaterialSelected;
import com.app.salesapp.distribution.model.ReceivedRequest;
import com.app.salesapp.distribution.model.ReceivedResponse;
import com.app.salesapp.draft.DraftModel;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;
import com.mindorks.paracamera.Camera;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CreateDistributionActivity extends BaseActivity implements CreateDistributionView {

    private static final int PICK_PHOTO_BEFORE = 1000;
    private static final int PICK_PHOTO_AFTER = 1001;

    @Inject
    protected SalesAppService salesAppService;
    @Inject
    protected UserService userService;

    private String photosBefore = "";
    private String photosAfter = "";
    private ActivityCreateDistributionBinding binding;
    private CreateDistributionPresenter presenter;
    private CreateDistributionViewModel viewModel;
    private ArrayList<MaterialSelected> materialSelectedts;
    public ListChannelResponseModel.ChannelList selectedChannel;

    private Camera camera;
    private ReceivedResponse.ReceivedList selectedMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        viewModel = new CreateDistributionViewModel();
        presenter = new CreateDistributionPresenter(this, userService, salesAppService, viewModel);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_distribution);
        binding.setPresenter(presenter);
        binding.setViewModel(viewModel);

        setSupportActionBar(binding.layoutToolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null));
        TextView title = binding.layoutToolbar.textView2;
        title.setText("Distribution");

        materialSelectedts = new ArrayList<>();
        presenter.getReceived(new ReceivedRequest(userService.getUserPreference().token, userService.getCurrentProgram()));

        binding.contentCreateDistribution.layoutPickPhotoBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext(), false);
                startActivityForResult(chooseImageIntent, PICK_PHOTO_BEFORE);
            }
        });

        selectedChannel = userService.getPostAttendanceChannel();
        if(selectedChannel != null){
            if(!selectedChannel.name.equals("")){
                binding.contentCreateDistribution.currLocation.setText(selectedChannel.name);
            }else{
                binding.contentCreateDistribution.currLocation.setText("Please Check in");
            }
        }else{
            binding.contentCreateDistribution.currLocation.setText("Please Check in");
        }

        binding.contentCreateDistribution.layoutPickPhotoAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    camera.takePicture();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.layoutToolbar.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPostDistribution();
            }
        });

        binding.contentCreateDistribution.addSelMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateDistributionActivity.this, DistributionMaterialActivity.class);
                startActivity(i);
            }
        });


        initCamera();
    }

    private void doPostDistribution() {
        String token = userService.getAccessToken();
        String programId = userService.getCurrentProgram();
        String usersOrganizationId = "";

        if(binding.contentCreateDistribution.inputRemark.getText().toString().equals("")){
            binding.contentCreateDistribution.inputRemark.setError("Required");
        }else if(photosBefore.equals("")){
            showSnackBar(this,"Photo Before is Required");
        }else if(photosAfter.equals("")){
            showSnackBar(this,"Photo After is Required");
        }else{
            usersOrganizationId = selectedChannel != null ? selectedChannel.users_organization_id : "";
            String remarks = String.valueOf(binding.contentCreateDistribution.inputRemark.getText());
            String pictureBefore = photosBefore;
            String pictureAfter = photosAfter;

            ArrayList<MaterialRequestModel> item = new ArrayList<>();
            for (MaterialSelected data : materialSelectedts) {
                if (data.isSelected){
                    MaterialRequestModel model = new MaterialRequestModel(data.outboundId, data.input, 1, remarks);
                    item.add(model);
                }
            }
            presenter.postDistribution( token, programId, usersOrganizationId, item, pictureBefore, pictureAfter);
        }

    }

    private void initCamera() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("Teamforce")
                .setName("Teamforce_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000).build(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                userService.setMaterialSelected(null);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadSpinnerData(List<ListChannelResponseModel.ChannelList> data) {

    }

    @Override
    public void loadSpinnerMaterialData(List<ReceivedResponse.ReceivedList> data) {
        materialSelectedts = new ArrayList<>();
        for(int i = 0 ; i < data.size(); i++){
            ReceivedResponse.ReceivedList materials = data.get(i);
            MaterialSelected material = new MaterialSelected(materials.outboundId, materials.materialName, false , 0)   ;
            materialSelectedts.add(material);
        }
        userService.setMaterialSelected(materialSelectedts);
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
    public void onErrorPost(String appErrorMessage, final DistributionPostRequest distributionPostRequest) {
        AlertDialog dialog = new AlertDialog.Builder(CreateDistributionActivity.this)
                .setTitle("Post Distribution")
                .setMessage("Something wrong, " + appErrorMessage)
                .setCancelable(false)
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        doPostDistribution();
                        dialog.dismiss();
                    }
                }).setNegativeButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userService.addDraft(new DraftModel(2, distributionPostRequest));
                        finish();
                    }
                }).create();

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                Util.movePhotoToGallery(getApplicationContext(), camera.getCameraBitmapPath(), "Teamforce_After_"+Util.getCurrentDateTime()+"_" + System.currentTimeMillis() + ".jpg");
                photosAfter = Util.encodeToBase64(BitmapFactory.decodeFile(camera.getCameraBitmapPath()), Bitmap.CompressFormat.JPEG, 50);
                binding.contentCreateDistribution.imgPicFrameAfter.setImageBitmap(BitmapFactory.decodeFile(camera.getCameraBitmapPath()));
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_PHOTO_BEFORE) {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
            if (bitmap != null) {
                File imageFile = ImagePicker.getTempFile(getApplicationContext());
                boolean isCamera = (data == null ||
                        data.getData() == null ||
                        data.getData().toString().contains(imageFile.toString()));
                if (isCamera) {
                    Util.movePhotoToGallery(getApplicationContext(), imageFile.getPath(), "Teamforce_Before_"+Util.getCurrentDateTime()+"_" + System.currentTimeMillis() + ".jpg");
                }

                binding.contentCreateDistribution.imgPicFrameBefore.setImageBitmap(bitmap);
                photosBefore = Util.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 50);
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int getTotalSelectedMaterial() {
        int i = 0;
        if(materialSelectedts != null){
            for (MaterialSelected data : materialSelectedts) {
                if (data.isSelected) i++;
            }
        }
        return i;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        materialSelectedts = new ArrayList<>();
        presenter.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        materialSelectedts = userService.getMaterialSelected();
        binding.contentCreateDistribution.material.setText(getTotalSelectedMaterial()+" Material Selected");
    }
}

