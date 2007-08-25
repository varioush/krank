package com.arcmind.jpa.course.inheritance2.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;


@Entity (name="In2Subject")
@Inheritance (strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Subject implements Serializable {

	private Long id;
	private Set<Role> roles;
	private String name;
	private ContactInfo contactInfo;

	@Id @GeneratedValue (strategy=GenerationType.SEQUENCE)
	@Column(name="SUBJECT_ID")
	public Long getId() {
		return id;
	}
	
	@Column (name="SUBJECT_NAME")
	public String getName() {
		return name;
	}
	
	@Transient
	public Set<Role> getRoles() {
		if (roles == null) {
			roles = new java.util.HashSet<Role>();
		}
		return roles;
	}
	
	@OneToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="SUBJECT_CONTACT_INFO_ID")
	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	

	public Subject (String name, ContactInfo contactInfo) {
		this.name = name;
		this.contactInfo = contactInfo;
		if (contactInfo!=null) {
			contactInfo.setSubject(this);
		}
	}
	
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Subject (String name) {
		this.name = name;
	}

	
	public Subject () {
	}
	
	public void setId(Long id) {
		this.id = id;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}
	
}
