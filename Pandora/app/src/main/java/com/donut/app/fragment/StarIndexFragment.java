package com.donut.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bis.android.plug.refresh_recycler.layoutmanager.FullyGridLayoutManager;
import com.donut.app.R;
import com.donut.app.activity.StarDetailDescriptionActivity;
import com.donut.app.adapter.StarDetailWorksAdapter;
import com.donut.app.customview.EllipsizingTextView;
import com.donut.app.fragment.base.BaseFragment;
import com.donut.app.http.message.subjectStar.StarPlan;
import com.donut.app.http.message.subjectStar.SubjectStarResponse;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class StarIndexFragment extends BaseFragment {

    @ViewInject(R.id.star_detail_tv_description)
    private EllipsizingTextView tvDes;

    @ViewInject(R.id.star_detail_list_moments)
    private LinearLayout listMoments;

    @ViewInject(R.id.star_detail_list_works)
    private RecyclerView listWorks;

    @ViewInject(R.id.star_detail_layout_moments)
    private View layoutMoments;

    private static final String STAR_RESPONSE = "STAR_RESPONSE";

    private SubjectStarResponse starResponse;

    public static StarIndexFragment newInstance(SubjectStarResponse starResponse) {
        StarIndexFragment fragment = new StarIndexFragment();
        Bundle args = new Bundle();
        args.putParcelable(STAR_RESPONSE, starResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            starResponse = getArguments().getParcelable(STAR_RESPONSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_star_index, container, false);
        ViewUtils.inject(this, view);
        showData();
        return view;
    }

    private void showData() {
        if (starResponse == null) {
            return;
        }

//        ViewTreeObserver observer = tvDes.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            boolean isfirstRunning = true;
//
//            @Override
//            public void onGlobalLayout() {
//                if (!isfirstRunning) return;
//                Layout layout2 = tvDes.getLayout();
//                if (tvDes != null && layout2 != null) {
//                    int lines = layout2.getLineCount();
//                    if (lines < 2) return;
//                    if (layout2.getEllipsisCount(lines - 1) == 0) return;
//
//                    String showText = starResponse.getDescription();
//                    showText = showText.substring(0, showText.length() - layout2.getEllipsisCount(lines - 1) - 4).concat("... <font color='#81D8D0'>详情>></font>");
//                    tvDes.setText(Html.fromHtml(showText));
//                    tvDes.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getActivity(),StarDetailDescriptionActivity.class);
//                            intent.putExtra(StarDetailDescriptionActivity.CONTENT, starResponse.getDescription());
//                            startActivity(intent);
//                        }
//                    });
//                    isfirstRunning = false;
//                }
//            }
//        });
        tvDes.setMaxLines(5);
        tvDes.setText(starResponse.getDescription());
        tvDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StarDetailDescriptionActivity.class);
                intent.putExtra(StarDetailDescriptionActivity.CONTENT, starResponse.getDescription());
                startActivity(intent);
            }
        });

        if (starResponse.getStarPlans() == null || starResponse.getStarPlans().size() <= 0) {
            layoutMoments.setVisibility(View.GONE);
        } else {
            for (StarPlan starPlan : starResponse.getStarPlans()) {
//                View view = LayoutInflater.from(getActivity())
//                        .inflate(R.layout.star_detail_moments_item_layout, null, false);
//
//                TextView tvTime = (TextView) view.findViewById(R.id.star_detail_moments_tv_time);
//                TextView tvContent = (TextView) view.findViewById(R.id.star_detail_moments_tv_content);
//                tvTime.setText(starPlan.getCreateTime());
//                tvContent.setText(starPlan.getContent());

                TextView view = new TextView(getActivity());
                view.setTextColor(getResources().getColor(R.color.text_gray3));
                view.setTextSize(14);
                view.setPadding(0, 0, 0, (int) (5 / getResources().getDisplayMetrics().density + 0.5f));
                view.setText(starPlan.getCreateTime() + "——" + starPlan.getContent());
                listMoments.addView(view);
            }
        }

        StarDetailWorksAdapter worksAdapter = new StarDetailWorksAdapter(starResponse.getStarWorks());
        listWorks.setAdapter(worksAdapter);
        listWorks.setLayoutManager(new FullyGridLayoutManager(getActivity(), 4));
    }


}
