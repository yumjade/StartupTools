package com.loopeer.android.apps.startuptools.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopeer.android.apps.startuptools.R;
import com.loopeer.android.apps.startuptools.StartUpToolsApp;
import com.loopeer.android.apps.startuptools.model.Product;
import com.loopeer.android.apps.startuptools.ui.view.FloatingActionButton;
import com.loopeer.android.apps.startuptools.util.ServiceUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by KorHsien on 2015/3/23.
 */
public class ProductFragment extends Fragment {

    @InjectView(R.id.product_imageView_logo)
    ImageView mLogoImageView;
    @InjectView(R.id.product_textView_name)
    TextView mNameTextView;
    @InjectView(R.id.product_textView_website)
    TextView mWebsiteTextView;
    @InjectView(R.id.product_textView_desc)
    TextView mDescTextView;
    @InjectView(R.id.fab)
    FloatingActionButton mFab;

    Product mProduct;

    public static ProductFragment newInstance(int pos) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int pos = getArguments() == null ? 0 : getArguments().getInt("pos");
        mProduct = ((StartUpToolsApp) StartUpToolsApp.getInstance()).getProducts().get(pos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        updateView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @SuppressWarnings("unused")
    @OnClick({
            R.id.product_textView_website,
            R.id.fab
    })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.product_textView_website:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mProduct.websiteUrl));
                startActivity(intent);
                break;
            case R.id.fab:
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(mProduct.linkUrl));
                startActivity(intent1);
                break;
        }
    }

    public void updateView() {
        ServiceUtils.getExternalPicasso().load(mProduct.logoUrl).placeholder(R.mipmap.logo_placeholder).into(mLogoImageView);
        mNameTextView.setText(mProduct.name);
        mWebsiteTextView.setVisibility(TextUtils.isEmpty(mProduct.websiteUrl) ? View.GONE : View.VISIBLE);
        mWebsiteTextView.setText(mProduct.websiteUrl);
        mDescTextView.setText(mProduct.desc);

        int paddingBottom;
        int visibility;
        if (!TextUtils.isEmpty(mProduct.linkUrl)) {
            paddingBottom = (int) getResources().getDimension(R.dimen.product_textView_paddingBottom);
            visibility = View.VISIBLE;
        } else {
            paddingBottom = 0;
            visibility = View.GONE;
        }
        mDescTextView.setPadding(0, 0, 0, paddingBottom);
        mFab.setVisibility(visibility);
    }
}