package delivery;

public class ExceptionMessage {
    public final static String CARGO_DIMENSION_IS_NULL = "Cargo size cannot be empty";
    public final static String DELIVERY_WORKLOAD_IS_NULL = "Delivery workload cannot be empty";
    public final static String LONG_DISTANCE_FOR_FRAGILE_CARGO = "Fragile cargo cannot be transported for a distance over than 30 km";
    public static final String SHORT_DISTANCE = "Distance should be more than 0";
    public static final String LONG_DISTANCE = "Distance is not available. ";
}
