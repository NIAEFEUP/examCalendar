package examcalendar.parser;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

/**
 * Created by Duarte on 01/07/2016.
 */
public abstract class ExcelParser {

    File file;
    Feedback feedback;

    public ExcelParser(String file) {
        this.file = new File(file);
        this.feedback = new Feedback();
    }

    public boolean generate(){
        try {
            Workbook wb;
            if (FilenameUtils.getExtension(file.getName()).equals("xlsx"))
                wb = new XSSFWorkbook(new FileInputStream(file));
            else if (FilenameUtils.getExtension(file.getName()).equals("xls")) {
                POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
                wb = new HSSFWorkbook(fs);
            } else {
                return false;
            }
            Sheet sheet = wb.getSheetAt(0);

            return parseSheet(sheet);
        }catch (Exception e){
            getFeedback().addError(e.getMessage(),0+"",0+"");
            e.printStackTrace();
            return false;
        }
    }

    protected boolean parseSheet(Sheet sheet) {
        Row row = null;

        Iterator<Row> rowIterator = sheet.rowIterator();

        while (rowIterator.hasNext()){
            row = rowIterator.next();
            parseRow(row);
        }


        if(hasGenerated()) {
            feedback.setGenerated(true);
        }else{
            feedback.addError("Erro a ler a lista de professores", row.getRowNum()+"",0+"");
            return false;
        }
        return true;
    }


    protected abstract boolean hasGenerated();

    protected abstract void parseRow(Row row);

    protected boolean isBlankCell(String cell){
        return cell.isEmpty() || cell.matches("^\\s+$");
    }

    public File getFile() {
        return file;
    }

    public Feedback getFeedback() {
        return feedback;
    }
}
