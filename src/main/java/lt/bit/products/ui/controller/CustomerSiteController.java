package lt.bit.products.ui.controller;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;
import lt.bit.products.ui.model.User;
import lt.bit.products.ui.model.UserProfile;
import lt.bit.products.ui.service.CartService;
import lt.bit.products.ui.service.UserService;
import lt.bit.products.ui.service.domain.UserRole;
import lt.bit.products.ui.service.domain.UserStatus;
import lt.bit.products.ui.service.error.UserValidator;
import lt.bit.products.ui.service.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
class CustomerSiteController {

  private final static Logger LOG = LoggerFactory.getLogger(CustomerSiteController.class);
  private final CartService cartService;
  private final UserService userService;
  private final UserValidator userValidator;
  private final MessageSource messages;

  CustomerSiteController(CartService cartService, UserService userService,
      UserValidator userValidator, MessageSource messages) {
    this.cartService = cartService;
    this.userService = userService;
    this.userValidator = userValidator;
    this.messages = messages;
  }

  @PostMapping("/cart/add")
  @ResponseBody
  ModelAndView addToCart(@RequestParam UUID productId, @RequestParam String productName,
      @RequestParam BigDecimal productPrice) {
    cartService.addToCart(productId, productName, productPrice);
    cartService.getCartAmount();
    return getCartItemsWithModelAndView();
  }

  @PostMapping("/cart/items/count")
  @ResponseBody
  ModelAndView updateItemCount(@RequestParam UUID productId, @RequestParam Integer itemCount) {
    cartService.updateItemCount(productId, itemCount);
    cartService.getCartAmount();
    return getCartItemsWithModelAndView();
  }

  @GetMapping("/cart/{id}/remove")
  @ResponseBody
  int removeFromCart(@PathVariable("id") UUID productId) {
    cartService.removeFromCart(productId);
    return cartService.getTotalItems();
  }

  @GetMapping("/cart/amount")
  @ResponseBody
  BigDecimal getCartAmount() {
    return cartService.getCartAmount();
  }

  private ModelAndView getCartItemsWithModelAndView() {
    ModelAndView mv = new ModelAndView("cartItems");
    mv.addObject("cartItems", cartService.getCartItems());
    return mv;
  }

  @GetMapping("/cart/checkout")
  String showCheckoutForm(Model model) {
    model.addAttribute("cartItems", cartService.getCartItems());
    UserProfile userProfile = userService.isAuthenticated()
        ? userService.getUserProfile(userService.getCurrentUserId())
        : new UserProfile();
    model.addAttribute("cartAmount", cartService.getCartAmount());
    model.addAttribute("profileData", userProfile);
    model.addAttribute("authenticated", userService.isAuthenticated());
    model.addAttribute("currentUsername", userService.getCurrentUsername());
    return "checkoutForm";
  }

  @GetMapping("/register")
  String showRegistrationForm(Model model) {
    model.addAttribute("user", new User());
    return "registrationForm";
  }

  @PostMapping("/register")
  String submitRegistrationForm(@ModelAttribute User newUser, Model model) {
    newUser.setRole(UserRole.USER);
    newUser.setStatus(UserStatus.INACTIVE);
    userService.saveUser(newUser);
    return "redirect:/";
  }

  @GetMapping("/profile")
  String showProfile(Model model) {
    if (!userService.isAuthenticated()) {
      return "login";
    }
    Integer currentUserId = userService.getCurrentUserId();
    UserProfile profile = userService.getUserProfile(currentUserId);
    model.addAttribute("profileData", profile);
    model.addAttribute("authenticated", userService.isAuthenticated());
    model.addAttribute("currentUsername", userService.getCurrentUsername());
    return "profile";
  }

  @PostMapping("/profile")
  String submitRegistrationForm(@ModelAttribute UserProfile updatedProfile, Model model) {
    try {
      userValidator.validate(updatedProfile);
      userService.saveUserProfile(updatedProfile);
    } catch (ValidationException e) {
      model.addAttribute("errorMsg", messages.getMessage("validation.error." + e.getCode(), null, Locale.getDefault()));
      model.addAttribute("profileData", updatedProfile);
      return "profile";
    }
    return "redirect:/";
  }
}
