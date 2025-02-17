package delivery;

public enum CargoDimension {
    LARGE(200.0), SMALL(100.0);

    private final double cargoDimensionPrice;

    CargoDimension(final double cargoDimensionPrice) {
        this.cargoDimensionPrice = cargoDimensionPrice;
    }

    public double getCargoDimensionPrice() {
        return cargoDimensionPrice;
    }
}
