package lt.bit.products.ui.service.error;

import lt.bit.products.ui.model.Product;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ProductValidator {

  public void validate(Product product) throws ValidationException {
    validateName(product.getName());
    validateQuantity(product.getQuantity());
  }

  private void validateName(String name) throws ValidationException {
    if (!StringUtils.hasLength(name)) {
      throw new ValidationException(ErrorCode.EMPTY_VALUE, new Object[]{"Name"});
    }

    if (name.length() < 3) {
      throw new ValidationException(ErrorCode.VALUE_TOO_SHORT, new Object[]{"Name", 3});
    }
  }

  private void validateQuantity(double quantity) throws ValidationException {
    if (quantity < 0) {
      throw new ValidationException(ErrorCode.NEGATIVE_NUMBER, new Object[]{"Quantity"});
    }
  }
}
