package org.springframework.samples.petclinic.patriot;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.petclinic.model.BaseEntity;

/**
 * Simple JavaBean domain object representing a US State. Contains the 2-letter state code
 * and full state name.
 *
 * @author Edward McKeown
 */
@Entity
@Table(name = "us_states")
@Getter
@Setter
public class UsState extends BaseEntity {

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Override
	public String toString() {
		return code;
	}

}
