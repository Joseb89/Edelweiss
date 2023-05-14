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
     * Saves a new user to the users database
     * @param userDTO - the user payload from external API
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
}
