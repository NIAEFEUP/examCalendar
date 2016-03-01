package examination.domain;

/**
 * @author Duarte
 * @version 1.0
 * @created 18-fev-2016 16:42:19
 */
public class Room {

	private int capacity;
	private String codRoom;
	private boolean pc;

	public Room(String codRoom, int capacity, boolean pc){
		this.capacity = capacity;
		this.codRoom = codRoom;
		this.setPc(pc);
	}

	public void finalize() throws Throwable {

	}

	public String getCodRoom() {
		return codRoom;
	}

	public void setCodRoom(String codRoom) {
		this.codRoom = codRoom;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isPc() {
		return pc;
	}

	public void setPc(boolean pc) {
		this.pc = pc;
	}

	@Override
	public String toString() {
		return "Room{" +
				"codRoom='" + codRoom + '\'' +
				'}';
	}
}