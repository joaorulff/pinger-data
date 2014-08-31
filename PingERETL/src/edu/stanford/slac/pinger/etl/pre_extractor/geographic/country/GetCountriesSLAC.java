package edu.stanford.slac.pinger.etl.pre_extractor.geographic.country;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

/* this class get all countries on http://www-iepm.slac.stanford.edu/pinger/pingerworld/all-nodes.cf
 * the reader arquive above was created by:
 * @monitoring_countries=("Algeria","Bangladesh","Bolivia","Brazil","Burkina Faso","Canada","China","Germany","India","Italy","Japan","Jordan","Malaysia","Mexico","Pakistan","Palestine","Russia","South Africa","Sri Lanka","Switzerland","Taiwan","United States",);
  @remote_countries=("Afghanistan","Albania","Algeria","Andorra","Angola","Argentina","Armenia","Australia","Austria","Azerbaijan","Bahamas","Bahrain","Bangladesh","Belarus","Belgium","Benin","Bhutan","Bolivia","Bosnia Herzegovina","Botswana","Brazil","Brunei","Bulgaria","Burkina Faso","Burundi","Cambodia","Cameroon","Canada","Cape Verde","Chile","China","Colombia","Costa Rica","Croatia","Cuba","Cyprus","Czech Republic","Democratic Republic of Congo","Denmark","Dominican Republic","Ecuador","Egypt","El Salvador","Eritrea","Estonia","Ethiopia","Faroe Islands","Finland","France","French Polynesia","Gabon","Gambia","Georgia","Germany","Ghana","Gibralter","Greece","Greenland","Guatemala","Guinea","Honduras","Hungary","Iceland","India","Indonesia","Iran","Iraq","Ireland","Israel","Italy","Ivory Coast","Jamaica","Japan","Jordan","Kazakhstan","Kenya","Korea, Rep","Kuwait","Kyrgyzstan","Laos","Latvia","Lebanon","Lesotho","Liberia","Libya Arab Jamahiriya","Liechtenstein","Lithuania","Luxembourg","Macedonia","Madagascar","Malawi","Malaysia","Maldives","Mali","Mauritania","Mauritius","Mexico","Moldova","Mongolia","Morocco","Mozambique","Myanmar","Namibia","Nepal","Netherlands","Netherlands Antilles","New Zealand","Nicaragua","Niger","Nigeria","Norway","Oman","Pakistan","Palestine","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Poland","Portugal","Qatar","Republic of the Congo","Reunion-French Colony","Romania","Russia","Rwanda","San Marino","Saudi Arabia","Senegal","Serbia and Montenegro","Seychelles","Sierra Leone","Singapore","Slovak Republic","Slovenia","Solomon Islands","Somalia","South Africa","Spain","Sri Lanka","Sudan","Swaziland","Sweden","Switzerland","Syria","Taiwan","Tajikistan","Tanzania","Thailand","Togo","Trinidad and Tobago","Tunisia","Turkey","Turkmenistan","Uganda","Ukraine","United Arab Emirates","United Kingdom","United States","Uruguay","Uzbekistan","Venezuela","Vietnam","Yemen","Zambia","Zimbabwe",);
 * 
 */

//ATTENTION: if update this file remove de coma em "korea , rep"

public class GetCountriesSLAC {
	
	public String[] getSLACCountries() {
		 
	try{
		
		
		File arquive = new File("data/txt/slaccountriesnames.txt");
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
