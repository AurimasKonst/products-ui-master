package lt.bit.products.ui.service;

import java.util.List;
import java.util.UUID;
import lt.bit.products.ui.model.Supplier;

public class SupplierServiceMock implements SupplierService {

  private static final List<Supplier> MOCKED_SUPPLIERS = List.of(
      new Supplier(UUID.randomUUID(), "S1-Mock"),
      new Supplier(UUID.randomUUID(), "S2-Mock"));

  @Override
  public List<Supplier> getSuppliers() {
    return MOCKED_SUPPLIERS;
  }
}
