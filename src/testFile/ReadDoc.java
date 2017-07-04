package testFile;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.xwpf.usermodel.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


/**
 * Created by zhangkl on 2017/6/23.
 */
public class ReadDoc {

    public static void main(String[] args) throws IOException {
        ReadDoc readDoc = new ReadDoc();
        readDoc.readDoc2Json("C:\\Users\\zhangkl\\Desktop\\太平洋证券基金理财接口文档.docx");
    }


    public void getJson(String funcID, String funcName, String funcID_hs, List params, List result_filter) {

        JSONObject json_data = new JSONObject();
        JSONArray reslutArray = new JSONArray();
        JSONArray paramsArray = new JSONArray();
        for (int i = 0; i < result_filter.size(); i++) {
            JSONObject json_result = new JSONObject();
            json_result.put("dest", result_filter.get(i).toString().toUpperCase());
            json_result.put("value", result_filter.get(i).toString().toLowerCase());
            reslutArray.add(json_result);
        }

        for (int i = 0; i < params.size(); i++) {
            JSONObject json_params = new JSONObject();
            String destStr = params.get(i).toString().toLowerCase();
            String valueStr = params.get(i).toString().toUpperCase();
            if ("sec_id".equals(destStr)) {
                continue;
            } else if ("funcid".equals(destStr)) {
                valueStr = "@@DEFAULT(" + funcID_hs + ")";
                json_params.put("dest", destStr);
                json_params.put("value", valueStr);
            } else if ("op_branch_no".equals(destStr)) {
                json_params.put("dest", "op_branch_no");
                json_params.put("value", "@@DEFAULT(1000)");
            } else if ("op_entrust_way".equals(destStr)) {
                json_params.put("dest", "op_entrust_way");
                json_params.put("value", "@@DEFAULT(G)");
            } else if ("password".equals(destStr)) {
                json_params.put("dest", "password");
                json_params.put("value", "USER_PWD");
            } else {
                json_params.put("dest", destStr);
                json_params.put("value", valueStr);
            }
            paramsArray.add(json_params);
        }
        json_data.put("name", funcName + "(" + funcID + ")");
        json_data.put("result_filter", reslutArray);
        json_data.put("params", paramsArray);
        json_data.put("function", funcID_hs);

        System.out.println("\"" + funcID + "\"" + ":" + json_data + ",");

    }

    public void readDoc2Json(String filePath) throws IOException {
        FileInputStream in = new FileInputStream(filePath);//载入文档
        //如果是office2007  docx格式
        XWPFDocument xwpf = new XWPFDocument(in);//得到word文档的信息
        /*List<XWPFParagraph> listParagraphs = xwpf.getParagraphs();//得到段落信息
        XWPFParagraph xwpfParagraph = listParagraphs.get(0);
        XWPFDocument first_doc = xwpfParagraph.getDocument();*/
        Iterator<XWPFTable> it = xwpf.getTablesIterator();//得到word中的表格
        while (it.hasNext()) {

            List params = new ArrayList();
            List result_filter = new ArrayList();
            String funcID = "";
            String funcID_hs = "332633";//TODO 恒生接口号
            String funcName = "";

            params.add("funcid");


            XWPFTable table = it.next();
            List<XWPFTableRow> rows = table.getRows();
            //读取每一行数据
            for (int i = 0; i < rows.size(); i++) {
                XWPFTableRow row = rows.get(i);
                //读取每一列数据
                List<XWPFTableCell> cells = row.getTableCells();
                XWPFTableCell cell = cells.get(0);
                String value = cell.getText();
                //输出当前的单元格的数据
                if ("接口编码".equals(value)) {
                    funcID = cells.get(1).getText();
                } else if ("接口名称".equals(value)) {
                    funcName = cells.get(1).getText();
                } else if ("输入参数".equals(value)) {
                    for (int j = i + 4; j < rows.size(); j++) {
                        XWPFTableRow row_p = rows.get(j);
                        if (!"".equals(getKey(row_p))) {
                            if ("FLAG".equals(getKey(row_p))) {
                                params.add("op_branch_no");
                                params.add("op_entrust_way");
                                params.add("op_station");
                                params.add("branch_no");
                                params.add("client_id");
                                params.add("fund_account");
                                params.add("password");
                                params.add("password_type");
                                params.add("user_token");
                            } else {
                                params.add(getKey(row_p));
                            }
                        } else {
                            break;
                        }
                    }
                } else if ("输出参数".equals(value)) {
                    for (int j = i + 4; j < rows.size(); j++) {
                        XWPFTableRow row_p = rows.get(j);
                        if (!"".equals(getKey(row_p))) {
                            result_filter.add(getKey(row_p));
                        } else {
                            break;
                        }
                    }
                }
            }
            getJson(funcID, funcName, funcID_hs, params, result_filter);
        }
    }

    private String getKey(XWPFTableRow row) {
        List<XWPFTableCell> cells = row.getTableCells();
        if (cells.size()>2){
            if ("".equals(cells.get(0).getText())) {
                if ("".equals(cells.get(1).getText())) {
                    return cells.get(2).getText();
                }
            }
        }
        return "";
    }
}
