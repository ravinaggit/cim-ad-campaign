package com.cim.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.cim.utils.CustomDateTimeDeserializer;
import com.cim.utils.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity
public class AdInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String partner;
	
	private int duration;
	
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@JsonDeserialize(using = CustomDateTimeDeserializer.class)
	private LocalDateTime dateTime;
	
	private String content;
	
	public AdInfo() {
	}

	public AdInfo(String partner, int duration, String content) {
		super();
		this.partner = partner;
		this.duration = duration;
		this.content = content;
	}

	public AdInfo(String partner, int duration, LocalDateTime dateTime, String content) {
		super();
		this.partner = partner;
		this.duration = duration;
		this.dateTime = dateTime;
		this.content = content;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "AdInfo [partner=" + partner + ", duration=" + duration + ", createdTime=" + dateTime + ", content="
				+ content + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((partner == null) ? 0 : partner.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdInfo other = (AdInfo) obj;
		if (partner == null) {
			if (other.partner != null)
				return false;
		} else if (!partner.equals(other.partner))
			return false;
		return true;
	}
	
	
	
	
}
