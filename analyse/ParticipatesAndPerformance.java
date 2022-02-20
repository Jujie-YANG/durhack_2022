package analyse;

import static java.util.List.of;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Scanner;
import java.util.TreeMap;

public class ParticipatesAndPerformance
{
	public static void main(String[] args) throws IOException
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
		
		freqAndPerformances.remove(0);
		
		Map<Integer, List<BigDecimal>> freqAndPerfDens = new TreeMap<>(Integer::compareTo);
		for (var e : freqAndPerformances.entrySet())
		{
			List<BigDecimal> dens = new LinkedList<>();
			e.getValue().forEach(
					(v) -> dens.add(BigDecimal.valueOf(v).divide(BigDecimal.valueOf(e.getKey()) , 4 , RoundingMode.UP))
			);
			freqAndPerfDens.put(e.getKey() , dens);
		}
		
		System.out.println(freqAndPerfDens);
		
		// write out as json
		JSONObject out = new JSONObject(freqAndPerfDens);
		Writer f = new FileWriter("analyse/res/FreqAndPerfDens.json");
		f.write(out.toString());
		f.close();
		
		// get average for all sets
		Map<Integer, BigDecimal> freqAndAvgPerfDens = new TreeMap<>(Integer::compareTo);
		
		for (Entry<Integer, List<BigDecimal>> e : freqAndPerfDens.entrySet())
		{
			BigDecimal[] totalWithCount
					= e.getValue().stream()
					.filter(Objects::nonNull)
					.map(bd -> new BigDecimal[] {bd , BigDecimal.ONE})
					.reduce((a , b) -> new BigDecimal[] {a[0].add(b[0]) , a[1].add(BigDecimal.ONE)})
					.get();
			BigDecimal mean = totalWithCount[0].divide(totalWithCount[1] , RoundingMode.HALF_UP);
			
			freqAndAvgPerfDens.put(e.getKey() , mean);
		}
		
		// System.out.println(freqAndAvgPerfDens);
		// write out as json
		JSONObject out2 = new JSONObject();
		try
		{
			Field changeMap = out2.getClass().getDeclaredField("map");
			changeMap.setAccessible(true);
			changeMap.set(out2 , new TreeMap<>(Integer::compareTo));
			changeMap.setAccessible(false);
		} catch (IllegalAccessException | NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		out2 = new JSONObject(freqAndAvgPerfDens);
		Writer f1 = new FileWriter("analyse/res/FreqAndAvgPerfDens.json");
		f1.write(out2.toString());
		f1.close();
	}
}
		/*
		// get average for all sets
		Map<Integer, Double> freqAndAvgPerformance = new TreeMap<>(Integer::compareTo);
		
		for (Entry<Integer, List<Integer>> e : freqAndPerformances.entrySet())
		{
			OptionalDouble avg = e.getValue().stream().mapToDouble((a) -> a).average();
			freqAndAvgPerformance.put(e.getKey() , avg.getAsDouble());
		}
		
		freqAndAvgPerformance.remove(0);
		
		// write to csv
		String eol = System.getProperty("line.separator");
		
		try (Writer writer = new FileWriter("analyse/res/FrequencyOfParticipationAndPerformance.csv"))
		{
			for (Entry<Integer, Double> entry : freqAndAvgPerformance.entrySet())
			{
				writer.append(entry.getKey().toString())
						.append(',')
						.append(entry.getValue().toString())
						.append(eol);
			}
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}*/