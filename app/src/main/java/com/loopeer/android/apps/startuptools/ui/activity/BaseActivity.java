package com.loopeer.android.apps.startuptools.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import com.loopeer.android.apps.startuptools.R;
import com.loopeer.android.apps.startuptools.api.service.ConnectService;
import com.loopeer.android.apps.startuptools.util.ServiceUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.InjectView;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by tudou on 15-3-20.
 */
public class BaseActivity extends SwipeBackActivity {

    @InjectView(R.id.toolbar_actionbar)
    Toolbar mActionBarToolbar;

    protected ConnectService mConnectService;

    protected boolean mIsInterceptTouchEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnectService = ServiceUtils.getApiService().connectService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        mIsInterceptTouchEvent = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * Converts an intent into a {@link android.os.Bundle} suitable for use as fragment arguments.
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

    /**
     * Converts a fragment arguments bundle into an intent.
     */
    public static Intent fragmentArgumentsToIntent(Bundle arguments) {
        Intent intent = new Intent();
        if (arguments == null) {
            return intent;
        }

        final Uri data = arguments.getParcelable("_uri");
        if (data != null) {
            intent.setData(data);
        }

        intent.putExtras(arguments);
        intent.removeExtra("_uri");
        return intent;
    }

    public boolean isImmActive() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.isActive();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void setupActionBarToolbar() {
        setSupportActionBar(mActionBarToolbar);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return onBackUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (!onBackUp()) {
            super.onBackPressed();
        }
    }

    public boolean onBackUp() {
        switch (this.getClass().getSimpleName()) {
            case "ProductsActivity":
            case "ProductActivity":
                getSwipeBackLayout().scrollToFinishActivity();
                break;
            case "FeedbackActivity":
            case "AboutActivity":
                finish();
                break;
        }
        return true;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        switch (intent.getComponent().getShortClassName()) {
            case ".ui.activity.ProductsActivity":
            case ".ui.activity.ProductActivity":
                overridePendingTransition(R.anim.slide_in, 0);
                break;
            case ".ui.activity.FeedbackActivity":
            case ".ui.activity.AboutActivity":
                overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        switch (this.getClass().getSimpleName()) {
            case "ProductsActivity":
            case "ProductActivity":
                overridePendingTransition(0, 0);
                break;
            case "FeedbackActivity":
            case "AboutActivity":
                overridePendingTransition(R.anim.activity_close_enter, R.anim.activity_close_exit);
                break;
        }
    }
}
