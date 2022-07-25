package lt.bit.products.ui.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lt.bit.products.ui.model.CartItem;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttributes;

@Service
@SessionAttributes("cartItems")
public class CartService {

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
  }

  public void updateItemCount(UUID productId, Integer itemCount) {
    CartItem item = cartItems.get(productId);
    item.setCount(itemCount);
  }

  public List<CartItem> getCartItems() {
    return this.cartItems.values().stream()
        .sorted(Comparator.comparing(CartItem::getProductName))
        .collect(Collectors.toList());
  }

  public void clearCartItems() {
    cartItems.clear();
  }

  public void removeFromCart(UUID productId) {
    cartItems.remove(productId);
  }

  public int getTotalItems() {
    return cartItems.values().stream().mapToInt(CartItem::getCount).sum();
  }

  public BigDecimal getCartAmount() {
    return cartItems.values().stream()
        .map(CartItem::getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
