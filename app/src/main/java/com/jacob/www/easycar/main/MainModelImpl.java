package com.jacob.www.easycar.main;

import com.jacob.www.easycar.base.BaseModelImpl;

/**
 * Created by ASUS-NB on 2017/11/12.
 */

public class MainModelImpl extends BaseModelImpl implements Model {
    @Override
    public void getData(LoadingCallBack callBack) {
        callBack.onLoaded();
    }
}