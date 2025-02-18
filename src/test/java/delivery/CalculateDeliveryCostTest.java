package delivery;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static delivery.CargoDimension.LARGE;
import static delivery.CargoDimension.SMALL;
import static delivery.DeliveryWorkload.ELEVATED;
import static delivery.DeliveryWorkload.HIGH;
import static delivery.DeliveryWorkload.NORMAL;
import static delivery.DeliveryWorkload.VERY_HIGH;
import static delivery.ExceptionMessage.CARGO_DIMENSION_IS_NULL;
import static delivery.ExceptionMessage.DELIVERY_WORKLOAD_IS_NULL;
import static delivery.ExceptionMessage.LONG_DISTANCE;
import static delivery.ExceptionMessage.LONG_DISTANCE_FOR_FRAGILE_CARGO;
import static delivery.ExceptionMessage.SHORT_DISTANCE;
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
    void negativeTest(String exceptionMessage,
            Class<? extends RuntimeException> exception,
                      int distance,
                      CargoDimension cargoDimension,
                      boolean isFragile,
                      DeliveryWorkload deliveryWorkload) {
        Exception actualException = assertThrows(exception,
                () -> CalculationDeliveryCost.getDeliveryCost(distance, cargoDimension, isFragile, deliveryWorkload));
        assertEquals(exceptionMessage, actualException.getMessage());
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
                Arguments.of(SHORT_DISTANCE, IllegalArgumentException.class, -1, LARGE, false, VERY_HIGH),
                Arguments.of(SHORT_DISTANCE, IllegalArgumentException.class, 0, LARGE, false, VERY_HIGH),
                Arguments.of(LONG_DISTANCE, IllegalArgumentException.class, 101, LARGE, false, VERY_HIGH),
                Arguments.of(CARGO_DIMENSION_IS_NULL, NullPointerException.class, 31, null, false, VERY_HIGH),
                Arguments.of(DELIVERY_WORKLOAD_IS_NULL, NullPointerException.class, 31, SMALL, false, null),
                Arguments.of(LONG_DISTANCE_FOR_FRAGILE_CARGO, IllegalArgumentException.class, 31, SMALL, true, VERY_HIGH)
        );
    }
}
