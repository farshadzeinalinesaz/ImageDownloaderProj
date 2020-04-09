package com.global_relay.globalrelayimagedownloaderproj.view.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.global_relay.globalrelayimagedownloaderproj.R;
import com.global_relay.globalrelayimagedownloaderproj.view.fragments.ImageDownloadFragment;
import com.global_relay.globalrelayimagedownloaderproj.view.fragments.ImagePreviewFragment;
import com.global_relay.globalrelayimagedownloaderproj.view.fragments.PreDownloadConfigurationFragment;
import com.google.android.material.button.MaterialButton;
import com.worldline.breadcrumbview.BreadcrumbView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener
{
    private String[] pagesTitles;

    private List<Fragment> fragmentList = new ArrayList<>();
    private ImagePreviewFragment imagePreviewFragment;
    private PreDownloadConfigurationFragment preDownloadConfigurationFragment;
    private ImageDownloadFragment imageDownloadFragment;

    private ViewPagerAdapter viewPagerAdapter;


    @BindView(R.id.viewPagerContent)
    public ViewPager viewPagerContent;
    @BindView(R.id.breadCrumbHeader)
    public BreadcrumbView breadCrumbHeader;
    @BindView(R.id.btnBackStep)
    public MaterialButton btnBackStep;
    @BindView(R.id.btnNextStep)
    public MaterialButton btnNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragmentsList();
        btnBackStep.setEnabled(false);
        pagesTitles=getResources().getStringArray(R.array.pagesTitles);
        setupViewPagerContent();
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


    @OnClick({R.id.btnNextStep, R.id.btnBackStep})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btnNextStep: {
                    viewPagerContent.setCurrentItem(viewPagerContent.getCurrentItem()+1);
                break;
            }
            case R.id.btnBackStep: {
                viewPagerContent.setCurrentItem(viewPagerContent.getCurrentItem()-1);
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
