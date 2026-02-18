package org.springframework.samples.petclinic.patriot;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.petclinic.model.NamedEntity;

/**
 * Simple JavaBean domain object representing an Incentive Type in the Patriot Thanks
 * system. Incentive types categorize discounts (e.g., Veteran, Active Duty, First
 * Responder, Spouse).
 *
 * @author Edward McKeown
 */
@Entity
@Table(name = "incentive_types")
@Getter
@Setter
public class IncentiveType extends NamedEntity {

	@Column(name = "description")
	private String description;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

}
