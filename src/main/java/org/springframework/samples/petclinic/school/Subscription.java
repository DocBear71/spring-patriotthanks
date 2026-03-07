package org.springframework.samples.petclinic.school;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.samples.petclinic.model.NamedEntity;

import java.time.LocalDateTime;


@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@SQLDelete(sql = "UPDATE subscriptions SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Subscription extends NamedEntity {


	@Column(name = "description")
	@NotEmpty(message = "Please provide a detailed description for this plan.")
	private String description;


	@Column(name = "monthly_price")
	@NotNull(message = "Monthly price is required")
	private Integer monthlyPrice;


	@Column(name = "annual_price")
	@NotNull(message = "Annual price is required")
	private Integer annualPrice;


	@Column(name = "featured", nullable = false)
	private boolean featured = false;


	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;


	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;


	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

}
