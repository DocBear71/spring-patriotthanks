package org.springframework.samples.petclinic.school;

import jakarta.validation.Valid;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface SubscriptionRepository extends Repository<Subscription, Integer> {

	@Transactional(readOnly = true)
	Collection<Subscription> findAllBy();

	void save(@Valid Subscription subscription);

}
