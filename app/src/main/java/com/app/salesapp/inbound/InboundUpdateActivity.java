package com.app.salesapp.inbound;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.OptimizedActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindorks.paracamera.Camera;

import javax.inject.Inject;

public class InboundUpdateActivity extends BaseActivity implements InboundUpdateView {
    private String photos = "";
    private Camera camera;
    private RelativeLayout progressLayout;
    private Button btnPhoto;
    private ImageView btnSubmit;
    private ImageView picFrame;
    private ImageView resetImage;
    private LinearLayout selectReceive;
    private TextView title;
    private TextView quantity;
    private TextView desc;
    private TextView dateTV;

    private InboundUpdatePresenter inboundUpdatePresenter;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    private EditText qtyReceived;
    private EditText qtyReturnerET;
    private EditText remarkET;
    private InboundModel.OutboundList inboundModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbound_update);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        TextView title = toolbar.findViewById(R.id.textView2);
        title.setText("Receive");

        inboundUpdatePresenter = new InboundUpdatePresenter(this, salesAppService);
        inboundModel = userService.getCurrentInbound();
        initComponents();
        initCamera();
    }

    public void setViewCurrentInbound(){
        if(inboundModel != null) {
            title.setText(inboundModel.material_name + " - " + inboundModel.quantity + " pcs");
            desc.setText(inboundModel.description);
            quantity.setText(inboundModel.quantity + " - " + inboundModel.status);
            dateTV.setText(Util.formatDateFromAPI(inboundModel.date,null));
        }else{
            title.setText("Please tap here to select");
            desc.setText("");
            quantity.setText("");
            dateTV.setText("");
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

    private void initComponents() {
        selectReceive = (LinearLayout)findViewById(R.id.selectReceive);
        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        showLoading(false);
        qtyReceived = (EditText) findViewById(R.id.qtyReceivedET);
        qtyReturnerET = (EditText) findViewById(R.id.qtyReturnerET);
        remarkET = (EditText) findViewById(R.id.remarkET);
        btnPhoto = (Button) findViewById(R.id.btnPhoto);
        resetImage = (ImageView) findViewById(R.id.resetImage);
        btnSubmit = (ImageView) findViewById(R.id.sendButton);
        title = (TextView) findViewById(R.id.titleTV);
        quantity = (TextView) findViewById(R.id.locationTV);
        desc = (TextView) findViewById(R.id.descriptionTV);
        dateTV = (TextView) findViewById(R.id.dateTV);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSoftKeyboard();
                if(photos.equals("")) {
                    Toast.makeText(InboundUpdateActivity.this, "Harus menyertakan Photo !", Toast.LENGTH_SHORT).show();
                } else  {
                    if (inboundModel != null)
                        inboundUpdatePresenter.postInboundUpdate(
                                userService.getAccessToken(),
                                userService.getCurrentProgram(),
                                inboundModel.outbound_id,
                                qtyReceived.getText().toString(),
                                qtyReturnerET.getText().toString(),
                                remarkET.getText().toString(),
                                photos);
                }
            }
        });


        resetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photos = null;
                picFrame.setImageBitmap(null);
                picFrame.setVisibility(View.GONE);
                resetImage.setVisibility(View.GONE);
            }
        });


        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    camera.takePicture();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        picFrame = (ImageView) findViewById(R.id.pic_frame);
        setViewCurrentInbound();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                btnPhoto.setText("RETAKE PHOTO");
                picFrame.setVisibility(View.VISIBLE);
                resetImage.setVisibility(View.VISIBLE);
                Util.movePhotoToGallery(getApplicationContext(), camera.getCameraBitmapPath(),"Teamforce_" + System.currentTimeMillis()+".jpg");
                photos = Util.encodeToBase64(BitmapFactory.decodeFile(camera.getCameraBitmapPath()), Bitmap.CompressFormat.JPEG, 50);
                picFrame.setImageBitmap(BitmapFactory.decodeFile(camera.getCameraBitmapPath()));
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        inboundModel = userService.getCurrentInbound();
        setViewCurrentInbound();
    }

    @Override
    public void showLoading(boolean show) {
        if (show)
            progressLayout.setVisibility(View.VISIBLE);
        else
            progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessPostInboundUpdate(Response<String> response) {
        AlertDialog dialog = new AlertDialog.Builder(InboundUpdateActivity.this)
                .setTitle("Post Received")
                .setMessage("Success")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data = new Intent();
                        String text = "Success Update";
                        data.setData(Uri.parse(text));
                        setResult(RESULT_OK, data);
                        userService.setCurrentInbound(null);
                        finish();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onErrorPostInboundUpdate(String message) {
        AlertDialog dialog = new AlertDialog.Builder(InboundUpdateActivity.this)
                .setTitle("Post Received")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
