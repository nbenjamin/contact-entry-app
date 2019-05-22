package org.coding.exercise.contact.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document
@NoArgsConstructor
public class Contact implements Serializable {
    @Id
    private String id;
    private Name name;
    private Address address;
    private List<Phone> phone;
    private String email;

}
