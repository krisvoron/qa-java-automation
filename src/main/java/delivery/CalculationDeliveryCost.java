package delivery;

import static delivery.ExceptionMessage.CARGO_DIMENSION_IS_NULL;
import static delivery.ExceptionMessage.DELIVERY_WORKLOAD_IS_NULL;
import static delivery.ExceptionMessage.LONG_DISTANCE;
import static delivery.ExceptionMessage.LONG_DISTANCE_FOR_FRAGILE_CARGO;
import static delivery.ExceptionMessage.SHORT_DISTANCE;

public class CalculationDeliveryCost {
    private static final double MIN_DELIVERY_COST = 400.0;

    public static double getDeliveryCost(final int distance,
                                  final CargoDimension cargoDimension,
                                  final boolean isFragile,
                                  final DeliveryWorkload deliveryWorkload) {
        if (cargoDimension == null)
            throw new NullPointerException(CARGO_DIMENSION_IS_NULL);
        if (deliveryWorkload == null)
            throw new NullPointerException(DELIVERY_WORKLOAD_IS_NULL);
        if (isFragile && distance > 30)
            throw new IllegalArgumentException(LONG_DISTANCE_FOR_FRAGILE_CARGO);
        double deliveryCost = (getDistanceCost(distance) +
                cargoDimension.getCargoDimensionPrice() +
                getFragileCost(isFragile)) * deliveryWorkload.getDeliveryRatio();
        return Math.max(deliveryCost, MIN_DELIVERY_COST);
    }

    private static double getDistanceCost(final int distance) {
        // В задаче нет ограничения сверху на расстояние. Предположим, что доставка доступна в пределах города
        if (distance > 100) throw new IllegalArgumentException(LONG_DISTANCE);
        if (distance > 30) return 300.0;
        if (distance > 10) return 200.0;
        if (distance > 2) return 100.0;
        if (distance > 0) return 50.0;
        throw new IllegalArgumentException(SHORT_DISTANCE);
    }

    private static double getFragileCost(final boolean isFragile) {
        return isFragile ? 300.0 : 0.0;
    }
}