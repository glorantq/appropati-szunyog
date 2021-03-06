package hu.appropati.szunyog.calculation;

import lombok.Data;

/**
 * Számolás végeredménye
 *
 * @see hu.appropati.szunyog.screens.CalculationDataScreen
 * @since 1.0
 * @author Gerber Lóránt Viktor
 */
@Data
public class CalculationParameter {
    public enum Type {
        PERSON_A_SPEED, PERSON_B_SPEED, PERSON_A_B_DISTANCE,
        TARGET_DISTANCE,
        FLY_SPEED, START_TIME,
        WIND_SPEED
    }
}
