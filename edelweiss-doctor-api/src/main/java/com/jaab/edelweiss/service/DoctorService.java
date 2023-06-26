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
     * @param physicianId - the ID of the doctor
     * @return - the new prescription
     */
    public Mono<PrescriptionDTO> createPrescription(PrescriptionDTO prescriptionDTO, Long physicianId) {
        String[] doctorName = setDoctorName(physicianId);
        prescriptionDTO.setDoctorFirstName(doctorName[0]);
        prescriptionDTO.setDoctorLastName(doctorName[1]);

        return webClient.post()
                .uri(PRESCRIPTION_API_URL + "/newPrescription")
                .body(Mono.just(prescriptionDTO), PrescriptionDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PrescriptionDTO.class);
    }

    /**
     * Creates a new appointment and sends it to the appointment API
     * @param appointmentDTO - the AppointmentDTO payload
     * @param physicianId - the ID of the doctor
     * @return - the new appointment
     */
    public Mono<AppointmentDTO> createAppointment(AppointmentDTO appointmentDTO, Long physicianId) {
        String[] doctorName = setDoctorName(physicianId);
        appointmentDTO.setDoctorFirstName(doctorName[0]);
        appointmentDTO.setDoctorLastName(doctorName[1]);

        return webClient.post()
                .uri(APPOINTMENT_API_URL + "/newAppointment")
                .body(Mono.just(appointmentDTO), AppointmentDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AppointmentDTO.class);
    }

    /**
     * Retrieves patient information from patient API based on patient ID
     * @param patientId - the ID of the patient
     * @return - the patient's information
     */
    public Mono<PatientDTO> getPatientById(Long patientId) {
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
     * Retrieves a list of patients from patient API based on their first name
     * @param firstName - the first name of the patient
     * @return - the PatientDTO Set from the patient API
     */
    public Flux<PatientDTO> getPatientsByFirstName(String firstName) {
        return webClient.get()
                .uri(PATIENT_API_URL + "/getPatientsByFirstName/" + firstName)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PatientDTO.class);
    }

    /**
     * Retrieves a list of patients from patient API based on their last name
     * @param lastName - the last name of the patient
     * @return - the PatientDTO Set from the patient API
     */
    public Flux<PatientDTO> getPatientsByLastName(String lastName) {
        return webClient.get()
                .uri(PATIENT_API_URL + "/getPatientsByLastName/" + lastName)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PatientDTO.class);
    }

    /**
     * Retrieves a list of patients from patient API based on their blood type
     * @param bloodType - the blood type of the patient
     * @return - the PatientDTO Set from the patient API
     */
    public Flux<PatientDTO> getPatientsByBloodType(String bloodType) {
        return webClient.get()
                .uri(PATIENT_API_URL + "/getPatientsByBloodType/" + bloodType)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PatientDTO.class);
    }

    /**
     * Retrieves the prescriptions from the prescription API for the doctor with the specified ID
     * @param physicianId - the ID of the doctor
     * @return - the list of the prescriptions
     */
    public Flux<PrescriptionDTO> getPrescriptions(Long physicianId) {
        String[] doctorName = setDoctorName(physicianId);

        return webClient.get()
                .uri(PRESCRIPTION_API_URL + "/myPrescriptions/" + doctorName[0] + "/" + doctorName[1])
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PrescriptionDTO.class);
    }

    /**
     * Retrieves the appointments from the appointment API for the doctor with the specified ID
     * @param physicianId - the ID of the doctor
     * @return - the list of the appointments
     */
    public Flux<AppointmentDTO> getAppointments(Long physicianId) {
        String[] doctorName = setDoctorName(physicianId);

        return webClient.get()
                .uri(APPOINTMENT_API_URL + "/myAppointments/" + doctorName[0] + "/" + doctorName[1])
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(AppointmentDTO.class);
    }

    /**
     * Retrieves a doctor from the doctor database based on the physician ID and sets the doctor's
     * first and last name to a String array
     * @param physicianId - the ID of the doctor
     * @return - the String array containing the doctor's first and last name
     */
    private String[] setDoctorName(Long physicianId) {
        String[] doctorName = new String[2];
        Doctor doctor = doctorRepository.getReferenceById(physicianId);
        doctorName[0] = doctor.getFirstName();
        doctorName[1] = doctor.getLastName();
        return doctorName;
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
