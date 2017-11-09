package code;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;


@SuppressWarnings("deprecation")
public class ShortestDistanceFinder {
	//private String api_key = "AIzaSyB_6lwSEOCtBSQEc0CuCO18MwgqEyKWvD0";
	//private String api_key = "AIzaSyD0gODgOz343gRU6dfO1nhamgBPAK_LYA4";
	//private String api_key = "AIzaSyCB3dh6FZ9agAuW93_gMWdWNZhA6RGVVgE";
	private String api_key = "AIzaSyAvB2-H-1j1kFYbOre1Ksn24PkbCHXPWDs";
	/*
	 * return distance in meter
	 */
	public int getDistance(String origin, String destination) throws Exception{
		int dist = 0;
		HttpClient client = new DefaultHttpClient();
		
		HttpGet request1 = new HttpGet("https://maps.googleapis.com/maps/api/distancematrix/json?origins="
				 + origin + "&destinations=" + destination + "&key=" + api_key);
		HttpResponse response1 = client.execute(request1);
		int code = response1.getStatusLine().getStatusCode();

		try (BufferedReader br = new BufferedReader(new InputStreamReader((response1.getEntity().getContent())));) {
			// Read in all of the post results into a String.
			String output = "";
			Boolean keepGoing = true;
			while (keepGoing) {
				String currentLine = br.readLine();
				if (currentLine == null) {
					keepGoing = false;
				} else {
					output += currentLine;
				}
			}
			//System.out.println(output);
			JSONObject jsonResult = new JSONObject(output);
			JSONArray data = jsonResult.getJSONArray("rows");
			if(data != null) {
			    dist = jsonResult.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getInt("value");	
			    //System.out.println(dist);
			}
			
		}

		catch (Exception e) {
			System.out.println("Exception" + e);

		}
		return dist;
	}
}