package com.wz.modules.migration.service.impl;

import com.wz.modules.deviceinfo.dao.DeviceMacSnMapDao;
import com.wz.modules.deviceinfo.entity.DeviceMacSnEntity;
import com.wz.modules.migration.service.MigrationService;
import com.wz.modules.migration.util.LocationInfoUtil;
import com.wz.modules.projectinfo.dao.LocationInfoDao;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import com.wz.modules.sys.dao.UserDao;
import com.wz.modules.sys.dao.UserProjectDao;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.entity.UserProjectEntity;
import com.wz.modules.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MigrationServiceImpl implements MigrationService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProjectDao userProjectDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LocationInfoDao locationInfoDao;

    @Autowired
    private DeviceMacSnMapDao deviceMacSnMapDao;

    @Override
    public Boolean migrateUserProjects() {
        Map<String, Object> map = new HashMap<>();
        List<UserEntity> users = userService.queryList(map);

        if (!CollectionUtils.isEmpty(users)) {
            String[] userIds = users.stream().map(UserEntity::getId).collect(Collectors.toList()).toArray(new String[]{});
            for (UserEntity user : users) {
                String projectIds = user.getProjectIds();
                if (StringUtils.isNotBlank(projectIds)) {
                    List<UserProjectEntity> userProjectEntities = Arrays.stream(projectIds.split(","))
                            .map(String::trim)
                            .map(t -> {
                                UserProjectEntity userPro = new UserProjectEntity();
                                userPro.setUserId(user.getId());
                                userPro.setProjectId(t);
                                return userPro;
                            }).collect(Collectors.toList());
                    userProjectDao.saveBatch(userProjectEntities);
                }
            }
            userDao.updateBatchProjectIds(userIds);
        }

        return true;
    }

    @Override
    public Boolean migrateLocationInfos() {
        List<LocationInfoEntity> locationInfoEntities = locationInfoDao.fetchAll();
        buildTree(locationInfoEntities);
        return true;
    }

    @Override
    public Boolean loadDeviceMacSnMap() {
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<DeviceMacSnEntity> list = new ArrayList<>();
        String cellData = null;
        String filePath = "/Users/tuxiaoyue/Downloads/串码和电箱号对应表1.xlsx";
        String columns[] = {"sn","mac"};
        wb = readExcel(filePath);
        if(wb != null){
            sheet = wb.getSheetAt(0);
            int rownum = sheet.getPhysicalNumberOfRows();
            row = sheet.getRow(0);
            int colnum = row.getPhysicalNumberOfCells();
            for (int i = 1; i<rownum; i++) {
                DeviceMacSnEntity entity = new DeviceMacSnEntity();
                row = sheet.getRow(i);
                if(row !=null){
                    cellData = (String) getCellFormatValue(row.getCell(0));
                    entity.setDeviceSn(cellData);
                    cellData = (String) getCellFormatValue(row.getCell(1));
                    entity.setDeviceMac(cellData);
                }else{
                    break;
                }
                list.add(entity);
            }
        }
        //遍历解析出来的list
        for (DeviceMacSnEntity entity : list) {
            System.out.print(entity.getDeviceMac()+":"+entity.getDeviceSn()+",");
            System.out.println();
        }
        deviceMacSnMapDao.batchInsert(list);

        return true;
    }

    public static void main(String[] args) {
        new MigrationServiceImpl().loadDeviceMacSnMap();
    }

    private static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }


    private static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }


    private void buildTree(List<LocationInfoEntity> locationInfoEntities) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(locationInfoEntities)) {
            return;
        }

        locationInfoEntities.forEach(locationInfoEntity -> {
            LocationInfoUtil.parseLocationNodeType(locationInfoEntity);
        });
        locationInfoDao.updateTypeBatch(locationInfoEntities);
    }
}
