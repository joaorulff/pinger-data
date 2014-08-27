package edu.stanford.slac.pinger.getcontinentinformation;

// the complete file can be found at data/csv/allcontinents.csv

public class generateContinentsCSV {
	
	public static void main(String[] args) {
	
		getContinentsSLAC c = new getContinentsSLAC();
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
