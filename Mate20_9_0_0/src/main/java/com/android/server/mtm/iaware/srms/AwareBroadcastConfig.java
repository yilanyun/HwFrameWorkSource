package com.android.server.mtm.iaware.srms;

import android.os.RemoteException;
import android.rms.HwSysResManager;
import android.rms.IUpdateWhiteListCallback;
import android.rms.IUpdateWhiteListCallback.Stub;
import android.rms.iaware.AwareLog;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.server.mtm.MultiTaskManagerService;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class AwareBroadcastConfig {
    private static final int PROXYBR_FGAPPLEVAL_ID = 3;
    private static final int PROXYBR_PROXYPARAM_ID = 4;
    private static final int PROXYBR_TRIM_ACTION_ID = 6;
    private static final int PROXYBR_UNPROXY_ACTION_ID = 1;
    private static final int PROXYBR_UNPROXY_PKG_ID = 2;
    private static final int PROXYBR_UNPROXY_SYS_ID = 5;
    private static final int SIZE_PROXY_PARAM = 7;
    private static final String TAG = "AwareBroadcastConfig";
    private static final int TOUCH_CHACK_COUNT_INDEX = 0;
    private static final int TOUCH_NO_CHACK_COUNT_INDEX = 1;
    private static final int UNPROXYSYS_SPLIT_LENGTH = 2;
    private static final int UNPROXY_HIGH_SPEED_INDEX = 6;
    private static final int UNPROXY_MAX_DURATION_INDEX = 2;
    private static final int UNPROXY_MAX_SPEED_INDEX = 3;
    private static final int UNPROXY_MIDDLE_SPEED_INDEX = 5;
    private static final int UNPROXY_MIN_SPEED_INDEX = 4;
    private static AwareBroadcastConfig sInstance = null;
    private final ArraySet<String> mAwareProxyTrimActionList = new ArraySet();
    private final ArraySet<String> mAwareUnProxyActionList = new ArraySet();
    private final ArraySet<String> mAwareUnProxyPkgList = new ArraySet();
    private final ArrayMap<String, ArraySet<String>> mAwareUnProxySys = new ArrayMap();
    private int mFGAppLevel = 4;
    private AtomicBoolean mHasReadXml = new AtomicBoolean(false);
    private int mNoTouchCheckCount = 200;
    private int mTouchCheckCount = 60;
    private int mUnProxyHighSpeed = 20;
    private int mUnProxyMaxDuration = 20000;
    private int mUnProxyMaxSpeed = 150;
    private int mUnProxyMiddleSpeed = 40;
    private int mUnProxyMinSpeed = 60;
    private IUpdateWhiteListCallback mUpdateWhiteListCallback = new Stub() {
        public void update() throws RemoteException {
            AwareLog.d(AwareBroadcastConfig.TAG, "IUpdateWhiteListCallback update whiteList.");
            AwareBroadcastConfig.this.mHasReadXml.set(false);
            AwareBroadcastConfig.this.updateConfigFromRMS();
        }
    };

    public static AwareBroadcastConfig getInstance() {
        AwareBroadcastConfig awareBroadcastConfig;
        synchronized (AwareBroadcastConfig.class) {
            if (sInstance == null) {
                sInstance = new AwareBroadcastConfig();
            }
            awareBroadcastConfig = sInstance;
        }
        return awareBroadcastConfig;
    }

    private AwareBroadcastConfig() {
    }

    public void doinit() {
        if (!HwSysResManager.getInstance().registerResourceCallback(this.mUpdateWhiteListCallback)) {
            AwareLog.e(TAG, "IUpdateWhiteListCallback register failed");
        }
        updateConfigFromRMS();
    }

    private void updateConfigFromRMS() {
        if (AwareBroadcastDebug.getDebugDetail()) {
            AwareLog.i(TAG, "updateConfigFromRMS begin.");
        }
        if (!this.mHasReadXml.get()) {
            updateUnProxyActionFromRMS(32);
            updateUnProxyPkgFromRMS(32);
            updateFgAppLevelFromRMS(32);
            updateProxyParamFromRMS(32);
            updateUnProxySysFromRMS(32);
            updateTrimActionFromRMS(32);
            this.mHasReadXml.set(true);
            if (MultiTaskManagerService.self() != null) {
                AwareBroadcastPolicy iawareBrPolicy = MultiTaskManagerService.self().getIawareBrPolicy();
                if (iawareBrPolicy != null) {
                    iawareBrPolicy.updateXmlConfig();
                } else {
                    AwareLog.e(TAG, "updateConfigFromRMS exception, don't get polciy.");
                }
            } else {
                AwareLog.e(TAG, "updateConfigFromRMS exception, don't get mtm service.");
            }
        }
    }

    private void updateUnProxyActionFromRMS(int rmsGroupId) {
        String str = HwSysResManager.getInstance().getWhiteList(rmsGroupId, 1);
        if (str != null) {
            if (AwareBroadcastDebug.getDebugDetail()) {
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("get unproxy actions from rms :");
                stringBuilder.append(str);
                AwareLog.i(str2, stringBuilder.toString());
            }
            ArraySet<String> actionList = new ArraySet();
            for (String content : str.split(CPUCustBaseConfig.CPUCONFIG_GAP_IDENTIFIER)) {
                String content2 = content2.trim();
                if (!content2.isEmpty()) {
                    actionList.add(content2);
                }
            }
            synchronized (this.mAwareUnProxyActionList) {
                this.mAwareUnProxyActionList.clear();
                this.mAwareUnProxyActionList.addAll(actionList);
            }
        }
    }

    private void updateUnProxyPkgFromRMS(int rmsGroupId) {
        String str = HwSysResManager.getInstance().getWhiteList(rmsGroupId, 2);
        if (str != null) {
            if (AwareBroadcastDebug.getDebugDetail()) {
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("get unproxy pkgs from rms :");
                stringBuilder.append(str);
                AwareLog.i(str2, stringBuilder.toString());
            }
            ArraySet<String> pkgList = new ArraySet();
            for (String content : str.split(CPUCustBaseConfig.CPUCONFIG_GAP_IDENTIFIER)) {
                String content2 = content2.trim();
                if (!content2.isEmpty()) {
                    pkgList.add(content2);
                }
            }
            synchronized (this.mAwareUnProxyPkgList) {
                this.mAwareUnProxyPkgList.clear();
                this.mAwareUnProxyPkgList.addAll(pkgList);
            }
        }
    }

    private void updateFgAppLevelFromRMS(int rmsGroupId) {
        String str = HwSysResManager.getInstance().getWhiteList(rmsGroupId, 3);
        if (str != null && !str.isEmpty()) {
            if (AwareBroadcastDebug.getDebugDetail()) {
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("get fg app level from rms :");
                stringBuilder.append(str);
                AwareLog.i(str2, stringBuilder.toString());
            }
            if (str.startsWith(CPUCustBaseConfig.CPUCONFIG_GAP_IDENTIFIER)) {
                str = str.substring(1);
            }
            try {
                int fgAppLevel = Integer.parseInt(str.trim());
                if (fgAppLevel < 0) {
                    AwareLog.e(TAG, "fg app level error, it is wrong config");
                } else {
                    this.mFGAppLevel = fgAppLevel;
                }
            } catch (NumberFormatException e) {
                AwareLog.e(TAG, "fg app level error in config");
            }
        }
    }

    private void updateProxyParamFromRMS(int rmsGroupId) {
        String str = HwSysResManager.getInstance().getWhiteList(rmsGroupId, 4);
        if (str != null && !str.isEmpty()) {
            if (AwareBroadcastDebug.getDebugDetail()) {
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("get proxy param from rms :");
                stringBuilder.append(str);
                AwareLog.i(str2, stringBuilder.toString());
            }
            if (str.startsWith(CPUCustBaseConfig.CPUCONFIG_GAP_IDENTIFIER)) {
                str = str.substring(1);
            }
            String[] contentArray = str.trim().split(CPUCustBaseConfig.CPUCONFIG_GAP_IDENTIFIER);
            if (contentArray.length != 7) {
                AwareLog.e(TAG, "proxy param size error in config");
                return;
            }
            int value;
            ArrayList actionList = new ArrayList(7);
            int length = contentArray.length;
            int i = 0;
            while (i < length) {
                String content = contentArray[i].trim();
                if (!content.isEmpty()) {
                    value = 0;
                    try {
                        actionList.add(Integer.valueOf(Integer.parseInt(content)));
                        i++;
                    } catch (NumberFormatException e) {
                        AwareLog.e(TAG, "proxy param error in config");
                        return;
                    }
                }
                return;
            }
            length = ((Integer) actionList.get(0)).intValue();
            int noTouchCheckCount = ((Integer) actionList.get(1)).intValue();
            int unProxyMaxDuration = ((Integer) actionList.get(2)).intValue();
            i = ((Integer) actionList.get(3)).intValue();
            int unProxyMinSpeed = ((Integer) actionList.get(4)).intValue();
            int unProxyMiddleSpeed = ((Integer) actionList.get(5)).intValue();
            value = ((Integer) actionList.get(6)).intValue();
            if (length <= 0 || noTouchCheckCount <= 0) {
                AwareLog.e(TAG, "check count error, it is wrong config");
            } else if (i <= 0 || unProxyMinSpeed <= 0 || i < unProxyMinSpeed) {
                AwareLog.e(TAG, "unproxy max speed less than min speed, it is wrong config");
            } else if (unProxyMaxDuration <= 0) {
                AwareLog.e(TAG, "unproxy max duration error, it is wrong config");
            } else if (unProxyMiddleSpeed <= 0 || value <= 0) {
                AwareLog.e(TAG, "check high or middle error, it is wrong config");
            } else {
                this.mTouchCheckCount = length;
                this.mNoTouchCheckCount = noTouchCheckCount;
                this.mUnProxyMaxDuration = unProxyMaxDuration;
                this.mUnProxyMaxSpeed = i;
                this.mUnProxyMinSpeed = unProxyMinSpeed;
                this.mUnProxyMiddleSpeed = unProxyMiddleSpeed;
                this.mUnProxyHighSpeed = value;
            }
        }
    }

    private void updateTrimActionFromRMS(int rmsGroupId) {
        String str = HwSysResManager.getInstance().getWhiteList(rmsGroupId, 6);
        if (str != null) {
            if (AwareBroadcastDebug.getDebugDetail()) {
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("get trim action from rms :");
                stringBuilder.append(str);
                AwareLog.i(str2, stringBuilder.toString());
            }
            ArraySet<String> actionList = new ArraySet();
            for (String content : str.split(CPUCustBaseConfig.CPUCONFIG_GAP_IDENTIFIER)) {
                String content2 = content2.trim();
                if (!content2.isEmpty()) {
                    actionList.add(content2);
                }
            }
            synchronized (this.mAwareProxyTrimActionList) {
                this.mAwareProxyTrimActionList.clear();
                this.mAwareProxyTrimActionList.addAll(actionList);
            }
        }
    }

    private void updateUnProxySysFromRMS(int rmsGroupId) {
        String str = HwSysResManager.getInstance().getWhiteList(rmsGroupId, 5);
        if (str != null) {
            if (AwareBroadcastDebug.getDebugDetail()) {
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("get no proxy sys from rms :");
                stringBuilder.append(str);
                AwareLog.i(str2, stringBuilder.toString());
            }
            ArrayMap<String, ArraySet<String>> sysApp = new ArrayMap();
            String[] contentArray = str.split(CPUCustBaseConfig.CPUCONFIG_GAP_IDENTIFIER);
            int size = contentArray.length;
            int i = 0;
            int i2 = 0;
            while (i2 < size) {
                String content = contentArray[i2].trim();
                if (!content.isEmpty()) {
                    String[] pkgItems = content.split(":");
                    if (pkgItems.length == 2) {
                        ArraySet<String> actions = new ArraySet();
                        String sysPkg = pkgItems[i].trim();
                        String[] actionBrs = pkgItems[1].trim().split(",");
                        int actionSize = actionBrs.length;
                        for (int index = i; index < actionSize; index++) {
                            actions.add(actionBrs[index].trim());
                        }
                        ArraySet<String> actionsExist = (ArraySet) sysApp.get(sysPkg);
                        if (actionsExist != null) {
                            actionsExist.addAll(actions);
                        } else {
                            sysApp.put(sysPkg, actions);
                        }
                    } else {
                        AwareLog.e(TAG, "check unproxy sys app br error, it is wrong config");
                    }
                }
                i2++;
                i = 0;
            }
            synchronized (this.mAwareUnProxySys) {
                this.mAwareUnProxySys.clear();
                this.mAwareUnProxySys.putAll(sysApp);
            }
        }
    }

    public ArraySet<String> getUnProxyActionList() {
        ArraySet arraySet;
        synchronized (this.mAwareUnProxyActionList) {
            arraySet = new ArraySet(this.mAwareUnProxyActionList);
        }
        return arraySet;
    }

    public ArraySet<String> getUnProxyPkgList() {
        ArraySet arraySet;
        synchronized (this.mAwareUnProxyPkgList) {
            arraySet = new ArraySet(this.mAwareUnProxyPkgList);
        }
        return arraySet;
    }

    public ArrayMap<String, ArraySet<String>> getUnProxySysList() {
        ArrayMap arrayMap;
        synchronized (this.mAwareUnProxySys) {
            arrayMap = new ArrayMap(this.mAwareUnProxySys);
        }
        return arrayMap;
    }

    public ArraySet<String> getTrimActionList() {
        ArraySet arraySet;
        synchronized (this.mAwareProxyTrimActionList) {
            arraySet = new ArraySet(this.mAwareProxyTrimActionList);
        }
        return arraySet;
    }

    public int getFGAppLevel() {
        return this.mFGAppLevel;
    }

    public int getTouchCheckCount() {
        return this.mTouchCheckCount;
    }

    public int getNoTouchCheckCount() {
        return this.mNoTouchCheckCount;
    }

    public int getUnProxyMaxDuration() {
        return this.mUnProxyMaxDuration;
    }

    public int getUnProxyMaxSpeed() {
        return this.mUnProxyMaxSpeed;
    }

    public int getUnProxyMinSpeed() {
        return this.mUnProxyMinSpeed;
    }

    public int getUnProxyMiddleSpeed() {
        return this.mUnProxyMiddleSpeed;
    }

    public int getUnProxyHighSpeed() {
        return this.mUnProxyHighSpeed;
    }
}
