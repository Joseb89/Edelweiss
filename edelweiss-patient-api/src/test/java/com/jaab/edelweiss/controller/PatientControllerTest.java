package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.service.PatientService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PatientController.class)
public class PatientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PatientService patientService;

    private Patient james, bethany, carver;

    private Address jamesAddress, bethanyAddress, carverAddress;

    @BeforeEach
    public void init() {
        Assertions.assertNotNull(webTestClient);
        Assertions.assertNotNull(patientService);

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
    public void createPatientTest() {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(james, userDTO);

        when(patientService.createPatient(any(Patient.class))).thenReturn(userDTO);

        webTestClient.post()
                .uri("/newPatient")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Long.class).isEqualTo(1L);
    }

    @Test
    public void getPatientByIdTest() {
        PatientDTO patientDTO = new PatientDTO();
        BeanUtils.copyProperties(james, patientDTO);

        when(patientService.getPatientById(anyLong())).thenReturn(patientDTO);

        webTestClient.get()
                .uri("/physician/getPatientById/" + patientDTO.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.firstName", Matchers.is("James"));
    }

    @Test
    public void getPatientByIdExceptionTest() {
        webTestClient.get()
                .uri("/physician/getPatientById/" + 4L)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void getPatientsByFirstNameTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "James";

        when(patientService.getPatientsByFirstName(testParameter))
                .thenReturn(patients.stream()
                                    .filter(n -> Objects.equals(n.getFirstName(), testParameter))
                        .collect(Collectors.toList()));

        webTestClient.get()
                .uri("/physician/getPatientsByFirstName/" + testParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.firstName", Matchers.is(testParameter));
    }

    @Test
    public void getPatientsByFirstNameEmptyListTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "Aveline";

        when(patientService.getPatientsByFirstName(testParameter))
                .thenReturn(patients.stream()
                        .filter(n -> Objects.equals(n.getFirstName(), testParameter))
                        .collect(Collectors.toList()));

        webTestClient.get()
                .uri("/physician/getPatientsByFirstName/" + testParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(0);
    }

    @Test
    public void getPatientsByLastNameTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "Hawke";

        when(patientService.getPatientsByLastName(testParameter))
                .thenReturn(patients.stream()
                        .filter(n -> Objects.equals(n.getLastName(), testParameter))
                        .collect(Collectors.toList()));

        webTestClient.get()
                .uri("/physician/getPatientsByLastName/" + testParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(3);
    }

    @Test
    public void getPatientsByLastNameEmptyListTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "Vallen";

        when(patientService.getPatientsByLastName(testParameter))
                .thenReturn(patients.stream()
                        .filter(n -> Objects.equals(n.getLastName(), testParameter))
                        .collect(Collectors.toList()));

        webTestClient.get()
                .uri("/physician/getPatientsByLastName/" + testParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(0);
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "O-";

        when(patientService.getPatientsByBloodType(testParameter))
                .thenReturn(patients.stream()
                        .filter(n -> Objects.equals(n.getBloodType(), testParameter))
                        .collect(Collectors.toList()));

        webTestClient.get()
                .uri("/physician/getPatientsByBloodType/" + testParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(2);
    }

    @Test
    public void getPatientsByBloodTypeEmptyListTest() {
        List<PatientDTO> patients = setupPatientDTO();
        String testParameter = "B+";

        when(patientService.getPatientsByBloodType(testParameter))
                .thenReturn(patients.stream()
                        .filter(n -> Objects.equals(n.getBloodType(), testParameter))
                        .collect(Collectors.toList()));

        webTestClient.get()
                .uri("/physician/getPatientsByBloodType/" + testParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(0);
    }

    @Test
    public void getAddressTest() {
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(carverAddress, addressDTO);

        when(patientService.getAddress(anyLong())).thenReturn(addressDTO);

        webTestClient.get()
                .uri("/physician/getPatientAddress/" + carverAddress.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.streetAddress", Matchers.is("59 Gallows St"));
    }

    @Test
    public void getAddressEmptyList() {
        webTestClient.get()
                .uri("/physician/getPatientAddress/" + 4L)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    public void updateAddressTest() {
        Address updatedAddress = new Address(carver.getId(), carver, "654 Adamant Ave", "Boise",
                "ID", 96521);

        webTestClient.patch()
                .uri("/patient/" + carver.getId() +"/updateAddress")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedAddress)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.city", Matchers.is("Boise"));
    }

    @Test
    public void updatePatientInfoTest() {
        Patient updatedInfo = new Patient(bethany.getId(), null, "Amell", null,
                "circlemage", bethanyAddress, null, null, null);

        webTestClient.patch()
                .uri("/patient/" + bethany.getId() + "/updatePatientInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedInfo)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.lastName", Matchers.is("Amell"));
    }

    @Test
    public void deletePatientTest() {
        webTestClient.delete()
                .uri("/deleteUser/" + carver.getId())
                .exchange()
                .expectStatus().is4xxClientError();
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
