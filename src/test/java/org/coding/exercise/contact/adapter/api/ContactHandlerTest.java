package org.coding.exercise.contact.adapter.api;


import org.coding.exercise.contact.adapter.datastore.ContactRepository;
import org.coding.exercise.contact.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContactHandlerTest {

    @Autowired
    private RouterFunction contactRouterFunction;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private WebTestClient webTestClient;
    private Contact contact;

    @BeforeEach
    public void beforeEach() {
        contact = new Contact();
        contact.setAddress(new Address("Cox Rd", "Glen Allen", "VA", "23060"));
        contact.setEmail("demo@mail.com");
        contact.setName(new Name("Harold", "Francis", "Gilkey"));
        contact.setPhone(singletonList(new Phone("484-484-5850", PhoneType.HOME)));
    }

    @Test
    void saveContacts_withValidContact_returnCreated() {

        EntityExchangeResult<Contact> actual = webTestClient.post().uri("/contacts").body(just(contact), Contact.class)
                .exchange().expectStatus().isCreated().expectBody(Contact.class).returnResult();
        assertThat(actual.getResponseBody().getId(), is(notNullValue()));
    }

    @Test
    void getContacts_withAvailableContacts_returnOK() {
        // Currently contact is added through commandline runner, so by default when application starts there will be
        // one contact record already.
        List<Contact> contacts =webTestClient.get().uri("/contacts").exchange().expectStatus().isOk().expectBodyList(Contact.class)
                .returnResult().getResponseBody();

        assertThat(contacts.size(), is(greaterThan(0)));
    }


    @Test
    void getContact_withValidId_returnOK() {
        EntityExchangeResult<Contact> actual = webTestClient.post().uri("/contacts").body(just(contact), Contact.class)
                .exchange().expectStatus().isCreated().expectBody(Contact.class).returnResult();
        assertThat(actual.getResponseBody().getId(), is(notNullValue()));
        webTestClient.get().uri("/contacts/"+actual.getResponseBody().getId()).exchange().expectStatus().isOk()
                .expectBodyList(Contact.class).hasSize(1);
    }


    @Test
    void deleteContact_withValidId_returnOK() {
        EntityExchangeResult<Contact> actual = webTestClient.post().uri("/contacts").body(just(contact), Contact.class)
                .exchange().expectStatus().isCreated().expectBody(Contact.class).returnResult();

        webTestClient.delete().uri("/contacts/" + actual.getResponseBody().getId())
                .exchange().expectStatus().isOk().expectBody(Void.class);

        webTestClient.get().uri("/contacts/"+actual.getResponseBody().getId()).exchange().expectStatus().isNotFound();
    }

    @Test
    void deleteContact_withInValidId_returnNotFound() {
        webTestClient.delete().uri("/contacts/234sds")
                .exchange().expectStatus().isNotFound();
    }

    @Test
    void updateContact_withValidId_returnOK() {
        EntityExchangeResult<List<Contact>> result =  webTestClient.get().uri("/contacts").exchange()
                .expectStatus().isOk().expectBodyList(Contact.class).returnResult();

        assertThat(result.getResponseBody().get(0).getEmail(), is(equalTo("demo@mail.com")));
        Contact updateContact = result.getResponseBody().get(0);
        updateContact.setEmail("update@email.com");

        EntityExchangeResult<Contact> actual = webTestClient.put().uri("/contacts/"+ updateContact.getId())
                .body(just(updateContact), Contact.class)
                .exchange().expectStatus().isOk().expectBody(Contact.class).returnResult();

        assertThat(actual.getResponseBody().getEmail(), is(equalTo(updateContact.getEmail())));

    }

    @Test
    void updateContact_withInValidId_returnNotFound() {
        webTestClient.put().uri("/contacts/2sdw3s")
                .body(just(contact), Contact.class)
                .exchange().expectStatus().isNotFound();

    }
}