package vicoMain;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import utils.BookInfoExcelListener;
import utils.ExcelContent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;


public class ExcelReadWrite {
    public static void main(String[] args) throws FileNotFoundException {

        String path="F:\\vico\\vicoTest\\VicoGuPiaoXuan\\src\\main\\resources\\loginTestCase.xls";
        File file = new File(path);
        BookInfoExcelListener datas = new BookInfoExcelListener(new ExcelContent());
        //实例化监听
        ExcelReader excelReader = EasyExcel.read(new FileInputStream(file), ExcelContent.class,datas).build();
        //读取excel第一页内容
        ReadSheet readSheet = EasyExcel.readSheet(0).headRowNumber(6).build();
        //读取Excel
        excelReader.read(readSheet);
        //这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
        excelReader.finish();
        //拿到excel所有数据
        LinkedList<ExcelContent> datass = datas.getDatas();
        for(ExcelContent dates:datass){
            if (dates.getIndexx()!=null){
                String username = dates.getUserName();
                String passwd = dates.getPassword();
                if (username!=null && passwd!=null){
                    //给excel写数据
                    dates.setIsSuccess("false");
                }
                System.out.println(dates.getTestPoint());
            }
        }
        String paths="F:\\vico\\vicoTest\\VicoGuPiaoXuan\\src\\main\\resources\\loginTestCaseA.xls";
        File filess = new File(paths);
        EasyExcel.write(filess,ExcelContent.class).relativeHeadRowIndex(0).sheet(0).doWrite(datass);

    }
}
