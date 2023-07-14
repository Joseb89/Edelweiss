package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.UserRepository;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Role;
import com.jaab.edelweiss.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Copies UserDTO payload from external API into new User entity and saves it to the user database
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
     * Updates user information based on UserDTO payload from external API and merges it to the user database
     * @param userDTO - the UserDTO payload from the external API
     * @return - the UserDTO object containing the updated information
     */
    public UserDTO updateUserInfo(UserDTO userDTO) {
        User user = userRepository.getReferenceById(userDTO.getId());
        UserDTO returnUser = new UserDTO();

        if (userDTO.getLastName() != null)
            user.setLastName(userDTO.getLastName());

        if (userDTO.getEmail() != null)
            user.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null)
            user.setPassword(userDTO.getPassword());

        BeanUtils.copyProperties(user, returnUser);

        userRepository.save(user);

        return returnUser;
    }
}
