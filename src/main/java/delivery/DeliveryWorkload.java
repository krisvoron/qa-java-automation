package delivery;

public enum DeliveryWorkload {
    VERY_HIGH(1.6),
    HIGH(1.4),
    ELEVATED(1.2),
    NORMAL(1.0);

    private final double deliveryRatio;

    DeliveryWorkload(final double deliveryRatio) {
        this.deliveryRatio = deliveryRatio;
    }

    public double getDeliveryRatio() {
        return deliveryRatio;
    }
}
