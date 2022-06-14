package lt.bit.products.ui.service;

import java.util.List;
import lt.bit.products.ui.model.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SupplierService {

  public List<Supplier> getSuppliers() {
    return List.of();
  }
}
