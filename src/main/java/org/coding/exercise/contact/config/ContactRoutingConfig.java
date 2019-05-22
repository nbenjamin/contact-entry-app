package org.coding.exercise.contact.config;

import org.coding.exercise.contact.adapter.api.ContactHandler;
import org.coding.exercise.contact.adapter.datastore.ContactRepository;
import org.coding.exercise.contact.domain.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static java.util.Collections.singletonList;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ContactRoutingConfig {

    @Bean
    public CommandLineRunner commandLineRunner(ContactRepository contactRepository) {
        return args -> {
            Contact contact = new Contact();
            contact.setAddress(new Address("Cox Rd", "Glen Allen", "VA", "23060"));
            contact.setEmail("demo@mail.com");
            contact.setName(new Name("Harold", "Francis", "Gilkey"));
            contact.setPhone(singletonList(new Phone("484-484-5850", PhoneType.HOME)));
            contactRepository.save(contact).subscribe();

            contactRepository.findAll().log().subscribe(System.out::println);

        };
    }

    @Bean
    public RouterFunction<ServerResponse> contactRouterFunction(ContactHandler contactHandler) {
        return nest(path("/contacts"),
                route(GET("/"), contactHandler::getContacts)
                        .andRoute(POST("/"), contactHandler::saveContacts)
                        .andRoute(PUT("/{id}"), contactHandler::updateContact)
                        .andRoute(GET("/{id}"), contactHandler::getContact)
                        .andRoute(DELETE("/{id}"), contactHandler::deleteContact));
    }
}
