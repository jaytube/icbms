package com.wz.modules.app.service.impl;

import com.wz.front.dto.ProjectBoxStatusCntDto;
import com.wz.modules.app.service.ApiKKService;
import com.wz.modules.common.utils.RedisUtil;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.devicelog.entity.DeviceElectricityLogEntity;
import com.wz.modules.kk.biz.KkBizUtil;
import com.wz.modules.kk.entity.DeviceAlarm;
import com.wz.modules.kk.entity.LineUpper;
import com.wz.modules.kk.entity.PageInfo;
import com.wz.socket.cinterface.ClientRequestCmdSend;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service("kKApiService")
public class ApiKKServiceImpl implements ApiKKService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DeviceBoxInfoService deviceBoxInfoService;

    @Override
    public String setCmdSwitch(String projectId, String deviceBoxMac, String switchAddStrs, String cmd) {
        String result = "0";
        // 0表示分闸 1表示合闸
        if ("open".equals(cmd)) {
            cmd = "1";
        } else if ("close".equals(cmd)) {
            cmd = "0";
        }
        DeviceBoxInfoEntity deviceBox = this.deviceBoxInfoService.queryByMac(deviceBoxMac, projectId);
        if (null != deviceBox) {
            String deviceBoxAddress = Integer.parseInt(deviceBox.getDeviceBoxNum().substring(10)) + "";
            String resultJson = redisUtil.hget(0, "TERMINAL_STATUS", deviceBoxAddress);
            JSONObject dataObj = JSONObject.fromObject(resultJson);
            String gatewayAddress = dataObj.getString("gatewayId");
            String[] switchIds = switchAddStrs.split(",");
            if (switchIds != null && switchIds.length > 0) {
                for (int i = 0; i < switchIds.length; i++) {
                    // 这里一个一个开关调用
                    ClientRequestCmdSend.getIntance().sendControlSwichCmd(gatewayAddress, deviceBoxAddress,
                            switchIds[i], cmd);

                }
            }
        }
        return result;
    }

    @Override
    public List<DeviceSwitchInfoEntity> getBoxChannelsRealData(String deviceBoxMac, String projectId) {
        List<DeviceSwitchInfoEntity> switchList = new ArrayList<DeviceSwitchInfoEntity>();
        DeviceBoxInfoEntity deviceBox = this.deviceBoxInfoService.queryByMac(deviceBoxMac, projectId);
        if (null != deviceBox) {
            String field = Integer.parseInt(deviceBox.getDeviceBoxNum().substring(10)) + "_";
            Map<String, String> result = redisUtil.fuzzyGet(0, "REAL_DATA", field);
            for (String k : result.keySet()) {
                String resultJson = result.get(k);
                JSONObject jsonObj = JSONObject.fromObject(resultJson);
                DeviceSwitchInfoEntity dsInfo = new DeviceSwitchInfoEntity();
                dsInfo.setAddress((Integer.parseInt(jsonObj.getString("switchAddr")) + 1) + "");
                dsInfo.setDeviceSwitchName("线路" + (Integer.parseInt(jsonObj.getString("switchAddr")) + 1));
                dsInfo.setDeviceSwitchStatus("0".equals(jsonObj.getString("switchOnoff")) ? "true" : "false");
                dsInfo.setSwitchPower(jsonObj.getString("power"));// 功率
                dsInfo.setSwitchVoltage(jsonObj.getString("voltage"));// 电压
                dsInfo.setSwitchElectric(jsonObj.getString("electricCurrent"));// 电流
                dsInfo.setSwitchLeakage(jsonObj.getString("leakageCurrent"));// 漏电流
                dsInfo.setSwitchTemperature(jsonObj.getString("temperature"));// 温度
                switchList.add(dsInfo);
            }
        }
        return switchList;
    }

    @Override
    public PageInfo<DeviceAlarm> getAlarmsByTime(String deviceBoxMac, String startTime, String endTime, String pageSize,
                                                 String page) {
        PageInfo<DeviceAlarm> pageInfo = new PageInfo<DeviceAlarm>();
        try {
            String resultJson = KkBizUtil.getAlarmsByTime(deviceBoxMac, startTime, endTime, pageSize, page);
            JSONObject dataObj = JSONObject.fromObject(resultJson);
            String result = dataObj.getString("code");
            if ("0".equals(result)) {
                JSONObject pageData = dataObj.getJSONObject("data");
                pageInfo.setTotal(pageData.getString("total"));
                pageInfo.setTotalPage(pageData.getString("totalPage"));
                JSONArray alarmsDataArray = JSONArray.fromObject(pageData.getString("datas"));
                List<DeviceAlarm> alarmList = new ArrayList<DeviceAlarm>();
                if (alarmsDataArray.size() > 0) {
                    for (int i = 0; i < alarmsDataArray.size(); i++) {
                        DeviceAlarm alarm = new DeviceAlarm();
                        JSONObject obj = alarmsDataArray.getJSONObject(i);
                        alarm.setInfo(obj.getString("info"));
                        alarm.setNode(obj.getString("node"));
                        alarm.setTime(obj.getString("time"));
                        alarm.setType(obj.getString("type"));
                        alarmList.add(alarm);
                        System.out.println(obj.get("node"));
                    }
                }
                Collections.sort(alarmList);
                pageInfo.setDataList(alarmList);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return pageInfo;
    }

    @Override
    public String setUpdateDateTime(String deviceBoxMac, String cmd) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void doKkSchedule() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doKkAlarmSchedule() {
        // TODO Auto-generated method stub

    }

    @Override
    public List<DeviceElectricityLogEntity> getBoxDayPower(String deviceBoxMac) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void doKkDayElectricity() {
        // TODO Auto-generated method stub

    }

    @Override
    public List<LineUpper> getLineUpper(String deviceBoxMac) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void processDeviceBoxOnline(List<DeviceBoxInfoEntity> deviceBoxList) {
        // TODO Auto-generated method stub

    }

    @Override
    public Map<String, Integer> statDeviceBoxOnline(String projectId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, Integer> statDeviceBoxOnline(List<DeviceBoxInfoEntity> boxList) {
        return null;
    }

    @Override
    public Map<String, Integer> statDeviceBoxOnline(List<DeviceBoxInfoEntity> boxList, Map<String, String> redisStatus) {
        return null;
    }

    public void doKkProject() {

    }

    @Override
    public void setTerminalPsrams(String terminalAddr, String switchAddr, String maxPower, String maxEletric) {
        ClientRequestCmdSend.getIntance().sendWriteThresholdParamsCmd("0", terminalAddr, switchAddr, "3",
                Short.parseShort(maxPower));// 配置功率门限
        ClientRequestCmdSend.getIntance().sendWriteThresholdParamsCmd("0", terminalAddr, switchAddr, "5",
                Short.parseShort(maxEletric));// 配置电流门限
    }

    @Override
    public Map<String, String> getTerminalParams(String termainlAddr, String switchAddr) {
        ClientRequestCmdSend.getIntance().sendReadThresholdParamsCmd("0", termainlAddr, switchAddr, "3");// 功率门限
        ClientRequestCmdSend.getIntance().sendReadThresholdParamsCmd("0", termainlAddr, switchAddr, "5");// 电流门限
        return null;
    }

    /**
     * @param gwAddress       网关地址
     * @param gwNum           组网网关个数
     * @param gwList          组网网关列表(从小到到)
     * @param startAddr       组网起始节点地址
     * @param endAddr         组网结束节点地址
     * @param gwDelayTime     组网网关之间发送延时
     * @param dxDelayTime     组网节点之间发送延时
     * @param getServerTimeWx 获取服务器时间准许网络延时误差
     * @param okFlag          配置有效标志位
     * @param queueFlag       服务器发送命令至网关，网关插队或排队发送至节点标志位
     * @param reportCycle     上报周期
     * @param gw2DxOverTime   网关发送节点命令回应超时时间
     * @return
     */
    @Override
    public void setGwParams(String gwAddress, String gwNum, String gwList, String startAddr, String endAddr,
                            String gwDelayTime, String dxDelayTime, String getServerTimeWx, String okFlag, String queueFlag,
                            String reportCycle, String gw2DxOverTime) {
        ClientRequestCmdSend.getIntance().setGwParams(gwAddress, gwNum, gwList, startAddr, endAddr, gwDelayTime,
                dxDelayTime, getServerTimeWx, okFlag, queueFlag, reportCycle, gw2DxOverTime);
    }

    @Override
    public void getGwParams(String gwAddress) {
        ClientRequestCmdSend.getIntance().getGwParams(gwAddress);
    }


    @Override
    public void doSynTerminalConfig(String projectId) {
        // TODO Auto-generated method stub

    }

    @Override
    public ProjectBoxStatusCntDto getBoxesRecentStatus(String projectId) {
        return null;
    }

}
