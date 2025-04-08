package org.borispopic.paymenttransfersystem.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EntryType {
    DEBIT("DEBIT"),
    CREDIT("CREDIT");

    private final String value;

    public static EntryType formValue(String value) {
        return Arrays.stream(EntryType.values())
                .filter(argument -> argument.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("EntryType with \"" + value + "\" does not exist."));
    }
}
