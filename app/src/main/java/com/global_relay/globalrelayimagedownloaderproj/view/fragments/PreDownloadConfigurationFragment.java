package com.global_relay.globalrelayimagedownloaderproj.view.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.global_relay.globalrelayimagedownloaderproj.R;
import com.global_relay.globalrelayimagedownloaderproj.model.to.ImageTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreDownloadConfigurationFragment extends Fragment {
    private LinearLayoutManager linearLayoutManager;
    private ImagePreviewRecycleAdapter imagePreviewRecycleAdapter;


    @BindView(R.id.btnChangeSaveLocation)
    public MaterialButton btnChangeSaveLocation;
    @BindView(R.id.switchDefaultLocation)
    public SwitchMaterial switchDefaultLocation;
    @BindView(R.id.txtSaveLocationVal)
    public TextView txtSaveLocationVal;
    @BindView(R.id.switchShowNotification)
    public SwitchMaterial switchShowNotification;
    @BindView(R.id.recycleImagePreview)
    public RecyclerView recycleImagePreview;

    public PreDownloadConfigurationFragment() {
    }

    public static PreDownloadConfigurationFragment newInstance() {
        PreDownloadConfigurationFragment fragment = new PreDownloadConfigurationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pre_download_configuration, container, false);
        ButterKnife.bind(this, rootView);
        setupRecycleImagePreview();
        return rootView;
    }


    private void setupRecycleImagePreview() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        imagePreviewRecycleAdapter = new ImagePreviewRecycleAdapter();
        recycleImagePreview.setLayoutManager(linearLayoutManager);
        imagePreviewRecycleAdapter.submitList(fakeImageTOList());
        recycleImagePreview.setAdapter(imagePreviewRecycleAdapter);
    }


    private List<ImageTO> fakeImageTOList() {
        List<ImageTO> imageTOList = new ArrayList<>();
        imageTOList.add(new ImageTO(1, "Title 1", "Desc 1", null));
        imageTOList.add(new ImageTO(1, "Title 2", "Desc 2", null));
        imageTOList.add(new ImageTO(1, "Title 3", "Desc 3", null));
        return imageTOList;
    }

    @OnClick({R.id.switchDefaultLocation,R.id.switchShowNotification,R.id.btnChangeSaveLocation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switchDefaultLocation: {
                btnChangeSaveLocation.setEnabled(!switchDefaultLocation.isChecked());
                break;
            }
            case R.id.switchShowNotification: {

                break;
            }
            case R.id.btnChangeSaveLocation: {
                File initialFile = Environment.getExternalStorageDirectory();
                new ChooserDialog(getActivity())
                        .withFilter(true, false)
                        .withStartFile(initialFile.getAbsolutePath())
                        // to handle the result(s)
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(String path, File pathFile) {
                                Toast.makeText(getActivity(), "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                            }
                        }).build().show();
                break;
            }
            default: {
                break;
            }
        }
    }


    public static class ImagePreviewRecycleAdapter extends ListAdapter<ImageTO, ImagePreviewRecycleAdapter.ViewHolder> {
        private static DiffUtil.ItemCallback<ImageTO> diffCallback = new DiffUtil.ItemCallback<ImageTO>() {
            @Override
            public boolean areItemsTheSame(@NonNull ImageTO oldItem, @NonNull ImageTO newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull ImageTO oldItem, @NonNull ImageTO newItem) {
                return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDesc().equals(newItem.getDesc());
            }
        };

        public ImagePreviewRecycleAdapter() {
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

        public ImageTO getImageAt(int position) {
            return getItem(position);
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
            }
        }
    }
}