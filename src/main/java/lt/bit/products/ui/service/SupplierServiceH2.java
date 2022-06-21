package lt.bit.products.ui.service;

import java.util.List;
import lt.bit.products.ui.model.Supplier;
import lt.bit.products.ui.service.domain.SupplierEntity;
import lt.bit.products.ui.service.domain.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SupplierServiceH2 implements SupplierService {

  private final SupplierRepository repository;
  private final ModelMapper mapper;

  public SupplierServiceH2(SupplierRepository repository, ModelMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  public List<Supplier> getSuppliers() {
    List<SupplierEntity> suppliers = repository.findAll();
    // @formatter:off
    return mapper.map(suppliers, new TypeToken<List<Supplier>>() {}.getType());
    // @formatter:on
  }
}
