package com.gabriaum.field.account;

import com.gabriaum.field.Core;
import com.gabriaum.field.Field;
import com.gabriaum.field.account.inventory.type.LevelType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Account {

    private final UUID uniqueId;
    private final String name;

    private LevelType shearLevel = LevelType.ONE;
    private double flowers = 0.0;

    public void updateShearLevel(LevelType shearLevel) {
        this.shearLevel = shearLevel;

        save("shearLevel");
    }

    public void upgradeShearLevel() {
        updateShearLevel(nextShearLevel());
        removeFlower(shearLevel.getPrice());
    }

    public LevelType nextShearLevel() {
        return LevelType.read(shearLevel.ordinal() + 1);
    }

    public String formatToString() {
        String numberString = Double.toString(flowers);

        int firstDigits = Integer.parseInt(numberString.substring(0, numberString.indexOf('.')));
        int decimalDigit = Character.getNumericValue(numberString.charAt(numberString.indexOf('.') + 1));

        return firstDigits + "." + decimalDigit;
    }

    public void addFlower() {
        double normalValue = shearLevel.getEarnings();
        double pvpValue = (normalValue + ((normalValue * 30) / 100));

        flowers += Field.getPlugin(Field.class).getPlayersPvPActive().contains(uniqueId) ? pvpValue : normalValue;

        save("flowers");
    }

    public void removeFlower(double quantity) {
        flowers -= quantity;

        save("flowers");
    }

    public void save(String... fields) {
        for (String field : fields)
            Core.getAccountData().update(this, field);
    }
}
