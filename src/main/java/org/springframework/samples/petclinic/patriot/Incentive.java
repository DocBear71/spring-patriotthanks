package org.springframework.samples.petclinic.patriot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.RoundingMode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import org.springframework.samples.petclinic.model.BaseEntity;

/**
 * Simple JavaBean domain object representing an Incentive in the Patriot Thanks system.
 * Incentives are discounts and special offers provided by businesses to veterans, active
 * military, first responders, and their families.
 *
 * @author Edward McKeown
 */
@Entity
@Table(name = "incentives")
@Getter
@Setter
@SQLDelete(sql = "UPDATE incentives SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Incentive extends BaseEntity {

	@Column(name = "title")
	@NotBlank
	private String title;

	@Column(name = "description")
	@NotBlank
	private String description;

	@Column(name = "discount_amount")
	private BigDecimal discountAmount;

	@Column(name = "discount_percentage")
	private BigDecimal discountPercentage;

	@Column(name = "terms_and_conditions")
	private String termsAndConditions;

	@Column(name = "verification_required")
	private String verificationRequired;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Column(name = "is_active")
	private Boolean isActive = true;

	@ManyToOne
	@JoinColumn(name = "business_id")
	@NotNull
	private Business business;

	@Column(name = "submitted_by_user_id")
	private Integer submittedByUserId;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "business_incentive_types",
		joinColumns = @JoinColumn(name = "incentive_id"),
		inverseJoinColumns = @JoinColumn(name = "incentive_type_id"))
	private List<IncentiveType> incentiveTypes = new ArrayList<>();

	/**
	 * Returns a formatted discount string. Shows either percentage or fixed amount
	 * discount.
	 * @return formatted discount string (e.g., "10%" or "$5.00 off")
	 */
	public String getFormattedDiscount() {
		if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
			return discountPercentage.stripTrailingZeros().toPlainString() + "%";
		}
		else if (discountAmount != null && discountAmount.compareTo(BigDecimal.ZERO) > 0) {
			return "$" + discountAmount.setScale(2, RoundingMode.HALF_UP).toPlainString() + " off";
		}
		return "See details";
	}

	/**
	 * Checks if this incentive is currently valid based on start and end dates.
	 * @return true if the incentive is currently valid
	 */
	public boolean isCurrentlyValid() {
		LocalDate today = LocalDate.now();

		// No start date means always valid from the start
		boolean afterStart = (startDate == null) || !today.isBefore(startDate);

		// No end date means no expiration
		boolean beforeEnd = (endDate == null) || !today.isAfter(endDate);

		return afterStart && beforeEnd && Boolean.TRUE.equals(isActive);
	}

}
