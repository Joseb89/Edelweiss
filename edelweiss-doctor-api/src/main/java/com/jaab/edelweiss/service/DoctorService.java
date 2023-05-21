package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.dao.PrescriptionRepository;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;

@Service
public class DoctorService {

    private final WebClient webClient;

    private DoctorRepository doctorRepository;

    private PrescriptionRepository prescriptionRepository;

    private static final String PATIENT_API_URL = "http://localhost:8082/physician";

    @Autowired
    public DoctorService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Autowired
    public void setDoctorRepository(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Autowired
    public void setPrescriptionRepository(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Saves a new doctor to the doctor database
     * @param doctor - the new doctor
     * @return - the userDTO payload
     */
    public UserDTO createDoctor(Doctor doctor) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(doctor, userDTO);
        UserDTO userData = sendUserData(userDTO);
        doctor.setId(userData.getId());
        doctorRepository.save(doctor);
        return userData;
    }

    /**
     * Saves a new prescription to the pharmacy database
     * @param prescription - the new prescription
     * @param id - the ID of the doctor
     * @return - new prescription
     */
    public Prescription createPrescription(Prescription prescription, Long id) {
        Doctor doctor = doctorRepository.getReferenceById(id);
        prescription.setDoctor(doctor);
        prescription.setDoctorFirstName(doctor.getFirstName());
        prescription.setDoctorLastName(doctor.getLastName());
        prescription.setPrescriptionStatus(Status.PENDING);
        prescriptionRepository.save(prescription);
        return prescription;
    }

    /**
     * Retrieves patient information from patient API based on patient ID
     * @param patientId - the ID of the patient
     * @return - the patient's information
     */
    public Mono<PatientDTO> getPatientDataById(Long patientId) {
        return webClient.get()
                .uri(PATIENT_API_URL + "/getPatientById/" + patientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PatientDTO.class);
    }

    /**
     * Retrieves a list of patients based on their first name.
     * @param firstName - the first name of the patient
     * @return - the PatientDTO set from the patient API
     */
    public Flux<PatientDTO> getPatientDataByFirstName(String firstName) {
        return webClient.get()
                .uri(PATIENT_API_URL + "/getPatientByFirstName/" + firstName)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PatientDTO.class);
    }

    /**
     * Retrieves a list of patients based on their last name.
     * @param lastName - the last name of the patient
     * @return - the PatientDTO set from the patient API
     */
    public Flux<PatientDTO> getPatientDataByLastName(String lastName) {
        return webClient.get()
                .uri(PATIENT_API_URL + "/getPatientByLastName/" + lastName)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PatientDTO.class);
    }

    /**
     * Sends UserDTO object to the Main API
     * @param userDTO - the userDTO object
     * @return - the userDTO payload
     */
    private UserDTO sendUserData(UserDTO userDTO) {
        return webClient.post()
                .uri("http://localhost:8081/newPhysician")
                .body(Mono.just(userDTO), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UserDTO.class)
                .block();
    }
}
