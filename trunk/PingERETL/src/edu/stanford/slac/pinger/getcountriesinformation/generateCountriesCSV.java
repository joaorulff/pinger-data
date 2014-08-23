package edu.stanford.slac.pinger.getcountriesinformation;

//this class compares countries of geonames from http://peric.github.io/GetCountries/ with countries 
// of http://www-iepm.slac.stanford.edu/pinger/pingerworld/all-nodes.cf and shows a CSV format

public class generateCountriesCSV {
	
	public static void main(String[] args) {
		
		
		getCountriesGeonames g = new getCountriesGeonames();
		getCountriesSLAC s = new getCountriesSLAC();
		
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
		int equals = 0;
		
		
		for (i = 0; i < countriesS.length ; i++) {    	
			
						
			//System.out.println(countriesS[i]);			
						
			for (j = 0; j < countriesG.length ; j++) {    	
			 	
			                          
					//System.out.print(countriesG[j][1]);
									  	
					
					if (countriesS[i].equalsIgnoreCase(countriesG[j][1])){
						//System.out.println(countriesS[i]); 
						//System.out.println(countriesG[j][1]);
						equals++;
						
						for (k = 0; k < countriesG[0].length ; k++) {
							
							equalCountries[m][k] = countriesG[j][k];
							//System.out.println(equalCountries[m][k]);
											
							
						}
						
						m++;
			
						
					}
					
			}     	

				
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
		
		String line = "";
		int id = 0;
		
		System.out.print("id,countryCode,countryName,population,continentCode,areaInSqKm"+"\n");	
								
		for (i = 0; i < equalCountries.length ; i++) {    
			
			for (j = 0; j < equalCountries[0].length ; j++) {    
				
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

