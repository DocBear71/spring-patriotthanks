package org.springframework.samples.petclinic.patriot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for Incentive data. Used to transfer incentive information to the
 * frontend without causing JSON serialization issues from bidirectional JPA
 * relationships.
 *
 * @author Edward McKeown
 */
public class IncentiveDTO {

	/** Date formatter for display strings (e.g., "Dec 30, 2026"). */
	private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy");

	private Integer id;

	private String title;

	private String description;

	private BigDecimal discountAmount;

	private BigDecimal discountPercentage;

	private String verificationRequired;

	private LocalDate startDate;

	private LocalDate endDate;

	private String formattedDiscount;

	private boolean currentlyValid;

	private String validityDisplay;

	private List<IncentiveTypeDTO> incentiveTypes = new ArrayList<>();

	/**
	 * Default constructor for IncentiveDTO.
	 */
	public IncentiveDTO() {
	}

	/**
	 * Constructor to create DTO from Incentive entity. Populates all fields including
	 * computed validity status and display string.
	 * @param incentive the Incentive entity to convert
	 */
	public IncentiveDTO(Incentive incentive) {
		this.id = incentive.getId();
		this.title = incentive.getTitle();
		this.description = incentive.getDescription();
		this.discountAmount = incentive.getDiscountAmount();
		this.discountPercentage = incentive.getDiscountPercentage();
		this.verificationRequired = incentive.getVerificationRequired();
		this.startDate = incentive.getStartDate();
		this.endDate = incentive.getEndDate();
		this.formattedDiscount = incentive.getFormattedDiscount();
		this.currentlyValid = incentive.isCurrentlyValid();
		this.validityDisplay = computeValidityDisplay(incentive);

		// Convert incentive types
		if (incentive.getIncentiveTypes() != null) {
			this.incentiveTypes = incentive.getIncentiveTypes()
				.stream()
				.map(IncentiveTypeDTO::new)
				.collect(Collectors.toList());
		}
	}

	/**
	 * Computes the validity display string based on the incentive's start and end dates.
	 * <ul>
	 * <li>If the start date is in the future: "Starts [start date]"</li>
	 * <li>If currently active with an end date: "Valid until [end date]"</li>
	 * <li>If no dates are set: empty string (no display needed)</li>
	 * </ul>
	 * @param incentive the Incentive entity to compute the display string for
	 * @return the formatted validity display string, or empty string if no dates apply
	 */
	private String computeValidityDisplay(Incentive incentive) {
		LocalDate today = LocalDate.now();
		LocalDate start = incentive.getStartDate();
		LocalDate end = incentive.getEndDate();

		// If start date is in the future, show when it starts
		if (start != null && today.isBefore(start)) {
			return "Starts " + start.format(DISPLAY_DATE_FORMAT);
		}

		// If currently active and has an end date, show valid until
		if (end != null) {
			return "Valid until " + end.format(DISPLAY_DATE_FORMAT);
		}

		// No relevant dates to display
		return "";
	}

	// Getters and Setters

	/**
	 * Gets the incentive ID.
	 * @return the incentive ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the incentive ID.
	 * @param id the incentive ID to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the incentive title.
	 * @return the incentive title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the incentive title.
	 * @param title the incentive title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the incentive description.
	 * @return the incentive description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the incentive description.
	 * @param description the incentive description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the discount amount.
	 * @return the discount amount
	 */
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	/**
	 * Sets the discount amount.
	 * @param discountAmount the discount amount to set
	 */
	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	/**
	 * Gets the discount percentage.
	 * @return the discount percentage
	 */
	public BigDecimal getDiscountPercentage() {
		return discountPercentage;
	}

	/**
	 * Sets the discount percentage.
	 * @param discountPercentage the discount percentage to set
	 */
	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	/**
	 * Gets the verification required description.
	 * @return the verification required description
	 */
	public String getVerificationRequired() {
		return verificationRequired;
	}

	/**
	 * Sets the verification required description.
	 * @param verificationRequired the verification required description to set
	 */
	public void setVerificationRequired(String verificationRequired) {
		this.verificationRequired = verificationRequired;
	}

	/**
	 * Gets the incentive start date.
	 * @return the start date
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Sets the incentive start date.
	 * @param startDate the start date to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the incentive end date.
	 * @return the end date
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Sets the incentive end date.
	 * @param endDate the end date to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the formatted discount string.
	 * @return the formatted discount string
	 */
	public String getFormattedDiscount() {
		return formattedDiscount;
	}

	/**
	 * Sets the formatted discount string.
	 * @param formattedDiscount the formatted discount string to set
	 */
	public void setFormattedDiscount(String formattedDiscount) {
		this.formattedDiscount = formattedDiscount;
	}

	/**
	 * Gets whether this incentive is currently valid.
	 * @return true if the incentive is currently valid
	 */
	public boolean isCurrentlyValid() {
		return currentlyValid;
	}

	/**
	 * Sets whether this incentive is currently valid.
	 * @param currentlyValid the validity status to set
	 */
	public void setCurrentlyValid(boolean currentlyValid) {
		this.currentlyValid = currentlyValid;
	}

	/**
	 * Gets the formatted validity display string.
	 * @return the validity display string (e.g., "Valid until Dec 30, 2026")
	 */
	public String getValidityDisplay() {
		return validityDisplay;
	}

	/**
	 * Sets the formatted validity display string.
	 * @param validityDisplay the validity display string to set
	 */
	public void setValidityDisplay(String validityDisplay) {
		this.validityDisplay = validityDisplay;
	}

	/**
	 * Gets the list of incentive types.
	 * @return the list of incentive type DTOs
	 */
	public List<IncentiveTypeDTO> getIncentiveTypes() {
		return incentiveTypes;
	}

	/**
	 * Sets the list of incentive types.
	 * @param incentiveTypes the list of incentive type DTOs to set
	 */
	public void setIncentiveTypes(List<IncentiveTypeDTO> incentiveTypes) {
		this.incentiveTypes = incentiveTypes;
	}

	/**
	 * Nested DTO for IncentiveType data.
	 */
	public static class IncentiveTypeDTO {

		private Integer id;

		private String name;

		/**
		 * Default constructor for IncentiveTypeDTO.
		 */
		public IncentiveTypeDTO() {
		}

		/**
		 * Constructor to create DTO from IncentiveType entity.
		 * @param type the IncentiveType entity to convert
		 */
		public IncentiveTypeDTO(IncentiveType type) {
			this.id = type.getId();
			this.name = type.getName();
		}

		/**
		 * Gets the incentive type ID.
		 * @return the incentive type ID
		 */
		public Integer getId() {
			return id;
		}

		/**
		 * Sets the incentive type ID.
		 * @param id the incentive type ID to set
		 */
		public void setId(Integer id) {
			this.id = id;
		}

		/**
		 * Gets the incentive type name.
		 * @return the incentive type name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the incentive type name.
		 * @param name the incentive type name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

	}

}
