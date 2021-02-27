package com.wz.modules.deviceinfo.component;

import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ExportXlsView extends AbstractXlsView {

    private final String[] headers = {"位置信息", "电箱(MAC)", "展位号", "二级开关", "电箱容量", "是否受控"};
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook,
                                      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Object data = map.get("data");
        List<DeviceBoxInfoEntity> dataList = (List<DeviceBoxInfoEntity>) data;
        Sheet deviceSheet = workbook.createSheet("电箱设备信息");
        Row head = deviceSheet.createRow(0);
        int columnSize = headers.length;
        for (int j=0; j<columnSize; j++) {
            head.createCell(j).setCellValue(headers[j]);
        }
        AtomicInteger i = new AtomicInteger();
        dataList.stream().forEach(h -> {
            Row row = deviceSheet.createRow(i.getAndIncrement());
            row.createCell(1).setCellValue(h.getLocationName());
            row.createCell(2).setCellValue(h.getDeviceBoxNum());
            row.createCell(3).setCellValue(h.getStandNo());
            row.createCell(4).setCellValue(h.getSecBoxGateway());
            row.createCell(5).setCellValue(h.getBoxCapacity());
            row.createCell(6).setCellValue(h.getControlFlag());
        });

    }
}
