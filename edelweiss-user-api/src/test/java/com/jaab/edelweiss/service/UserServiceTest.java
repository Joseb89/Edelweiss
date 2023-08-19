package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.UserRepository;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Role;
import com.jaab.edelweiss.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    AutoCloseable closeable;

    private UserDTO lace;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);

        assertNotNull(userRepository);
        assertNotNull(userService);

        lace = new UserDTO(null, "Lace", "Harding",
                "inquisitionscout@gmail.com", "bowandarrow");
    }

    @Test
    public void createUserTest() {
        User user = new User();
        BeanUtils.copyProperties(lace, user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        Long id = userService.createUser(lace, Role.PATIENT);
        assertEquals(id, user.getId());
    }

    @Test
    public void updateUserInfoTest() {
        UserDTO updatedData = new UserDTO(1L, "Lace", "Trevelyan",
                "inquisitionscout@gmail.com", "mathias");
        User user = new User();
        BeanUtils.copyProperties(lace, user);
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        assertEquals("Harding", user.getLastName());
        assertEquals("bowandarrow", user.getPassword());
        userService.updateUserInfo(updatedData);
        assertEquals("Trevelyan", user.getLastName());
        assertEquals("mathias", user.getPassword());
    }

    @Test
    public void deleteUserTest() {
        Long patientId = 1L;
        userService.deleteUser(patientId);
        verify(userRepository, times(1)).deleteById(patientId);
    }
}
