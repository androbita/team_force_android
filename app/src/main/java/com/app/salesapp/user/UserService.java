package com.app.salesapp.user;

import android.text.TextUtils;
import android.util.Log;

import com.app.salesapp.Gps.ConfigResponseModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.attendance.createattendance.PostAttendanceRequestModel;
import com.app.salesapp.chart.UserResponseModel;
import com.app.salesapp.distribution.model.DistributionPostRequest;
import com.app.salesapp.distribution.model.MaterialSelected;
import com.app.salesapp.distribution.model.ReceivedResponse;
import com.app.salesapp.draft.DraftModel;
import com.app.salesapp.fcm.NotificationModel;
import com.app.salesapp.feedback.PostFeedbackRequest;
import com.app.salesapp.inbound.InboundModel;
import com.app.salesapp.login.LoginResponseModel;
import com.app.salesapp.salesreport.ProductModel;
import com.app.salesapp.salesreport.SellingReportRequestModel;
import com.app.salesapp.salesreport.SellingTypeModel;
import com.app.salesapp.training.AudienceModel;
import com.app.salesapp.training.model.ModuleSelectedModel;
import com.app.salesapp.util.SalesAppPreference;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class UserService {
    public static final String MATERIAL_NAME = "materialName";
    public static final String USER_ORGANIZATION_ID = "usersOrganizationId";
    public static final String MERCHANDISE_DISTRIBUTION_ID = "merchandiseDistributionId";
    public static final String CURRENT_PROGRAM = "currentProgram";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String USER_PREFERENCE = "user_preference";
    private static final String FEEDBACK_LIST = "feedback_list";
    private static final String NOTIFICATION_LIST = "notification_list";
    private static final String NOTIFICATION_MODEL = "notification_model";
    private static final String INBOUND_MODEL = "inbound_model";
    private static final String USER_LOGO = "user_logo";
    private static final String DRAFT_LIST = "draft_list";
    private static final String CHANNEL_LIST = "channel_list";
    private static final String SUBJECT_LIST = "subject_list";
    private static final String PRODUCT_LIST = "product_list";
    private static final String SELLING_TYPE_LIST = "selling_type_list";
    private static final String RECEIVE_LIST = "receive_list";
    private static final String TRACKING_LIST = "tracking_config";
    private static final String CURRENT_CHANNEL = "current_channel";
    private static final String USER_LIST = "user_list";
    private static final String POST_ATTENDANCE_CHANNEL = "post_attendance_channel";
    private static final String AUDIANCE_LIST = "list_audiance";
    private static final String MODULE_TRAINING_LIST = "list_module_training";
    private static final String MATERIAL_SELECTED = "distribution_selected_material";

    protected final SalesAppPreference salesAppPreference;
    protected final Gson gson;

    private static final String BEARER = "Bearer ";

    private String currentProgram;
    private ArrayList<PostFeedbackRequest> draftFeedback;
    private ArrayList<NotificationModel> notifications;
    private ArrayList<DraftModel> drafts;
    private NotificationModel currentNotification;
    private InboundModel.OutboundList currentInbound;
    private ArrayList<ListChannelResponseModel.ChannelList> channels;
    private ArrayList<ListChannelResponseModel.SubjectList> subjects;
    private ArrayList<ConfigResponseModel.TrackingConfig> tracking;
    private ArrayList<ProductModel> products;
    private ArrayList<SellingTypeModel> sellingTypes;
    private ArrayList<ReceivedResponse.ReceivedList> receives;
    private List<UserResponseModel> usersList;

    public UserService(SalesAppPreference salesAppPreference, Gson gson) {
        this.salesAppPreference = salesAppPreference;
        this.gson = gson;
    }

    public String getAccessToken() {
        if (getUserPreference() != null)
            return getUserPreference().token;
        else return "";
    }

    public String getRefreshToken() {
        return salesAppPreference.getString(REFRESH_TOKEN, null);
    }

    @Deprecated
    public SalesAppPreference getSalesAppPreference() {
        return salesAppPreference;
    }

    public boolean isLoggedIn() {
        return getUserPreference() != null && !TextUtils.isEmpty(getUserPreference().token);
    }

    public void clearData() {
        saveUserPreference(null);

        salesAppPreference.setString(MATERIAL_NAME, null);
        salesAppPreference.setString(USER_ORGANIZATION_ID, null);
        salesAppPreference.setString(MERCHANDISE_DISTRIBUTION_ID, null);
        salesAppPreference.setString(CURRENT_PROGRAM, null);
        salesAppPreference.setString(ACCESS_TOKEN, null);
        salesAppPreference.setString(REFRESH_TOKEN, null);
        salesAppPreference.setString(USER_PREFERENCE, null);
        saveChannelList(new ArrayList<ListChannelResponseModel.ChannelList>());
        saveDraftFeedback(new ArrayList<PostFeedbackRequest>());
        saveListSellingType(new ArrayList<SellingTypeModel>());
        saveNotifications(new ArrayList<NotificationModel>());
        saveProductList(new ArrayList<ProductModel>());
        saveReceiveList(new ArrayList<ReceivedResponse.ReceivedList>());
        saveSubjectList(new ArrayList<ListChannelResponseModel.SubjectList>());
        saveTackingList(new ArrayList<ConfigResponseModel.TrackingConfig>());
        saveToken(null, null);
        saveUserList(new ArrayList<UserResponseModel>());
        saveUserLogo(null);
    }

    public void saveToken(String accessToken, String refreshToken) {
        salesAppPreference.setString(ACCESS_TOKEN, accessToken);
        salesAppPreference.setString(REFRESH_TOKEN, refreshToken);
    }

    public void saveUserPreference(LoginResponseModel loginResponseModel) {
        String data = gson.toJson(loginResponseModel);
        salesAppPreference.setString(USER_PREFERENCE, data);
        Log.d("syspref","saved");
    }

    public LoginResponseModel getUserPreference() {
        if (!TextUtils.isEmpty(salesAppPreference.getString(USER_PREFERENCE, null))) {
            return gson.fromJson(salesAppPreference.getString(USER_PREFERENCE, null), LoginResponseModel.class);
        }

        return null;
    }

    public boolean hasConfigPreference(){
        String key = salesAppPreference.getString(TRACKING_LIST,null);
        if (key != null ) {
            return true;
        }

        return false;
    }
    public String getAuthHeader() {
        return BEARER + this.getAccessToken();
    }

    public void setCurrentProgram(String currentProgram) {
        salesAppPreference.setString(CURRENT_PROGRAM, currentProgram);
    }

    public String getCurrentProgram() {
        return salesAppPreference.getString(CURRENT_PROGRAM, "");
    }

    public String getMaterialName() {
        return salesAppPreference.getString(MATERIAL_NAME, null);
    }

    public void setMaterialName(String materialName) {
        salesAppPreference.setString(MATERIAL_NAME, materialName);
    }

    public String getUsersOrganizationId() {
        return salesAppPreference.getString(USER_ORGANIZATION_ID, null);
    }

    public void setUsersOrganizationId(String usersOrganizationId) {
        salesAppPreference.setString(USER_ORGANIZATION_ID, usersOrganizationId);
    }

    public String getMerchandiseDistributionId() {
        return salesAppPreference.getString(MERCHANDISE_DISTRIBUTION_ID, null);
    }

    public void setMerchandiseDistributionId(String merchandiseDistributionId) {
        salesAppPreference.setString(MERCHANDISE_DISTRIBUTION_ID, merchandiseDistributionId);
    }

    public void saveDraftFeedback(ArrayList<PostFeedbackRequest> postFeedbackRequest) {
        String data = gson.toJson(postFeedbackRequest);
        salesAppPreference.setString(FEEDBACK_LIST, data);
    }

    public ArrayList<PostFeedbackRequest> getDraftFeedback() {
        draftFeedback = gson.fromJson(salesAppPreference.getString(FEEDBACK_LIST, null), new TypeToken<ArrayList<PostFeedbackRequest>>(){}.getType());
        if(draftFeedback==null)
            draftFeedback = new ArrayList<PostFeedbackRequest>();
        return draftFeedback;
    }

    public void saveNotifications(ArrayList<NotificationModel> notificationModel) {
        String data = gson.toJson(notificationModel);
        salesAppPreference.setString(NOTIFICATION_LIST, data);
    }
    public void addNotification(NotificationModel notificationModel) {
        notifications = getNotifications();
        notifications.add(notificationModel);
        String data = gson.toJson(notifications);
        salesAppPreference.setString(NOTIFICATION_LIST, data);
    }
    public ArrayList<NotificationModel> getNotifications() {
        notifications = gson.fromJson(salesAppPreference.getString(NOTIFICATION_LIST, null), new TypeToken<ArrayList<NotificationModel>>(){}.getType());
        if(notifications==null)
            notifications = new ArrayList<NotificationModel>();
        return notifications;
    }

    public void setNotificationModel(NotificationModel model){
        String data = gson.toJson(model);
        salesAppPreference.setString(NOTIFICATION_MODEL, data);
    }

    public NotificationModel getCurrentNotification() {
        return gson.fromJson(salesAppPreference.getString(NOTIFICATION_MODEL, null),NotificationModel.class);
    }

    public void setCurrentInbound(InboundModel.OutboundList currentInbound) {
        String data = gson.toJson(currentInbound);
        salesAppPreference.setString(INBOUND_MODEL, data);
    }

    public InboundModel.OutboundList getCurrentInbound() {
        return gson.fromJson(salesAppPreference.getString(INBOUND_MODEL, null),InboundModel.OutboundList.class);
    }

    public void saveUserLogo(String logo) {
        salesAppPreference.setString(USER_LOGO,logo);
    }

//    public int getUserLogo(){
//        return R.drawable.logo_tf_white;
//    }

    public String getUserLogo(){
        return salesAppPreference.getString(USER_LOGO,"");
    }


    public ArrayList<DraftModel> getListDraft() {
        drafts = gson.fromJson(salesAppPreference.getString(DRAFT_LIST, null), new TypeToken<ArrayList<DraftModel>>(){}.getType());
        if(drafts==null){
            drafts = new ArrayList<DraftModel>();
        } else {
            drafts = filterListDraft(drafts);
        }
        return drafts;
    }

    public ArrayList<DraftModel> getAllListDraft() {
        drafts = gson.fromJson(salesAppPreference.getString(DRAFT_LIST, null), new TypeToken<ArrayList<DraftModel>>(){}.getType());
        if(drafts==null){
            drafts = new ArrayList<DraftModel>();
        }

        return drafts;
    }

    private ArrayList<DraftModel> filterListDraft(ArrayList<DraftModel> draftModels) {
        String token = getUserPreference().token;
        ListIterator<DraftModel> listIterator = draftModels.listIterator();
        while(listIterator.hasNext()){
            String currentToken ="";
            DraftModel draftModel = listIterator.next();
            LinkedTreeMap<?,?> mapData = (LinkedTreeMap<?,?>)draftModel.data;

            if (draftModel.type == 0) {
                PostAttendanceRequestModel requestModel = gson.fromJson(gson.toJsonTree(mapData), PostAttendanceRequestModel.class);
                currentToken =requestModel.token;
            } else if (draftModel.type == 1) {
                SellingReportRequestModel requestModel = gson.fromJson(gson.toJsonTree(mapData), SellingReportRequestModel.class);
                currentToken =requestModel.getToken();
            } else if (draftModel.type == 2) {
                DistributionPostRequest requestModel = gson.fromJson(gson.toJsonTree(mapData), DistributionPostRequest.class);
                currentToken =requestModel.token;
            }

            if(!currentToken.equalsIgnoreCase(token)){
                listIterator.remove();
            }
        }
        return draftModels;
    }

    public void addDraft(DraftModel draftModel) {
        drafts = getAllListDraft();
        drafts.add(draftModel);
        String data = gson.toJson(drafts);
        salesAppPreference.setString(DRAFT_LIST, data);
    }

    public void saveDraftListAfterRemoved(ArrayList<DraftModel> draft) {
        drafts = getAllListDraft();
        String token = getUserPreference().token;
        ListIterator<DraftModel> listIterator = drafts.listIterator();
        while(listIterator.hasNext()){
            String currentToken ="";
            DraftModel draftModel = listIterator.next();
            LinkedTreeMap<?,?> mapData = (LinkedTreeMap<?,?>)draftModel.data;

            if (draftModel.type == 0) {
                PostAttendanceRequestModel requestModel = gson.fromJson(gson.toJsonTree(mapData), PostAttendanceRequestModel.class);
                currentToken =requestModel.token;
            } else if (draftModel.type == 1) {
                SellingReportRequestModel requestModel = gson.fromJson(gson.toJsonTree(mapData), SellingReportRequestModel.class);
                currentToken =requestModel.getToken();
            } else if (draftModel.type == 2) {
                DistributionPostRequest requestModel = gson.fromJson(gson.toJsonTree(mapData), DistributionPostRequest.class);
                currentToken =requestModel.token;
            }

            if(currentToken.equalsIgnoreCase(token)){
                listIterator.remove();
            }
        }

        drafts.addAll(draft);
        String data = gson.toJson(drafts);
        salesAppPreference.setString(DRAFT_LIST, data);
    }

    public void saveChannelList(List<ListChannelResponseModel.ChannelList> channel_list) {
        String data = gson.toJson(channel_list);
        salesAppPreference.setString(CHANNEL_LIST, data);
    }

    public void saveSubjectList(List<ListChannelResponseModel.SubjectList> subject_list) {
        String data = gson.toJson(subject_list);
        salesAppPreference.setString(SUBJECT_LIST, data);
    }

    public List<ListChannelResponseModel.ChannelList> getListChannel() {
        channels = gson.fromJson(salesAppPreference.getString(CHANNEL_LIST, null), new TypeToken<ArrayList<ListChannelResponseModel.ChannelList>>(){}.getType());
        if(channels==null)
            channels = new ArrayList<>();
        return channels;
    }

    public List<ListChannelResponseModel.SubjectList> getListSubject() {
        subjects = gson.fromJson(salesAppPreference.getString(SUBJECT_LIST, null), new TypeToken<ArrayList<ListChannelResponseModel.SubjectList>>(){}.getType());
        if(subjects==null)
            subjects = new ArrayList<>();
        return subjects;
    }
    /*
    save config to preferance
     */
    public void saveTackingList(List<ConfigResponseModel.TrackingConfig> tracking_list) {
        String data = gson.toJson(tracking_list);
        String a = salesAppPreference.setString(TRACKING_LIST, data);
    }
    /*
        get config from preferance
    */
    public List<ConfigResponseModel.TrackingConfig> getListTracking() {
        tracking = gson.fromJson(salesAppPreference.getString(TRACKING_LIST, null), new TypeToken<ArrayList<ConfigResponseModel.TrackingConfig>>(){}.getType());
        if(tracking==null)
            tracking = new ArrayList<>();
        return tracking;
    }

    public void saveProductList(List<ProductModel> listProduct) {
        String data = gson.toJson(listProduct);
        salesAppPreference.setString(PRODUCT_LIST, data);
    }

    public List<ProductModel> getListProduct() {
        products = gson.fromJson(salesAppPreference.getString(PRODUCT_LIST, null), new TypeToken<ArrayList<ProductModel>>(){}.getType());
        if(products==null)
            products = new ArrayList<>();
        return products;
    }

    public void saveListSellingType(List<SellingTypeModel> listSellingType) {
        String data = gson.toJson(listSellingType);
        salesAppPreference.setString(SELLING_TYPE_LIST, data);
    }

    public List<SellingTypeModel> getSellingTypeList() {
        sellingTypes = gson.fromJson(salesAppPreference.getString(SELLING_TYPE_LIST, null), new TypeToken<ArrayList<SellingTypeModel>>(){}.getType());
        if(sellingTypes==null)
            sellingTypes = new ArrayList<>();
        return sellingTypes;
    }

    public void saveReceiveList(List<ReceivedResponse.ReceivedList> receivedLists) {
        String data = gson.toJson(receivedLists);
        salesAppPreference.setString(RECEIVE_LIST, data);
    }

    public void saveUserList(List<UserResponseModel> users) {
        String data = gson.toJson(users);
        salesAppPreference.setString(USER_LIST, data);
    }

    public  List<UserResponseModel> getUserList(){
        usersList = gson.fromJson(salesAppPreference.getString(USER_LIST, null), new TypeToken<ArrayList<UserResponseModel>>(){}.getType());
        if(usersList==null) {
            usersList = new ArrayList<>();
        }
        return usersList;
    }

    public List<ReceivedResponse.ReceivedList> getReceiveList() {
        receives = gson.fromJson(salesAppPreference.getString(RECEIVE_LIST, null), new TypeToken<ArrayList<ReceivedResponse.ReceivedList>>(){}.getType());
        if(receives==null)
            receives = new ArrayList<>();
        return receives;
    }

    public String getFrequecy(){
        if (getListTracking() != null && getListTracking().size() > 0) {
            ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
            return tracking.frequency;
        } else {
            return "0";
        }
    }

    public String getIncludeSaturday(){
        ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
        return tracking.includeSaturday;
    }

    public String getIncludeSunday(){
        ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
        return tracking.includeSunday;
    }

    public String getTimeStart(){
        ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
        return tracking.timeStart;
    }

    public String getTimeStop(){
        ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
        return tracking.timeStop;
    }

    public void setSelectedChannel(String channelName) {
        salesAppPreference.setString(CURRENT_CHANNEL, channelName);
    }

    public String getSelectedChannel() {
        return salesAppPreference.getString(CURRENT_CHANNEL, "");
    }

    public void setPostAttendanceChannel(ListChannelResponseModel.ChannelList chlist){
        String data = gson.toJson(chlist);
        salesAppPreference.setString(POST_ATTENDANCE_CHANNEL, data);
    }

    public ListChannelResponseModel.ChannelList getPostAttendanceChannel(){
         ListChannelResponseModel.ChannelList receives = gson.fromJson(salesAppPreference.getString(POST_ATTENDANCE_CHANNEL, null), ListChannelResponseModel.ChannelList.class);
        return receives;
    }

    public void setAudanceTraining(ArrayList<AudienceModel> model) {
        String data = gson.toJson(model);
        salesAppPreference.setString(AUDIANCE_LIST, data);
    }

    public ArrayList<AudienceModel> getAudianceTraining(){
        ArrayList<AudienceModel> receives = gson.fromJson(salesAppPreference.getString(AUDIANCE_LIST, null),new TypeToken<ArrayList<AudienceModel>>(){}.getType());
        return receives;
    }

    public void setModuleTraining(ArrayList<ModuleSelectedModel> model) {
        String data = gson.toJson(model);
        salesAppPreference.setString(MODULE_TRAINING_LIST, data);
    }

    public ArrayList<ModuleSelectedModel> getModuleTraining(){
        ArrayList<ModuleSelectedModel> receives = gson.fromJson(salesAppPreference.getString(MODULE_TRAINING_LIST, null),new TypeToken<ArrayList<ModuleSelectedModel>>(){}.getType());
        return receives;
    }

    public void setMaterialSelected(ArrayList<MaterialSelected> material){
        String data = gson.toJson(material);
        salesAppPreference.setString(MATERIAL_SELECTED, data);
    }

    public ArrayList<MaterialSelected> getMaterialSelected(){
        ArrayList<MaterialSelected> receives = gson.fromJson(salesAppPreference.getString(MATERIAL_SELECTED, null),new TypeToken<ArrayList<MaterialSelected>>(){}.getType());
        return receives;
    }
}
