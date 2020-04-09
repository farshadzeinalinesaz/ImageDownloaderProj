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
import com.global_relay.globalrelayimagedownloaderproj.view.fragments.ImagePreviewFragment;
import com.google.android.material.button.MaterialButton;
import com.worldline.breadcrumbview.BreadcrumbView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> fragmentList = new ArrayList<>();
    private ImagePreviewFragment imagePreviewFragment;

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
        setupViewPagerContent();
    }

    private void initFragmentsList() {
        imagePreviewFragment = ImagePreviewFragment.newInstance();
        fragmentList.add(imagePreviewFragment);
    }

    private void setupViewPagerContent() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList);
        viewPagerContent.setAdapter(viewPagerAdapter);
        viewPagerContent.setCurrentItem(0);
    }


    @OnClick({R.id.btnNextStep, R.id.btnBackStep})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.btnNextStep: {
                break;
            }
            case R.id.btnBackStep: {
                break;
            }
            default: {
                break;
            }
        }
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
