package code;

public class Person {
	private String name;
	private String addr;
	private int capacity;
	public Person(String name, String addr, int cap) {
		this.name = name;
		this.addr = addr;
		this.capacity = cap;
	}
	public String getName() {
		return name;
	}
	public String getAddr() {
		return addr;
	}

	public int getCarCapacity() {
		return capacity;
	}
	public String toString() {
		return name + "," + addr + "," + capacity;
	}

}
