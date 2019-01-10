package com.app.salesapp.draft;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.PostAttendanceRequestModel;
import com.app.salesapp.distribution.model.DistributionPostRequest;
import com.app.salesapp.distribution.model.DistributionPostResponse;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.salesreport.SellingReportRequestModel;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MyDraftRecyclerAdapter extends RecyclerView.Adapter<MyDraftRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<DraftModel> mValues;

    @Inject
    public SalesAppService salesAppService;
    private CompositeSubscription compositeSubscription;
    private OnDraftListener listener;

    private Gson gson;
    public MyDraftRecyclerAdapter(Context context, ArrayList<DraftModel> items) {
        mValues = items;
        mContext = context;
        ((SalesApp) context).getSalesAppDeps().inject(this);
        compositeSubscription = new CompositeSubscription();
        gson = new Gson();
    }

    public void replaceData(ArrayList<DraftModel> data) {
        setList(data);
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<DraftModel> data) {
        mValues.clear();
        setList(data);
        notifyDataSetChanged();
    }


    private void setList(ArrayList<DraftModel>data) {
        mValues.addAll(data);
    }

    @Override
    public MyDraftRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_attendance_list, parent, false);
        final MyDraftRecyclerAdapter.ViewHolder holder = new MyDraftRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyDraftRecyclerAdapter.ViewHolder holder,int position) {
        holder.mItem = mValues.get(holder.getAdapterPosition());
        LinkedTreeMap<?,?> yourMap = (LinkedTreeMap<?,?>)holder.mItem.data;
        if(holder.mItem.type == 0) {
            //attendance
            final PostAttendanceRequestModel requestModel = gson.fromJson(gson.toJsonTree(yourMap), PostAttendanceRequestModel.class);
            holder.titleKegiatan.setText("Attendance");
            holder.detailKegiatan.setText(requestModel.subjectName);
            holder.locationTV.setText(requestModel.location);
            holder.img.setImageResource(R.drawable.marker);
            holder.btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doPostAttendance(requestModel,holder.getAdapterPosition());
                }
            });
        }else if(holder.mItem.type == 1) {
            //selling
            final SellingReportRequestModel requestModel =  gson.fromJson(gson.toJsonTree(yourMap), SellingReportRequestModel.class);
            holder.titleKegiatan.setText("Selling");
            holder.detailKegiatan.setText(requestModel.getInvoice_number() + " - " + requestModel.getUnique_code());
            holder.locationTV.setText(requestModel.getInvoice_date());
            holder.img.setImageResource(R.drawable.icon_selling);
            holder.btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doPostSelling(requestModel,holder.getAdapterPosition());
                }
            });
        }else if(holder.mItem.type == 2) {
            //distribution
            final DistributionPostRequest requestModel =  gson.fromJson(gson.toJsonTree(yourMap), DistributionPostRequest.class);
            holder.titleKegiatan.setText("Distribution");
            //holder.detailKegiatan.setText(requestModel.outboundId + " - " + requestModel.quantity);
            if (requestModel.item != null && requestModel.item.size() > 0) {
                holder.locationTV.setText(requestModel.item.get(0).remarks);
            }
            holder.img.setImageResource(R.drawable.icon_distribution);
            holder.btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doPostDistribution(requestModel,holder.getAdapterPosition());
                }
            });
        }

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveDraft(holder.getAdapterPosition());
            }
        });
    }


    private void doPostAttendance(PostAttendanceRequestModel requestModel, final int position) {
        listener.showLoading(true);
        Subscription subscribe = salesAppService.postAttendance(requestModel, new SalesAppService.ServiceCallback<Response<String>>() {

            @Override
            public void onSuccess(Response<String> response) {
                listener.showLoading(false);

                if (response.isSuccess()) {
                    listener.onSuccessPost(response.message);
                    listener.onRemoveDraft(position);
                } else {
                    listener.onErrorPost(response.message);
                }
            }

            @Override
            public void onError(NetworkError error) {
                listener.showLoading(false);
                listener.onErrorPost(error.getAppErrorMessage());
            }
        });
        compositeSubscription.add(subscribe);
    }

    private void doPostDistribution(final DistributionPostRequest distributionPostRequest,final int position) {
        listener.showLoading(true);
        Subscription subscribe = salesAppService.postDistribution(distributionPostRequest,
                new SalesAppService.ServiceCallback<Response<DistributionPostResponse>>() {
                    @Override
                    public void onSuccess(Response<DistributionPostResponse> response) {
                        listener.showLoading(false);

                        if (response.isSuccess()) {
                            listener.onSuccessPost(response.message);
                            listener.onRemoveDraft(position);
                        } else {
                            listener.onErrorPost(response.message);
                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        listener.showLoading(false);
                        listener.onErrorPost(error.getAppErrorMessage());
                    }
                });

        compositeSubscription.add(subscribe);
    }

    private void doPostSelling(SellingReportRequestModel sellingReportRequestModel,final int position) {
        listener.showLoading(true);
        Subscription subscribe = salesAppService.postSelling(sellingReportRequestModel, new SalesAppService.ServiceCallback<Response<String>>() {

            @Override
            public void onSuccess(Response<String> response) {
                listener.showLoading(false);
                if (response.isSuccess()) {
                    listener.onSuccessPost(response.message);
                    listener.onRemoveDraft(position);
                }
                else
                    listener.onErrorPost(response.message);
            }

            @Override
            public void onError(NetworkError error) {
                listener.showLoading(false);
                listener.onErrorPost(error.getAppErrorMessage());
            }
        });
        compositeSubscription.add(subscribe);
    }

    public void setListener(DraftActivity listener) {
        this.listener = listener;
    }

    public interface OnDraftListener{
        void showLoading(boolean b);
        void onSuccessPost(String response);
        void onErrorPost(String message);

        void onRemoveDraft(int position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleKegiatan;
        public final TextView detailKegiatan;
        public final ImageView btnRemove;
        public final ImageView btnRetry;
        public final TextView locationTV;
        public DraftModel mItem;
        public final ImageView img;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleKegiatan = (TextView)view.findViewById(R.id.titleTV);
            detailKegiatan = (TextView)view.findViewById(R.id.dateTV);
            locationTV = (TextView)view.findViewById(R.id.locationTV);
            btnRemove = (ImageView)view.findViewById(R.id.btnRemove);
            btnRetry = (ImageView)view.findViewById(R.id.btnRetry);
            img = (ImageView)view.findViewById(R.id.ImgList);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}

