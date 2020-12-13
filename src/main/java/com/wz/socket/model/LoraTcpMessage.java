package com.wz.socket.model;

import com.wz.socket.utils.Constant;

public class LoraTcpMessage extends TcpMessage {
		//开关地址-1字节
		private String switchAddr;
		// 开关合分-1字节
		private String switchOnoff;

		// 是否受控-1字节
		private String controlFlag;

		// 电压-2字节
		private String voltage;

		// 漏电流-2字节
		private String leakageCurrent;

		// 功率-2字节
		private String power;

		// 温度-2字节
		private String temperature;

		// 电流-2字节
		private String electricCurrent;

		// 告警-2字节
		private String alarm;

		// 电量-4字节
		private String electricCnt;
		
		//门锁开关-1字节
		private String lockStatus;

		// A相电压-2字节
		private String phaseVoltageA;

		// B相电压-2字节
		private String phaseVoltageB;

		// C相电压-2字节
		private String phaseVoltageC;

		// A相电流-2字节
		private String phaseCurrentA;

		// B相电流-2字节
		private String phaseCurrentB;

		// C相电流-2字节
		private String phaseCurrentC;

		// N相电流-2字节
		private String phaseCurrentN;

		// A相功率-2字节
		private String phasePowerA;

		// B相功率-2字节
		private String phasePowerB;

		// C相功率-2字节
		private String phasePowerC;

		// A相告警-2字节
		private String phaseAlarmA;

		// B相告警-2字节
		private String phaseAlarmB;

		// C相告警-2字节
		private String phaseAlarmC;
		
		//从机地址域-1字节
		private byte addrByte;
		
		//功能域-1字节 
		private byte cmdByte;
		
		//数据地址高位-1字节  高位一般表示功能
		private byte dataAddrHighByte;
		
		//数据地址低位-1字节 低位一般表示开关地址
		private byte dataAddrLowByte;
		
		//数据内容高位--1字节
		private byte dataContentHighByte;
		
		//数据内容低位--1字节
		private byte dataContentLowByte;
		
		//CRC校验低位
		private byte crcLowByte;
		
		//CRC校验高位
		private byte crcHighByte;
		
		public LoraTcpMessage() {
			setProtocolType(Constant.SHUANGRUI_CAIJIYI_TCP);
		}

		public String getSwitchAddr() {
			return switchAddr;
		}

		public void setSwitchAddr(String switchAddr) {
			this.switchAddr = switchAddr;
		}

		public String getSwitchOnoff() {
			return switchOnoff;
		}

		public void setSwitchOnoff(String switchOnoff) {
			this.switchOnoff = switchOnoff;
		}

		public String getControlFlag() {
			return controlFlag;
		}

		public void setControlFlag(String controlFlag) {
			this.controlFlag = controlFlag;
		}

		public String getVoltage() {
			return voltage;
		}

		public void setVoltage(String voltage) {
			this.voltage = voltage;
		}

		public String getLeakageCurrent() {
			return leakageCurrent;
		}

		public void setLeakageCurrent(String leakageCurrent) {
			this.leakageCurrent = leakageCurrent;
		}

		public String getPower() {
			return power;
		}

		public void setPower(String power) {
			this.power = power;
		}

		public String getTemperature() {
			return temperature;
		}

		public void setTemperature(String temperature) {
			this.temperature = temperature;
		}

		public String getElectricCurrent() {
			return electricCurrent;
		}

		public void setElectricCurrent(String electricCurrent) {
			this.electricCurrent = electricCurrent;
		}

		public String getAlarm() {
			return alarm;
		}

		public void setAlarm(String alarm) {
			this.alarm = alarm;
		}

		public String getPhaseVoltageA() {
			return phaseVoltageA;
		}

		public void setPhaseVoltageA(String phaseVoltageA) {
			this.phaseVoltageA = phaseVoltageA;
		}

		public String getPhaseVoltageB() {
			return phaseVoltageB;
		}

		public void setPhaseVoltageB(String phaseVoltageB) {
			this.phaseVoltageB = phaseVoltageB;
		}

		public String getPhaseVoltageC() {
			return phaseVoltageC;
		}

		public void setPhaseVoltageC(String phaseVoltageC) {
			this.phaseVoltageC = phaseVoltageC;
		}

		public String getPhaseCurrentA() {
			return phaseCurrentA;
		}

		public void setPhaseCurrentA(String phaseCurrentA) {
			this.phaseCurrentA = phaseCurrentA;
		}

		public String getPhaseCurrentB() {
			return phaseCurrentB;
		}

		public void setPhaseCurrentB(String phaseCurrentB) {
			this.phaseCurrentB = phaseCurrentB;
		}

		public String getPhaseCurrentC() {
			return phaseCurrentC;
		}

		public void setPhaseCurrentC(String phaseCurrentC) {
			this.phaseCurrentC = phaseCurrentC;
		}

		public String getPhaseCurrentN() {
			return phaseCurrentN;
		}

		public void setPhaseCurrentN(String phaseCurrentN) {
			this.phaseCurrentN = phaseCurrentN;
		}

		public String getPhasePowerA() {
			return phasePowerA;
		}

		public void setPhasePowerA(String phasePowerA) {
			this.phasePowerA = phasePowerA;
		}

		public String getPhasePowerB() {
			return phasePowerB;
		}

		public void setPhasePowerB(String phasePowerB) {
			this.phasePowerB = phasePowerB;
		}

		public String getPhasePowerC() {
			return phasePowerC;
		}

		public void setPhasePowerC(String phasePowerC) {
			this.phasePowerC = phasePowerC;
		}

		public String getPhaseAlarmA() {
			return phaseAlarmA;
		}

		public void setPhaseAlarmA(String phaseAlarmA) {
			this.phaseAlarmA = phaseAlarmA;
		}

		public String getPhaseAlarmB() {
			return phaseAlarmB;
		}

		public void setPhaseAlarmB(String phaseAlarmB) {
			this.phaseAlarmB = phaseAlarmB;
		}

		public String getPhaseAlarmC() {
			return phaseAlarmC;
		}

		public void setPhaseAlarmC(String phaseAlarmC) {
			this.phaseAlarmC = phaseAlarmC;
		}

		public String getElectricCnt() {
			return electricCnt;
		}

		public void setElectricCnt(String electricCnt) {
			this.electricCnt = electricCnt;
		}

		public String getLockStatus() {
			return lockStatus;
		}

		public void setLockStatus(String lockStatus) {
			this.lockStatus = lockStatus;
		}

		public byte getAddrByte() {
			return addrByte;
		}

		public void setAddrByte(byte addrByte) {
			this.addrByte = addrByte;
		}

		public byte getCmdByte() {
			return cmdByte;
		}

		public void setCmdByte(byte cmdByte) {
			this.cmdByte = cmdByte;
		}

		public byte getDataAddrHighByte() {
			return dataAddrHighByte;
		}

		public void setDataAddrHighByte(byte dataAddrHighByte) {
			this.dataAddrHighByte = dataAddrHighByte;
		}

		public byte getDataAddrLowByte() {
			return dataAddrLowByte;
		}

		public void setDataAddrLowByte(byte dataAddrLowByte) {
			this.dataAddrLowByte = dataAddrLowByte;
		}

		public byte getDataContentHighByte() {
			return dataContentHighByte;
		}

		public void setDataContentHighByte(byte dataContentHighByte) {
			this.dataContentHighByte = dataContentHighByte;
		}

		public byte getDataContentLowByte() {
			return dataContentLowByte;
		}

		public void setDataContentLowByte(byte dataContentLowByte) {
			this.dataContentLowByte = dataContentLowByte;
		}

		public byte getCrcLowByte() {
			return crcLowByte;
		}

		public void setCrcLowByte(byte crcLowByte) {
			this.crcLowByte = crcLowByte;
		}

		public byte getCrcHighByte() {
			return crcHighByte;
		}

		public void setCrcHighByte(byte crcHighByte) {
			this.crcHighByte = crcHighByte;
		}
}
