package com.donut.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.donut.app.R;
import com.donut.app.http.message.JobCodeResponse;

import java.util.List;

/**
 * Created by Qi on 2016/11/25.
 */

public class JobSignRecyclerViewAdapter  extends RecyclerView.Adapter{

    private List<JobCodeResponse.JobCode> jobCodeList;

    public JobSignRecyclerViewAdapter(List<JobCodeResponse.JobCode> list){
        this.jobCodeList = list;
    }

    private String selectedName;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_job_sign_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder item = (ItemViewHolder) holder;
        final JobCodeResponse.JobCode jobCode = jobCodeList.get(position);
        item.button.setText(jobCode.getName());
        item.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectedCode = jobCode.getCode();
                selectedName = jobCode.getName();
                JobSignRecyclerViewAdapter.this.notifyDataSetChanged();
            }
        });

        if (selectedName != null && selectedName.equals(jobCode.getName())) {
            item.button.setSelected(true);
        } else {
            item.button.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return jobCodeList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final Button button;

        public ItemViewHolder(View view) {
            super(view);
            button = (Button) view.findViewById(R.id.item_layout_btn);
        }
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }
}
