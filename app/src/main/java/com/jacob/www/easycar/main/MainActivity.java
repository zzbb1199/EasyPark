package com.jacob.www.easycar.main;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.jacob.www.easycar.R;
import com.jacob.www.easycar.base.App;
import com.jacob.www.easycar.data.GarageBean;
import com.jacob.www.easycar.data.SearchSuggestionItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainContract.View, AMapNaviListener, AMapNaviViewListener, AMap.OnMyLocationChangeListener, FloatingSearchView.OnQueryChangeListener, FloatingSearchView.OnSearchListener, Inputtips.InputtipsListener, GeocodeSearch.OnGeocodeSearchListener {
    private String TAG = "MainActivity";
    AMapNavi mAMapNavi;
    MainContract.Presenter presenter;
    //起始点经纬度
    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    //终点经纬度
    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    //途中经过的点的经纬度，一般都没用上
    protected final List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>();
    //获取 AMapNaviView 实例
    AMapNaviView mAMapNaviView;
    //地图对象
    MapView mMapView = null;
    AMap aMap;
    //定位的style
    MyLocationStyle myLocationStyle;
    String currentCity;
    /**
     * 关键字搜索相关
     */
    GeocodeSearch geocodeSearch;
    InputtipsQuery query;
    Inputtips inputTips;

    //目前我所在位置的经纬度
    double myLongitude = 0, myLatitude = 0;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenter(this);

        initView(savedInstanceState);

    }
    


    
    private void initView(Bundle savedInstanceState) {
        mSearchView.setOnQueryChangeListener(this);
        mSearchView.setOnSearchListener(this);
        //搜索功能初始化
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        inputTips = new Inputtips(this, query);
        inputTips.setInputtipsListener(this);
        //地图
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(1000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMyLocationChangeListener(this);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));

    }

    /**
     * 开始导航,根据经纬度
     */
    public void startNavi(double latitude, double longitude) {
        isSimulate = false;
        mAMapNavi.stopNavi();
        double myLatitude = this.myLatitude;
        double myLongitude = this.myLongitude;
        Log.e("TAG", "收到的经纬度" + latitude + " " + longitude);
        Log.e("TAG", "我的经纬度:" + myLatitude + " " + myLongitude);
        mAMapNaviView.setVisibility(View.VISIBLE);
        mMapView.setVisibility(View.INVISIBLE);
        //算路终点坐标
        NaviLatLng mEndLatlng = new NaviLatLng(latitude, longitude);
        //算路起点坐标
        NaviLatLng mStartLatlng = new NaviLatLng(myLatitude, myLongitude);
        sList.add(mStartLatlng);
        eList.add(mEndLatlng);
        mAMapNavi = AMapNavi.getInstance(App.getAppContext());
        //添加监听回调，用于处理算路成功
        mAMapNavi.addAMapNaviListener(this);
        mAMapNaviView.setAMapNaviViewListener(this);
        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setLayoutVisible(false);
        mAMapNaviView.setViewOptions(options);
        onInitNaviSuccess();
    }

    @Override
    public void onMyLocationChange(Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        currentCity = location.getProvider();

    }


    @Override
    protected void onResume() {
        Log.e("TAG", "onResume");
        super.onResume();
        mAMapNaviView.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        Log.e("TAG", "onResume");
        super.onPause();
        mAMapNaviView.onPause();
        mMapView.onPause();
    }

    protected void onDestroy() {
        Log.e("TAG", "onResume");
        super.onDestroy();
        mAMapNaviView.onDestroy();
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        mMapView.onDestroy();
    }

    @Override
    public void onInitNaviFailure() {
        Log.e("TAG", "算路失败");
    }

    @Override
    public void onInitNaviSuccess() {
        /**
         * 方法:
         *   int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
         * 参数:
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         * 说明:
         *      以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         * 注意:
         *      不走高速与高速优先不能同时为true
         *      高速优先与避免收费不能同时为true
         */
        Log.e("TAG", "算路成功");
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    //算路成功后执行的
    @Override
    public void onCalculateRouteSuccess(int[] ints) {

        if(!isSimulate){
            Log.e(TAG,"GPS");
            mAMapNavi.startNavi(NaviType.GPS);
        }
        else{
            Log.e(TAG,"EMULATOR");
            mAMapNavi.startNavi(NaviType.EMULATOR);
        }

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }




    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        if (!newQuery.equals("")) {
            Log.i(TAG, "oldQuery" + oldQuery + " " + "new" + newQuery);
            query = new InputtipsQuery(newQuery, currentCity);
            inputTips.setQuery(query);
            inputTips.requestInputtipsAsyn();
        }

    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
           SearchSuggestionItem item = (SearchSuggestionItem) searchSuggestion;
           Log.i(TAG,item.getSuggestoin());
           Log.i(TAG,item.getLatLonPoint().getLatitude()+""+item.getLatLonPoint().getLongitude()+"");
    }

    @Override
    public void onSearchAction(String currentQuery) {
        Log.i(TAG, "currentQuery" + currentQuery);
        GeocodeQuery query = new GeocodeQuery(currentQuery, currentCity);
        geocodeSearch.getFromLocationNameAsyn(query);
    }


    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        List<SearchSuggestionItem> suggestionItems = new ArrayList<>();
        for (Tip tip : list) {
            suggestionItems.add(new SearchSuggestionItem(tip.getName(),tip.getPoint()));
            
        }
        mSearchView.swapSuggestions(suggestionItems);
    }

    /**
     * 坐标转文字
     *
     * @param regeocodeResult
     * @param i
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        
    }

    /**
     * 文字转坐标
     *
     * @param geocodeResult
     * @param i
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
      if (i == 1000){
          //请求成功,得到经纬度
          LatLonPoint latLonPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
          Log.i(TAG,latLonPoint.getLatitude()+" "+latLonPoint.getLongitude()+"");
          Log.e(TAG,"开始搜索车库");
          presenter.getNearGarage(latLonPoint.getLongitude(),latLonPoint.getLatitude(),2);
          //发起请求
      }
    }
    boolean isSimulate = false;
    public void getRealItem(){
        isSimulate = true;
        int pos=horizontalInfiniteCycleViewPager.getRealItem();
        double lat = bean.getData().get(pos).getPositionLatitude();
        double lon = bean.getData().get(pos).getPositionLongitude();
        sList.clear();
        eList.clear();
        sList.add(new NaviLatLng(myLatitude,myLongitude));
        eList.add(new NaviLatLng(lat,lon));
        mAMapNavi = AMapNavi.getInstance(App.getAppContext());
        mAMapNaviView.setVisibility(View.VISIBLE);
        mMapView.setVisibility(View.INVISIBLE);
        //添加监听回调，用于处理算路成功
        mAMapNavi.addAMapNaviListener(this);
        mAMapNaviView.setAMapNaviViewListener(this);
        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setLayoutVisible(false);
        mAMapNaviView.setViewOptions(options);
        onInitNaviSuccess();
    }
    HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager;
    GarageBean bean;
    @Override
    public void showGarage(GarageBean garageBean) {
        bean = garageBean;
        horizontalInfiniteCycleViewPager = (HorizontalInfiniteCycleViewPager) findViewById(R.id.hicvp);
        MainAdapter adapter = new MainAdapter(this,garageBean);
        horizontalInfiniteCycleViewPager.setAdapter(adapter);
    }
}
