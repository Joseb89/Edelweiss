package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private WebClient webClient;

    @Mock
    AutoCloseable closeable;

    private Patient james, bethany, carver;

    private Address jamesAddress, bethanyAddress, carverAddress;

    @BeforeAll
    public static void setup() {

    }

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);

        Assertions.assertNotNull(patientService);
        Assertions.assertNotNull(webClient);
        Assertions.assertNotNull(patientRepository);

        james = new Patient(1L, "James", "Hawke", "championofkirkwall@gmail.com",
                "magerebellion", jamesAddress, 7130042356L,
                "Varric Tethras", "O+");

        bethany = new Patient(2L, "Bethany", "Hawke", "circlemage@gmail.com",
                "daughterofamell", bethanyAddress, 7130042357L,
                "Varric Tethras", "O-");

        carver = new Patient(3L, "Carver", "Hawke", "templarknight@gmail.com",
                "sonofamell", carverAddress, 7130042357L,
                "Varric Tethras", "O-");

        jamesAddress = new Address(james.getId(), james, "58 Hightown Court",
                "San Antonio", "TX", 78615);

        bethanyAddress = new Address(bethany.getId(), bethany, "59 Gallows St",
                "San Antonio", "TX", 78615);

        carverAddress = new Address(carver.getId(), carver, "59 Gallows St",
                "San Antonio", "TX", 78615);
    }

    @Test
    public void  createPatientTest() {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(james, userDTO);
        when(patientService.createPatient(james)).thenReturn(userDTO);
        UserDTO userData = patientService.createPatient(james);
        assertThat(userData.getEmail()).isEqualTo("championofkirkwall@gmail.com");
    }

    @Test
    public void getPatientByIdTest() {
        PatientDTO patientDTO = new PatientDTO();
        BeanUtils.copyProperties(james, patientDTO);
        when(patientService.getPatientById(Mockito.any(Long.class))).thenReturn(patientDTO);
        PatientDTO getPatient = patientService.getPatientById(james.getId());
        assertThat(getPatient.getFirstName()).isEqualTo("James");
    }

    @Test
    public void getPatientByIdExceptionTest() {
        Long patientId = 4L;

        when(patientService.getPatientById(patientId)).thenThrow(PatientNotFoundException.class);
        assertThrows(PatientNotFoundException.class, ()-> patientService.getPatientById(patientId));
    }

    @Test
    public void getPatientsByFirstNameTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "Carver";

        when(patientService.getPatientsByFirstName(testParameter)).thenReturn(patients.stream()
                .filter(n -> Objects.equals(n.getFirstName(), testParameter))
                .collect(Collectors.toList()));

        List<PatientDTO> getPatients = patientService.getPatientsByFirstName(testParameter);

        assertThat(getPatients.size()).isEqualTo(1);
    }

    @Test
    public void getPatientsByFirstNameEmptyListTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "Fenris";

        when(patientService.getPatientsByFirstName(testParameter)).thenReturn(patients.stream()
                .filter(n -> Objects.equals(n.getFirstName(), testParameter))
                .collect(Collectors.toList()));

        List<PatientDTO> getPatients = patientService.getPatientsByFirstName(testParameter);

        assertThat(getPatients.size()).isEqualTo(0);
    }

    @Test
    public void getPatientsByLastNameTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "Hawke";

        when(patientService.getPatientsByLastName(testParameter)).thenReturn(patients.stream()
                .filter(n -> Objects.equals(n.getLastName(), testParameter))
                .collect(Collectors.toList()));

        List<PatientDTO> getPatients = patientService.getPatientsByLastName(testParameter);

        assertThat(getPatients.size()).isEqualTo(3);
    }

    @Test
    public void getPatientsByLastNameEmptyListTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "Vallen";

        when(patientService.getPatientsByLastName(testParameter)).thenReturn(patients.stream()
                .filter(n -> Objects.equals(n.getLastName(), testParameter))
                .collect(Collectors.toList()));

        List<PatientDTO> getPatients = patientService.getPatientsByLastName(testParameter);

        assertThat(getPatients.size()).isEqualTo(0);
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "O-";

        when(patientService.getPatientsByBloodType(testParameter)).thenReturn(patients.stream()
                .filter(n -> Objects.equals(n.getBloodType(), testParameter))
                .collect(Collectors.toList()));

        List<PatientDTO> getPatients = patientService.getPatientsByBloodType(testParameter);

        assertThat(getPatients.size()).isEqualTo(2);
    }

    @Test
    public void getPatientsByBloodTypeEmptyListTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "A+";

        when(patientService.getPatientsByBloodType(testParameter)).thenReturn(patients.stream()
                .filter(n -> Objects.equals(n.getBloodType(), testParameter))
                .collect(Collectors.toList()));

        List<PatientDTO> getPatients = patientService.getPatientsByBloodType(testParameter);

        assertThat(getPatients.size()).isEqualTo(0);
    }

    @Test
    public void getAddressTest() {
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(bethanyAddress, addressDTO);
        when(patientService.getAddress(2L)).thenReturn(addressDTO);
        AddressDTO getAddress = patientService.getAddress(2L);
        assertThat(getAddress.getStreetAddress()).isEqualTo("59 Gallows St");
    }

    @Test
    public void getAddressExceptionTest() {
        Long patientId = 4L;

        when(patientService.getAddress(patientId)).thenThrow(PatientNotFoundException.class);
        assertThrows(PatientNotFoundException.class, ()-> patientService.getAddress(patientId));
    }

    @Test
    public void updateAddressTest() {
        Address updatedAddress = new Address(carver.getId(), carver, "654 Adamant Ave", "Boise",
                "ID", 96521);
        carver.setAddress(carverAddress);
        assertThat(carver.getAddress().getCity()).isEqualTo("San Antonio");
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(updatedAddress, addressDTO);
        when(patientService.updateAddress(updatedAddress, carverAddress.getId())).thenReturn(addressDTO);
        AddressDTO newAddress = patientService.updateAddress(updatedAddress, carverAddress.getId());
        assertThat(newAddress.getCity()).isEqualTo("Boise");
    }

    @Test
    public void updateUserInfoTest() {
        Patient updatedPatient = new Patient(2L, null, "Amell", null,
                "circlemage", bethanyAddress, null, null, null);

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(bethany, userDTO);
        assertThat(userDTO.getPassword()).isEqualTo("daughterofamell");
        userDTO.setLastName(updatedPatient.getLastName());
        userDTO.setPassword(updatedPatient.getPassword());
        Mono<UserDTO> updatedData = Mono.just(userDTO);
        when(patientService.updateUserInfo(updatedPatient, bethany.getId())).thenReturn(updatedData);
        Mono<UserDTO> userData = patientService.updateUserInfo(updatedPatient, bethany.getId());
        assertThat(Objects.requireNonNull(userData.block()).getPassword()).isEqualTo("circlemage");
    }

    @Test
    public void deleteUserTest() {
        Patient patient = new Patient();
        BeanUtils.copyProperties(james, patient);
        Mono<Void> deletePatient = patientService.deleteUser(james.getId());
        assertThat(deletePatient).isNull();
        verify(patientService, times(1)).deleteUser(james.getId());
    }

    private List<PatientDTO> setupPatientDTO() {
        PatientDTO jamesDTO = new PatientDTO();
        PatientDTO bethanyDTO = new PatientDTO();
        PatientDTO carverDTO = new PatientDTO();

        BeanUtils.copyProperties(james, jamesDTO);
        BeanUtils.copyProperties(bethany, bethanyDTO);
        BeanUtils.copyProperties(carver, carverDTO);

        List<PatientDTO> patients = new ArrayList<>();

        patients.add(jamesDTO);
        patients.add(bethanyDTO);
        patients.add(carverDTO);

        return patients;
    }
}
