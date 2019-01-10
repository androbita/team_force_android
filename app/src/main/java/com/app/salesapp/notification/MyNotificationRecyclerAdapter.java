package com.app.salesapp.notification;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.fcm.NotificationModel;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.RxBus;
import com.app.salesapp.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.inject.Inject;

public class MyNotificationRecyclerAdapter  extends RecyclerView.Adapter<MyNotificationRecyclerAdapter.ViewHolder> {

    @Inject
    public RxBus rxBus;

    private Context mContext;
    private ArrayList<NotificationModel> mValues;

    @Inject
    public UserService userService;

    public MyNotificationRecyclerAdapter(Context context, ArrayList<NotificationModel> items) {
        mValues = items;
        mContext = context;
        ((SalesApp) mContext.getApplicationContext()).getSalesAppDeps().inject(this);
    }

    public void replaceData(ArrayList<NotificationModel> data) {
        setList(data);
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<NotificationModel> data) {
        mValues.clear();
        setList(data);
        notifyDataSetChanged();
    }


    private void setList(ArrayList<NotificationModel>data) {
        mValues.addAll(data);
    }

    @Override
    public MyNotificationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_notification_list, parent, false);
        final MyNotificationRecyclerAdapter.ViewHolder holder = new MyNotificationRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    public SpannableStringBuilder makeBoldText(String string){
        SpannableStringBuilder str = new SpannableStringBuilder(string);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, string.indexOf("commented"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    @Override
    public void onBindViewHolder(final MyNotificationRecyclerAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        holder.notifTV.setText(makeBoldText(holder.mItem.message));
        if(holder.mItem.getDateTime()!= null) {
            SimpleDateFormat sdf = new SimpleDateFormat(Util.DEFAULT_TIME_PATTERN);
            holder.notifDateTV.setText(sdf.format(holder.mItem.getDateTime()));
        }
        //if(holder.mItem.type.equals("COMMENT")){
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,DetailCommentActivity.class);
                userService.setNotificationModel(holder.mItem);
                mContext.startActivity(intent);

            }
        });
        //}
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final View divider;
        public  final TextView notifTV;
        public  final TextView notifDateTV;
        public final ImageView notifImage;
        public NotificationModel mItem;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            divider = view.findViewById(R.id.notif_divider);
            notifTV = (TextView)view.findViewById(R.id.notif_tv);
            notifDateTV = (TextView)view.findViewById(R.id.notif_date_tv);
            notifImage = (ImageView) view.findViewById(R.id.notif_image_tv);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}