package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.dto.*;
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

    private static final String USER_API_URL = "http://localhost:8081";

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
     * @param doctor - the new Doctor
     * @return - the UserDTO payload
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
     * @param newPrescription - the PrescriptionDTO payload
     * @param physicianId - the ID of the doctor
     * @return - the new prescription
     */
    public Mono<PrescriptionDTO> createPrescription(PrescriptionDTO newPrescription, Long physicianId) {
        String[] doctorName = setDoctorName(physicianId);
        newPrescription.setDoctorFirstName(doctorName[0]);
        newPrescription.setDoctorLastName(doctorName[1]);

        return webClient.post()
                .uri(PRESCRIPTION_API_URL + "/newPrescription")
                .body(Mono.just(newPrescription), PrescriptionDTO.class)
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
     * Retrieves a patient from the patient API based on the patient's ID
     * @param patientId - the ID of the patient
     * @return - the patient's information from the patient API
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
     * Retrieves a list of patients from the patient API based on their first name
     * @param firstName - the first name of the patient
     * @return - the PatientDTO List from the patient API
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
     * Retrieves a list of patients from the patient API based on their last name
     * @param lastName - the last name of the patient
     * @return - the PatientDTO List from the patient API
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
     * Retrieves a list of patients from the patient API based on their blood type
     * @param bloodType - the blood type of the patient
     * @return - the PatientDTO List from the patient API
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
     * Retrieves the address of the patient with the corresponding ID from the patient API
     * @param patientId - the ID of the patient
     * @return - the patient's address
     */
    public Mono<AddressDTO> getPatientAddress(Long patientId) {
        return webClient.get()
                .uri(PATIENT_API_URL + "/getPatientAddress/" + patientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AddressDTO.class);
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
     * Sends the updated doctor information to the user API
     * @param doctor - the Doctor payload
     * @param physicianId - the ID of the doctor
     * @return - the UserDTO object containing the updated information
     */
    public Mono<UserDTO> updateUserInfo(Doctor doctor, Long physicianId) {
        UserDTO userDTO = updateDoctorInfo(doctor, physicianId);

        return webClient.patch()
                .uri(USER_API_URL + "/updateUserInfo")
                .body(Mono.just(userDTO), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UserDTO.class);
    }

    /**
     * Updates a prescription with the corresponding ID and sends it to the prescription API
     * @param prescriptionDTO - the UpdatePrescriptionDTO payload containing the updated information
     * @param prescriptionId - the ID of the prescription
     * @return - the updated prescription
     */
    public Mono<UpdatePrescriptionDTO> updatePrescriptionInfo(UpdatePrescriptionDTO prescriptionDTO,
                                                        Long prescriptionId) {
        UpdatePrescriptionDTO updatedPrescription = new UpdatePrescriptionDTO();
        BeanUtils.copyProperties(prescriptionDTO, updatedPrescription);
        updatedPrescription.setId(prescriptionId);

        return webClient.patch()
                .uri(PRESCRIPTION_API_URL + "/updatePrescriptionInfo/" + prescriptionId)
                .body(Mono.just(updatedPrescription), UpdatePrescriptionDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UpdatePrescriptionDTO.class);
    }

    /**
     * Updates an appointment with the corresponding ID and sends it to the appointment API
     * @param appointmentDTO - the AppointmentDTO payload containing the updated information
     * @param appointmentId - the ID of the appointment
     * @return - the updated appointment
     */
    public Mono<AppointmentDTO> updateAppointmentInfo(AppointmentDTO appointmentDTO, Long appointmentId) {
        AppointmentDTO updatedAppointment = new AppointmentDTO();
        BeanUtils.copyProperties(appointmentDTO, updatedAppointment);
        updatedAppointment.setId(appointmentId);

        return webClient.patch()
                .uri(APPOINTMENT_API_URL + "/updateAppointmentInfo/" + appointmentId)
                .body(Mono.just(updatedAppointment), AppointmentDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AppointmentDTO.class);
    }

    /**
     * Deletes a doctor from the doctor database and sends a request to the user API to delete the user with
     * the corresponding ID
     * @param physicianId - the ID of the doctor
     * @return - the delete request
     */
    public Mono<Void> deleteUser(Long physicianId) {
        deleteDoctor(physicianId);

        return webClient.delete()
                .uri(USER_API_URL + "/deleteUser/" + physicianId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(Void.class);
    }

    /**
     * Sends a request to the patient API to delete the patient with the specified ID
     * @param patientId - the ID of the patient
     * @return - the delete request
     */
    public Mono<Void> deletePatient(Long patientId) {
        return webClient.delete()
                .uri(PATIENT_API_URL + "/deletePatient/" + patientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(Void.class);
    }

    /**
     * Deletes a doctor from the doctor database based on their ID
     * @param physicianId - the ID of the doctor
     */
    private void deleteDoctor(Long physicianId){ doctorRepository.deleteById(physicianId); }

    /**
     * Retrieves a doctor from the doctor database based on the doctor's ID and sets the doctor's
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
     * Updates the information of the doctor via a Doctor payload, merges it to the doctor database,
     * and stores it in a UserDTO object
     * @param doctor - the Doctor payload
     * @param physicianId - the ID of the doctor
     * @return - the UserDTO object with the updated information
     */
    private UserDTO updateDoctorInfo(Doctor doctor, Long physicianId) {
        Doctor getDoctor = doctorRepository.getReferenceById(physicianId);
        UserDTO userDTO = new UserDTO(physicianId);

        if (doctor.getLastName() != null) {
            getDoctor.setLastName(doctor.getLastName());
            userDTO.setLastName(doctor.getLastName());
        }

        if (doctor.getEmail() != null) {
            getDoctor.setEmail(doctor.getEmail());
            userDTO.setEmail(doctor.getEmail());
        }

        if (doctor.getPassword() != null) {
            getDoctor.setPassword(doctor.getPassword());
            userDTO.setPassword(doctor.getPassword());
        }

        if (doctor.getPhoneNumber() != null)
            getDoctor.setPhoneNumber(doctor.getPhoneNumber());

        doctorRepository.save(getDoctor);

        return userDTO;
    }

    /**
     * Sends a UserDTO payload to the user API and returns the user ID
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
