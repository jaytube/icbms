package com.wz.modules.migration.util;

import com.wz.modules.migration.enums.LocationNodeType;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Cherry
 * @Date: 2020/12/13
 * @Desc: LocationInfoUtil
 */
public class LocationInfoUtil {

    public static void parseLocationNodeType(LocationInfoEntity locationInfoEntity) {
        String rootStr = locationInfoEntity.getRoot();
        if (StringUtils.isBlank(rootStr)) {
            return;
        }
        String[] split = StringUtils.split(rootStr, ',');
        if (split.length == 1) {
            locationInfoEntity.setType(LocationNodeType.ROOT.toString());
        } else if (split.length == 2) {
            locationInfoEntity.setType(LocationNodeType.VENUE.toString());
        } else if (split.length == 3) {
            locationInfoEntity.setType(LocationNodeType.EXHIBITION.toString());
        } else if (split.length == 4) {
            locationInfoEntity.setType(LocationNodeType.DEVICE.toString());
        }
    }
}
