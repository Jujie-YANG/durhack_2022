package analyse;

import static java.util.List.of;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.OptionalDouble;
import java.util.Scanner;
import java.util.TreeMap;

public class ParticipatesAndPerformance
{
	public static void main(String[] args) throws FileNotFoundException
	{
		File list = new File("analyse/data/ID-medals.csv");
		Scanner s = new Scanner(list);
		
		Map<Integer, List<Integer>> freqAndPerformances = new TreeMap<>(Integer::compareTo);
		Map<String, Integer> priorities = Map.of(
				"Gold" , 4 ,
				"Silver" , 2 ,
				"Bronze" , 1 ,
				"NA" , 0
		);
		
		int freq = 0;
		int score = 0;
		String lastId = "1";
		
		while (s.hasNextLine())
		{
			while (true)
			{
				if (!s.hasNextLine())
				{
					if (freqAndPerformances.containsKey(freq)) freqAndPerformances.get(freq).add(score);
					else freqAndPerformances.put(freq , new LinkedList<>(of(score)));
					break;
				}
				
				String[] arr = s.nextLine().split(",");
				String id = arr[0];
				String stat = arr[1];
				
				if (!id.equals(lastId))
				{
					if (freqAndPerformances.containsKey(freq))
					{
						freqAndPerformances.get(freq).add(score);
						// freqAndPerformances.get(freq).sort(Integer::compareTo);
					}
					else freqAndPerformances.put(freq , new LinkedList<>(of(score)));
					
					lastId = id;
					freq = 1;
					score = priorities.get(stat);
					break;
				}
				else
				{
					score += priorities.get(stat);
					lastId = id;
					freq++;
				}
			}
		}
		
		System.out.println(freqAndPerformances);
		
		// get average for all sets
		Map<Integer , Double> freqAndAvgPerformance = new TreeMap<>(Integer::compareTo);
		
		for (Entry<Integer , List<Integer>> e : freqAndPerformances.entrySet())
		{
			OptionalDouble avg = e.getValue().stream().mapToDouble((a) -> a).average();
			freqAndAvgPerformance.put(e.getKey() , avg.getAsDouble());
		}
		
		System.out.println(freqAndAvgPerformance);
	}
}