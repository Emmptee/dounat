package com.donut.app.mvp.home.search;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.donut.app.R;
import com.donut.app.databinding.ActivityContentSearchItemBinding;
import com.donut.app.http.message.home.ContentItem;

import java.util.List;

public class ContentItemSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ContentItem> contentItems;

    private SubjectSearchContract.ContextView mListener;

    private static int SUBJECT_TYPE = 1;

    ContentItemSearchAdapter(List<ContentItem> contentItems,
                             SubjectSearchContract.ContextView listener) {

        this.contentItems = contentItems;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SUBJECT_TYPE) {
            ActivityContentSearchItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.activity_content_search_item,
                    parent,
                    false);
            BindingContentItemHolder holder = new BindingContentItemHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        }  else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            BindingContentItemHolder subjectHolder = (BindingContentItemHolder) holder;
            ContentItem contentItem = contentItems.get(position);
            if(contentItem.getContentName() == null){
                contentItem.setContentName("达成了" + contentItem.getUserName() + "的心愿");
            }
            subjectHolder.binding.setHandler(this);
            subjectHolder.binding.setDetail(contentItem);
    }

    @Override
    public int getItemViewType(int position) {
            return SUBJECT_TYPE;

    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }

    public void onContentItemViewClick(ContentItem detail) {
        mListener.onContentItemClick(detail);
    }



    private static class BindingContentItemHolder extends RecyclerView.ViewHolder {
        private ActivityContentSearchItemBinding binding;

        private BindingContentItemHolder(View view) {
            super(view);
        }

        public ActivityContentSearchItemBinding getBinding() {
            return binding;
        }

        public void setBinding(ActivityContentSearchItemBinding binding) {
            this.binding = binding;
        }
    }

}
