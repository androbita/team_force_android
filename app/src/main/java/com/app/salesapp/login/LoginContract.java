package com.app.salesapp.login;

public interface LoginContract {
    public interface View {
        public void showErrorLogin(String errorMessage);

        public void showSuccessLogin(LoginResponseModel loginResponseModel);

        void showLoading(boolean show);

        void setVersionName(String versionName);
    }

    public interface Presenter {
        public void doLogin(String apikey, String username, String password, String imei, String deviceToken);

        void onDestroy();

        void checkUserSession();

        void doGetLogo();

        void setVersionName(String versionName);
    }
}
