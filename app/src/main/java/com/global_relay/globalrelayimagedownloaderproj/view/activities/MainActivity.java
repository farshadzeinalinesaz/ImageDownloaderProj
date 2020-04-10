package com.global_relay.globalrelayimagedownloaderproj.view.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.global_relay.globalrelayimagedownloaderproj.R;
import com.global_relay.globalrelayimagedownloaderproj.model.to.ImageTO;
import com.global_relay.globalrelayimagedownloaderproj.utils.Utils;
import com.global_relay.globalrelayimagedownloaderproj.view.fragments.ImageDownloadFragment;
import com.global_relay.globalrelayimagedownloaderproj.view.fragments.ImagePreviewFragment;
import com.global_relay.globalrelayimagedownloaderproj.view.fragments.PreDownloadConfigurationFragment;
import com.global_relay.globalrelayimagedownloaderproj.view_model.ImageDownloaderViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.worldline.breadcrumbview.BreadcrumbView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, Observer<List<ImageTO>> {
    private Utils utils;
    private ImageDownloaderViewModel imageDownloaderViewModel;
    private LiveData<List<ImageTO>> userListLiveData;
    private Snackbar snackBarNoInternet;


    private String[] pagesTitles;

    private List<Fragment> fragmentList = new ArrayList<>();
    private ImagePreviewFragment imagePreviewFragment;
    private PreDownloadConfigurationFragment preDownloadConfigurationFragment;
    private ImageDownloadFragment imageDownloadFragment;

    private ViewPagerAdapter viewPagerAdapter;

    @BindView(R.id.rootConstraintLayout)
    public ConstraintLayout rootConstraintLayout;
    @BindView(R.id.viewPagerContent)
    public ViewPager viewPagerContent;
    @BindView(R.id.breadCrumbHeader)
    public BreadcrumbView breadCrumbHeader;
    @BindView(R.id.btnBackStep)
    public MaterialButton btnBackStep;
    @BindView(R.id.btnNextStep)
    public MaterialButton btnNextStep;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    utils.cancelSnackBar(snackBarNoInternet);
                } else {
                    setupNoInternetSnackBar();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        utils = Utils.getInstance(this);
        setTitle(getResources().getString(R.string.app_name_title));
        initFragmentsList();
        btnBackStep.setEnabled(false);
        pagesTitles = getResources().getStringArray(R.array.pagesTitles);
        setupViewPagerContent();
        imageDownloaderViewModel = ViewModelProviders.of(this).get(ImageDownloaderViewModel.class);
        userListLiveData = imageDownloaderViewModel.getImagesList();
        userListLiveData.observe(this, this);


        if (!utils.isInternetAvailable()) {
            setupNoInternetSnackBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, getReceiverIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public ImageDownloaderViewModel getImageDownloaderViewModel() {
        return imageDownloaderViewModel;
    }

    private void initFragmentsList() {
        imagePreviewFragment = ImagePreviewFragment.newInstance();
        preDownloadConfigurationFragment = PreDownloadConfigurationFragment.newInstance();
        imageDownloadFragment = ImageDownloadFragment.newInstance();
        fragmentList.add(imagePreviewFragment);
        fragmentList.add(preDownloadConfigurationFragment);
        fragmentList.add(imageDownloadFragment);
    }

    private void setupViewPagerContent() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList);
        viewPagerContent.setAdapter(viewPagerAdapter);
        viewPagerContent.setCurrentItem(0);
        viewPagerContent.addOnPageChangeListener(this);
    }

    private void setupNoInternetSnackBar() {
        snackBarNoInternet = utils.showSnackBar(rootConstraintLayout, getResources().getString(R.string.internet_disconnected), Snackbar.LENGTH_INDEFINITE, getResources().getString(R.string.check), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.openDeviceConnectionSetting();
            }
        });
    }

    private IntentFilter getReceiverIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        // Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        return intentFilter;
    }


    @OnClick({R.id.btnNextStep, R.id.btnBackStep})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btnNextStep: {
                viewPagerContent.setCurrentItem(viewPagerContent.getCurrentItem() + 1);
                break;
            }
            case R.id.btnBackStep: {
                viewPagerContent.setCurrentItem(viewPagerContent.getCurrentItem() - 1);
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        btnBackStep.setEnabled(position != 0);
        btnNextStep.setEnabled(position != fragmentList.size() - 1);
        breadCrumbHeader.setPath(pagesTitles[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onChanged(List<ImageTO> imageTOS) {

    }

    // TODO: 09/04/20 Update this code and replace with interface later
    public void startObservingFragment2(String webData) {
        preDownloadConfigurationFragment.startObserving(webData);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragmentList;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragmentList) {
            super(fm, behavior);
            this.fragmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList != null ? fragmentList.size() : 0;
        }
    }
}
