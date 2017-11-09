package code;
import code.ShortestDistanceFinder;
import code.Person;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
public class App{
//	private static final String databasefile = "PEOPLE DATABASE/database.csv";
	private static final String databasefile = "database.csv";
	
	private static String destination = "12345+8TH+AVE+NE+SEATTLE";
	private static ShortestDistanceFinder sd;
	private static Map<String, Integer> savedmap;
	private static Set<String> passenger;
	private static List<String> driver;
	private static DistanceGenerator dg;
	
	
	public static void main(String[] args) throws Exception {
		
//		File file = new File(dmapfile);
//		file.delete();
		dg = new DistanceGenerator();
		driver = new ArrayList<String>();
		passenger = new HashSet<String>();
		sd = new ShortestDistanceFinder();
		savedmap = new HashMap<String, Integer>();
		Scanner scanner = new Scanner(new File(databasefile));
		dg.generateDistance(databasefile);	
		dg.getAllDistance(savedmap);
        scanner.useDelimiter(",");
        scanner.nextLine();
        int dcount = 0;
        int pcount = 0;
        while(scanner.hasNextLine()) {
        	String temp = scanner.nextLine();
        	if(temp.equals(",,,,")||temp.equals("")) {
        		break;
        	}
        	String data[] = temp.split(",");
        	if(data[4].equalsIgnoreCase("yes")) { // going
        		if(data[3].equalsIgnoreCase("yes")) { // driver
        			driver.add(data[0] + "@" + data[1].replace(' ', '+'));
        			dcount++;
        		} else { // passenger
        			passenger.add(data[0] + "@" + data[1].replace(' ', '+'));
        			pcount++;
        		}
        	}
        }

        scanner.close();
        int cap = 5;
        List<List<String>> comb = new ArrayList<>();
        List<String> nopickup = new ArrayList<String>();
        String destkey = "destination@"+destination;
        for(int i = 0; i < dcount; i++) {
        	int currmin = Integer.MAX_VALUE;
        	String minp = "???@???";
        	for(String p : passenger) {
        		int dist = savedmap.get(destkey + "," + p);
        		if(dist < currmin) {
        			currmin = dist;
        			minp = p;
        		}
        	}
        	comb.add(new ArrayList<String>());
        	comb.get(i).add(minp);
 //       	curr.add(minp);
        	passenger.remove(minp);   	
        }
        for(int i = 0; i < pcount-dcount; i++) {
        	int currmin = Integer.MAX_VALUE;
        	String minp = "";
        	int bucketidx = 0;
        	
        	for(int j = 0; j< dcount; j++) {
        		for(String p: passenger) {
        			String currkey = p + "," + comb.get(j).get(comb.get(j).size() -1);
        			if(savedmap.containsKey(currkey)) {
        				int dist = savedmap.get(currkey);
        				if(dist < currmin && comb.get(j).size() < cap - 1) {
        					minp = p;
        					bucketidx = j;
        					currmin = dist;
        				}
        			}else {
        				System.out.println(p + "," + comb.get(j).get(comb.get(j).size() -1));
        			}
        		}
        	}
        	if(minp.equals("")) {
        		//do nothing, car full
        	}else {
        		comb.get(bucketidx).add(minp);
        		passenger.remove(minp);
        	}
        }
        if(!passenger.isEmpty()) {
        	for(String p : passenger) {
        		nopickup.add(p);
        	}
        }
        for(int i = 0; i < dcount ; i++) {
        	int currmax = savedmap.get(driver.get(i)+"," + destkey);
        	int maxidx = i;
        	for(int j = i; j < dcount; j++) {
        		int dist = savedmap.get(driver.get(j)+"," +destkey);
        		if(dist > currmax ) {
        			maxidx = j;
        			currmax = j;
        		}
        	}
        	String a = driver.get(i);
        	driver.set(i, driver.get(maxidx));
        	driver.set(maxidx, a);       	
        }
        boolean[] flag = new boolean[dcount];
        for(int i = 0; i < dcount; i++) {
        	int minidx = 0;
        	int currmin = Integer.MAX_VALUE;
        	for(int j = 0; j < dcount; j++) {
        		String lastelem = comb.get(j).get( comb.get(j).size() - 1 );
        		String currkey = lastelem + "," + driver.get(i);
        		int dist = savedmap.get(currkey);
        		if(dist < currmin && !flag[j]) {
        			minidx = j;
        			currmin = dist;
        		}
        	}
        	comb.get(minidx).add(driver.get(i));
        	flag[minidx] = true;
        }
        
        StringBuilder sb = new StringBuilder();
        for(List<String> c : comb) {
        	Collections.reverse(c);
        	for(int i = 0; i < c.size(); i++) {
        		String[] curr = c.get(i).split("@");
        		String st = curr[0];
        		String address = curr[1].replace('+',' ');
        		if(i == 0) {
        			sb.append("** " + st + " **\n");
        		} else if(i == c.size()-1) {
        			sb.append("   -" + st + " : " +  address + "\n___________________\n");
        		}else {
        			sb.append("   -" +  st + " : " + address + "\n");
        		}
        	}
        }
        
        File outputdm = new File("result.txt");
        outputdm.createNewFile();
        System.out.println(sb.toString());
        Files.write(Paths.get("result.txt"), sb.toString().getBytes(), StandardOpenOption.WRITE);
        
        
        
        
        
        
        
        
        
        
        
//////////// Algo 2, bruteforce O(n!)///////////////
//        for(Person p1 : driver) {
//        	String addr1 = p1.getAddr();
//        	dmap.put(p1.getName() + "@" + addr1, 0);
//        }
//        updateMap(dmap,savedmap, passenger);//2
//        updateMap(dmap,savedmap, passenger);//3
//        updateMap(dmap,savedmap, passenger);//4
//        updateMap(dmap,savedmap, passenger);//5
//        
//        Map<String, String> res = new HashMap<>();
//        for(Person d : driver) {
//        	String dkey = d.getName() + "@" + d.getAddr();
//        	for(String st: dmap.keySet()) {
//        		String[] starr = st.split(",");
//        		if(starr[0].equals(dkey) && starr.length <= d.getCarCapacity()) {
//        			if(!res.containsKey(d.getName())) {
//        				res.put(d.getName(), st);
//        			} else {
//        				String currcombination = res.get(d.getName());
//        				String[] currarr = currcombination.split(",");
//        				if(currarr.length < starr.length) {
//        					res.put(d.getName(), st);
//        				} else if(currarr.length == starr.length) {
//        					if(dmap.get(currcombination) > dmap.get(st) ) {
//        						res.put(d.getName(), st);
//        					}
//        				} else {
//        					//do nothing, still the best;
//        				}
//        			}
//        		}
//        	}
//        	String bestcomb = res.get(d.getName());
//        	String[] bestarr;
//        	if(bestcomb == null) {
//        		bestarr = new String[]{d.getName()};
//        	} else {
//        		bestarr = bestcomb.split(",");
//        	}
//	        for(String b: bestarr) {
//	        	Iterator<String> iter1 = dmap.keySet().iterator();
//	        	while (iter1.hasNext()) {
//	        		String k1 = iter1.next();
//	        		if(k1.indexOf(b) >= 0) {
//	        			iter1.remove();
//	        		}
//	        	}
//	        }
//        }
//        
//        StringBuilder sb = new StringBuilder();
//        for(String kd: res.keySet()) { 
//        	sb.append(res.get(kd) + "\n");
//        	System.out.println(res.get(kd));
//        	
//        }
//        File outputdm = new File("result.txt");
//        outputdm.createNewFile();
//        Files.write(Paths.get("result.txt"), sb.toString().getBytes(), StandardOpenOption.WRITE);
        
	}
	
	
	public static void updateMap(Map<String, Integer> dm,Map<String, Integer> sm, List<Person> p) throws IOException {
		HashMap<String, Integer> newdm = new HashMap<String, Integer>();
//		StringBuilder sbdm = new StringBuilder();
		for(String k : dm.keySet()) {
        	for(Person p3: p) {
        		String nextAddr = p3.getName() + "@" + p3.getAddr();
        		if(!dm.containsKey(k+"," + nextAddr) && k.indexOf(nextAddr) < 0) {
	        		String[] arr = k.split(",");
	        		String lastAddr = arr[arr.length-1];	        		
	        		String key = lastAddr + "," + nextAddr;
	        		int nextdist = 0;
	        		if(sm.containsKey(lastAddr + "," + nextAddr)) {
	        			nextdist = sm.get(key);
	        			newdm.put(k+"," + nextAddr, dm.get(k) + nextdist);
	        				
	        		} else {
	        			try {
	        				System.out.println("missing key");
	        				String[] lastAddrArr = lastAddr.split("@");
							nextdist = sd.getDistance(lastAddrArr[lastAddrArr.length - 1], p3.getAddr());
							System.out.println(nextdist);
							newdm.put(lastAddr + "," + nextAddr, nextdist);
							newdm.put(nextAddr + "," + lastAddr, nextdist);
							newdm.put(k+"," + nextAddr, dm.get(k) + nextdist);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	       			}
        		}
        	}
        }
		for(String k1 : newdm.keySet()) {
			dm.put(k1, newdm.get(k1));
//			sbdm.append(k1 + ";" + newdm.get(k1) + "\n");
		}
//		Files.write(Paths.get(dmapfile), sbdm.toString().getBytes(), StandardOpenOption.APPEND);

	}
}
