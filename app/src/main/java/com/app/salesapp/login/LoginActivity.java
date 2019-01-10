package com.app.salesapp.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.BuildConfig;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.common.DeviceUtil;
import com.app.salesapp.main.MainActivity;
import com.app.salesapp.user.UserService;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    private static final int MY_PERMISSIONS_REQUEST_PERMISSION = 99;
    TextView mBtnSignIn;
    LoginContract.Presenter mPresenter;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;
    private TextView appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);
        setContentView(R.layout.activity_login);
        FirebaseInstanceId.getInstance().getToken();
        mPresenter = new LoginPresenter(this, salesAppService, userService);
        mBtnSignIn = (TextView) findViewById(R.id.btnSignIn);
        emailEditText = (EditText) findViewById(R.id.emailET);
        passwordEditText = (EditText) findViewById(R.id.passwordET);
        appVersion = (TextView) findViewById(R.id.appVersion);
        mPresenter.checkUserSession();
        mPresenter.doGetLogo();
        mPresenter.setVersionName(BuildConfig.VERSION_NAME);

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.doLogin(getString(R.string.api_key), emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        null,
                        FirebaseInstanceId.getInstance().getToken());
            }
        });
        checkPermissions();
    }
    
    private void checkPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA};

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_PERMISSION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }
    }

    @Override
    public void showErrorLogin(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessLogin(LoginResponseModel loginResponseModel) {
        Intent view = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(view);
        finish();
    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void setVersionName(String versionName) {
        appVersion.setText(getString(R.string.version) + versionName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
