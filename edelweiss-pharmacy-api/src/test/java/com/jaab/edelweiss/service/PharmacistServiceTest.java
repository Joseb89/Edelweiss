package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PharmacistRepository;
import com.jaab.edelweiss.exception.PharmacistNotFoundException;
import com.jaab.edelweiss.model.Pharmacist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.jaab.edelweiss.utils.TestUtils.createPharmacist;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PharmacistServiceTest {

    @InjectMocks
    private PharmacistService pharmacistService;

    @Mock
    private PharmacistRepository pharmacistRepository;

    private Pharmacist pharmacist;

    @BeforeEach
    void init() {
        pharmacist = createPharmacist();
    }

    @Test
    public void createPharmacistTest() {
        Pharmacist newPharmacist = pharmacistService.createPharmacist(pharmacist);

        assertEquals("Alistair", newPharmacist.getFirstName());
        assertEquals("Theirin", newPharmacist.getLastName());
    }

    @Test
    public void updatePharmacistInfoTest() {
        assertEquals("kingofferelden@yahoo.com", pharmacist.getEmail());
        assertEquals("sonofmaric", pharmacist.getPassword());

        Map<String, Object> fields = new HashMap<>();
        fields.put("email", "greywarden@gmail.com");
        fields.put("password", "duncansheir");

        when(pharmacistRepository.findById(anyLong())).thenReturn(Optional.of(pharmacist));

        Pharmacist updatedPharmacist = pharmacistService.updatePharmacistInfo(pharmacist.getId(), fields);

        assertEquals("greywarden@gmail.com", updatedPharmacist.getEmail());
        assertEquals("duncansheir", updatedPharmacist.getPassword());
    }

    @Test
    public void deletePharmacistTest() {
        when(pharmacistRepository.findById(anyLong())).thenReturn(Optional.of(pharmacist));

        pharmacistService.deletePharmacist(pharmacist.getId());

        verify(pharmacistRepository, times(1)).deleteById(pharmacist.getId());
    }

    @Test
    public void deletePharmacistExceptionTest() {
        assertThrows(PharmacistNotFoundException.class,
                () -> pharmacistService.deletePharmacist(pharmacist.getId()));
    }
}
