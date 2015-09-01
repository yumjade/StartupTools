package com.loopeer.android.apps.startuptools.ui.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.laputapp.http.Response;
import com.loopeer.android.apps.startuptools.NavUtils;
import com.loopeer.android.apps.startuptools.R;
import com.loopeer.android.apps.startuptools.StartUpToolsApp;
import com.loopeer.android.apps.startuptools.adapter.ProductsListAdapter;
import com.loopeer.android.apps.startuptools.cache.CacheApis;
import com.loopeer.android.apps.startuptools.cache.Callbacks;
import com.loopeer.android.apps.startuptools.model.Product;
import com.loopeer.android.apps.startuptools.util.ThemeUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by KorHsien on 2015/3/24.
 */
public class ProductsActivity extends BaseActivity implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.products_list)
    ListView mProductsList;
    @InjectView(R.id.emptyView_progressBar)
    ProgressBar mProgressBar;
    @InjectView(R.id.emptyView_textView)
    TextView mEmptyViewTextView;

    private ProductsListAdapter mProductsListAdapter;
    private ArrayList<Product> mProducts;
    private int mParentItemPosition;
    private int mCategoryId;
    private int mTintColor;

    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.inject(this);

        mParentItemPosition = getIntent().getIntExtra(NavUtils.EXTRA_PARENT_ITEM_POSITION, 0);
        mCategoryId = getIntent().getIntExtra(NavUtils.EXTRA_CATEGORY_ID, 0);
        setTitle(getIntent().getStringExtra(NavUtils.EXTRA_CATEGORY_TITLE));
        setTheme(ThemeUtils.getThemeByParentItemPosition(mParentItemPosition));
        mTintColor = StartUpToolsApp.getAppResources().getIntArray(R.array.primary_list_colors)[mParentItemPosition];

        setupActionBarToolbar();
        setupSwipeRefreshLayout();
        setupProductsList();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        return mIsInterceptTouchEvent || super.dispatchTouchEvent(ev);
    }

    protected void setupActionBarToolbar() {
        super.setupActionBarToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarToolbar.setNavigationIcon(R.mipmap.ic_ab_back);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActionBarToolbar.getNavigationIcon().setTint(mTintColor);
        } else {
            mActionBarToolbar.getNavigationIcon().setColorFilter(mTintColor, PorterDuff.Mode.SRC_IN);
        }
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(mTintColor);
    }

    private void setupProductsList() {
        mProductsListAdapter = new ProductsListAdapter(this);
        mProductsList.setAdapter(mProductsListAdapter);
        mProductsList.setOnItemClickListener(this);
        mProgressBar.setVisibility(View.GONE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mProgressBar.getIndeterminateDrawable().setTint(mTintColor);
//        } else {
//            mProgressBar.getIndeterminateDrawable().setColorFilter(mTintColor, PorterDuff.Mode.SRC_IN);
//        }
//        mProductsList.setEmptyView(mProgressBar);
        getProducts();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(NavUtils.EXTRA_PARENT_ITEM_POSITION, mParentItemPosition);
        intent.putExtra(NavUtils.EXTRA_PRODUCT_SORT, position);
        ((StartUpToolsApp) StartUpToolsApp.getInstance()).setProducts(mProducts);
        startActivity(intent);
        mIsInterceptTouchEvent = true;
    }

    private void getRemoteProducts(final int categoryId, final int page, final int pageSize) {
        mConnectService.getProducts(categoryId, page, pageSize, new Callback<Response<ArrayList<Product>>>() {
            @Override
            public void success(Response<ArrayList<Product>> productsResponse, retrofit.client.Response response) {
                if (!productsResponse.isSuccessed()) {
                    Toast.makeText(ProductsActivity.this, productsResponse.mMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (productsResponse.mData != null) {
                    /* in case the server doesn't assign categoryId & sort */
                    for (int i = 0; i < productsResponse.mData.size(); i++) {
                        productsResponse.mData.get(i).categoryId = categoryId;
                        productsResponse.mData.get(i).sort = (page - 1) * pageSize + i;
                    }
                    CacheApis.getProductCache().save(productsResponse.mData, categoryId);

                    if (mProducts == null || page == NavUtils.FIRST_PAGE) {
                        mProducts = productsResponse.mData;
                    } else {
                        mProducts.addAll(productsResponse.mData);
                    }
                }

                isLoading = false;
                updateView();
                mSwipeRefreshLayout.setRefreshing(false);

                if (productsResponse.mTotalSize > page * pageSize) {
                    getRemoteProducts(categoryId, page + 1, pageSize);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    Toast.makeText(ProductsActivity.this, R.string.toast_network_error, Toast.LENGTH_SHORT).show();
                }

                isLoading = false;
                updateView();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateView() {
        mProductsListAdapter.setData(mProducts);
        mProductsListAdapter.notifyDataSetChanged();

        mEmptyViewTextView.setVisibility(
                !isLoading && mProducts != null && mProducts.size() == 0 ? View.VISIBLE : View.GONE);
//        mProgressBar.setVisibility(
//                (mProducts == null || mProducts.size() == 0) && isLoading ? View.VISIBLE : View.GONE);
        mSwipeRefreshLayout.setRefreshing((mProducts == null || mProducts.size() == 0) && isLoading);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("category_id", mCategoryId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCategoryId = savedInstanceState.getInt("category_id");
    }

    @SuppressWarnings("unchecked")
    private void loadLocalProducts(final int categoryId) {
        CacheApis.getProductCache().read(new Callbacks.ApiBaseCallback<ArrayList<Product>>() {
            @Override
            public void onCacheLoaded(ArrayList<Product> localData) {
                if (mProducts == null || mProducts.size() == 0) {
                    mProducts = localData;
                }

                updateView();
            }
        }, categoryId);
    }

    private void getProducts() {
        isLoading = true;
        loadLocalProducts(mCategoryId);
        getRemoteProducts(mCategoryId, NavUtils.FIRST_PAGE, NavUtils.PAGE_SIZE);
    }

    @Override
    public void onRefresh() {
        getProducts();
    }
}
