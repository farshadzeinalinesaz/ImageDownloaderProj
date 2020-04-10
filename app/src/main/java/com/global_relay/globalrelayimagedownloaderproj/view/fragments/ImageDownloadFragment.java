package com.global_relay.globalrelayimagedownloaderproj.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageDownloadFragment extends Fragment implements Observer<ImageTO> {
    public ShareImageDownloadViewModel shareImageDownloadViewModel;
    private MutableLiveData<ImageTO> imageTOMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ImageTO>> imageMutableLiveData = new MutableLiveData<>();
    private List<ImageTO> imageTOList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private ImagePreviewRecycleAdapter imagePreviewRecycleAdapter;
    private String download_status;
    private String remaining_items;
    public boolean isDownloading;

    @BindView(R.id.btnDownload)
    public MaterialButton btnDownload;
    @BindView(R.id.txtDownloadStatusTitle)
    public TextView txtDownloadStatusTitle;
    @BindView(R.id.recycleImagePreview)
    public RecyclerView recycleImagePreview;

    public ImageDownloadFragment() {
    }

    public static ImageDownloadFragment newInstance() {
        ImageDownloadFragment fragment = new ImageDownloadFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_image_download, container, false);
        ButterKnife.bind(this, rootView);
        download_status = getResources().getString(R.string.download_status);
        remaining_items = getResources().getString(R.string.remaining_items);
        setupRecycleImagePreview();
        btnDownload.setEnabled(imageTOList!=null && imageTOList.size()>0);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shareImageDownloadViewModel = ViewModelProviders.of(getActivity()).get(ShareImageDownloadViewModel.class);
        imageTOMutableLiveData = shareImageDownloadViewModel.getImageTOMutableLiveData();
        imageTOMutableLiveData.observe(getViewLifecycleOwner(), this);
        imageMutableLiveData=shareImageDownloadViewModel.getImageMutableLiveData();
        imageMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<ImageTO>>() {
            @Override
            public void onChanged(List<ImageTO> imageTOS) {
                imageTOList=imageTOS;
                imagePreviewRecycleAdapter.submitList(imageTOS);
                recycleImagePreview.setAdapter(imagePreviewRecycleAdapter);
                btnDownload.setEnabled(!isDownloading && imageTOList!=null && imageTOList.size()>0);
            }
        });
    }


    @OnClick({R.id.btnDownload})
    public void doClick(View view)
    {
        if(view.getId()==R.id.btnDownload)
        {
            if (imageTOList != null && imageTOList.size() > 0) {
                isDownloading=true;
                shareImageDownloadViewModel.startDownloadSaveImages(imageTOList.get(0));
                btnDownload.setEnabled(false);
            }
        }
    }

    public void setupRecycleImagePreview() {
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        imagePreviewRecycleAdapter = new ImagePreviewRecycleAdapter(diffCallback);
        recycleImagePreview.setLayoutManager(linearLayoutManager);
        imagePreviewRecycleAdapter.submitList(imageTOList);
        recycleImagePreview.setAdapter(imagePreviewRecycleAdapter);
    }

    @Override
    public void onChanged(ImageTO imageTO) {
        imageTOList.remove(imageTO);
        txtDownloadStatusTitle.setText(String.format("%s /%s: %d", download_status, remaining_items, imageTOList.size()));
        txtDownloadStatusTitle.invalidate();
        if(imagePreviewRecycleAdapter!=null)
        {
            imagePreviewRecycleAdapter.submitList(imageTOList);
            imagePreviewRecycleAdapter.notifyItemRemoved(0);
        }
        if (imageTOList != null && imageTOList.size() > 0) {
            isDownloading=true;
            btnDownload.setEnabled(false);
            shareImageDownloadViewModel.startDownloadSaveImages(imageTOList.get(0));
        }
        else
        {
            isDownloading=false;
            btnDownload.setEnabled(true);
        }
    }


    public class ImagePreviewRecycleAdapter extends ListAdapter<ImageTO, ImagePreviewRecycleAdapter.ViewHolder> {
        public ImagePreviewRecycleAdapter(DiffUtil.ItemCallback<ImageTO> diffCallback) {
            super(diffCallback);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_download_card_item, parent, false);
            return new ImagePreviewRecycleAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setData(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.imgPreview)
            public ImageView imgPreview;
            @BindView(R.id.txtTitle)
            public TextView txtTitle;
            @BindView(R.id.progressBarDownload)
            public ProgressBar progressBarDownload;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(ViewHolder.this, itemView);
            }

            private void setData(int position) {
                ImageTO imageTO = getItem(position);
                txtTitle.setText(imageTO.getTitle());
                shareImageDownloadViewModel.loadImage(getActivity(), imageTO.getImagePath(), imgPreview);
                progressBarDownload.setVisibility(isDownloading ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }
}
