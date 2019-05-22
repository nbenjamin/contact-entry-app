package org.coding.exercise.contact.domain;

public enum PhoneType {

    HOME("home"), WORK("work"), MOBILE("mobile");

    String value;

    PhoneType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
