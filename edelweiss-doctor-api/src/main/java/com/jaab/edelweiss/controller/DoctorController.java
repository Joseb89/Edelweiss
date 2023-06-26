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
    public ResponseEntity<Mono<PrescriptionDTO>> createPrescription(@RequestBody PrescriptionDTO prescription,
                                                   @PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorService.createPrescription(prescription, physicianId));
    }

    /**
     * Sends appointment payload to the appointment API
     * @param appointment - the AppointmentDTO payload
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with appointment payload
     */
    @PostMapping(value = "/physician/{physicianId}/newAppointment", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<AppointmentDTO>> createAppointment(@RequestBody AppointmentDTO appointment,
                                                            @PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorService.createAppointment(appointment, physicianId));
    }

    /**
     * Retrieves a patient from the patient API based on the patient ID
     * @param patientId - the ID of the patient
     * @return - HTTP status response with patient data
     */
    @GetMapping(value = "/physician/getPatientById/{patientId}")
    public ResponseEntity<Mono<PatientDTO>> getPatientById(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorService.getPatientById(patientId));
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's first name
     * @param firstName - the first name of the patient
     * @return - HTTP status response with list of patients
     */
    @GetMapping(value = "/physician/getPatientsByFirstName/{firstName}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByFirstName(@PathVariable String firstName) {
        return ResponseEntity.ok(doctorService.getPatientsByFirstName(firstName));
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's last name
     * @param lastName - the last name of the patient
     * @return - HTTP status response with list of patients
     */
    @GetMapping(value = "/physician/getPatientsByLastName/{lastName}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(doctorService.getPatientsByLastName(lastName));
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

    /**
     * Retrieves the specified doctor's prescriptions from the prescription API
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with list of the doctor's prescriptions
     */
    @GetMapping(value = "/physician/{physicianId}/myPrescriptions")
    public ResponseEntity<Flux<PrescriptionDTO>> getPrescriptions(@PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorService.getPrescriptions(physicianId));
    }

    /**
     * Retrieves the specified doctor's appointments from the appointment API
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with list of the doctor's appointments
     */
    @GetMapping(value = "/physician/{physicianId}/myAppointments")
    public ResponseEntity<Flux<AppointmentDTO>> getAppointments(@PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorService.getAppointments(physicianId));
    }
}
