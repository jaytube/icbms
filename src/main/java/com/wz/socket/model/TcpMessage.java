package com.wz.socket.model;

public class TcpMessage {
	
	//协议类型
	private int protocolType;
	// 起始位
	private byte startByte;
	// 网管id
	private String gatewayId;
	// 终端id
	private String terminalId;
	// 版本号
	private String version;
	// 结束位
	private byte endByte;
	
	// 表示命令号，下述功能中已给具体命令号
	private int cmd;
	
	private byte[] data;
	
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public int getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}
	public byte getStartByte() {
		return startByte;
	}
	public void setStartByte(byte startByte) {
		this.startByte = startByte;
	}
	public String getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public byte getEndByte() {
		return endByte;
	}
	public void setEndByte(byte endByte) {
		this.endByte = endByte;
	}
	
}
