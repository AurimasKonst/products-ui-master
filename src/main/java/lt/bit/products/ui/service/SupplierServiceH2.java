package lt.bit.products.ui.service;

import java.util.List;
import java.util.UUID;
import lt.bit.products.ui.model.Supplier;
import lt.bit.products.ui.service.domain.ProductRepository;
import lt.bit.products.ui.service.domain.SupplierEntity;
import lt.bit.products.ui.service.domain.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SupplierServiceH2 implements SupplierService {

  private final SupplierRepository repository;
  private final ProductRepository productRepository;
  private final ModelMapper mapper;

  public SupplierServiceH2(SupplierRepository repository, ProductRepository productRepository, ModelMapper mapper) {
    this.repository = repository;
    this.productRepository = productRepository;
    this.mapper = mapper;
  }

  public List<Supplier> getSuppliers() {
    List<SupplierEntity> suppliers = repository.findAll();
    // @formatter:off
    return mapper.map(suppliers, new TypeToken<List<Supplier>>() {}.getType());
    // @formatter:on
  }

  @Override
  public Supplier getSupplier(UUID id) {
    return repository.findById(id).map(s -> mapper.map(s, Supplier.class)).orElseThrow();
  }

  @Override
  public void saveSupplier(Supplier supplier) /*throws ValidationException*/ {
    UUID id = supplier.getId();
//    if (id != null && !repository.existsById(id)) {
//      throw new ValidationException(ErrorCode.PRODUCT_NOT_FOUND, id);
//    }
    repository.save(mapper.map(supplier, SupplierEntity.class));
  }

  @Override
  public void deleteSupplier(UUID id) {
    productRepository.deleteAllBySupplierId(id);
    repository.deleteById(id);
  }
}
