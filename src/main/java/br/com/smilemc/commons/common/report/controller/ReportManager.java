package br.com.smilemc.commons.common.report.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.Common.InstanceType;
import br.com.smilemc.commons.common.report.Report;
import br.com.smilemc.commons.common.report.ReportMessage;
import br.com.smilemc.commons.common.util.string.StringDateUtils;
import redis.clients.jedis.Jedis;

public class ReportManager {

	private List<Report> reportList;

	public ReportManager() {
		// TODO Auto-generated constructor stub
		reportList = new ArrayList<>();
	}

	public void newReport(Report report) {
		if (Common.getInstanceType() == InstanceType.BUKKIT) {
			Common.debug("novo report");
			reportList.add(report);
		} else {
			ReportMessage reportMessage = new ReportMessage().setReportedName(report.getTarget())
					.setFormmatedTime(StringDateUtils.getDate()).setReason(report.getReason())
					.setReportedBy(report.getReportedBy());
			Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();
			jedis.set(Common.NEW_REPORT, Common.getGson().toJson(reportMessage));
			jedis.expire(Common.NEW_REPORT, 1);
			jedis.close();
			reportMessage = null;
		}
	}

	public List<Report> getReportList() {
		return reportList;
	}

	public void deleteReport(Report report) {
		if (Common.getInstanceType() == InstanceType.BUKKIT) 
			reportList.remove(report);

	}

}
