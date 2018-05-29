package com.donut.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.adapter.ABRecyclerViewHolder;
import com.bis.android.plug.refresh_recycler.adapter.ABRecyclerViewTypeAdapter;
import com.bis.android.plug.refresh_recycler.adapter.extra.ABRecyclerViewTypeExtraHolder;
import com.bis.android.plug.refresh_recycler.adapter.typeadapter.ABAdapterTypeRender;
import com.bumptech.glide.Glide;
import com.donut.app.R;
import com.donut.app.fragment.NoticeHotFragment;
import com.donut.app.http.message.noticeHot.PromotionResponse;
import com.donut.app.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeHotRecyclerViewAdapter extends ABRecyclerViewTypeAdapter {

    private Context mContext;
    private final List<PromotionResponse.Promotion> promotionList;
    private final NoticeHotFragment.OnNoticeHotFragmentListener mListener;

    private static String checkItemUuid;

    public NoticeHotRecyclerViewAdapter(Context context, List<PromotionResponse.Promotion> items,
                                        NoticeHotFragment.OnNoticeHotFragmentListener listener) {
        mContext = context;
        promotionList = items;
        mListener = listener;
    }

    @Override
    public ABAdapterTypeRender<ABRecyclerViewHolder> getAdapterTypeRender(int type) {
        return new ItemViewReader(mContext, this);
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    private class ItemViewReader implements ABAdapterTypeRender<ABRecyclerViewHolder> {
        private Context context;
        private NoticeHotRecyclerViewAdapter adapter;
        private ABRecyclerViewHolder holder;

        ItemViewReader(Context context, NoticeHotRecyclerViewAdapter adapter) {
            this.context = context;
            this.adapter = adapter;

            View view = LayoutInflater.from(context).inflate(R.layout.fragment_notice_hot, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            holder = new ABRecyclerViewTypeExtraHolder(view);
        }

        @Override
        public ABRecyclerViewHolder getReusableComponent() {
            return holder;
        }

        @Override
        public void fitEvents() {
            holder.obtainView(R.id.fragment_notice_hot_top_layout)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (checkItemUuid != null && checkItemUuid.equals(promotion.getUuid())) {
                                checkItemUuid = null;
                            } else {
                                checkItemUuid = promotion.getUuid();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
            holder.obtainView(R.id.fragment_notice_hot_content_btn_commit)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (promotion.getObtain() == 1) {
                                return;
                            }
                            if (goodsIdMap.size() <= 0) {
                                ToastUtil.showShort(context, "请选择要领取的奖品");
                                return;
                            }
                            if (null != mListener) {
                                mListener.onReceiveGoods(goodsIdMap, promotion.getUuid());
                            }
                        }
                    });
        }

        private PromotionResponse.Promotion promotion;

        private Map<String, Integer> goodsIdMap;

        @Override
        public void fitDatas(final int position) {
            promotion = promotionList.get(position);
            goodsIdMap = new HashMap<>();
            holder.obtainView(R.id.fragment_notice_hot_top_name, TextView.class).setText(promotion.getTitle());
            TextView topState = holder.obtainView(R.id.fragment_notice_hot_top_state, TextView.class);
            Button subBtn = holder.obtainView(R.id.fragment_notice_hot_content_btn_commit, Button.class);

            if (promotion.getObtain() == 0) {
                topState.setText("去参加");
                topState.setTextColor(context.getResources().getColor(R.color.text_tiffany));
                subBtn.setText("领取奖励");

                int noGiveNum = 0;
                List<PromotionResponse.Goods> goodsList = promotion.getGoodsList();
                for (PromotionResponse.Goods goods : goodsList) {
                    if ("0".equals(goods.getType())
                            && goods.getResidueNum() <= 0) {
                        noGiveNum++;
                    }
                }
                if (noGiveNum == goodsList.size()) {
                    subBtn.setSelected(false);
                    subBtn.setEnabled(false);
                } else {
                    subBtn.setSelected(true);
                    subBtn.setEnabled(true);
                }
            } else {
                topState.setText("已参加");
                topState.setTextColor(context.getResources().getColor(R.color.text_grayC3));
                subBtn.setText("已领取");
                subBtn.setSelected(false);
                subBtn.setEnabled(false);
            }

            holder.obtainView(R.id.fragment_notice_hot_content_des, TextView.class).setText(
                    Html.fromHtml("<font color='#81D8D0'>活动介绍：</font>" + promotion.getDescription()));
            holder.obtainView(R.id.fragment_notice_hot_content_time, TextView.class).setText(promotion.getValidTime());
            TextView tvPro = holder.obtainView(R.id.fragment_notice_hot_content_pro, TextView.class);
            if (promotion.getProType() == 0) {
                tvPro.setText("以下商品任选其一");
            } else {
                tvPro.setText("");
            }

            setGoodsView();

            View view = holder.obtainView(R.id.fragment_notice_hot_content);
            if (checkItemUuid != null && checkItemUuid.equals(promotion.getUuid())) {
                view.setVisibility(View.VISIBLE);
                Drawable drawable = context.getResources().getDrawable(R.drawable.activity_notice_item_top_down_icon);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                topState.setCompoundDrawables(null, null, drawable, null);
                holder.obtainView(R.id.fragment_notice_hot_bottom).setVisibility(View.GONE);
            } else {
                view.setVisibility(View.GONE);
                Drawable drawable = context.getResources().getDrawable(R.drawable.activity_notice_item_top_up_icon);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                topState.setCompoundDrawables(null, null, drawable, null);
                holder.obtainView(R.id.fragment_notice_hot_bottom).setVisibility(View.VISIBLE);
            }

        }

        private void setGoodsView() {
            if (promotion.getGoodsList() != null && promotion.getGoodsList().size() > 0) {
                final LinearLayout goodsView = holder.obtainView(R.id.fragment_notice_hot_content_goods, LinearLayout.class);
                goodsView.removeAllViews();
                int allGoodsNum = promotion.getGoodsList().size();
                int line = allGoodsNum / 3 + 1;

                for (int i = 0; i < line; i++) {
                    LinearLayout lineLinearLayout = new LinearLayout(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    lineLinearLayout.setLayoutParams(lp);
                    for (int j = i * 3; j < (i + 1) * 3; j++) {
                        if (j >= allGoodsNum) {
                            break;
                        }
                        final PromotionResponse.Goods goods = promotion.getGoodsList().get(j);
                        View view = LayoutInflater.from(context).inflate(R.layout.fragment_notice_hot_goods, null);
                        ImageView img = (ImageView) view.findViewById(R.id.fragment_notice_item_goods_img);
                        Glide.with(mContext)
                                .load(goods.getThumbnail())
                                .placeholder(R.drawable.default_bg)
                                .error(R.drawable.default_bg)
                                .centerCrop()
                                .into(img);

                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (null != mListener) {
                                    mListener.onGoodsDetail(goods.getUuid(), goods.getType());
                                }
                            }
                        });

                        TextView goodsNum = (TextView) view.findViewById(R.id.fragment_notice_item_goods_num);
                        goodsNum.setText(String.valueOf(goods.getNum()));

                        View viewNo = view.findViewById(R.id.fragment_notice_item_goods_no);
                        if (goods.getResidueNum() <= 0 && "0".equals(goods.getType())) {
                            viewNo.setVisibility(View.VISIBLE);
                        } else {
                            viewNo.setVisibility(View.GONE);
                        }
                        final String goodsId = goods.getUuid();
                        CheckBox checkBox = (CheckBox) view.findViewById(R.id.fragment_notice_item_goods_check);
                        if (promotion.getProType() == 0 && promotion.getObtain() == 0) {
                            checkBox.setVisibility(View.VISIBLE);
                            if (goodsIdMap.containsKey(goodsId)) {
                                checkBox.setChecked(true);
                            } else {
                                checkBox.setChecked(false);
                            }
                        } else {
                            checkBox.setVisibility(View.GONE);
                            if (!"0".equals(goods.getType())) {
                                goodsIdMap.put(goodsId, goods.getNum());
                            } else if ("0".equals(goods.getType())
                                    && goods.getResidueNum() >= goods.getNum()) {
                                goodsIdMap.put(goodsId, goods.getNum());
                            }
                        }
                        if (goods.getNum() > goods.getResidueNum()
                                && "0".equals(goods.getType())) {
                            //ToastUtil.showShort(context, "抱歉,剩余商品已不足,暂无法领取");
                            checkBox.setEnabled(false);
                        } else {
                            checkBox.setEnabled(true);
                        }

                        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {

                                    for (int m = 0; m < goodsView.getChildCount(); m++) {
                                        LinearLayout layout = (LinearLayout) goodsView.getChildAt(m);
                                        for (int n = 0; n < layout.getChildCount(); n++) {
                                            CheckBox c = (CheckBox) layout.getChildAt(n)
                                                    .findViewById(R.id.fragment_notice_item_goods_check);
                                            c.setChecked(false);
                                        }
                                    }
                                    goodsIdMap.clear();

                                    if (goods.getNum() > goods.getResidueNum()
                                            && "0".equals(goods.getType())) {
                                        //ToastUtil.showShort(context, "抱歉,剩余商品已不足,暂无法领取");
                                    } else {
                                        buttonView.setChecked(true);
                                        goodsIdMap.put(goodsId, goods.getNum());
                                    }
                                } else {
                                    goodsIdMap.remove(goodsId);
                                }
                            }
                        });

                        lineLinearLayout.addView(view);
                    }
                    goodsView.addView(lineLinearLayout);
                }
            }
        }
    }

    public void setCheckItemUuid(String checkItemUuid) {
        NoticeHotRecyclerViewAdapter.checkItemUuid = checkItemUuid;
    }
}
