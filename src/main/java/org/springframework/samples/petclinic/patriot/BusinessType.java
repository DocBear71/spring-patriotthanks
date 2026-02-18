package org.springframework.samples.petclinic.patriot;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.petclinic.model.NamedEntity;

import java.time.LocalDateTime;

/**
 * Simple JavaBean domain object representing a Business Type. Examples: Automotive,
 * Entertainment, Hardware, Pharmacy, Restaurant, Retail, Technology, Other
 *
 * @author Edward McKeown
 */
@Entity
@Table(name = "business_types")
@Getter
@Setter
public class BusinessType extends NamedEntity {

	@Column(name = "description")
	private String description;

	@Column(name = "display_order")
	private Integer displayOrder;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

}
