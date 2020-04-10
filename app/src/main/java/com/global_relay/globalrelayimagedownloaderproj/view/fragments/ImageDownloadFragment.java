package com.global_relay.globalrelayimagedownloaderproj.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.global_relay.globalrelayimagedownloaderproj.R;
import com.global_relay.globalrelayimagedownloaderproj.model.to.ImageTO;
import com.global_relay.globalrelayimagedownloaderproj.view.activities.MainActivity;
import com.global_relay.globalrelayimagedownloaderproj.view_model.ImageDownloaderViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDownloadFragment extends Fragment implements Observer<ImageTO> {
    public ImageDownloaderViewModel imageDownloaderViewModel;
    private MutableLiveData<ImageTO> imageTOMutableLiveData=new MutableLiveData<>();
    private List<ImageTO> imageTOList = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private ImagePreviewRecycleAdapter imagePreviewRecycleAdapter;
    private String download_status;
    private String remaining_items;

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
        download_status=getResources().getString(R.string.download_status);
        remaining_items=getResources().getString(R.string.remaining_items);
        startObserving();
        setupRecycleImagePreview();
        return rootView;
    }

    public void startObserving()
    {
        imageDownloaderViewModel = ((MainActivity) getActivity()).getImageDownloaderViewModel();
        imageTOMutableLiveData=imageDownloaderViewModel.getImageTOMutableLiveData();
        imageTOMutableLiveData.observe(this,this);
        imageTOList = imageDownloaderViewModel.getImageMutableLiveData().getValue();
    }

    public void startDownloading()
    {
        if(imageTOList!=null && imageTOList.size()>0)
        {
            imageDownloaderViewModel.startDownloadSaveImages(imageTOList.get(0));
        }
    }

    private void setupRecycleImagePreview()
    {
        linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        imagePreviewRecycleAdapter=new ImagePreviewRecycleAdapter(diffCallback);
        recycleImagePreview.setLayoutManager(linearLayoutManager);
        imagePreviewRecycleAdapter.submitList(imageTOList);
        recycleImagePreview.setAdapter(imagePreviewRecycleAdapter);
    }

    @Override
    public void onChanged(ImageTO imageTO) {
        imageTOList.remove(imageTO);
        txtDownloadStatusTitle.setText(String.format("%s /%s: %d",download_status,remaining_items,imageTOList.size()));
        txtDownloadStatusTitle.invalidate();
        imagePreviewRecycleAdapter.submitList(imageTOList);
        recycleImagePreview.getAdapter().notifyDataSetChanged();
        if(imageTOList!=null && imageTOList.size()>0)
        {
            imageDownloaderViewModel.startDownloadSaveImages(imageTOList.get(0));
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

        public ImageTO getImageAt(int position) {
            return getItem(position);
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
                imageDownloaderViewModel.loadImage(getActivity(), imageTO.getImagePath(), imgPreview);
                progressBarDownload.setVisibility(imageTO.isDownloaded()?View.INVISIBLE:View.VISIBLE);
            }
        }
    }
}
