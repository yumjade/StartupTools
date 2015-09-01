package com.loopeer.android.apps.startuptools.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.laputapp.http.Response;
import com.loopeer.android.apps.startuptools.NavUtils;
import com.loopeer.android.apps.startuptools.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by KorHsien on 2015/3/23.
 */
public class FeedbackActivity extends BaseActivity {

    public final static int TYPE_RECOMMEND = 0;
    public final static int TYPE_SUGGEST = 1;

    private int mType;

    private int mNoContentToastRes;
    private int mThanksToastRes;

    @InjectView(R.id.feedback_editText_content)
    EditText mContentEditText;
    @InjectView(R.id.feedback_editText_contact)
    EditText mContactEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);

        setSwipeBackEnable(false);

        mType = getIntent().getIntExtra(NavUtils.EXTRA_TYPE, TYPE_RECOMMEND);

        setupActionBarToolbar();
        setupEditText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done:
                sendFeedback();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setupActionBarToolbar() {
        super.setupActionBarToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionBarToolbar.setNavigationIcon(R.mipmap.ic_ab_back);
    }

    private void setupEditText() {
        switch (mType) {
            case TYPE_RECOMMEND:
                setTitle(getString(R.string.label_recommend));
                mContentEditText.setHint(getString(R.string.recommend_content_hint));
                mNoContentToastRes = R.string.toast_no_content_recommend;
                mThanksToastRes = R.string.toast_thanks_recommend;
                break;
            case TYPE_SUGGEST:
                setTitle(getString(R.string.label_suggest));
                mContentEditText.setHint(getString(R.string.suggest_content_hint));
                mNoContentToastRes = R.string.toast_no_content_suggest;
                mThanksToastRes = R.string.toast_thanks_suggest;
                break;
        }
    }

    private void sendFeedback() {
        String content = mContentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(FeedbackActivity.this, mNoContentToastRes, Toast.LENGTH_SHORT).show();
            return;
        }
        String contact = mContactEditText.getText().toString();
        Callback<Response> callback = new Callback<Response>() {
            @Override
            public void success(Response response, retrofit.client.Response response2) {
                if (!response.isSuccessed()) {
                    Toast.makeText(FeedbackActivity.this, response.mMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(FeedbackActivity.this, mThanksToastRes, Toast.LENGTH_SHORT).show();
                onBackUp();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    Toast.makeText(FeedbackActivity.this, R.string.toast_network_error, Toast.LENGTH_SHORT).show();
                }
            }
        };
        switch (mType) {
            case TYPE_RECOMMEND:
                mConnectService.sendFeedback(content, contact, callback);
                break;
            case TYPE_SUGGEST:
                mConnectService.contribute(content, contact, callback);
                break;
        }
    }
}
