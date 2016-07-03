package examcalendar.parser;

import examcalendar.optimizer.domain.Room;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Hashtable;
import java.util.Set;

import static examcalendar.parser.ExcelRegex.ROOM;

/**
 * Created by Duarte on 01/07/2016.
 */
public class RoomsParser extends ExcelParser {
    Hashtable<String, Room> rooms = new Hashtable<String, Room>();

    public RoomsParser(String file) {
        super(file);
    }

    @Override
    protected boolean parseSheet(Sheet sheet) {
        Row row = null;
        Cell cell = null;
        String cellContent;

        String roomNumber;
        int roomCapacity;
        boolean isPc;
        Room currRoom;

        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if(row != null) {
                cell = row.getCell(0);
                cellContent = cell.getStringCellValue();
                if(isBlankCell(cellContent) || !cellContent.matches(ROOM))
                    continue;

                roomNumber = cellContent.trim();

                try{
                    roomCapacity = (int) row.getCell(1).getNumericCellValue();
                }catch (Exception e){
                    feedback.addError("Célula não contém um número válido", row.getRowNum()+"", 2+"");
                    return false;
                }

                isPc = row.getCell(2).getStringCellValue().equalsIgnoreCase("sim");
                if(rooms.get(roomNumber)!= null){
                    feedback.addWarning("Sala já existe", row.getRowNum()+"", 0+"");
                    continue;
                }

                currRoom = new Room(roomNumber,roomCapacity,isPc);
                rooms.put(roomNumber,currRoom);
            }
        }

        feedback.setResult(true);
        return true;
    }

    public Hashtable<String, Room> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        Set<String> keys = rooms.keySet();

        for (String key:keys){
            Room room = rooms.get(key);
            str.append(room.getCodRoom()+" "+room.getCapacity()+" "+room.isPc()+"\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        String file = "src/test/java/rooms.xlsx";
        RoomsParser parser = new RoomsParser(file);
        parser.generate();
        System.out.println(parser.toString());
        System.out.println(parser.getFeedback().toString());

    }
}
