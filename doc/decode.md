# 终端发送数据包格式

```text
1. 服务器通信地址端口: 119.96.233.75: 65003
2. TCP 数据 —> 16进制
```

## 举例分析

```text
例一（实时数据）：

1A 00 02 00 FC 01 00 00 00 01 90 00 00 00 00 01 4A 00 00 00 00 00 34
00 00 00 EA 00 DF 00 EC 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
00 00 00 00 00 00 00 01 1B 1C 00 02 00 00 01 00 1D 1A 00 02 00 FC 01
00 00 00 01 90 00 00 00 00 01 4A
```

### 例一数据解析

|  数据包索引  |     位置属性      |                     原始值（16进制代码）                     |         解析值（实际意义值）         |
| :----------: | :---------------: | :----------------------------------------------------------: | :----------------------------------: |
|    第1位     | protoType指令类型 |                              1A                              | 26（网关主动上报实时数据与告警数据） |
| 第2位-第3位  |      网关ID       |                            00 02                             |                  2                   |
| 第4位-第5位  |      终端ID       |                            00  FC                            |                 252                  |
|    第6位     |       版本        |                              01                              |                  1                   |
| 第7位-第53位 |     data数组      | 00  00 00 01 90 00 00 00 00 01 4A 00 00 00 00 00 34 00 00 00 EA 00 DF 00 EC 00 00  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 |                                      |
|    第54位    |       跳过        |                                                              |                                      |
|    第55位    |       结束        |                              1B                              |                                      |
|    剩下的    | 有字符，长度可变  |                                                              |                                      |

### 其中data数据解析
