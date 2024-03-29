package com.jaab.edelweiss.utils;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.model.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class TestUtils {

    public static String doctorFirstName = "Wynne";

    public static String doctorLastName = "Langrene";

    public static String firstNameTestParameter = "James";

    public static String lastNameTestParameter = "Hawke";

    public static String bloodTypeTestParameter = "AB-";

    public static final long ID = 1L;

    public static final int YEAR = (LocalDate.now().getYear()) + 1;

    private static final List<PatientDTO> PATIENTS = createPatients();

    public static Doctor createDoctor(Long physicianId) {
        return new Doctor(physicianId, doctorFirstName, doctorLastName, "seniorenchanter@aol.com",
                "spiritoffaith", 6687412012L, "Hematology");
    }

    public static List<PatientDTO> getPatientsByFirstName() {
        return PATIENTS.stream()
                .filter(p -> Objects.equals(p.firstName(), TestUtils.firstNameTestParameter))
                .toList();
    }

    public static List<PatientDTO> getPatientsByLastName() {
        return PATIENTS.stream()
                .filter(p -> Objects.equals(p.lastName(), TestUtils.lastNameTestParameter))
                .toList();
    }

    public static List<PatientDTO> getPatientsByBloodType() {
        return PATIENTS.stream()
                .filter(p -> Objects.equals(p.bloodType(), TestUtils.bloodTypeTestParameter))
                .toList();
    }

    public static List<PrescriptionDTO> getPrescriptions() {
        List<PrescriptionDTO> prescriptions = createPrescriptions();

        return prescriptions.stream()
                .filter(p -> Objects.equals(p.getDoctorFirstName(), doctorFirstName) &&
                        Objects.equals(p.getDoctorLastName(), doctorLastName))
                .toList();
    }

    public static List<AppointmentDTO> getAppointments() {
        List<AppointmentDTO> appointments = createAppointments();

        return appointments.stream()
                .filter(a -> a.getDoctorFirstName().equals(doctorFirstName) &&
                        a.getDoctorLastName().equals(doctorLastName))
                .toList();
    }

    private static List<PatientDTO> createPatients() {
        PatientDTO james = new PatientDTO(1L, "James", "Hawke",
                "championofkirkwall@gmail.com", 7130042356L,
                "Varric Tethras", "AB+");

        PatientDTO bethany = new PatientDTO(2L, "Bethany", "Hawke",
                "circlemage@yahoo.com", 7130042357L,
                "Varric Tethras", "AB-");

        PatientDTO carver = new PatientDTO(3L, "Carver", "Hawke",
                "templarknight@aol.com", 7130042357L,
                "Varric Tethras", "AB-");

        List<PatientDTO> patients = new ArrayList<>();

        patients.add(james);
        patients.add(bethany);
        patients.add(carver);

        return patients;
    }

    private static List<PrescriptionDTO> createPrescriptions() {
        PrescriptionDTO felandris = new PrescriptionDTO(1L, doctorFirstName, doctorLastName,
                "Felandris", (byte) 50, Status.PENDING);

        PrescriptionDTO ambrosia = new PrescriptionDTO(2L, doctorFirstName, doctorLastName,
                "Ambrosia", (byte) 60, Status.PENDING);

        PrescriptionDTO lyrium = new PrescriptionDTO(3L, "Solas", "Wolffe",
                "Lyrium", (byte) 75, Status.PENDING);

        List<PrescriptionDTO> prescriptions = new ArrayList<>();

        prescriptions.add(felandris);
        prescriptions.add(ambrosia);
        prescriptions.add(lyrium);

        return prescriptions;
    }

    private static List<AppointmentDTO> createAppointments() {
        AppointmentDTO firstAppointment = new AppointmentDTO(1L, doctorFirstName, doctorLastName,
                "Dane", "Cousland", LocalDate.of((YEAR + 1), 10, 5),
                LocalTime.of(10, 30));

        AppointmentDTO secondAppointment = new AppointmentDTO(2L, "Solas", "Wolffe",
                "Evelyn", "Trevelyan", LocalDate.of((YEAR + 1), 10, 5),
                LocalTime.of(10, 30));

        AppointmentDTO thirdAppointment = new AppointmentDTO(3L, doctorFirstName, doctorLastName,
                "Alistair", "Theirin", LocalDate.of((YEAR + 1), 10, 8),
                LocalTime.of(13, 45));

        List<AppointmentDTO> appointments = new ArrayList<>();

        appointments.add(firstAppointment);
        appointments.add(secondAppointment);
        appointments.add(thirdAppointment);

        return appointments;
    }
}
