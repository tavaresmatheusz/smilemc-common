package br.com.smilemc.commons.common.report;

import lombok.Getter;

@Getter
public class Report {

	private String target, reportedBy;
	private String reason;
	private String time;

	public Report(String target, String reportedBy, String reason, String time) {
		// TODO Auto-generated constructor stub
		this.target = target;
		this.reportedBy = reportedBy;
		this.reason = reason;
		this.time = time;
	}

}
