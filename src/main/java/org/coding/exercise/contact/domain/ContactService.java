package org.coding.exercise.contact.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContactService {

    Flux<Contact> getContacts();

    Mono<Contact> getContact();

    Mono<Contact> saveContact(Contact contact);

    Mono<Contact> updateContact(Contact contact);

    Mono<Void> deleteContact(Contact contact);

}
