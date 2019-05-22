package org.coding.exercise.contact.adapter.datastore;

import org.coding.exercise.contact.domain.Contact;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ContactRepository extends ReactiveMongoRepository<Contact, String> {

}
