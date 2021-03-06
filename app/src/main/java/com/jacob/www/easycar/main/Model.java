package com.jacob.www.easycar.main;

import com.jacob.www.easycar.net.LoadingCallBack;

/**
 * Created by ASUS-NB on 2017/11/12.
 */

public interface Model  {
    void getData(double longitude,double latitude,double distance,LoadingCallBack callBack);

    void getLot(String gId,LoadingCallBack callBack);
    
    void changeInfo(String uId,String type,String value,LoadingCallBack callBack);
    
    void getGarget(String uId,LoadingCallBack callBack);
    
    void addUserParkPosition(String uId,String garageId,String parkId,LoadingCallBack callBack);
    
    void deletePark(String uId,LoadingCallBack callBack);
}
