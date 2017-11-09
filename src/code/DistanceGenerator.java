package code;

import code.ShortestDistanceFinder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class DistanceGenerator {
	
//	private final String dmapfile = "MAP DATA/addr_and_distance.txt";
//	private final String databasefile = "PEOPLE DATABASE/database.csv";
	
	private final String dmapfile = "addr_and_distance.txt";
	private final String databasefile = "database.csv";
	
	private static String destination = "12345+8TH+AVE+NE+SEATTLE";
	private Map<String, Integer> dmap;
	private Set<String> people;
	private ShortestDistanceFinder sd;
	public void getAllDistance(Map<String,Integer> dm) throws FileNotFoundException {
		Scanner sdm = new Scanner(new FileReader(dmapfile));
		while(sdm.hasNextLine()) {
			String curr = sdm.nextLine();
			String[] arr = curr.split(";");
			if(arr.length != 2){
				System.out.println("SOMETHING IS WRONG");
			}
			dm.put(arr[0], Integer.parseInt(arr[1]));
		}
		sdm.close();
		
	}
	public void generateDistance(String input) throws Exception {
		StringBuilder sbdm = new StringBuilder();
		dmap = new HashMap<String, Integer>();
		people = new HashSet<String>();
		sd = new ShortestDistanceFinder();
		File outputdm = new File(dmapfile);
		try {
			outputdm.createNewFile();
			Scanner sdm = new Scanner(new FileReader(dmapfile));
			while(sdm.hasNextLine()) {
				String curr = sdm.nextLine();
				String[] arr = curr.split(";");
				if(arr.length != 2){
					System.out.println("SOMETHING IS WRONG");
				}
				dmap.put(arr[0], Integer.parseInt(arr[1]));
			}
			sdm.close();
			
			Scanner scanner = new Scanner(new File(databasefile));
	        scanner.useDelimiter(",");
	        scanner.nextLine();
	        while(scanner.hasNextLine()) {
	        	String temp = scanner.nextLine();
	        	if(temp.equals(",,,,")|| temp.equals("")) {
	        		break;
	        	}
	        	String data[] = temp.split(",");
	        	String addr = data[1].replace(' ', '+');        	
	        	people.add(data[0] + "@" + addr);
	        }
	        scanner.close();
	        people.add("destination@"+destination);
	        
	        for(String p1 : people) {
	        	for(String p2 : people) {
	        		String dkey = p1 + "," + p2;
	        		if( !dmap.containsKey(dkey) && !p1.equals(p2)) {
	        			String[] p1arr = p1.split("@");
	        			String[] p2arr = p2.split("@");
	        			String origin = p1arr[1];
	        			String destination = p2arr[1];
	        			int dist = sd.getDistance(origin, destination);
	        			System.out.println(dist);
	        			sbdm.append(dkey + ";"+dist+"\n");	        			
	        		}
	        	}
	        }
	        Files.write(Paths.get(dmapfile), sbdm.toString().getBytes(), StandardOpenOption.APPEND);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
