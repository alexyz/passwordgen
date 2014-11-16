import java.io.*;
import java.net.*;
import java.util.*;

public class CharWeights {
	
	public static void main (String[] args) throws Exception {
		
		URL url = new URL("http://en.wikipedia.org/w/api.php?format=json&action=query&titles=United_Kingdom&prop=revisions&rvprop=content");
		URLConnection con = url.openConnection();
		Map<String,Integer> m = new TreeMap<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
			int ci;
			while ((ci = br.read()) != -1) {
				char c = Character.toLowerCase((char) ci);
				if (c >= 'a' && c <= 'z') {
					String s = String.valueOf(c);
					Integer i = m.get(s);
					if (i == null) {
						i = 1;
					} else {
						i++;
					}
					m.put(s, i);
				}
			}
		}
		for (String s : m.keySet()) {
			System.out.println(s + " " + m.get(s));
		}
		int n = 0;
		for (String s : m.keySet()) {
			System.out.println("p[" + n++ + "]=" + m.get(s) + ";");
		}
	}
	
}
