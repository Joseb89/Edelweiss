package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.service.PatientService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This class is a controller for the endpoints that allow doctors to access patient information
 * from the doctor API
 *
 * @author Joseph Barr
 */
@RestController
@RequestMapping(value = "/physician")
public class DoctorPatientController {

    private final PatientService patientService;

    public DoctorPatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Retrieves a patient from the patient database based on the patient's ID
     *
     * @param patientId - the ID of the patient
     * @return - the patient with the specified ID
     */
    @GetMapping(value = "/getPatientById/{patientId}")
    public Mono<PatientDTO> getPatientById(@PathVariable Long patientId) {
        return Mono.just(patientService.getPatientById(patientId));
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's first name
     *
     * @param firstName - the first name of the patient
     * @return - the list of patients matching the criteria
     */
    @GetMapping(value = "/getPatientsByFirstName/{firstName}")
    public Flux<PatientDTO> getPatientsByFirstName(@PathVariable String firstName) {
        return Flux.fromIterable(patientService.getPatientsByFirstName(firstName));
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's last name
     *
     * @param lastName - the last name of the patient
     * @return - the list of patients matching the criteria
     */
    @GetMapping(value = "/getPatientsByLastName/{lastName}")
    public Flux<PatientDTO> getPatientsByLastName(@PathVariable String lastName) {
        return Flux.fromIterable(patientService.getPatientsByLastName(lastName));
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's blood type
     *
     * @param bloodType - the blood type of the patient
     * @return - the patients of patients matching the criteria
     */
    @GetMapping(value = "/getPatientsByBloodType/{bloodType}")
    public Flux<PatientDTO> getPatientsByBloodType(@PathVariable String bloodType) {
        return Flux.fromIterable(patientService.getPatientsByBloodType(bloodType));
    }

    /**
     * Retrieves the patient's address from the address database
     *
     * @param patientId - the ID of the patient
     * @return - the AddressDTO payload containing the patient's address
     */
    @GetMapping(value = "/getPatientAddress/{patientId}")
    public Mono<AddressDTO> getAddress(@PathVariable Long patientId) {
        return Mono.just(patientService.getAddress(patientId));
    }

    /**
     * Deletes a patient from the patient database based on the patient's ID
     *
     * @param patientId - the ID of the patient
     */
    @DeleteMapping(value = "/deletePatient/{patientId}")
    public String deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);

        return "Patient successfully deleted.";
    }
}
