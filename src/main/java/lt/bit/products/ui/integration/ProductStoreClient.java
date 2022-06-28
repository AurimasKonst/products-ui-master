package lt.bit.products.ui.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductStoreClient {

  @Value("${endpoint.productstore.products_count}")
  private String productsCountUrl;

  private final RestTemplate restTemplate;

  ProductStoreClient(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Long getProductsCount() {
    return restTemplate.exchange(productsCountUrl, HttpMethod.GET, null, Long.class).getBody();
  }
}
