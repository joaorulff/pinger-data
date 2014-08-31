package edu.stanford.slac.pinger.etl.pre_extractor.geographic.country;

/* this class compares countries of geonames from http://peric.github.io/GetCountries/ with countries 
* of http://www-iepm.slac.stanford.edu/pinger/pingerworld/all-nodes.cf and shows a CSV format
* id,countryCode,countryName,population,continentCode,areaInSqKm. The don't match SLAC countries are put with null parameters.
* 
* The final arquive with the manual adjustments can be founded at data/csv/allcountries.csv. This arquive was generated doing visual comparison 
* between the data/allcountriesgeonames.json for the unmatched countries.
* 
*/

public class GenerateCountriesCSV {
	
	public static void main(String[] args) {
				
		GetCountriesGeonames g = new GetCountriesGeonames();
		GetCountriesSLAC s = new GetCountriesSLAC();
		
		String[][] countriesG = g.getGeonameCountries();
		String[] countriesS = s.getSLACCountries();
				
		int i;	
		int j = 0;
        
		
		/*for (i = 0; i < countriesG.length ; i++) {    	
 	
			for (j = 0; j < 2; j++) {                            
				System.out.print(countriesG[i][j]);
 		
			}          	

			System.out.print("\n");
		}*/
		
		//System.out.println(countriesG.length);
		
		
		
		/*
		  for (i = 0; i < countries2.length ; i++) {    	
				 	
			System.out.println(countries2[i]);
		}
				
		System.out.println(countries2.length);
		
		 */
		
		String equalCountries[][] = new String[200][5];
		int m = 0;
		int k;
		//int elements = 0 ; // created for to know how many are equal
		
		//System.out.println(countriesG[0][1].toLowerCase()); 
		
		//System.out.println(countriesS[0].toLowerCase()); 
		int match = 0;
		String[] dontmatches = new String[100]; // this vector is for get the don't matches slac countries names	
		int l = 0;
			
		for (i = 0; i < countriesS.length ; i++) {    	
				
			//System.out.println(countriesS[i]);			
						
			for (j = 0; j < countriesG.length ; j++) {    	 	
			                          
					//System.out.print(countriesG[j][1]);						  	
					
					if (countriesS[i].equalsIgnoreCase(countriesG[j][1])){
						//System.out.println(countriesS[i]); 
						//System.out.println(countriesG[j][1]);
											
						for (k = 0; k < countriesG[0].length ; k++) {
							
							equalCountries[m][k] = countriesG[j][k];
							//System.out.println(equalCountries[m][k]);
											
							
						}
						match = 1;	
						m++;	
						
					}				
			}     
			
			if (match == 0){
				
				dontmatches[l] = countriesS[i];
				l++;				
			}
			
			match = 0;					
		}
		
		/*for (i = 0; i < equalCountries.length ; i++) {    	
	 	
		for (j = 0; j < equalCountries[0].length; j++) {                            
			System.out.print(equalCountries[i][0]);
		
		}          	

		System.out.print("\n");
		}*/
		
		j = 0;
		
		while(equalCountries[j][1]!=null){		
				
			l = j;
			j++;		
		}	
		
		l++; // getting the first null position
		
		i = 0;
	
		while (dontmatches[i]!= null){  	
			
			equalCountries[l][1] = 	dontmatches[i];
			i++;
			l++;
			
		}
	
		//System.out.println(equalCountries[1][0]);
		//System.out.println(equalCountries[1][1]);
		//System.out.println(equalCountries[1][2]);
		//System.out.println(equalCountries[1][3]);
		//System.out.println(equalCountries[1][4]);
		
		/*for (i = 0; i < equalCountries.length ; i++) {    	
	 	
			for (j = 0; j < equalCountries[0].length; j++) {                            
				System.out.print(equalCountries[i][j]);
		
			}          	

		System.out.print("\n");
		}*/
		
		
		// testando o array de iguais		
		/*for (i = 0; i < equalCountries.length ; i++) {    	
	 	
		for (j = 0; j < equalCountries[0].length; j++) {                            
			System.out.print(equalCountries[i][j]);
	
		}          	

		System.out.print("\n");
		}*/
		
		// generating the format
		
		String line = "";
		int id = 1;
		
		System.out.print("id,countryCode,countryName,population,continentCode,areaInSqKm"+"\n");	
								
		for (i = 0; i < equalCountries.length ; i++) {    
			
			for (j = 0; j < equalCountries[0].length ; j++) {    
				
				if (equalCountries[i][j] == null){equalCountries[i][j] = "\\N";}
				
				if(j==0){ 
					System.out.print(id+",");
					id++; 
				}
				if(j==4){System.out.println(equalCountries[i][j]);}
				else{
					
					System.out.print(equalCountries[i][j]+",");						
				}		
			}	
		}	
		
		
			
	}
}

