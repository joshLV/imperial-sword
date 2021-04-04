// package cn.lovike.tool.basic.framework.controller;
//
// import cn.lovike.tool.basic.framework.common.util.excel.ExportUtil;
// import cn.lovike.tool.basic.framework.common.util.excel.ImportUtil;
// import cn.lovike.tool.basic.framework.common.util.excel.MessageUtil;
// import cn.lovike.tool.basic.framework.exception.BusException;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;
//
// import javax.servlet.http.HttpServletResponse;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
//
// /**
//  * Excel 导入导出
//  *
//  * @author lovike
//  * @since 2020/10/29
//  */
// @RestController
// public class ExcelTestController {
//
//     private final Logger logger = LoggerFactory.getLogger(ExcelTestController.class);
//
//     @Autowired
//     private IPermissionService permissionService;
//
//     @RequestMapping("/export")
//     public void export(HttpServletResponse response) {
//         List<Permission> permissionList = permissionService.selectByRoleId(1L);
//         String           downFilename   = "permissionList_" + System.currentTimeMillis() + ".xlsx";
//         response.setContentType("application/x-download");
//         response.setHeader("Content-Disposition", "attachment; filename=" + downFilename);
//         // 第一行标题
//         final String[] titles       = MessageUtil.getPermListTitles();
//         OutputStream   outputStream = null;
//         try {
//             outputStream = response.getOutputStream();
//             // xlsx 导出
//             ExportUtil.exportExcel(titles, permissionList, (headers, entity) -> {
//                 Map<String, String> map = new HashMap<>(14);
//                 // id
//                 map.put(headers[0], String.valueOf(entity.getId()));
//                 // 权限名称
//                 map.put(headers[1], entity.getPermName());
//                 // 权限号码
//                 map.put(headers[2], entity.getUrl());
//
//                 // 生成时间
//                 // map.put(headers[5], Optional.ofNullable(entity.getGmtCreate())
//                 //         .map((date) -> DateUtil.dateToStr(entity.getGmtCreate(), DateUtil.DATE_TIME_FORMATTER))
//                 //         .orElse(null));
//
//                 return map;
//             }, ExportUtil.EXCEL_SUFFIX_XLSX, outputStream);
//         } catch (Exception e) {
//             logger.error("导出异常", e);
//         }
//     }
//
//     @PostMapping("importList")
//     public void exportList(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
//
//         String excelName = file.getOriginalFilename();
//
//         String suffix = null;
//
//         if (excelName.endsWith(ImportUtil.EXCEL_SUFFIX_XLSX)) {
//             suffix = ImportUtil.EXCEL_SUFFIX_XLSX;
//         }
//
//         if (excelName.endsWith(ImportUtil.EXCEL_SUFFIX_XLS)) {
//             suffix = ImportUtil.EXCEL_SUFFIX_XLS;
//         }
//
//         if (null == suffix) {
//             throw new BusException(SystemEnum.ERROR_FILE);
//         }
//
//         InputStream inputStream = null;
//         try {
//             inputStream = file.getInputStream();
//             ImportUtil.Result<List<Permission>> result = ImportUtil.importExcel(suffix, (row) -> {
//                 Permission    cmCheckInfoVO   = new Permission();
//                 StringBuilder errorSb         = new StringBuilder();
//                 int           rowNum          = row.getRowNum();
//                 String        id              = getStringCellVal(row.getCell(0));
//                 String        accountName     = getStringCellVal(row.getCell(1));
//                 String        idNumber        = getStringCellVal(row.getCell(2));
//                 String        accountMobile   = getStringCellVal(row.getCell(3));
//                 String        checkCode       = getStringCellVal(row.getCell(4));
//                 String        gmtCreate       = getStringCellVal(row.getCell(5));
//                 String        riskLevel       = getStringCellVal(row.getCell(6));
//                 String        checkResult     = getStringCellVal(row.getCell(7));
//                 String        checkSchedule   = getStringCellVal(row.getCell(8));
//                 String        headCheckUser   = getStringCellVal(row.getCell(9));
//                 String        branchCheckUser = getStringCellVal(row.getCell(10));
//                 String        subCheckUser    = getStringCellVal(row.getCell(11));
//                 String        orgCode         = getStringCellVal(row.getCell(12));
//
//                 if (errorSb.length() > 0) {
//                     return ImportUtil.Result.fail("第" + rowNum + "行数据错误:<br/>" + errorSb.toString());
//                 }
//                 // cmCheckInfoVO.setId(id);
//                 // cmCheckInfoVO.setAccountName(accountName);
//                 // cmCheckInfoVO.setIdNumber(idNumber);
//                 // cmCheckInfoVO.setAccountMobile(accountMobile);
//                 // cmCheckInfoVO.setCheckCode(checkCode);
//                 // cmCheckInfoVO.setGmtCreate(DateUtil.strToDate(gmtCreate, DateUtil.DATE_TIME_FORMATTER));
//                 // cmCheckInfoVO.setRiskLevel(Integer.valueOf(riskLevel));
//
//                 return ImportUtil.Result.success(cmCheckInfoVO);
//             }, inputStream);
//
//             if (result.isSuccess()) {
//                 // 保存到数据库
//                 List<Permission> permissionList = result.getData();
//                 for (Permission permission : permissionList) {
//                     // permissionService.save()
//                 }
//
//                 // 也可导入为另一张 Excel 表
//                 // for (CmCheckInfoVO checkInfoVO : cmCheckInfoVOList) {
//                 //     cmCheckInfoService.importAssign(checkInfoVO);
//                 // }
//                 // exportListByType(response, cmCheckInfoVOList, true);
//             }
//         } catch (Exception e) {
//             logger.error("导入异常", e);
//             // throw new BusException(StatusCode.BUSSINESS_ERROR, "IMPORT_FAIL", e);
//             throw new CustomException(SystemEnum.ERROR_FILE);
//         } finally {
//             if (null != inputStream) {
//                 try {
//                     inputStream.close();
//                 } catch (IOException e) {
//                     logger.error("导入异常,inputStream关闭失败", e);
//                 }
//             }
//         }
//     }
// }
