package edu.stanford.slac.pinger.main.pre;

import java.util.Arrays;

import edu.stanford.slac.pinger.beans.ContinentBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateContinentCSV {

	public static void start() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(ContinentBean.CSV_HEADER);
		try {
			Object[] arrContinents = ContinentBean.MAP.values().toArray();
			Arrays.sort(arrContinents);
			for (Object continentObj : arrContinents) {
				ContinentBean cb = (ContinentBean) continentObj;
				sb.append(cb.toString(','));
				sb.append("\n");
			}
		}catch (Exception e) {
			Logger.error("ContinentInstantiator " , e);
		}
		Utils.writeIntoFile(sb.toString(), C.CONTINENT_CSV);
		Logger.log("Successfully written into " + C.CONTINENT_CSV);
	}
	
	public static void main(String args[]) {
		start();
	}
}
