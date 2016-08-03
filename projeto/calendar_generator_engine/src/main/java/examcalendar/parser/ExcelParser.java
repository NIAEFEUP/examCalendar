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
            Row row;
            Cell cell;
            String cellContent;

            return parseSheet(sheet);
        }catch (Exception e){
            getFeedback().addError(e.getMessage(),0+"",0+"");
            return false;
        }
    }

    protected abstract boolean parseSheet(Sheet sheet);

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
