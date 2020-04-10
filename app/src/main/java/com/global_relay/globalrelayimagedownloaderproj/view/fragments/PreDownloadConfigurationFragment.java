package com.global_relay.globalrelayimagedownloaderproj.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.global_relay.globalrelayimagedownloaderproj.R;
import com.global_relay.globalrelayimagedownloaderproj.model.to.ImageTO;
import com.global_relay.globalrelayimagedownloaderproj.view_model.ImageDownloadViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreDownloadConfigurationFragment extends Fragment implements Observer<List<ImageTO>> {
    private static final int FILE_CHOOSER_RQ = 100;

    public ImageDownloadViewModel imageDownloadViewModel;
    private MutableLiveData<List<ImageTO>> imageMutableLiveData = new MutableLiveData<>();

    private LinearLayoutManager linearLayoutManager;
    private ImagePreviewRecycleAdapter imagePreviewRecycleAdapter;


    @BindView(R.id.btnChangeSaveLocation)
    public MaterialButton btnChangeSaveLocation;
    @BindView(R.id.switchDefaultLocation)
    public SwitchMaterial switchDefaultLocation;
    @BindView(R.id.txtSaveLocationVal)
    public TextView txtSaveLocationVal;
    @BindView(R.id.recycleImagePreview)
    public RecyclerView recycleImagePreview;

    public PreDownloadConfigurationFragment() {
    }

    public static PreDownloadConfigurationFragment newInstance() {
        PreDownloadConfigurationFragment fragment = new PreDownloadConfigurationFragment();
        return fragment;
    }

    private DiffUtil.ItemCallback<ImageTO> diffCallback = new DiffUtil.ItemCallback<ImageTO>() {
        @Override
        public boolean areItemsTheSame(@NonNull ImageTO oldItem, @NonNull ImageTO newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ImageTO oldItem, @NonNull ImageTO newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDesc().equals(newItem.getDesc());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pre_download_configuration, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        imageDownloadViewModel= ViewModelProviders.of(this).get(ImageDownloadViewModel.class);
        imageMutableLiveData = imageDownloadViewModel.getImageMutableLiveData();
        setupRecycleImagePreview();

        txtSaveLocationVal.setText(imageDownloadViewModel.getDataLocalPath());
    }

    public void startObserving(String webData) {
        imageMutableLiveData.observe(this, this);
        imageDownloadViewModel.startFetchingImages(webData);
    }


    private void setupRecycleImagePreview() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        imagePreviewRecycleAdapter = new ImagePreviewRecycleAdapter(diffCallback);
        recycleImagePreview.setLayoutManager(linearLayoutManager);
        imagePreviewRecycleAdapter.submitList(imageMutableLiveData.getValue());
        recycleImagePreview.setAdapter(imagePreviewRecycleAdapter);
    }

    @OnClick({R.id.switchDefaultLocation, R.id.btnChangeSaveLocation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switchDefaultLocation: {
                btnChangeSaveLocation.setEnabled(!switchDefaultLocation.isChecked());
                break;
            }
            case R.id.btnChangeSaveLocation: {
                new MaterialFilePicker()
                        .withActivity(getActivity())
                        .withRequestCode(FILE_CHOOSER_RQ)
                        .withHiddenFiles(true)
                        .start();
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void onChanged(List<ImageTO> imageTOS) {
        imagePreviewRecycleAdapter.submitList(imageMutableLiveData.getValue());
        recycleImagePreview.setAdapter(imagePreviewRecycleAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RQ && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            if (filePath != null && !filePath.isEmpty()) {
                imageDownloadViewModel.updateDataLocalPath(filePath);
                txtSaveLocationVal.setText(imageDownloadViewModel.getDataLocalPath());
            }
        }
    }


    public class ImagePreviewRecycleAdapter extends ListAdapter<ImageTO, ImagePreviewRecycleAdapter.ViewHolder> {

        public ImagePreviewRecycleAdapter(DiffUtil.ItemCallback<ImageTO> diffCallback) {
            super(diffCallback);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_download_config_card_item, parent, false);
            return new ImagePreviewRecycleAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setData(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.chkDownloadable)
            public CheckBox chkDownloadable;
            @BindView(R.id.imgPreview)
            public ImageView imgPreview;
            @BindView(R.id.txtTitle)
            public TextView txtTitle;
            @BindView(R.id.txtSubTitle)
            public TextView txtSubTitle;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(ViewHolder.this, itemView);
            }

            private void setData(int position) {
                ImageTO imageTO = getItem(position);
                txtTitle.setText(imageTO.getTitle());
                txtSubTitle.setText(imageTO.getDesc());
                imageDownloadViewModel.loadImage(getActivity(), imageTO.getImagePath(), imgPreview);
            }
        }
    }
}
