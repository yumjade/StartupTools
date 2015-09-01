package com.loopeer.android.apps.startuptools.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.laputapp.http.Response;
import com.loopeer.android.apps.startuptools.NavUtils;
import com.loopeer.android.apps.startuptools.R;
import com.loopeer.android.apps.startuptools.StartUpToolsApp;
import com.loopeer.android.apps.startuptools.adapter.PrimaryListAdapter;
import com.loopeer.android.apps.startuptools.adapter.SecondaryListAdapter;
import com.loopeer.android.apps.startuptools.cache.CacheApis;
import com.loopeer.android.apps.startuptools.cache.Callbacks;
import com.loopeer.android.apps.startuptools.model.Category;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;


public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private final static int SPLASH_DISPLAY_LENGTH = 1500;

    @InjectView(R.id.launch_splash)
    ImageView mLaunchSplashImageView;
    @InjectView(R.id.primary_list)
    ListView mPrimaryList;
    @InjectView(R.id.secondary_list)
    ListView mSecondaryList;
    @InjectView(R.id.emptyView_progressBar)
    ProgressBar mProgressBar;
    @InjectView(R.id.emptyView_textView)
    TextView mEmptyViewTextView;
    @InjectView(R.id.toolbar_overlay)
    LinearLayout mOverlayMenu;

    private PrimaryListAdapter mPrimaryListAdapter;

    private boolean mIsOverlayMenuOpen;

    private SecondaryListAdapter mSecondaryListAdapter;
    private ArrayList<Category>[] mCategories = new ArrayList[8];

    private int mCurrentParentItemPosition;

    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mIsInterceptTouchEvent = true;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mLaunchSplashImageView.setVisibility(View.GONE);
                mIsInterceptTouchEvent = false;
            }
        }, SPLASH_DISPLAY_LENGTH);

        UmengUpdateAgent.update(this);

        setSwipeBackEnable(false);

        setupPrimaryList();
        setupSecondaryList();
        setupOverlayMenu();
        setupActionBarToolbar();
    }

    @SuppressWarnings("unused")
    @OnClick({
            R.id.overlay_item_recommend,
            R.id.overlay_item_suggest,
            R.id.overlay_item_about
    })
    void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.overlay_item_recommend:
                intent.setClass(this, FeedbackActivity.class);
                intent.putExtra(NavUtils.EXTRA_TYPE, FeedbackActivity.TYPE_RECOMMEND);
                break;
            case R.id.overlay_item_suggest:
                intent.setClass(this, FeedbackActivity.class);
                intent.putExtra(NavUtils.EXTRA_TYPE, FeedbackActivity.TYPE_SUGGEST);
                break;
            case R.id.overlay_item_about:
                intent.setClass(this, AboutActivity.class);
                break;
        }
        startActivity(intent);
        toggleOverlayMenu();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (mIsInterceptTouchEvent) {
            return true;
        }

        if (mIsOverlayMenuOpen && ev.getAction() == MotionEvent.ACTION_DOWN) {
            int[] location = new int[2];
            mActionBarToolbar.getLocationOnScreen(location);
            if (ev.getY() > (location[1] + mActionBarToolbar.getHeight())) {
                toggleOverlayMenu();
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onBackUp() {
        return false;
    }

    private void getRemoteCategories(final int parentId, final int page, final int pageSize) {
        mConnectService.getCategories(parentId, page, pageSize, new Callback<Response<ArrayList<Category>>>() {
            @Override
            public void success(Response<ArrayList<Category>> categoriesResponse, retrofit.client.Response response) {
                if (!categoriesResponse.isSuccessed()) {
                    Toast.makeText(MainActivity.this, categoriesResponse.mMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (categoriesResponse.mData != null) {
                    /* in case the server doesn't assign parentId & sort */
                    for (int i = 0; i < categoriesResponse.mData.size(); i++) {
                        categoriesResponse.mData.get(i).parentId = parentId;
                        categoriesResponse.mData.get(i).sort = (page - 1) * pageSize + i;
                    }
                    CacheApis.getCategoryCache().save(categoriesResponse.mData, parentId);

                    if (mCategories[parentId - 1] == null || page == NavUtils.FIRST_PAGE) {
                        mCategories[parentId - 1] = categoriesResponse.mData;
                    } else {
                        mCategories[parentId - 1].addAll(categoriesResponse.mData);
                    }
                }

                if (mCurrentParentItemPosition == parentId - 1) {
                    isLoading = false;
                    updateView();
                }

                if (categoriesResponse.mTotalSize > page * pageSize) {
                    getRemoteCategories(parentId, page + 1, pageSize);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    Toast.makeText(MainActivity.this, R.string.toast_network_error, Toast.LENGTH_SHORT).show();
                }

                if (mCurrentParentItemPosition == parentId - 1) {
                    isLoading = false;
                    updateView();
                }
            }
        });
    }

    private void updateView() {
        mSecondaryListAdapter.setData(mCategories[mCurrentParentItemPosition]);
        mSecondaryListAdapter.notifyDataSetChanged();

        mEmptyViewTextView.setVisibility(
                !isLoading && mCategories[mCurrentParentItemPosition] != null && mCategories[mCurrentParentItemPosition].size() == 0
                        ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(
                (mCategories[mCurrentParentItemPosition] == null || mCategories[mCurrentParentItemPosition].size() == 0) && isLoading
                        ? View.VISIBLE : View.GONE);
    }

    private void setupPrimaryList() {
        mPrimaryListAdapter = new PrimaryListAdapter(MainActivity.this);
        mPrimaryList.setAdapter(mPrimaryListAdapter);
        mPrimaryList.setOnItemClickListener(this);
        mPrimaryList.setVerticalScrollBarEnabled(false);
        mPrimaryList.setItemChecked(0, true);
        getCategories();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.primary_list:
                mPrimaryListAdapter.setCheckPosition(position);
                mCurrentParentItemPosition = position;
                if (mCategories[mCurrentParentItemPosition] == null) {
                    getCategories();
                }
                updateView();
                break;
            case R.id.secondary_list:
                Intent intent = new Intent(this, ProductsActivity.class);
                intent.putExtra(NavUtils.EXTRA_PARENT_ITEM_POSITION, mCurrentParentItemPosition);
                intent.putExtra(NavUtils.EXTRA_CATEGORY_ID, mCategories[mCurrentParentItemPosition].get(position).id);
                intent.putExtra(NavUtils.EXTRA_CATEGORY_TITLE, mCategories[mCurrentParentItemPosition].get(position).title);
                startActivity(intent);
                mIsInterceptTouchEvent = true;
                break;
        }
    }

    private void setupSecondaryList() {
        mSecondaryListAdapter = new SecondaryListAdapter(this);
        mSecondaryList.setAdapter(mSecondaryListAdapter);
        mSecondaryList.setOnItemClickListener(this);

        /* backward compatibility */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mProgressBar.getIndeterminateDrawable().setColorFilter(
                    StartUpToolsApp.getAppResources().getColor(R.color.theme_accent), PorterDuff.Mode.SRC_IN);
        }
        mSecondaryList.setEmptyView(mProgressBar);

        updateView();
    }

    private void setupOverlayMenu() {
        mOverlayMenu.post(new Runnable() {
            @Override
            public void run() {
                mOverlayMenu.setTranslationY(-mOverlayMenu.getHeight());
            }
        });
    }

    private void toggleOverlayMenu() {
        if (mIsOverlayMenuOpen) {
            closeOverlayMenu();
            mIsOverlayMenuOpen = false;
        } else {
            openOverlayMenu();
            mIsOverlayMenuOpen = true;
        }
    }

    private void openOverlayMenu() {
        ObjectAnimator actionBarAnimator = ObjectAnimator.ofFloat(mActionBarToolbar, "translationY", mOverlayMenu.getHeight());
        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(mOverlayMenu, "translationY", 0);
        AnimatorSet set = new AnimatorSet();
        set.play(actionBarAnimator).with(overlayAnimator);
        set.start();
    }

    private void closeOverlayMenu() {
        ObjectAnimator actionBarAnimator = ObjectAnimator.ofFloat(mActionBarToolbar, "translationY", 0);
        ObjectAnimator overlayAnimator = ObjectAnimator.ofFloat(mOverlayMenu, "translationY", -mOverlayMenu.getHeight());
        AnimatorSet set = new AnimatorSet();
        set.play(actionBarAnimator).with(overlayAnimator);
        set.start();
    }

    protected void setupActionBarToolbar() {
        super.setupActionBarToolbar();
        mActionBarToolbar.setNavigationIcon(R.mipmap.ic_ab_menu);
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOverlayMenu();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void loadLocalCategories(final int parentId) {
        CacheApis.getCategoryCache().read(new Callbacks.ApiBaseCallback<ArrayList<Category>>() {
            @Override
            public void onCacheLoaded(ArrayList<Category> localData) {
                if (mCategories[parentId - 1] == null || mCategories[parentId - 1].size() == 0) {
                    mCategories[parentId - 1] = localData;
                }

                if (mCurrentParentItemPosition == parentId - 1) {
                    updateView();
                }
            }
        }, parentId);
    }

    private void getCategories() {
        isLoading = true;
        loadLocalCategories(mCurrentParentItemPosition + 1);
        getRemoteCategories(mCurrentParentItemPosition + 1, NavUtils.FIRST_PAGE, NavUtils.PAGE_SIZE);
    }
}
