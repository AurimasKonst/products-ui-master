package lt.bit.products.ui.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lt.bit.products.ui.model.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

@Service
@SessionAttributes("cartItems")
public class CartService {

  private static final Logger LOG = LoggerFactory.getLogger(CartService.class);
  private Map<UUID, CartItem> cartItems = new HashMap<>();

  public void addToCart(UUID productId, String productName, BigDecimal productPrice) {
    CartItem item;
    if (cartItems.containsKey(productId)) {
      item = cartItems.get(productId);
      item.setCount(item.getCount() + 1);
    } else {
      item = new CartItem(productId, productName, productPrice, 1);
    }
    cartItems.put(productId, item);
    LOG.info("Cart: " + cartItems);
  }

  public List<CartItem> getCartItems() {
    return this.cartItems.values().stream()
        .sorted(Comparator.comparing(CartItem::getProductName))
        .collect(Collectors.toList());
  }
}