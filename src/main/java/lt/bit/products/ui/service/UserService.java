package lt.bit.products.ui.service;

import java.security.AccessControlException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lt.bit.products.ui.model.User;
import lt.bit.products.ui.model.UserProfile;
import lt.bit.products.ui.service.domain.UserEntity;
import lt.bit.products.ui.service.domain.UserProfileEntity;
import lt.bit.products.ui.service.domain.UserProfileRepository;
import lt.bit.products.ui.service.domain.UserRepository;
import lt.bit.products.ui.service.domain.UserRole;
import lt.bit.products.ui.service.domain.UserStatus;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.SessionAttributes;

@Service
@Transactional
@SessionAttributes({"authenticated", "admin", "userId", "userName"})
public class UserService {

  private final UserRepository repository;
  private final UserProfileRepository profileRepository;
  private final ModelMapper mapper;

  private boolean authenticated;
  private boolean admin;
  private Integer currentUserId;
  private String currentUsername;

  public UserService(UserRepository repository,
      UserProfileRepository profileRepository, ModelMapper mapper) {
    this.repository = repository;
    this.profileRepository = profileRepository;
    this.mapper = mapper;
  }

  public void login(String username, String password) {
    Optional<UserEntity> user = repository.findByUsernameAndPassword(username, password);
    user.ifPresent(u -> {
      setAuthenticated(true);
      setAdmin(u.getRole() == UserRole.ADMIN);
      setCurrentUserId(u.getId());
      setCurrentUsername(u.getUsername());
      repository.updateLastLoginTime(LocalDateTime.now());
    });
  }

  public void logout() {
    setAuthenticated(false);
    setAdmin(false);
    setCurrentUserId(null);
    setCurrentUsername(null);
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  private void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  public boolean isAdmin() {
    return admin;
  }

  private void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public Integer getCurrentUserId() {
    return currentUserId;
  }

  private void setCurrentUserId(Integer currentUserId) {
    this.currentUserId = currentUserId;
  }

  public String getCurrentUsername() {
    return currentUsername;
  }

  private void setCurrentUsername(String currentUsername) {
    this.currentUsername = currentUsername;
  }

  public List<User> getUsers() {
    List<UserEntity> users = repository.findAll();
    // @formatter:off
    return mapper.map(users, new TypeToken<List<User>>() {}.getType());
    // @formatter:on
  }

  public User getUser(Integer id) {
    Optional<UserEntity> user = repository.findById(id);
    if (user.stream().anyMatch(u -> u.getRole() == UserRole.ADMIN)) {
      throw new AccessControlException("permission.error.ADMIN_USER_EDITING");
    }
    return user.map(u -> mapper.map(u, User.class)).orElseThrow();
  }

  public UserProfile getUserProfile(Integer userId) {
    return mapper.map(profileRepository.findById(userId).orElseGet(UserProfileEntity::new), UserProfile.class);
  }

  public void saveUserProfile(UserProfile updatedProfile) {
    UserProfileEntity profileEntity = mapper.map(updatedProfile, UserProfileEntity.class);
    profileEntity.setUserId(getCurrentUserId());
    profileRepository.save(profileEntity);
  }

  public void saveUser(User user) {
    UserEntity savedUser = repository.save(mapper.map(user, UserEntity.class));
    UserProfileEntity profile = mapper.map(user.getProfile(), UserProfileEntity.class);
    profile.setUserId(savedUser.getId());
    profileRepository.save(profile);
  }

  public void deleteUser(Integer id) {
    Optional<UserEntity> user = repository.findById(id);
    if (user.stream().anyMatch(u -> u.getRole() == UserRole.ADMIN)) {
      throw new AccessControlException("permission.error.ADMIN_USER_DELETION");
    }
    repository.deleteById(id);
  }

  public void changeStatus(UserStatus newStatus, Integer userId) {
    repository.updateStatus(newStatus, userId);
  }
}
