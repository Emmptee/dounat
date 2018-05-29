package com.donut.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.donut.app.R;
import com.donut.app.http.message.InitOpenMemberResponse;

import java.util.List;

/**
 */
public class VipRecyclerViewAdapter extends RecyclerView.Adapter<VipRecyclerViewAdapter.ItemViewHolder> {

    public interface OnRecyclerViewListener
    {
        void onItemClick(InitOpenMemberResponse.MemberDetail detail);
    }

    private final List<InitOpenMemberResponse.MemberDetail> memberList;

    private String selectUuid = "";

    private OnRecyclerViewListener mListener;

    public VipRecyclerViewAdapter(List<InitOpenMemberResponse.MemberDetail> items,
                                  OnRecyclerViewListener listener) {
        memberList = items;
        mListener = listener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vip_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {

        final InitOpenMemberResponse.MemberDetail detail = memberList.get(position);
        holder.cb.setText(detail.getName());
        holder.tvPrice.setText(detail.getDescription());

        if (selectUuid.equals(detail.getUuid())) {
            holder.cb.setChecked(true);
        } else {
            holder.cb.setChecked(false);
        }
        if (position == memberList.size() - 1) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }

        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUuid = detail.getUuid();
                mListener.onItemClick(detail);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox cb;
        public final TextView tvPrice;
        public final View line;

        public ItemViewHolder(View view) {
            super(view);
            cb = (CheckBox) view.findViewById(R.id.vip_item_cb);
            tvPrice = (TextView) view.findViewById(R.id.vip_item_tv_price);
            line = view.findViewById(R.id.vip_item_bottom_line);
        }
    }

    public void setSelectUuid(String selectUuid) {
        this.selectUuid = selectUuid;
    }
}
