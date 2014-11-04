package org.bnez.xiaoyue.lsfy.report;

import net.sf.json.JSONObject;

public class ReportCondition {
	private String folderName;
	private String reportName;
	private String from;
	private String to;
	private String zhibiao;
	private String fanwei;
	private String passport;
	private int headerCancel;
	private boolean isLongTagPath;
	private String month;
	private String year;
	private String sfft;
	private int tableTag;
	private String court;
	private boolean isLastCancel;
	private int type;
	private String page;
	private String  zb;
	
	public ReportCondition()
	{
		headerCancel = 1;
		isLongTagPath = false;
		tableTag = 2;
	}
	
	public String toString()
	{
		return "[" + getReportName() + "," + getZhibiao() + "," + getFanwei() + "," + getFrom() + "," + getTo() + "]";
	}
	
	public String toJson()
	{
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}

	public int getHeaderCancel() {
		return headerCancel;
	}

	public void setHeaderCancel(int headerCancel) {
		this.headerCancel = headerCancel;
	}

	@Deprecated
	public boolean isLongTagPath() {
		return isLongTagPath;
	}

	@Deprecated
	public void setLongTagPath(boolean isLongTagPath) {
		this.isLongTagPath = isLongTagPath;
		if(isLongTagPath)
			this.tableTag = 3;
		else
			this.tableTag = 2;
	}

	public String getCourt() {
		return court;
	}

	public void setCourt(String court) {
		this.court = court;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getZhibiao() {
		return zhibiao;
	}

	public void setZhibiao(String zhibiao) {
		this.zhibiao = zhibiao;
	}

	public String getFanwei() {
		return fanwei;
	}

	public void setFanwei(String fanwei) {
		this.fanwei = fanwei;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getSfft() {
		return sfft;
	}

	public void setSfft(String sfft) {
		this.sfft = sfft;
	}

	public int getTableTag()
	{
		return tableTag;
	}

	public void setTableTag(int tableTag)
	{
		this.tableTag = tableTag;
	}

	public boolean isLastCancel()
	{
		return isLastCancel;
	}

	public void setLastCancel(boolean isLastCancel)
	{
		this.isLastCancel = isLastCancel;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getZb() {
		return zb;
	}

	public void setZb(String zb) {
		this.zb = zb;
	}






}
