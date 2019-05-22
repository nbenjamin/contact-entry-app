package org.coding.exercise.contact.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Name implements Serializable {

    private String first;
    private String middle;
    private String last;
}
