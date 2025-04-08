package org.borispopic.paymenttransfersystem.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    PERSONAL("personal"),
    COMMERCIAL("commercial"),
    INTERNAL_CASH("internal_cash");

    private final String value;

    public static AccountType formValue(String value) {
        return Arrays.stream(AccountType.values())
                .filter(argument -> argument.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("AccountType with \"" + value + "\" does not exist."));
    }
}
