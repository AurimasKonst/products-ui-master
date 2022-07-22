package lt.bit.products.ui.service.error;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

import lt.bit.products.ui.model.User;
import lt.bit.products.ui.model.UserProfile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UserValidator {

  public void validate(User user) throws ValidationException {
    validateFields(user);
    validatePasswords(user.getPassword(), user.getConfirmedPassword(), user.getId() == null);
  }

  public void validate(UserProfile profile) throws ValidationException {
    if (isBlank(profile.getName())
        || isBlank(profile.getAddress())
        || isBlank(profile.getEmail())
        || isBlank(profile.getPhone())) {
      throw new ValidationException(ErrorCode.ALL_FIELDS_REQUIRED);
    }
  }

  private void validateFields(User user) throws ValidationException {
    if (isBlank(user.getUsername()) || isNull(user.getRole()) || isNull(user.getStatus())) {
      throw new ValidationException(ErrorCode.ALL_FIELDS_REQUIRED);
    }
  }

  private void validatePasswords(String newPassword, String confirmedPassword, boolean newUser)
      throws ValidationException {

    if (StringUtils.hasLength(newPassword) && !newPassword.equals(confirmedPassword)) {
      throw new ValidationException(ErrorCode.PASSWORDS_DO_NOT_MATCH);
    } else if (newUser && !StringUtils.hasLength(newPassword)) {
      throw new ValidationException(ErrorCode.PASSWORD_REQUIRED);
    }
  }

}
