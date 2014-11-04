package org.bnez.xiaoyue.lsfy.tdh;

import java.util.Date;

public class Xiaci {

	private Long id;
	private String ysah;
	private String ysfy;
	private String yy;
	private Integer status;
	private Date updateAt;
	private Date createAt;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getYsah() {
		return ysah;
	}

	public void setYsah(String ysah) {
		this.ysah = ysah;
	}

	public String getYsfy() {
		return ysfy;
	}

	public void setYsfy(String ysfy) {
		this.ysfy = ysfy;
	}

	public String getYy() {
		return yy;
	}

	public void setYy(String yy) {
		this.yy = yy;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUpdateAt()
	{
		return updateAt;
	}

	public void setUpdateAt(Date updateAt)
	{
		this.updateAt = updateAt;
	}

	public Date getCreateAt()
	{
		return createAt;
	}

	public void setCreateAt(Date createAt)
	{
		this.createAt = createAt;
	}

}
