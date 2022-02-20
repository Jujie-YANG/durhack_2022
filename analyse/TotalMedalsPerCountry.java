package analyse;

import static java.math.BigDecimal.valueOf;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * measures medals per country as well in percentage
 */
public class TotalMedalsPerCountry
{
	public static void main(String[] args) throws FileNotFoundException
	{
		Map<String, Integer> countriesAndMedals = new TreeMap<>(String::compareTo);
		int total = 0;
		
		File list = new File("analyse/data/countries-medals.csv");
		Scanner s = new Scanner(list);
		
		while (s.hasNextLine())
		{
			String l = s.nextLine();
			
			String[] countryMedals = l.split(",[^\\s]");
			// assert countryMedals.length == 2;
			if (countryMedals.length != 2)
			{
				System.out.println(l);
				break;
			}
			
			String name = countryMedals[0];
			String stat = countryMedals[1];
			
			// assert countryMedals.equals("Bronze")
			if (countriesAndMedals.containsKey(name))
			{
				if (!stat.equals("A"))
				{
					int before = countriesAndMedals.get(name);
					countriesAndMedals.replace(name , before + 1);
				}
			}
			else countriesAndMedals.put(name , stat.equals("A") ? 0 : 1);
			
			total += stat.equals("A") ? 0 : 1;
		}
		
		s.close();
		
		Map<String, BigDecimal> percentPerCountry = new TreeMap<>(String::compareTo);
		
		for (Entry<String , Integer> e : countriesAndMedals.entrySet())
		{
			BigDecimal perc = valueOf(e.getValue()).divide(valueOf(total) , 4 , RoundingMode.HALF_UP);
			percentPerCountry.put(e.getKey() , perc);
		}
		
		System.out.println(percentPerCountry);
	}
}