package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.UserRepository;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Role;
import com.jaab.edelweiss.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Copies a UserDTO payload from external API into a new User object and saves it to the user database
     * @param userDTO - the UserDTO payload from the external API
     * @param role - the user's role
     * @return - the user ID
     */
    public Long createUser(UserDTO userDTO, Role role) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setRole(role);
        userRepository.save(user);
        return user.getId();
    }

    /**
     * Updates user information based on the UserDTO payload from external API and merges it to the user database
     * @param userDTO - the UserDTO payload from the external API
     * @return - the UserDTO object containing the updated information
     */
    public UserDTO updateUserInfo(UserDTO userDTO) {
        User user = userRepository.getReferenceById(userDTO.getId());
        UserDTO returnUser = new UserDTO();

        updateIfUserIsNotNull(user::setLastName, userDTO.getLastName());

        updateIfUserIsNotNull(user::setEmail, userDTO.getEmail());

        updateIfUserIsNotNull(user::setPassword, userDTO.getPassword());

        BeanUtils.copyProperties(user, returnUser);

        userRepository.save(user);

        return returnUser;
    }

    /**
     * Deletes a user from the user database based on their ID
     * @param userId - the ID of the user
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Checks if an entity attribute is not null and sets the attribute of the User entity to the
     * checked attribute if so
     * @param user - the User entity to update
     * @param attribute - the attribute to check and set if it's not null
     * @param <T> - the type of the attribute to check
     */
    private <T> void updateIfUserIsNotNull(Consumer<T> user, T attribute) {
        if (attribute != null)
            user.accept(attribute);
    }
}
