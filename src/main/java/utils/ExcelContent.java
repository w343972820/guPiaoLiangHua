package utils;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExcelContent {
    @ExcelProperty(value = "序号",index = 0)
    private String indexx;
    @ExcelProperty(value = "测试点",index = 1)
    private String testPoint;
    @ExcelProperty(value = "账号",index = 2)
    private String userName;
    @ExcelProperty(value = "密码",index = 3)
    private String password;
    @ExcelProperty(value = "预期结果",index = 4)
    private String wantResult;
    @ExcelProperty(value = "实际结果",index = 5)
    private String testResult;
    @ExcelProperty(value = "是否通过",index = 6)
    private String isSuccess;
}
