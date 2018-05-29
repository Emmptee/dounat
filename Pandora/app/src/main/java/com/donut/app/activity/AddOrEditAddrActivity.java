package com.donut.app.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bis.android.plug.autolayout.AutoLinearLayout;
import com.donut.app.R;
import com.donut.app.activity.base.BaseActivity;
import com.donut.app.config.BehaviourEnum;
import com.donut.app.config.Constant;
import com.donut.app.http.HeaderRequest;
import com.donut.app.http.message.BaseResponse;
import com.donut.app.http.message.DeliveryAddress;
import com.donut.app.http.message.SaveAddressRequest;
import com.donut.app.service.SaveBehaviourDataService;
import com.donut.app.utils.FormatCheckUtil;
import com.donut.app.utils.JsonUtils;
import com.donut.app.utils.StatusBarUtil;
import com.donut.app.utils.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.com.bis.addresslibrary.model.CityModel;
import cn.com.bis.addresslibrary.model.DistrictModel;
import cn.com.bis.addresslibrary.model.ProvinceModel;
import cn.com.bis.addresslibrary.service.XmlParserHandler;
import cn.com.bis.addresslibrary.wheel.widget.OnWheelChangedListener;
import cn.com.bis.addresslibrary.wheel.widget.WheelView;
import cn.com.bis.addresslibrary.wheel.widget.adapter.ArrayWheelAdapter;

public class AddOrEditAddrActivity extends BaseActivity implements OnWheelChangedListener
{
    @ViewInject(R.id.consignee_et)
    private EditText mConsigneeEt;

    @ViewInject(R.id.phone_et)
    private EditText mPhoneEt;

    @ViewInject(R.id.addr_detail_et)
    private EditText mAddrDetailEt;

    @ViewInject(R.id.addr_choice_et)
    private TextView mAddrChoice;

    @ViewInject(R.id.addr_default_set_cb)
    private CheckBox mDefaultCb;

    @ViewInject(R.id.set_delault_linear)
    private AutoLinearLayout mDefaultLinear;

    @ViewInject(R.id.head_right_tv)
    private TextView mRight;

    @ViewInject(R.id.city_choice_linear)
    private LinearLayout mCityLinear;

    @ViewInject(R.id.id_province)
    private WheelView mViewProvince;

    @ViewInject(R.id.id_city)
    private WheelView mViewCity;

    @ViewInject(R.id.id_district)
    private WheelView mViewDistrict;

    @ViewInject(R.id.mengban)
    private TextView mMengban;

    public static final String ISADD="isAdd";

    public static final String HASADDR="hasAddr";

    public static final String ADDRESS="address";

    private String title;

    private String consignee,phone,addrdetail;
    private int default_addr;

    public static final int ADDRESS_EDIT_REQUEST=0;

    boolean isAdd=true;

    private String uuid;

    private DeliveryAddress addrInfo;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName ="";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode ="";

    private CharSequence temp;//监听前的文本
    private int editStart;//光标开始位置
    private int editEnd;//光标结束位置

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_addr);
        StatusBarUtil.setColor(AddOrEditAddrActivity.this, Constant.default_bar_color);
        ViewUtils.inject(this);
        initView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(isAdd){
            SaveBehaviourDataService.startAction(this, BehaviourEnum.ADDRESS_ADD.getCode()+"00");
        }else{
            SaveBehaviourDataService.startAction(this, BehaviourEnum.ADDRESS_EDIT.getCode()+"00");
        }
    }

    private void initView(){

        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);
        setUpData();
        isAdd=getIntent().getBooleanExtra(ISADD,true);
        if(isAdd){
            title="新增新地址";
        }else{
            title="编辑收货地址";
        }
        updateHeadTitle(title,true);
        mRight.setText(getString(R.string.save));
        if(getIntent().getBooleanExtra(HASADDR,false)){
            mDefaultLinear.setVisibility(View.VISIBLE);
        }else{
            mDefaultLinear.setVisibility(View.GONE);
        }
        initData();
        mConsigneeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
                editStart = mConsigneeEt.getSelectionStart();
                editEnd = mConsigneeEt.getSelectionEnd();
                if (temp.length() > Constant.LENGTH_32)
                {
                    ToastUtil.showShort(AddOrEditAddrActivity.this, getString(R.string.consignee_check_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mConsigneeEt.setText(s);
                    mConsigneeEt.setSelection(tempSelection);
                }
            }
        });

        mAddrDetailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
                editStart = mAddrDetailEt.getSelectionStart();
                editEnd = mAddrDetailEt.getSelectionEnd();
                if (temp.length() > Constant.LENGTH_512)
                {
                    ToastUtil.showShort(AddOrEditAddrActivity.this, getString(R.string.addr_max_check_tips));
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    mAddrDetailEt.setText(s);
                    mAddrDetailEt.setSelection(tempSelection);
                }
            }
        });

        mDefaultCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked){
                    if(isAdd){
                        SaveBehaviourDataService.startAction(AddOrEditAddrActivity.this, BehaviourEnum.ADDRESS_ADD.getCode()+"01");
                    }else {
                        SaveBehaviourDataService.startAction(AddOrEditAddrActivity.this, BehaviourEnum.ADDRESS_EDIT.getCode()+"01");
                    }
                }
            }
        });
    }

    private void initData(){
        if(!isAdd){
            addrInfo=getIntent().getParcelableExtra(ADDRESS);
            if(addrInfo==null) {
                return;
            }
            uuid=addrInfo.getUuid();
            consignee=addrInfo.getConsignee();
            phone=addrInfo.getPhone();
            addrdetail=addrInfo.getAddress();
            default_addr=addrInfo.getIsDefault();
            mConsigneeEt.setText(consignee);
            mPhoneEt.setText(phone);
            String addr[]=addrdetail.split(" ");
            mAddrChoice.setText(addr[0]);
            String addrDetail="";
            for(int i=1;i<addr.length;i++){
                addrDetail=addrDetail+addr[i];
            }
            mAddrDetailEt.setText(addrDetail);
            if(addrInfo.getIsDefault()!=null&&addrInfo.getIsDefault()==1){
                mDefaultLinear.setVisibility(View.GONE);
            }else{
                mDefaultLinear.setVisibility(View.VISIBLE);
                mDefaultCb.setChecked(false);
            }
        }
    }

    private void setUpData()
    {
        initProvinceDatas();
        ArrayWheelAdapter mAdapter = new ArrayWheelAdapter<String>(
                AddOrEditAddrActivity.this, mProvinceDatas);
        mViewProvince.setViewAdapter(mAdapter);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue)
    {
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(isAdd){
            SaveBehaviourDataService.startAction(AddOrEditAddrActivity.this, BehaviourEnum.ADDRESS_ADD.getCode()+"03");
        }else{
            SaveBehaviourDataService.startAction(AddOrEditAddrActivity.this, BehaviourEnum.ADDRESS_EDIT.getCode()+"03");
        }
    }

    @Override
    protected void onStop() {
        if(isAdd){
            SaveBehaviourDataService.startAction(this, BehaviourEnum.ADDRESS_ADD.getCode() + "xx");
        }else{
            SaveBehaviourDataService.startAction(this, BehaviourEnum.ADDRESS_EDIT.getCode() + "xx");
        }
        super.onStop();
    }

    @OnClick(value = { R.id.menu,R.id.addr_choice_et,R.id.cancle,R.id.sure,R.id.mengban})
    private void btnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.addr_choice_et:
                mCityLinear.setVisibility(View.VISIBLE);
                mMengban.setVisibility(View.VISIBLE);
                break;
            case R.id.menu:
                addAddressRequest();
                break;
            case R.id.cancle:
                mCityLinear.setVisibility(View.GONE);
                mMengban.setVisibility(View.GONE);
                break;
            case R.id.sure:
                showSelectedResult();
                break;
            case R.id.mengban:
                mCityLinear.setVisibility(View.GONE);
                mMengban.setVisibility(View.GONE);
                break;
        }
    }

    private void showSelectedResult()
    {
        if(mCurrentProviceName.equals(mCurrentCityName)){
            mAddrChoice.setText(mCurrentProviceName+mCurrentDistrictName);
        }else{
            mAddrChoice.setText(mCurrentProviceName+mCurrentCityName+mCurrentDistrictName);
        }
        mCityLinear.setVisibility(View.GONE);
        mMengban.setVisibility(View.GONE);
    }

    private void addAddressRequest(){
        consignee=mConsigneeEt.getText().toString().trim();
        if(TextUtils.isEmpty(consignee)){
            ToastUtil.showShort(this,getString(R.string.consignee_empty_tips));
            return;
        }
        phone=mPhoneEt.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtil.showShort(this,getString(R.string.phone_empty_tips));
            return;
        }
//        if(!FormatCheckUtil.isMobileNumber(phone)){
//            ToastUtil.showShort(this,getString(R.string.telphone_check_tip));
//            return;
//        }
        if(TextUtils.isEmpty(mAddrChoice.getText().toString())){
            ToastUtil.showShort(this,getString(R.string.addr_choice_empty_tip));
            return;
        }

        if(TextUtils.isEmpty(mAddrDetailEt.getText().toString().trim())){
            ToastUtil.showShort(this,getString(R.string.addr_empty_tips));
            return;
        }
        if(mAddrDetailEt.getText().toString().length()<5){
            ToastUtil.showShort(this,getString(R.string.addr_min_check_tips));
            return;
        }
        addrdetail=mAddrChoice.getText().toString()+" "+mAddrDetailEt.getText().toString();
        if(getIntent().getBooleanExtra(HASADDR,false)&&default_addr!=1){
            default_addr=mDefaultCb.isChecked()?1:0;
        }else{
            default_addr=1;
        }
        SaveAddressRequest request=new SaveAddressRequest();
        if(!isAdd){
               request.setUuid(uuid);
        }
        request.setConsignee(consignee);
        request.setPhone(phone);
        request.setIsDefault(default_addr);
        request.setAddress(addrdetail);
        sendNetRequest(request, HeaderRequest.ADDRESS_EDIT, ADDRESS_EDIT_REQUEST,
                true);
        if(isAdd){
            SaveBehaviourDataService.startAction(AddOrEditAddrActivity.this, BehaviourEnum.ADDRESS_ADD.getCode()+"02",request,HeaderRequest.ADDRESS_EDIT);
        }else{
            SaveBehaviourDataService.startAction(AddOrEditAddrActivity.this, BehaviourEnum.ADDRESS_EDIT.getCode()+"02",request,HeaderRequest.ADDRESS_EDIT);
        }

    }

    @Override
    public void onSuccess(String response, String url, int actionId)
    {
        super.onSuccess(response, url, actionId);
        switch (actionId){
            case ADDRESS_EDIT_REQUEST:
                BaseResponse res= JsonUtils.fromJson(response,
                        BaseResponse.class);
                if(COMMON_SUCCESS.equals(res.getCode())){
                    finish();
                }else{
                    ToastUtil.showShort(this,res.getMsg());
                }
                break;
        }
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
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[mViewDistrict.getCurrentItem()];
        mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas()
    {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList!= null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList!= null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i=0; i< provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j=0; j< cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k=0; k<districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }
}
