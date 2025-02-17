package delivery;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static delivery.CargoDimension.LARGE;
import static delivery.CargoDimension.SMALL;
import static delivery.DeliveryWorkload.*;
import static delivery.ExceptionMessage.CARGO_DIMENSION_IS_NULL;
import static delivery.ExceptionMessage.DELIVERY_WORKLOAD_IS_NULL;
import static delivery.ExceptionMessage.DISTANCE_IS_NEGATIVE_NUMBER;
import static delivery.ExceptionMessage.LONG_DISTANCE_FOR_FRAGILE_CARGO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculateDeliveryCostTest {

    @Tag("positive")
    @ParameterizedTest(name = "{0}")
    @MethodSource("testDataPositive")
    @DisplayName("Check delivery cost:")
    void positiveTest(String checkTitle,
                      int distance,
                      CargoDimension cargoDimension,
                      boolean isFragile,
                      DeliveryWorkload deliveryWorkload,
                      double expectedDeliveryCost) {
        var actualDeliveryCost = CalculationDeliveryCost.getDeliveryCost(distance, cargoDimension, isFragile, deliveryWorkload);
        assertEquals(expectedDeliveryCost, actualDeliveryCost);
    }

    @Tag("negative")
    @ParameterizedTest(name = "{0}")
    @MethodSource("testDataNegative")
    @DisplayName("Check exception:")
    void negativeTest(String checkTitle,
            Class<? extends RuntimeException> exception,
                      int distance,
                      CargoDimension cargoDimension,
                      DeliveryWorkload deliveryWorkload) {
        assertThrows(exception,
                () -> CalculationDeliveryCost.getDeliveryCost(distance, cargoDimension, true, deliveryWorkload));
    }

    private static Stream<Arguments> testDataPositive() {
        return Stream.of(
                Arguments.of("delivery ratio = 1, large cargo", 40, LARGE, false, NORMAL, 500),
                Arguments.of("delivery ratio = 1.2, large cargo", 40, LARGE, false, ELEVATED, 600),
                Arguments.of("delivery ratio = 1.4, large cargo", 40, LARGE, false, HIGH, 700),
                Arguments.of("delivery ratio = 1.6 and distance > 30 km, large cargo", 31, LARGE, false, VERY_HIGH, 800),

                Arguments.of("distance = 30 km, large cargo", 30, LARGE, true, VERY_HIGH, 1120),
                Arguments.of("10 km < distance < 30km, large cargo", 11, LARGE, true, VERY_HIGH, 1120),
                Arguments.of("distance = 10 km, large cargo", 10, LARGE, true, VERY_HIGH, 960),
                Arguments.of("2 km < distance < 10km, large cargo", 3, LARGE, true, VERY_HIGH, 960),
                Arguments.of("distance = 2 km, large cargo", 2, LARGE, true, VERY_HIGH, 880),
                Arguments.of("0 km <= distance < 2 km, large cargo", 1, LARGE, true, VERY_HIGH, 880),

                Arguments.of("small cargo", 40, SMALL, false, VERY_HIGH, 640),
                Arguments.of("fragile cargo", 20, SMALL, true, NORMAL, 600),

                Arguments.of("min delivery cost is 400", 5, SMALL, false, NORMAL, 400)
        );
    }

    private static Stream<Arguments> testDataNegative() {
        return Stream.of(
                Arguments.of(DISTANCE_IS_NEGATIVE_NUMBER, IllegalArgumentException.class, -1, LARGE, VERY_HIGH),
                Arguments.of(CARGO_DIMENSION_IS_NULL, NullPointerException.class, 31, null, VERY_HIGH),
                Arguments.of(DELIVERY_WORKLOAD_IS_NULL, NullPointerException.class, 31, SMALL, null),
                Arguments.of(LONG_DISTANCE_FOR_FRAGILE_CARGO, IllegalArgumentException.class, 31, SMALL, VERY_HIGH)
        );
    }
}
