package org.coding.exercise.contact.adapter.api;

import org.coding.exercise.contact.adapter.datastore.ContactRepository;
import org.coding.exercise.contact.domain.Contact;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static reactor.core.publisher.Mono.just;

@Component
public class ContactHandler {

    private ContactRepository contactRepository;

    public ContactHandler(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Mono<ServerResponse> saveContacts(ServerRequest request) {
        Mono<Contact> contact = request.bodyToMono(Contact.class);
        return contact.flatMap( c ->  status(HttpStatus.CREATED)
                .contentType(APPLICATION_JSON)
                .body(contactRepository.save(c), Contact.class));

    }

    public Mono<ServerResponse> getContacts(ServerRequest request) {
        return ok().body(contactRepository.findAll(), Contact.class).switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> getContact(ServerRequest request) {
        Mono<Contact> contact = contactRepository.findById(request.pathVariable("id"));
        return contact.flatMap(c -> ok().contentType(APPLICATION_JSON).body(just(c), Contact.class))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> deleteContact(ServerRequest request) {
        Mono<Contact> contact = contactRepository.findById(request.pathVariable("id"));
        return contact.flatMap( c -> ok().contentType(APPLICATION_JSON)
                .body(contactRepository.delete(c), Void.class))
                .switchIfEmpty(notFound().build());

    }

    public Mono<ServerResponse> updateContact(ServerRequest request) {
        Mono<Contact> contact = contactRepository.findById(request.pathVariable("id"));

        return contact.flatMap(c -> ok()
                .contentType(APPLICATION_JSON)
                .body(contactRepository.save(request.bodyToMono(Contact.class).block()), Contact.class))
                .switchIfEmpty(notFound().build());


    }
}
