package lt.bit.products.ui.controller;

import static lt.bit.products.ui.controller.ControllerBase.ADMIN_PATH;
import static lt.bit.products.ui.controller.UserController.USERS_PATH;

import java.security.AccessControlException;
import java.util.List;
import java.util.Locale;
import lt.bit.products.ui.model.User;
import lt.bit.products.ui.service.UserService;
import lt.bit.products.ui.service.domain.UserRole;
import lt.bit.products.ui.service.domain.UserStatus;
import lt.bit.products.ui.service.error.ValidationException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(ADMIN_PATH + USERS_PATH)
class UserController extends ControllerBase {

  static final String USERS_PATH = "/users";
  private final UserService userService;
  private final MessageSource messages;

  UserController(UserService userService, MessageSource messages) {
    this.userService = userService;
    this.messages = messages;
  }

  @GetMapping
  String showUsers(Model model) {
    if (!userService.isAuthenticated()) {
      return "login";
    }

    List<User> users = userService.getUsers();

    model.addAttribute("userItems", users);
    return "admin/userList";
  }

  @GetMapping("/{id}")
  String editUser(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
    if (!userService.isAuthenticated()) {
      return "login";
    }

    try {
      model.addAttribute("user", userService.getUser(id));
    } catch (AccessControlException e) {
      redirectAttributes.addFlashAttribute("errorMsg", messages.getMessage(e.getMessage(), null, Locale.getDefault()));
      return "redirect:" + ADMIN_PATH + USERS_PATH;
    }
    model.addAttribute("roles", UserRole.values());
    model.addAttribute("statuses", UserStatus.values());
    return "admin/userForm";
  }

  @GetMapping("/add")
  String addUser(Model model) {
    if (!userService.isAuthenticated()) {
      return "login";
    }
    model.addAttribute("user", new User());
    model.addAttribute("roles", UserRole.values());
    model.addAttribute("statuses", UserStatus.values());
    return "admin/userForm";
  }

  @PostMapping("/save")
  String saveUser(@ModelAttribute User editedUser, Model model) throws ValidationException {
    User existingUser = userService.getUser(editedUser.getId());
    existingUser.setUsername(editedUser.getUsername());
    existingUser.setRole(editedUser.getRole());
    existingUser.setStatus(editedUser.getStatus());
    userService.saveUser(existingUser);
    return "redirect:" + ADMIN_PATH + USERS_PATH;
  }

  @GetMapping("/delete")
  String deleteUser(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
    if (!userService.isAuthenticated()) {
      return "login";
    }

    try {
      userService.deleteUser(id);
    } catch (AccessControlException e) {
      redirectAttributes.addFlashAttribute("errorMsg", messages.getMessage(e.getMessage(), null, Locale.getDefault()));
    }
    return "redirect:" + ADMIN_PATH + USERS_PATH;
  }
}
