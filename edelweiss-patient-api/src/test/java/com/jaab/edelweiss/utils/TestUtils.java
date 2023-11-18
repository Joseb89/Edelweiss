package com.jaab.edelweiss.utils;

import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class TestUtils {

    public static Patient james = new Patient(1L, "James", "Hawke",
            "championofkirkwall@gmail.com", "magerebellion", null, 7130042356L,
            "Varric Tethras", "O+");

    public static Patient bethany = new Patient(2L, "Bethany", "Hawke",
            "circlemage@gmail.com", "daughterofamell", null, 7130042357L,
            "Varric Tethras", "O-");

    public static Patient carver = new Patient(3L, "Carver", "Hawke",
            "templarknight@gmail.com", "sonofamell", null, 7130042357L,
            "Varric Tethras", "O-");

    public static Address jamesAddress = new Address(james.getId(), james, "58 Hightown Court",
            "San Antonio", "TX", 78615);

    public static Address bethanyAddress = new Address(bethany.getId(), bethany, "59 Gallows St",
            "San Antonio", "TX", 78615);

    public static Address carverAddress = new Address(carver.getId(), carver, "59 Gallows St",
            "San Antonio", "TX", 78615);

    public static String firstNameTestParameter = "Bethany";

    public static String lastNameTestParameter = "Hawke";

    public static String bloodTypeTestParameter = "O-";

    private static final List<Patient> PATIENTS = createPatientList();

    public static List<Patient> getPatientsByFirstName() {
        return PATIENTS.stream()
                .filter(p -> Objects.equals(p.getFirstName(), firstNameTestParameter))
                .toList();
    }

    public static List<Patient> getPatientsByLastName() {
        return PATIENTS.stream()
                .filter(p -> Objects.equals(p.getLastName(), lastNameTestParameter))
                .toList();
    }

    public static List<Patient> getPatientsByBloodType() {
        return PATIENTS.stream()
                .filter(p -> Objects.equals(p.getBloodType(), bloodTypeTestParameter))
                .toList();
    }

    public static List<PatientDTO> getPatientDTOsByFirstName() {
        return PATIENTS.stream()
                .filter(p -> Objects.equals(p.getFirstName(), firstNameTestParameter))
                .map(PatientDTO::new)
                .toList();
    }

    public static List<PatientDTO> getPatientDTOsByLastName() {
        return PATIENTS.stream()
                .filter(p -> Objects.equals(p.getLastName(), lastNameTestParameter))
                .map(PatientDTO::new)
                .toList();
    }

    public static List<PatientDTO> getPatientDTOsByBloodType() {
        return PATIENTS.stream()
                .filter(p -> Objects.equals(p.getBloodType(), bloodTypeTestParameter))
                .map(PatientDTO::new)
                .toList();
    }

    private static List<Patient> createPatientList() {
        List<Patient> patients = new ArrayList<>();

        patients.add(james);
        patients.add(bethany);
        patients.add(carver);

        return patients;
    }
}
