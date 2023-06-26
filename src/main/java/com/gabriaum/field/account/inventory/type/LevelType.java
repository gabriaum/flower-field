package com.gabriaum.field.account.inventory.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum LevelType {

    ONE(1, 0.0, 0.7),
    TWO(2, 1500.0, 1.2),
    THREE(3, 2000.0, 4.7),
    FOUR(4, 3000.0, 10.5),
    FIVE(5, 8000.0, 19.2),
    SIX(6, 12000.0, 45.0),
    SEVEN(7, 18000.0, 87.0),
    EIGHT(8, 29000.0, 151.4),
    NINE(9, 36000.0, 206.3),
    TEN(10, 50000.0, 342.17);

    private final int level;
    private final double price, earnings;

    public static LevelType read(int ordinal) {
        return Arrays.stream(values()).filter(level -> level.ordinal() == ordinal).findFirst().orElse(ONE);
    }
}
