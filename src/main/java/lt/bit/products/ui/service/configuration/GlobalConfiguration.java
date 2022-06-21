package lt.bit.products.ui.service.configuration;

import lt.bit.products.ui.service.SupplierService;
import lt.bit.products.ui.service.SupplierServiceH2;
import lt.bit.products.ui.service.SupplierServiceMock;
import lt.bit.products.ui.service.domain.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class GlobalConfiguration {

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Primary
  @Bean("supplierServiceH2")
  SupplierService supplierServiceH2(SupplierRepository repository, ModelMapper mapper) {
    return new SupplierServiceH2(repository, mapper);
  }

  @Bean("supplierServiceMock")
  SupplierService supplierServiceMock() {
    return new SupplierServiceMock();
  }
}
