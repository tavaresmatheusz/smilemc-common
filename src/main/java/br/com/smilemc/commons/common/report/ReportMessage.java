package br.com.smilemc.commons.common.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportMessage {

	private String reportedName, reportedBy, reason, formmatedTime;
	
	public ReportMessage setReportedName(String string) {
		this.reportedName = string;
		return this;
	}
	
	public ReportMessage setReportedBy(String string) {
		this.reportedBy = string;
		return this;
	}
	
	public ReportMessage setReason(String string) {
		this.reason = string;
		return this;
	}
	
	public ReportMessage setFormmatedTime(String string) {
		this.formmatedTime = string;
		return this;
	}
	
}
