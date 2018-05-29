package com.donut.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.http.message.MyOrderResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 */
public class MyOrderItemRecyclerViewAdapter extends RecyclerView.Adapter{

    public interface OnItemClickListener{
        void OnItemClick(String uuid);

        /**
         * 1:确认收货 2:观看视频 3:数据报告 4:去支付 5:取消订单 6:删除订单
         * @param state
         */
        void OnBtnClick(String uuid, int state);
    }

    private final List<MyOrderResponse.MyOrder> orderList;

    private OnItemClickListener mListener;

    private View footerView;
    private static final int BOTTOM_TYPE = 1, ITEM_TYPE = 2;

    public MyOrderItemRecyclerViewAdapter(List<MyOrderResponse.MyOrder> items,
                                          OnItemClickListener listener,
                                          View footerView) {
        orderList = items;
        mListener = listener;
        this.footerView = footerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == BOTTOM_TYPE)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(lp);
            return new FooterViewHolder(footerView);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_order_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder item = (ItemViewHolder) holder;
            final MyOrderResponse.MyOrder myOrder = orderList.get(position);

            Glide.with(item.ivThumbnail.getContext())
                    .load(myOrder.getThumbnailUrl())
                    .placeholder(R.drawable.default_bg)
                    .error(R.drawable.default_bg)
                    .centerCrop()
                    .into(item.ivThumbnail);

            String strTitle = myOrder.getSubjectName();
            if (strTitle == null) {
                strTitle = "商品专区";
            }

            Integer type = myOrder.getType();
            if(type == null) {
                type = -1;
            }

            Integer expressStatus = myOrder.getExpressStatus();
            if(expressStatus == null) {
                expressStatus = -1;
            }

            Integer payStatus = myOrder.getPayStatus();
            if (payStatus == null) {
                payStatus = -1;
            }

            switch (type) {
                case 1:
                    strTitle="视频花絮/"+strTitle;
                    break;
                case 0:
                    strTitle="商品实物/"+strTitle;
                    break;
                case 2:
                    strTitle="报告文档/"+strTitle;
                    break;
            }
            item.tvSubjectName.setText(strTitle);

            item.btnCommit.setVisibility(View.GONE);
            item.btnCancel.setVisibility(View.GONE);
            item.btnDelete.setVisibility(View.GONE);
            item.btnLayout.setVisibility(View.GONE);

            String strPayStatus = "";
            switch (payStatus)
            {
                case 0:
                    strPayStatus="未支付";
                    item.btnCommit.setText("去支付");
                    item.btnCommit.setVisibility(View.VISIBLE);
                    item.btnCancel.setVisibility(View.VISIBLE);
                    item.btnLayout.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    //已支付状态

                    strPayStatus = "待发货";

                    if (type == 1 || type == 2) {
                        strPayStatus = "交易结束";
                        item.btnCommit.setText("在线观看");
                        item.btnCommit.setVisibility(View.VISIBLE);
                        item.btnLayout.setVisibility(View.VISIBLE);
                    } else {
                        if(expressStatus == 1) {
                            strPayStatus="已发货";
                            item.btnCommit.setText("确认收货");
                            item.btnCommit.setVisibility(View.VISIBLE);
                            item.btnLayout.setVisibility(View.VISIBLE);
                        } else if(expressStatus == 2){
                            strPayStatus = "交易结束";
                            item.btnLayout.setVisibility(View.VISIBLE);
                            item.btnDelete.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                    //item.btnCommit.setText("确认收货");
                    //item.btnLayout.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    strPayStatus="已退款";
                    break;
                case 3:
                    strPayStatus="已提现";
                    break;
                case 4:
                    strPayStatus="已取消";
                    item.btnLayout.setVisibility(View.VISIBLE);
                    item.btnDelete.setVisibility(View.VISIBLE);
                    break;
            }

            if (type == 0 && expressStatus == 2) {
                item.btnDelete.setVisibility(View.VISIBLE);
            }

            item.tvState.setText(strPayStatus);
            item.tvDescription.setText(myOrder.getGoodName());
            try {
                item.tvPrice.setText(String.format(
                        item.itemView.getContext().getString(R.string.order_detail_money),
                        new BigDecimal(myOrder.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP)));
                item.tvNum.setText("x" + myOrder.getNum());

                float amount = myOrder.getPrice() * myOrder.getNum() + myOrder.getFreight();

                item.tvPayAmount.setText(String.format(
                        item.itemView.getContext().getString(R.string.my_order_item_pay_amount),
                        new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP)));
                item.tvFreight.setText(String.format(
                        item.itemView.getContext().getString(R.string.my_order_item_freight),
                        new BigDecimal(myOrder.getFreight()).setScale(2, BigDecimal.ROUND_HALF_UP)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            final Integer finalType = type;
            final Integer finalPayStatus = payStatus;
            item.btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finalType == 0 && finalPayStatus == 1) {
                        // 实物,确认收货!!!
                        // ToastUtil.showShort(v.getContext(), "确认收货!");
                        mListener.OnBtnClick(myOrder.getOrderId(), 1);
                    } else if(finalType != 0 && finalPayStatus == 1){
                        // 虚拟, 在线观看!!!
                        // ToastUtil.showShort(v.getContext(), "在线观看!");

                        if (finalType == 1) {
                             //视频商品
                            mListener.OnBtnClick(myOrder.getGoodsId(), 2);
                        } else if (finalType == 2) {
                             //数据报告
                            mListener.OnBtnClick(myOrder.getStaticUrl(), 3);
                        }
                    } else if(finalPayStatus == 0){
                        //去支付
                        mListener.OnBtnClick(myOrder.getOrderId(), 4);
                    }
                }
            });

            item.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消订单
                    mListener.OnBtnClick(myOrder.getOrderId(), 5);
                }
            });

            item.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除订单
                    mListener.OnBtnClick(myOrder.getOrderId(), 6);
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.OnItemClick(myOrder.getOrderId());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == orderList.size()) {
            return BOTTOM_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivThumbnail;
        final TextView tvSubjectName, tvState;
        final TextView tvDescription, tvPrice, tvNum;
        final TextView tvPayAmount, tvFreight;
        final Button btnCommit, btnCancel, btnDelete;
        final View btnLayout;

        public ItemViewHolder(View view) {
            super(view);
            ivThumbnail = (ImageView) view.findViewById(R.id.order_item_iv_thumbnail);
            tvSubjectName = (TextView) view.findViewById(R.id.order_item_tv_subjectName);
            tvState = (TextView) view.findViewById(R.id.order_item_tv_state);
            tvDescription = (TextView) view.findViewById(R.id.order_item_tv_description);
            tvPrice = (TextView) view.findViewById(R.id.order_item_tv_price);
            tvNum = (TextView) view.findViewById(R.id.order_item_tv_num);
            tvPayAmount = (TextView) view.findViewById(R.id.order_item_tv_payAmount);
            tvFreight = (TextView) view.findViewById(R.id.order_item_tv_freight);
            btnCommit = (Button) view.findViewById(R.id.order_item_btn_commit);
            btnCancel = (Button) view.findViewById(R.id.order_item_btn_cancel);
            btnDelete = (Button) view.findViewById(R.id.order_item_btn_delete);
            btnLayout = view.findViewById(R.id.order_item_layout_btn);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder
    {
        public FooterViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
