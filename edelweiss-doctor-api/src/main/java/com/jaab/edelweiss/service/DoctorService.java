package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Doctor;
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

    private static final String PATIENT_API_URL = "http://localhost:8082/physician";

    private static final String PRESCRIPTION_API_URL = "http://localhost:8085/physician";

    private static final String APPOINTMENT_API_URL = "http://localhost:8086/physician";

    @Autowired
    public DoctorService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Autowired
    public void setDoctorRepository(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
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
     * Creates a new prescription and sends it to the prescription API
     * @param prescriptionDTO - the PrescriptionDTO payload
     * @param id - the ID of the doctor
     * @return - the new prescription
     */
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO, Long id) {
        Doctor doctor = doctorRepository.getReferenceById(id);
        prescriptionDTO.setDoctorFirstName(doctor.getFirstName());
        prescriptionDTO.setDoctorLastName(doctor.getLastName());

        return webClient.post()
                .uri(PRESCRIPTION_API_URL + "/newPrescription")
                .body(Mono.just(prescriptionDTO), PrescriptionDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PrescriptionDTO.class)
                .block();
    }

    /**
     * Creates a new appointment and sends it to the appointment API
     * @param appointmentDTO - the AppointmentDTO payload
     * @param id - the ID of the doctor
     * @return - the new appointment
     */
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO, Long id) {
        Doctor doctor = doctorRepository.getReferenceById(id);
        appointmentDTO.setDoctorFirstName(doctor.getFirstName());
        appointmentDTO.setDoctorLastName(doctor.getLastName());

        return webClient.post()
                .uri(APPOINTMENT_API_URL + "/newAppointment")
                .body(Mono.just(appointmentDTO), AppointmentDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AppointmentDTO.class)
                .block();
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
     * Retrieves a list of patients from patient API based on their first name.
     * @param firstName - the first name of the patient
     * @return - the PatientDTO Set from the patient API
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
     * Retrieves a list of patients from patient API based on their last name.
     * @param lastName - the last name of the patient
     * @return - the PatientDTO Set from the patient API
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
     * Sends UserDTO object to the user API
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
