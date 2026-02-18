package org.springframework.samples.petclinic.patriot;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.springframework.samples.petclinic.model.BaseEntity;

/**
 * Simple JavaBean domain object representing an Address. Addresses can be associated with
 * business locations.
 *
 * @author Edward McKeown
 */
@Entity
@Table(name = "addresses")
@Getter
@Setter
public class Address extends BaseEntity {

	@Column(name = "street_address")
	@NotEmpty
	private String streetAddress;

	@Column(name = "address_line_2")
	private String addressLine2;

	@Column(name = "city")
	@NotEmpty
	private String city;

	@ManyToOne
	@JoinColumn(name = "state_id")
	@NotNull
	private UsState state;

	@Column(name = "zip_code")
	@NotEmpty
	private String zipCode;

	@Column(name = "latitude")
	private BigDecimal latitude;

	@Column(name = "longitude")
	private BigDecimal longitude;

	@Column(name = "created_at", insertable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private LocalDateTime updatedAt;

	/**
	 * Returns a formatted string representation of the address.
	 * @return formatted address string
	 */
	public String getFullAddress() {
		StringBuilder sb = new StringBuilder();
		sb.append(streetAddress);
		if (addressLine2 != null && !addressLine2.isEmpty()) {
			sb.append(", ").append(addressLine2);
		}
		sb.append(", ").append(city);
		sb.append(", ").append(state.getCode());
		sb.append(" ").append(zipCode);
		return sb.toString();
	}

}
