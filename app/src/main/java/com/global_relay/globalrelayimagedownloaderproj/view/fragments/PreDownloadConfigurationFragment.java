package com.global_relay.globalrelayimagedownloaderproj.view.fragments;

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
import com.global_relay.globalrelayimagedownloaderproj.view_model.ShareImageDownloadViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreDownloadConfigurationFragment extends Fragment implements Observer<List<ImageTO>> {
    public ShareImageDownloadViewModel shareImageDownloadViewModel;
    private MutableLiveData<String> webData;
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
        shareImageDownloadViewModel = ViewModelProviders.of(getActivity()).get(ShareImageDownloadViewModel.class);
        imageMutableLiveData = shareImageDownloadViewModel.getImageMutableLiveData();
        imageMutableLiveData.observe(getViewLifecycleOwner(), this);
        webData = shareImageDownloadViewModel.getWebData();
        webData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String webData) {
                shareImageDownloadViewModel.startFetchingImages(webData);
            }
        });
        setupRecycleImagePreview();

        txtSaveLocationVal.setText(shareImageDownloadViewModel.getDataLocalPath());
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
                new ChooserDialog(getActivity())
                        .withFilter(true, false)
                        .withStartFile(shareImageDownloadViewModel.getDataLocalPath())
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                if (path != null && !path.isEmpty()) {
                                    shareImageDownloadViewModel.updateDataLocalPath(path);
                                    txtSaveLocationVal.setText(shareImageDownloadViewModel.getDataLocalPath());
                                }
                            }
                        })
                        .build()
                        .show();
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
                shareImageDownloadViewModel.loadImage(getActivity(), imageTO.getImagePath(), imgPreview);
            }
        }
    }
}
