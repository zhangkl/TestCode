package testFile;

import org.apache.poi.hssf.usermodel.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLResult;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import static oracle.net.aso.C09.c;
import static oracle.net.aso.C09.e;
import static oracle.net.aso.C09.f;
import static oracle.net.aso.C11.s;
import static oracle.net.aso.C11.w;


/**
 * Created by zhangkl on 2017/6/27.
 */
public class ReadExcel {

    String[] funcIds = new String[]{"330300","331100"};

    public static void main(String[] args) {
        ReadExcel readExcel = new ReadExcel();
        String filePaht = "D:\\tpy_data\\pangu\\柜台\\恒生统一接入平台_周边接口规范(UF2.0_20170503).xls";
        readExcel.readFile(filePaht,"功能列表");

    }

    public String readFile(String filePath,String sheetName) {
        List<Map> list = new ArrayList();
        String str = "";
        InputStream fis = null;
        try {
            fis = new FileInputStream(new File(filePath));
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheet(sheetName);
            for (String funcid : funcIds) {
                String funcName = "";
                List params = new ArrayList();
                //循环功能号列表，查找对应功能号跳转链接
                for (int i = 1; i < sheet.getLastRowNum(); i++) {
                    HSSFRow row = sheet.getRow(i);
                    HSSFCell cell = row.getCell(1);
                    String value = getValue(cell);
                    if (funcid.equals(value)) {
                        String linkInfo = getLinkInfo(cell);
                        String sheetName_imp = linkInfo.split("!C")[0].replace("'", "");
                        int rowNum_imp = Integer.parseInt(linkInfo.split("!C")[1]);
                        HSSFSheet sheet_imp = workbook.getSheet(sheetName_imp);
                        //从跳转的链接开始循环，读取当前功能接口的行
                        for (int j = rowNum_imp - 1; j < sheet_imp.getLastRowNum(); j++) {
                            HSSFRow row_imp = sheet_imp.getRow(j);
                            if (isContentRow(row_imp)) {
                                //循环当前功能接口的表格当前行的列
                                for (int k = 1; k < row_imp.getLastCellNum(); k++) {
                                    HSSFCell cell_imp = row_imp.getCell(k);
                                    String value_imp = getValue(cell_imp);
                                    if (value_imp != null && !value_imp.trim().equals("")) {
                                        if ("功能名称".equals(value_imp)) {
                                            funcName = getValue(row_imp.getCell(k + 1));
                                            break;
                                        } else if ("输入参数".equals(value_imp)) {
                                            for (int l = j + 1; l < sheet_imp.getLastRowNum(); l++) {
                                                HSSFRow paramRow = sheet_imp.getRow(l);
                                                if (isContentRow(paramRow)) {
                                                    HSSFCell paramCell = paramRow.getCell(2);
                                                    String paramValue = getValue(paramCell);
                                                    if ("参数名".equals(paramValue)) {
                                                        break;
                                                    } else {
                                                        params.add(paramValue);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
                Map map = new HashMap();
                map.put("funcid", funcid);
                map.put("funcName", funcName);
                map.put("params", params);
                list.add(map);
            }
            getXML(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    public void getXML(List<Map> list) throws IOException {
        // 创建文档并设置文档的根元素节点
        Element root = DocumentHelper.createElement("TEST_PACK");
        Document doucment = DocumentHelper.createDocument(root);
        //根节点
        root.addAttribute("node", " ");
        //子节点
        Element element1 = root.addElement("Test");
        for (Map map : list) {
            String funcid = (String) map.get("funcid");
            String funcName = (String) map.get("funcName");
            List params = (List) map.get("params");
            Element element2 = element1.addElement("sub");
            element2.addAttribute("id", funcid);
            element2.addAttribute("block", "1");
            element2.addAttribute("livetime", "5000");
            element2.addAttribute("pri", "8");
            element2.addAttribute("pack_ver", "32");
            element2.addAttribute("note", funcName);
            Element element3 = element2.addElement("route");
            element3.addAttribute("system", "");
            element3.addAttribute("branch", "");
            element3.addAttribute("esb_name", "");
            element3.addAttribute("esb_no", "0");
            element3.addAttribute("neighbor", "");
            element3.addAttribute("plugin", "");
            Element element4 = element2.addElement("inparams");
            element4.addAttribute("note", funcName);
            for (int i = 0; i < params.size(); i++) {
                String input = (String) params.get(i);
                Element element5 = element4.addElement("in");
                element5.addAttribute("name", input);
                if ("op_entrust_way".equals(input)) {
                    element5.addAttribute("value", "G");
                } else if ("client_id".equals(input) || "fund_account".equals(input)) {
                    element5.addAttribute("value", "680000520");
                } else if ("password".equals(input)) {
                    element5.addAttribute("value", "111111");
                } else if ("password_type".equals(input)) {
                    element5.addAttribute("value", "2");
                } else {
                    element5.addAttribute("value", "");
                }
            }
        }

        StringWriter stringWriter = new StringWriter();
        OutputFormat format = new OutputFormat("     ", true);
        FileWriter fileWriter = new FileWriter("test.xml");
        XMLWriter writer = new XMLWriter(fileWriter, format);
        writer.write(doucment);

        XMLWriter writer2 = new XMLWriter(stringWriter, format);
        writer2.write(doucment);
        System.out.println(stringWriter.getBuffer().toString());
        writer.flush();

    }

    public String getValue(HSSFCell cell) {
        String cellValue = "";
        if (null != cell) {
            // 以下是判断数据的类型，调对应的方法
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    // cellValue = cell.getNumericCellValue() + "";
                    DecimalFormat df = new DecimalFormat("0");
                    cellValue = df.format(cell.getNumericCellValue()); //长数字取出来变成了科学计数法形式，使用DecimalFormat格式化
                    break;
                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    cellValue = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                    cellValue = cell.getBooleanCellValue() + "";
                    break;
                case HSSFCell.CELL_TYPE_FORMULA: // 公式
                    cellValue = cell.getCellFormula() + "";
                    break;
                case HSSFCell.CELL_TYPE_BLANK: // 空值
                    cellValue = "";
                    break;
                case HSSFCell.CELL_TYPE_ERROR: // 故障
                    cellValue = "非法字符";
                    break;
                default:
                    cellValue = "未知类型";
                    break;
            }
        }
        return cellValue;
    }

    public String getLinkInfo(HSSFCell cell) {
        HSSFHyperlink hyperlink = cell.getHyperlink();
        return hyperlink.getAddress();
    }

    /**
     * 连续五格没有值，认为当前接口编号内容结束
     * @param row
     * @return
     */
    public boolean isContentRow(HSSFRow row) {

        if (row != null) {
            for (int i = 0; i < 4; i++) {
                if (row.getCell(i) != null) {
                    String value = getValue(row.getCell(i));
                    if (value != null && !"".equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
