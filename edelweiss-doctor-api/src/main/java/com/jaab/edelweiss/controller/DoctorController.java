package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * Saves a new doctor to the doctor database and sends data to the user API
     * @param doctor - the doctor payload
     * @return - HTTP status response with ID of doctor
     */
    @PostMapping(value = "/newPhysician", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPhysician(@RequestBody Doctor doctor) {
        UserDTO newDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.ok(newDoctor.getId());
    }

    /**
     * Sends prescription payload to the prescription API
     * @param prescription - the PrescriptionDTO payload
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with prescription payload
     */
    @PostMapping(value = "/physician/{physicianId}/newPrescription", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrescriptionDTO> createPrescription(@RequestBody PrescriptionDTO prescription,
                                                   @PathVariable Long physicianId) {
        PrescriptionDTO newPrescription = doctorService.createPrescription(prescription, physicianId);
        return ResponseEntity.ok(newPrescription);
    }

    /**
     * Sends appointment payload to the appointment API
     * @param appointment - the AppointmentDTO payload
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with appointment payload
     */
    @PostMapping(value = "/physician/{physicianId}/newAppointment", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointment,
                                                            @PathVariable Long physicianId) {
        AppointmentDTO newAppointment = doctorService.createAppointment(appointment, physicianId);
        return ResponseEntity.ok(newAppointment);
    }

    /**
     * Retrieves a patient from the patient API based on the patient ID
     * @param patientId - the ID of the patient
     * @return - HTTP status response with patient data
     */
    @GetMapping(value = "/physician/getPatientById/{patientId}")
    public ResponseEntity<Mono<PatientDTO>> getPatientById(@PathVariable Long patientId) {
        Mono<PatientDTO> patientData = doctorService.getPatientById(patientId);
        return ResponseEntity.ok(patientData);
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's first name
     * @param firstName - the first name of the patient
     * @return - HTTP status response with list of patients
     */
    @GetMapping(value = "/physician/getPatientsByFirstName/{firstName}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByFirstName(@PathVariable String firstName) {
        Flux<PatientDTO> patientData = doctorService.getPatientsByFirstName(firstName);
        return ResponseEntity.ok(patientData);
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's last name
     * @param lastName - the last name of the patient
     * @return - HTTP status response with list of patients
     */
    @GetMapping(value = "/physician/getPatientsByLastName/{lastName}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByLastName(@PathVariable String lastName) {
        Flux<PatientDTO> patientData = doctorService.getPatientsByLastName(lastName);
        return ResponseEntity.ok(patientData);
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's blood type
     * @param bloodType - the blood type of the patient
     * @return - HTTP status response with list of patients
     */
    @GetMapping(value = "/physician/getPatientsByBloodType/{bloodType}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByBloodType(@PathVariable String bloodType) {
        return ResponseEntity.ok(doctorService.getPatientsByBloodType(bloodType));
    }
}
