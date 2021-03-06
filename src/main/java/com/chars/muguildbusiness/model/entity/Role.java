package com.chars.muguildbusiness.model.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "authorities", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "authority"} )}
)
public class Role implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 20)
	private String authority;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
}
