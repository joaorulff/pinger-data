package edu.stanford.slac.pinger.main.pre.dimensions;

import java.util.Arrays;

import edu.stanford.slac.pinger.beans.MetricBean;
import edu.stanford.slac.pinger.general.C;
import edu.stanford.slac.pinger.general.Logger;
import edu.stanford.slac.pinger.general.utils.MeasurementUtils;
import edu.stanford.slac.pinger.general.utils.Utils;

public class CreateMetricCSV {

	public static void start() {
		StringBuilder sb = new StringBuilder();
		sb.append(MetricBean.CSV_HEADER);
		try {
			Object[] arrMetrics = MeasurementUtils.mapMetricBean.values().toArray();
			Arrays.sort(arrMetrics);
			for (Object metricObj : arrMetrics) {
				MetricBean mb = (MetricBean) metricObj;
				sb.append(mb.toString(','));
				sb.append("\n");
			}
		}catch (Exception e) {
			Logger.error("CreateMetricCSV " , e);
		}
		Utils.writeIntoFile(sb.toString(), C.METRIC_CSV);
		Logger.log("Successfully written into " + C.METRIC_CSV);
	}
	
	public static void main(String args[]) {
		start();
	}
}
