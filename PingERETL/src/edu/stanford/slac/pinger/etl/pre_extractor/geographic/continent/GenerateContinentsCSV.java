package edu.stanford.slac.pinger.etl.pre_extractor.geographic.continent;

// the complete file can be found at data/csv/allcontinents.csv

public class GenerateContinentsCSV {
	
	public static void main(String[] args) {
	
		GetContinentsSLAC c = new GetContinentsSLAC();
		int i;
		String continentsnames[] = c.getSLACContinents();
		
		System.out.print("id,continentCode,continentName,population,areaInSqKm"+"\n");
		
		int id = 1;
						
		for(i=0;i<continentsnames.length;i++){
					
			System.out.println(id+","+continentsnames[i]);	
			id++;
				
		}	
	
	}	

}
