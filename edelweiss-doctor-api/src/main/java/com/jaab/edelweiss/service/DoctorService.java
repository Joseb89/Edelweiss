package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.exception.AppointmentException;
import com.jaab.edelweiss.exception.DoctorNotFoundException;
import com.jaab.edelweiss.model.Doctor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class serves as a service for creating new physicians and maintaining their data
 *
 * @author Joseph Barr
 */
@Service
public class DoctorService {

    public final WebClient webClient;

    private DoctorRepository doctorRepository;

    private static final String USER_API_URL = "http://localhost:8081";

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
     * Creates a new appointment and sends it to the appointment API
     * @param newAppointment - the AppointmentDTO payload
     * @param physicianId - the ID of the doctor
     * @return - the new appointment
     */
    public Mono<AppointmentDTO> createAppointment(AppointmentDTO newAppointment, Long physicianId) {
        if (newAppointment.getAppointmentDate().isBefore(LocalDate.now()) &&
                newAppointment.getAppointmentTime().isBefore(LocalTime.now()))
            throw new AppointmentException("Appointment date must be today or later date.");

        String[] doctorName = setDoctorName(physicianId);

        newAppointment.setDoctorFirstName(doctorName[0]);
        newAppointment.setDoctorLastName(doctorName[1]);

        return webClient.post()
                .uri(APPOINTMENT_API_URL + "/newAppointment")
                .body(Mono.just(newAppointment), AppointmentDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AppointmentDTO.class);
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
        UserDTO updatedUser = updateDoctorInfo(doctor, physicianId);

        if (updatedUser == null) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(doctor, userDTO);
            return Mono.just(userDTO);
        }

        return webClient.patch()
                .uri(USER_API_URL + "/updateUserInfo")
                .body(Mono.just(updatedUser), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UserDTO.class);
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
     * Deletes a doctor from the doctor database and sends a DELETE request to the user API to delete the user
     * with the corresponding ID
     * @param physicianId - the ID of the doctor
     * @return - the DELETE request
     */
    public Mono<Void> deleteUser(Long physicianId) {
        deleteDoctor(physicianId);

        return deleteRequest(USER_API_URL + "/deleteUser/", physicianId);
    }

    /**
     * Sends a DELETE request to the appointment API to delete the appointment with the specified ID
     * @param appointmentId - the ID of the appointment
     * @return - the DELETE request
     */
    public Mono<Void> deleteAppointment(Long appointmentId) {
        return deleteRequest(APPOINTMENT_API_URL + "/deleteAppointment/", appointmentId);
    }

    /**
     * Deletes a doctor from the doctor database based on their ID and throws an exception if the specified
     * doctor is not found
     * @param physicianId - the ID of the doctor
     */
    private void deleteDoctor(Long physicianId){
        Optional<Doctor> doctor = doctorRepository.getDoctorById(physicianId);

        if (doctor.isEmpty())
            throw new DoctorNotFoundException("No doctor with specified ID found.");

        doctorRepository.deleteById(doctor.get().getId()); }

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

        updateDoctorIfNotNull(doctor.getLastName(), getDoctor::getLastName,
                getDoctor::setLastName, userDTO::setLastName);

        updateDoctorIfNotNull(doctor.getEmail(), getDoctor::getEmail,
                getDoctor::setEmail, userDTO::setEmail);

        updateDoctorIfNotNull(doctor.getPassword(), getDoctor::getPassword,
                getDoctor::setPassword, userDTO::setPassword);

        updateDoctorIfNotNull(doctor.getPhoneNumber(), getDoctor::getPhoneNumber, getDoctor::setPhoneNumber);

        doctorRepository.save(getDoctor);

        if (userDTOIsNull(userDTO))
            return null;

        return userDTO;
    }

    /**
     * Checks if an entity attribute is not null and does not equal the current entity value.
     * If so, it sets the attribute of the entity to the checked attribute
     * @param attribute - the attribute to check and set if it's not null
     * @param supplier - the value of the entity to check if it's equal to the new value
     * @param entity - the entity(s) to update
     * @param <T> - the type of the attribute to check
     */
    @SafeVarargs
    private <T> void updateDoctorIfNotNull(T attribute, Supplier<T> supplier, Consumer<T>... entity) {
        Predicate<T> predicate = input -> !input.equals(attribute);

        if (attribute != null && predicate.test(supplier.get()))
            Arrays.stream(entity).forEach(c -> c.accept(attribute));
    }

    /**
     * Checks to see if the values of the UserDTO object are null
     * @param userDTO - the UserDTO object to check
     * @return - true if specified values are null
     */
    private boolean userDTOIsNull(UserDTO userDTO){
        return userDTO.getLastName() == null &&
                userDTO.getEmail() == null &&
                userDTO.getPassword() == null;
    }

    /**
     * Returns the DELETE request of the corresponding endpoint
     * @param uri - the endpoint URI
     * @param id - the ID of the resource to delete
     * @return - the DELETE request
     */
    private Mono<Void> deleteRequest(String uri, Long id) {
        return webClient.delete()
                .uri(uri + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(Void.class);
    }

    /**
     * Sends a UserDTO payload to the user API and returns the user ID
     * @param userDTO - the userDTO object
     * @return - the userDTO payload
     */
    private UserDTO sendUserData(UserDTO userDTO) {
        return webClient.post()
                .uri(USER_API_URL + "/newPhysician")
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
