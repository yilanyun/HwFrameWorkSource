package com.android.server.hidata.appqoe;

import android.os.SystemProperties;
import android.util.Log;

public class HwAPPQoEUtils {
    public static final String ACTIVITY_TYPE_APP = "instant_app";
    public static final String ACTIVITY_TYPE_GAME = "game";
    public static final int APPID_TRAVEL = 1004;
    public static final int APP_CTRL_MONITOR = 300;
    public static final int APP_CTRL_UPDATE_GAME_INFO = 301;
    public static final int APP_DEFAULT_SCENCE_ID_DIVIDEND = 1000;
    public static final int APP_SCENCE_TYPE_AUDIO_VIDEO = 3;
    public static final int APP_SCENCE_TYPE_DEFAULT = 0;
    public static final int APP_SCENCE_TYPE_IGNORE = 255;
    public static final int APP_SCENCE_TYPE_PAY = 1;
    public static final int APP_SCENCE_TYPE_TAXI = 2;
    public static final int APP_TYPE_APK = 1000;
    public static final int APP_TYPE_GAME = 2000;
    public static final int APP_TYPE_GAME_SGAME = 2001;
    public static final int APP_TYPE_GENERAL_GAME = 3000;
    public static final int APP_TYPE_STREAMING = 4000;
    public static final int CHL_CHR_NETWORK_STATE_CELL = 801;
    public static final int CHL_CHR_NETWORK_STATE_WIFI = 800;
    public static final String EMCOM_PARA_READY_ACTION = "huawei.intent.action.ACTION_EMCOM_PARA_READY";
    public static final String EMCOM_PARA_READY_REC = "EXTRA_EMCOM_PARA_READY_REC";
    public static final String EMCOM_PARA_UPGRADE_PERMISSION = "huawei.permission.RECEIVE_EMCOM_PARA_UPGRADE";
    public static final boolean GAME_ASSISIT_ENABLE = "1".equalsIgnoreCase(SystemProperties.get("ro.config.gameassist", ""));
    public static final int GAME_SCENCE_IN_WAR = 200002;
    public static final int GAME_SCENCE_NOT_IN_WAR = 200001;
    public static final String INVALID_STRING_VALUE = "None";
    public static final int INVALID_VALUE = -1;
    public static final int MASKBIT_PARA_FILETYPE_NONCELL = 4;
    public static final int MAX_SLOT_ID = 2;
    public static final int MOBILE_DATA_OFF = 0;
    public static final int MOBILE_DATA_ON = 1;
    public static final String MPLINK_STATE_BroadcastPermission = "com.huawei.hidata.permission.MPLINK_START_CHECK";
    public static final String MPLINK_STATE_CHANGE = "com.android.server.hidata.arbitration.HwArbitrationStateMachine";
    public static final String MPLINK_STATE_KEY_NETWORK_TYPE = "MPLinkSuccessNetworkKey";
    public static final String MPLINK_STATE_KEY_UID = "MPLinkSuccessUIDKey";
    public static final int MSG_APP_STATE_BACKGROUND = 104;
    public static final int MSG_APP_STATE_BAD = 107;
    public static final int MSG_APP_STATE_END = 101;
    public static final int MSG_APP_STATE_FOREGROUND = 103;
    public static final int MSG_APP_STATE_GOOD = 106;
    public static final int MSG_APP_STATE_MONITOR = 105;
    public static final int MSG_APP_STATE_START = 100;
    public static final int MSG_APP_STATE_UNKNOW = 108;
    public static final int MSG_APP_STATE_UPDATE = 102;
    public static final int MSG_APP_STATE_WEAK = 109;
    public static final int MSG_CELL_STATE_CONNECTED = 7;
    public static final int MSG_CELL_STATE_DISABLE = 6;
    public static final int MSG_CELL_STATE_DISCONNECT = 8;
    public static final int MSG_CELL_STATE_ENABLE = 5;
    public static final int MSG_CHR_CELL_GOOD_AFTER_MPLINK = 205;
    public static final int MSG_CHR_WIFI_KQI = 206;
    public static final int MSG_INTERNAL_CELL_INFO_GET_SUCCESS = 204;
    public static final int MSG_INTERNAL_CHR_EXCP_TRIGGER = 203;
    public static final int MSG_INTERNAL_GAME_CALL_BACK_RE_REG = 202;
    public static final int MSG_INTERNAL_MODULE_INIT = 200;
    public static final int MSG_INTERNAL_NETWORK_STATE_CHANGE = 201;
    public static final int MSG_NETWORK_STATE_CELL = 801;
    public static final int MSG_NETWORK_STATE_MPLINK = 11;
    public static final int MSG_NETWORK_STATE_UKNOWN = 802;
    public static final int MSG_NETWORK_STATE_WIFI = 800;
    public static final int MSG_WIFI_STATE_CONNECTED = 3;
    public static final int MSG_WIFI_STATE_DISABLE = 2;
    public static final int MSG_WIFI_STATE_DISCONNECT = 4;
    public static final int MSG_WIFI_STATE_ENABLE = 1;
    public static final int PARA_FILETYPE_NONCELL = 16;
    public static final int PARA_PATHTYPE_COTA = 1;
    public static final int SCENE_ALIPAY_PAY = 100201;
    public static final int SCENE_AUDIO = 100105;
    public static final int SCENE_DOUYIN = 100501;
    public static final int SCENE_VIDEO = 100106;
    public static final int SCENE_WECHAT_PAY = 100104;
    public static final int SLOT_0 = 0;
    public static final int SLOT_1 = 1;
    public static final String TAG = "HiDATA_APPQoE";
    public static final String UNKNOWN_PACKAGE_NAME = "UNKNOWN";
    public static final int USER_DATA_TIME_ONE_DAY = 86400000;
    public static final int USER_DATA_TIME_ONE_WEEK = 604800000;
    public static final int USER_TYPE_COMMON = 1;
    public static final int USER_TYPE_DEFAULT = 0;
    public static final int USER_TYPE_RADICAL = 2;

    public static void logD(String info) {
        Log.d(TAG, info);
    }

    public static void logE(String info) {
        Log.e(TAG, info);
    }

    public static void logD(String tag, String info) {
        Log.d(tag, info);
    }
}
