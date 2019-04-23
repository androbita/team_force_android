package com.app.salesapp.notification;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.databinding.ActivityNotificationBinding;
import com.app.salesapp.fcm.NotificationModel;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.timeline.adapter.TimelineItemViewModel;
import com.app.salesapp.timeline.comment.TimelineCommentAdapter;
import com.app.salesapp.timeline.custom.RoundedCornersTransformation;
import com.app.salesapp.timeline.model.TimelineResponse;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.RxBus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import javax.inject.Inject;

public class DetailCommentActivity extends AppCompatActivity implements DetailCommentView {
    @Inject
    RxBus rxBus;

    @Inject
    UserService userService;

    @Inject
    SalesAppService salesAppService;

    DetailCommentPresenter presenter;

    ActivityNotificationBinding binding;
    private Object detailTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);
        presenter = new DetailCommentPresenter(this, salesAppService, userService);
        ImageView logo = (ImageView)findViewById(R.id.app_logo);
        Glide.with(getApplicationContext()).load(userService.getUserLogo())
                .error(R.drawable.sales_club_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);
        getDetailTimeline();

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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void showDetailComment(DetailTimelineModel response) {
        TimelineResponse.TimelineList temp = new TimelineResponse().new TimelineList();
        temp.comments = response.comment;
        temp.datetime = response.datetime;
        temp.location = response.location;
        temp.photo = response.photo;
        temp.photoProfile = response.photo_profile;
        temp.remark = response.remark;
        temp.timelineId = response.timeline_id;
        temp.totalComment = response.total_comment;
        temp.type = response.type;
        temp.username = response.username;
        final TimelineResponse.TimelineList data = temp;
        final TimelineCommentAdapter timelineCommentAdapter = new TimelineCommentAdapter(this, temp.comments);
        final TimelineItemViewModel viewModel = new TimelineItemViewModel(this, temp);

        Glide.with(getApplicationContext()).load(temp.photoProfile)
                .transform(new RoundedCornersTransformation(getApplicationContext(), 6, 0))
                .placeholder(R.drawable.ic_profile_blue)
                .error(R.drawable.ic_profile_blue)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.timelineDetail.imgUserTimeline);

        Glide.with(getApplicationContext()).load(temp.photoProfile)
                .transform(new RoundedCornersTransformation(getApplicationContext(), 6, 0))
                .placeholder(R.drawable.ic_profile_blue)
                .error(R.drawable.ic_profile_blue)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.timelineDetail.layoutTimelineItemDetail.imgUserDetailTimeline);

        Glide.with(getApplicationContext()).load(temp.photo)
                .placeholder(R.drawable.ic_camera)
                .error(R.drawable.ic_camera)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.timelineDetail.imgDetailTimeline);

        viewModel.setCommentVisibility(true);

        binding.timelineDetail.textCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCommentVisibility(viewModel.isCommentVisibility() ? false : true);
            }
        });

        binding.timelineDetail.imgUserTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setUserDetailVisibility(viewModel.isUserDetailVisibility() ? false : true);
            }
        });

        binding.timelineDetail.textLoadMoreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setAllCommentHasShow(true);
                timelineCommentAdapter.replaceData(data.comments);
                binding.timelineDetail.listComments.setAdapter(timelineCommentAdapter);
            }
        });

        binding.timelineDetail.inputComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textName, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    presenter.postComment(data.timelineId, textName.getText().toString());
                }

                return true;
            }
        });

        binding.timelineDetail.imgSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.postComment(data.timelineId, binding.timelineDetail.inputComment.getText().toString());
            }
        });

        binding.timelineDetail.listComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.timelineDetail.listComments.setAdapter(timelineCommentAdapter);
        binding.timelineDetail.setViewModel(viewModel);
        binding.setViewModel(viewModel);
        binding.timelineDetail.executePendingBindings();
    }

    @Override
    public void showToastMessage(String s) {

    }

    @Override
    public void setLoading(boolean b) {
        if(b) {
            binding.progressLayout.setVisibility(View.VISIBLE);
            binding.timelineLayout.setVisibility(View.GONE);
        }
        else {
            binding.progressLayout.setVisibility(View.GONE);
            binding.timelineLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showSnakeBar(String message) {

    }

    @Override
    public void postCommentSuccess() {
        binding.timelineDetail.inputComment.setText("");
        getDetailTimeline();
    }

    @Override
    public void isAuthFailure() {
        Intent intent = new Intent(DetailCommentActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    public void getDetailTimeline() {
        NotificationModel data = userService.getCurrentNotification();
        if(data!=null)
            presenter.getDetailTimeline(data);
    }
}
