package application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ZliczanieSlow {
	public static TreeMap<String, Integer> zliczSlowa (String tekstDoAnalizy) {
		Scanner odczyt = new Scanner(tekstDoAnalizy);
		ArrayList<String> slowa = new ArrayList<>();
		while(odczyt.hasNext()) {
			slowa.add(odczyt.next().toLowerCase());
		}
		odczyt.close();
		TreeMap<String, Integer> czestoscSlow = new TreeMap<>();
		for (String st:slowa) {
			if (czestoscSlow.containsKey(st)) {
				czestoscSlow.put(st, czestoscSlow.get(st)+1);
			} else {
				czestoscSlow.put(st, 1);
			}
		}
		TreeMap<String, Integer> pierwszeDziesiec =
				czestoscSlow.entrySet().stream()
			       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
			       .limit(10)
			       .collect(Collectors.toMap(
			          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, TreeMap::new));
		return pierwszeDziesiec;
	}

}