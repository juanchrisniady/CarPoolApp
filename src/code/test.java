package code;

import java.util.HashMap;
import java.util.Map;

public class test {
	public static void main(String[] args) {
		Map<String,Integer> m = new HashMap<String,Integer>();
		m.put("a", 1);
		m.put("b", 2);
		for(String k: m.keySet()) {
			System.out.println(k);
		}
	}
}
