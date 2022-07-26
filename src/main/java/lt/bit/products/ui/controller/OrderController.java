package lt.bit.products.ui.controller;

import static lt.bit.products.ui.controller.ControllerBase.ADMIN_PATH;
import static lt.bit.products.ui.controller.OrderController.ORDERS_PATH;

import javax.servlet.http.HttpServletRequest;
import lt.bit.products.ui.service.OrderService;
import lt.bit.products.ui.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ADMIN_PATH + ORDERS_PATH)
class OrderController extends ControllerBase {

  protected static final String ORDERS_PATH = "/orders";
  private final OrderService service;
  private final UserService userService;

  OrderController(OrderService service, UserService userService) {
    this.service = service;
    this.userService = userService;
  }

  @GetMapping
  String showOrders(Model model, HttpServletRequest request) {
    if (!userService.isAuthenticated()) {
      return "login";
    }

    model.addAttribute("orders", service.getOrders());
    return "admin/orderList";
  }
}
