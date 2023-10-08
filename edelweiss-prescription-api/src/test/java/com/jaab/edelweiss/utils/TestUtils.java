package com.jaab.edelweiss.utils;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class TestUtils {

    public static String doctorFirstName = "Rinoa";

    public static String doctorLastName = "Heartily";

    public static Prescription potion = new Prescription(1L, doctorFirstName, doctorLastName,
            "Potion", (byte) 20, Status.PENDING);

    public static Prescription phoenixDown = new Prescription(2L, doctorFirstName, doctorLastName,
            "Phoenix Down", (byte) 50, Status.PENDING);

    public static Prescription darkMatter = new Prescription(3L, "Squall", "Leonheart",
            "Dark Matter", (byte) 75, Status.APPROVED);

    public static PrescriptionDTO prescriptionDTO = new PrescriptionDTO(potion);

    public static UpdatePrescriptionDTO updatePrescriptionDTO =
            new UpdatePrescriptionDTO(potion.getId(), "Hi-Potion", (byte) 10);

    public static List<Prescription> getPrescriptionsByDoctorName() {
        List<Prescription> prescriptions = createPrescriptionList();

        return prescriptions.stream()
                .filter(p -> Objects.equals(p.getDoctorFirstName(), "Rinoa") &&
                        Objects.equals(p.getDoctorLastName(), "Heartily"))
                .toList();
    }

    public static List<Prescription> getPrescriptionsByPrescriptionStatus() {
        List<Prescription> prescriptions = createPrescriptionList();

        return prescriptions.stream()
                .filter(p -> Objects.equals(p.getPrescriptionStatus(), Status.APPROVED))
                .toList();
    }

    public static List<PrescriptionDTO> getPrescriptionDTOsByDoctorName() {
        List<Prescription> prescriptions = createPrescriptionList();

        return prescriptions.stream()
                .filter(p -> Objects.equals(p.getDoctorFirstName(), doctorFirstName) &&
                        Objects.equals(p.getDoctorLastName(), doctorLastName))
                .map(PrescriptionDTO::new)
                .toList();
    }

    public static List<PrescriptionDTO> getPrescriptionDTOsByPendingStatus() {
        List<Prescription> prescriptions = createPrescriptionList();

        return prescriptions.stream()
                .filter(p -> Objects.equals(p.getPrescriptionStatus(), Status.PENDING))
                .map(PrescriptionDTO::new)
                .toList();
    }

    private static List<Prescription> createPrescriptionList() {
        List<Prescription> prescriptions = new ArrayList<>();

        prescriptions.add(potion);
        prescriptions.add(phoenixDown);
        prescriptions.add(darkMatter);

        return prescriptions;
    }
}
