package com.wz.modules.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevicePlainInfoDto {

    private String boxNo;
    private String devEUI;
    private int gatewayId;
    private String projectId;

}
