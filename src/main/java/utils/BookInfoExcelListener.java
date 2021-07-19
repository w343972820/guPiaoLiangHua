package utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.SneakyThrows;

import java.util.LinkedList;

public class BookInfoExcelListener extends AnalysisEventListener<ExcelContent> {
    private ExcelContent excelModel;

    /**
     * 自定义用于暂时存储data
     * 可以通过实例获取该值
     */
    private LinkedList<ExcelContent> datas = new LinkedList<ExcelContent>();

    public BookInfoExcelListener(ExcelContent excelModel) {
        this.excelModel = excelModel;
    }

    public BookInfoExcelListener() {

    }
    public LinkedList<ExcelContent> getDatas(){
        return datas;
    }
    @Override
    public void invoke(ExcelContent excelContent, AnalysisContext analysisContext) {

        System.out.println("**************"+excelContent);
        datas.add(excelContent);
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("所有数据解析完成");

    }
}
