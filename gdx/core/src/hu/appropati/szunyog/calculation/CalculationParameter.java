package hu.appropati.szunyog.calculation;

import lombok.Data;

@Data
public class CalculationParameter {
    public enum Type {
        PERSON_A_SPEED, PERSON_B_SPEED, PERSON_A_B_DISTANCE,
        TARGET_DISTANCE,
        FLY_SPEED, START_TIME,
        WIND_SPEED
    }

    private final Type type;
    private final float value;
}
