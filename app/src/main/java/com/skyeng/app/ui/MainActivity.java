package com.skyeng.app.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.skyeng.app.R;
import com.skyeng.app.SkyengApp;
import com.skyeng.app.login.ui.MainLoginFragment;
import com.skyeng.app.user.UserController;
import com.skyeng.app.util.FragmentUtil;
import com.skyeng.app.util.UiAvailabilityUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Inject
    UserController userController;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((SkyengApp) getApplication()).getInjector().inject(this);

        setupStatusBarColor();
        setupToolbar();
        if (userController.isAuthenticated()) {
            openMainFragment();
        } else {
            openLoginFragment(savedInstanceState);
        }
    }

    public void openLoginFragment() {
        openLoginFragment(null);
    }

    private void openLoginFragment(Object savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            final Fragment existingFragment = getExistingLoginFragment();
            if (existingFragment == null) {
                final MainLoginFragment loginFragment = new MainLoginFragment();
                loginFragment.setArguments(getIntent().getExtras());
                final String tag = MainLoginFragment.class.getName();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, loginFragment, tag)
                        .commitAllowingStateLoss();
            }
        }
    }

    private Fragment getExistingLoginFragment() {
        final String tag = MainLoginFragment.class.getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    private void setupStatusBarColor() {
        final Window window = getWindow();
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_green_dark));
            }
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void openMainFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            removeLoginFragment();
            final String tag = MainFragment.class.getName();
            final Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (existingFragment == null) {
                if (UiAvailabilityUtil.isUiAvailable(this)) {
                    final MainFragment fragment = new MainFragment();
                    FragmentUtil.clearBackStack(this);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, fragment, tag)
                            .commitAllowingStateLoss();
                }
            }
        }
    }

    private void removeLoginFragment() {
        final Fragment existingLoginFragment = getExistingLoginFragment();
        if (existingLoginFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(existingLoginFragment)
                    .commitAllowingStateLoss();
        }
    }
}