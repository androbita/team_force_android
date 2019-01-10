package com.app.salesapp.salesreport;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ChannelSpinnerAdapter;
import com.app.salesapp.attendance.createattendance.ListChannelRequestModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.draft.DraftModel;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mindorks.paracamera.Camera;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class CreateSalesReportActivity extends BaseActivity implements CreateSalesReportView {

    private String photos = "";

    private Camera camera;
    private RelativeLayout progressLayout;
    private LinearLayout btnPhoto;
    private ImageView btnSubmit;
    private ImageView picFrame;

    private EditText date_invoiceET;
    private EditText invoiceET;
    private EditText quantityET;
    private EditText revenueET;
    private EditText unique_codeET;
    private EditText remarkET;

    private TextView location;

    private AutoCompleteTextView autoCompleteChannel;
    private AutoCompleteTextView autoCompleteTransType;
    private AutoCompleteTextView autoCompleteProduct;

    private CreateSalesReportPresenter reportPresenter;

    private ChannelSpinnerAdapter channelAdapter;
    private ArrayList<ListChannelResponseModel.ChannelList> channelLists;

    private SellingTypeAdapter sellingTypeAdapter;
    private ArrayList<SellingTypeModel> sellingTypeLists;

    private ProductSpinnerAdapter productSpinnerAdapter;
    private ArrayList<ProductModel> productLists;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    private ListChannelResponseModel.ChannelList selectedChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sales_report);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null));
        TextView title =(TextView) findViewById(R.id.textView2);
        title.setText("Selling");

        location =(TextView) findViewById(R.id.currLocation);
        selectedChannel = userService.getPostAttendanceChannel();
        if(selectedChannel != null){
            if(!selectedChannel.name.equals("")){
                location.setText(selectedChannel.name);
            }else{
                location.setText("Please Check in");
            }
        }else{
            location.setText("Please Check in");
        }

        reportPresenter = new CreateSalesReportPresenter(this, salesAppService,userService);

        initComponents();
        initCamera();

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

    ProductModel selectedProduct = null;
    SellingTypeModel selectedSelling = null;

    private void initComponents() {
        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        showLoading(false);
        invoiceET = (EditText) findViewById(R.id.invoice_numberET);
        quantityET = (EditText) findViewById(R.id.quantityET);
        revenueET = (EditText) findViewById(R.id.revenueET);
        unique_codeET = (EditText) findViewById(R.id.uniqueCodeET);
        remarkET = (EditText) findViewById(R.id.remarkET);
        btnSubmit = (ImageView) findViewById(R.id.sendButton);

        productLists = new ArrayList<>();
        productSpinnerAdapter = new ProductSpinnerAdapter(this, productLists);
        autoCompleteProduct = (AutoCompleteTextView) findViewById(R.id.autocomplete_product);
        productSpinnerAdapter.setDropDownViewResource(R.layout.spinner_view_resource);
        autoCompleteProduct.setAdapter(productSpinnerAdapter);
        autoCompleteProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectedProduct = (ProductModel) parent.getItemAtPosition(position);
            }
        });

        sellingTypeLists = new ArrayList<>();
        sellingTypeAdapter = new SellingTypeAdapter(this, sellingTypeLists);
        autoCompleteTransType = (AutoCompleteTextView) findViewById(R.id.autocomplete_trans_type);
        sellingTypeAdapter.setDropDownViewResource(R.layout.spinner_view_resource);
        autoCompleteTransType.setAdapter(sellingTypeAdapter);
        autoCompleteTransType.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectedSelling = (SellingTypeModel) parent.getItemAtPosition(position);

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSoftKeyboard();
                if (selectedSelling == null) {
                    autoCompleteTransType.setError(getString(R.string.msg_error_trs_type));
                } else if (selectedProduct == null) {
                    autoCompleteProduct.setError(getString(R.string.msg_error_product));
                } else if (quantityET.getText().equals("")) {
                    quantityET.setError(getString(R.string.msg_error_quantity_not_number));
                } else if (unique_codeET.getText().equals("")) {
                    unique_codeET.setError(getString(R.string.msg_error_uniq_code));
                } else
                    reportPresenter.postSellingReport(new SellingReportRequestModel(userService.getAccessToken(),
                            userService.getCurrentProgram(),
                            selectedChannel != null ? selectedChannel.users_organization_id : "",
                            selectedSelling.selling_type_id,
                            selectedProduct.program_product_id,
                            invoiceET.getText().toString(),
                            date_invoiceET.getText().toString(),
                            quantityET.getText().toString(),
                            revenueET.getText().toString(),
                            unique_codeET.getText().toString(),
                            photos,
                            remarkET.getText().toString(),
                            Util.getCurrentDateTime()));
            }
        });

        autoCompleteProduct.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!autoCompleteProduct.isPopupShowing())
                    autoCompleteProduct.showDropDown();
                return false;
            }
        });

        autoCompleteTransType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!autoCompleteTransType.isPopupShowing())
                    autoCompleteTransType.showDropDown();
                return false;
            }
        });


        date_invoiceET = (EditText) findViewById(R.id.date_invoiceET);

        date_invoiceET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnPhoto = (LinearLayout) findViewById(R.id.btnPhoto);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                Util.movePhotoToGallery(getApplicationContext(), camera.getCameraBitmapPath(),"Teamforce_Selling_" +Util.getCurrentDateTime()+"_" + System.currentTimeMillis()+".jpg");
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
        //reportPresenter.getListChannel(new ListChannelRequestModel(userService.getUserPreference().token, userService.getCurrentProgram()));
        reportPresenter.getListProduct(new ListChannelRequestModel(userService.getUserPreference().token, userService.getCurrentProgram()));
        reportPresenter.getListSellingType(new ListChannelRequestModel(userService.getUserPreference().token, userService.getCurrentProgram()));

    }

    @Override
    public void showLoading(boolean show) {
        if (show)
            progressLayout.setVisibility(View.VISIBLE);
        else
            progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void loadChannelSpinner(List<ListChannelResponseModel.ChannelList> data) {
        if (data.size() <= 0) {
            Toast.makeText(this, "Tidak ada Channel", Toast.LENGTH_SHORT).show();
            finish();
        }
        channelLists.clear();
        channelLists.addAll(data);
        channelAdapter.setTempItems(data);
        channelAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadProductSpinner(List<ProductModel> data) {
        if (data.size() <= 0) {
            Toast.makeText(this, "Tidak ada Produk", Toast.LENGTH_SHORT).show();
            finish();
        }
        productLists.clear();
        productLists.addAll(data);
        productSpinnerAdapter.setTempItems(data);
        productSpinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadSellingTypeSpinner(List<SellingTypeModel> data) {
        if (data.size() <= 0) {
            Toast.makeText(this, "Tidak ada Tipe  Transaksi", Toast.LENGTH_SHORT).show();
            finish();
        }
        sellingTypeLists.clear();
        sellingTypeLists.addAll(data);
        sellingTypeAdapter.setTempItems(data);
        sellingTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorPostSelling(String message,final SellingReportRequestModel sellingReportRequestModel) {
        AlertDialog dialog = new AlertDialog.Builder(CreateSalesReportActivity.this)
                .setTitle("Post Selling")
                .setMessage("Something wrong, " + message)
                .setCancelable(false)
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userService.addDraft(new DraftModel(1,sellingReportRequestModel));
                        finish();
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void onSuccessPostSelling(Response<String> response) {
        AlertDialog dialog = new AlertDialog.Builder(CreateSalesReportActivity.this)
                .setTitle("Post Selling")
                .setMessage("Success")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            ((EditText) getActivity().findViewById(R.id.date_invoiceET)).setText(day + "/" + (month + 1) + "/" + year);
        }
    }
}
