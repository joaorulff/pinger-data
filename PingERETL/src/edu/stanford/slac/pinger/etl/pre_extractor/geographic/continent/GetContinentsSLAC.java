package edu.stanford.slac.pinger.etl.pre_extractor.geographic.continent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

/* This classes take the SLAC continent names from http://www-iepm.slac.stanford.edu/pinger/pingerworld/all-nodes.cf
 *  and returns a not repeated string array of names
 */

public class GetContinentsSLAC {
	
	public String[] getSLACContinents() {
		 
		try{
			
			
			File arquive = new File("data/txt/slaccontinentsnames.txt");
			FileReader arq = new FileReader(arquive);
			BufferedReader lerArq = new BufferedReader(arq);
			
			String line = lerArq.readLine();
			
			line = line.replace("\"","");
			
			String countries[] = line.split(",");
			//int i;		
			
			/*for(i=0;i<countries.length;i++){
				
				
				System.out.println(countries[i]+"\n");
				
				
			}*/	
			
			// this conversion is for remove the monitoring who are remote countries too.
			 countries = new TreeSet<String>(Arrays.asList(countries))  
	               .toArray(new String[0]);   
			 
			 
			lerArq.close();
			
			return countries;
			
			//System.out.println(line);
		
		}catch (FileNotFoundException ex) {
			ex.getStackTrace();
			
		}catch (IOException ex2) {
			ex2.getStackTrace();
		}
		return null;
		
	}

}
