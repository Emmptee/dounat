package cn.com.bis.addresslibrary.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.com.bis.addresslibrary.R;
import cn.com.bis.addresslibrary.wheel.widget.OnWheelChangedListener;
import cn.com.bis.addresslibrary.wheel.widget.WheelView;
import cn.com.bis.addresslibrary.wheel.widget.adapter.ArrayWheelAdapter;


public class MainActivity extends BaseActivity implements OnClickListener,
        OnWheelChangedListener
{
    private WheelView mViewProvince;

    private WheelView mViewCity;

    private WheelView mViewDistrict;

    private Button mBtnConfirm;

    private LinearLayout mWheel;

    private RelativeLayout mChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setUpViews();
//        setUpListener();
//        setUpData();
    }

//    private void setUpViews()
//    {
//        mWheel = (LinearLayout) findViewById(R.id.wheel);
//        mViewProvince = (WheelView) findViewById(R.id.id_province);
//        mViewCity = (WheelView) findViewById(R.id.id_city);
//        mViewDistrict = (WheelView) findViewById(R.id.id_district);
//        // mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
//        mChoice = (RelativeLayout) findViewById(R.id.choice);
//    }
//
//    private void setUpListener()
//    {
//        mViewProvince.addChangingListener(this);
//        mViewCity.addChangingListener(this);
//        mViewDistrict.addChangingListener(this);
//        // mBtnConfirm.setOnClickListener(this);
//        mChoice.setOnClickListener(this);
//    }
//
//    private void setUpData()
//    {
//        initProvinceDatas();
//        ArrayWheelAdapter mAdapter = new ArrayWheelAdapter<String>(
//                MainActivity.this, mProvinceDatas);
//        mViewProvince.setViewAdapter(mAdapter);
//        // 设置可见条目数量
//        mViewProvince.setVisibleItems(7);
//        mViewCity.setVisibleItems(7);
//        mViewDistrict.setVisibleItems(7);
//        updateCities();
//        updateAreas();
//    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue)
    {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince)
        {
            updateCities();
        } else if (wheel == mViewCity)
        {
            updateAreas();
        } else if (wheel == mViewDistrict)
        {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas()
    {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null)
        {
            areas = new String[]{""};
        }
        mViewDistrict
                .setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities()
    {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null)
        {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
//         case R.id.btn_confirm:
//         showSelectedResult();
//         break;
//        case R.id.choice:
//            mWheel.setVisibility(View.VISIBLE);
//            break;
//         default:
//             break;
        }
    }

    private void showSelectedResult()
    {
        Toast.makeText(
                MainActivity.this,
                "当前选中:" + mCurrentProviceName + "," + mCurrentCityName + ","
                        + mCurrentDistrictName + "," + mCurrentZipCode,
                Toast.LENGTH_SHORT).show();
    }
}
