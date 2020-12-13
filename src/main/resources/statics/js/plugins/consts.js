var API_BASE_URL = "../app/dashbaord/";

var api = {
    API_GET_LOCATION : API_BASE_URL + "findLocInfoByPId",
    API_GET_SWITCHBOX :　API_BASE_URL + "getDeviceBoxInfoByLocation",
    API_GET_ADDRESS :　API_BASE_URL　+　"getRealDataByBoxMac",
    API_GET_ALARMS : API_BASE_URL + "queryDeviceAlarmList",
    API_CONTROL : API_BASE_URL + "setCmdSwitch"
}