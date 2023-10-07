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

    public static Prescription potion = new Prescription(1L, "Rinoa", "Heartily",
            "Potion", (byte) 20, Status.PENDING);

    public static Prescription phoenixDown = new Prescription(2L, "Rinoa", "Heartily",
            "Phoenix Down", (byte) 50, Status.PENDING);

    public static Prescription darkMatter = new Prescription(3L, "Squall", "Heartily",
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
        List<PrescriptionDTO> prescriptions = createPrescriptionDTOList();

        return prescriptions.stream()
                .filter(p -> Objects.equals(p.doctorFirstName(), "Rinoa") &&
                        Objects.equals(p.doctorLastName(), "Heartily"))
                .toList();
    }

    public static List<PrescriptionDTO> getPrescriptionDTOsByPendingStatus() {
        List<PrescriptionDTO> prescriptions = createPrescriptionDTOList();

        return prescriptions.stream()
                .filter(p -> Objects.equals(p.prescriptionStatus(), Status.PENDING))
                .toList();
    }

    private static List<Prescription> createPrescriptionList() {
        List<Prescription> prescriptions = new ArrayList<>();

        prescriptions.add(potion);
        prescriptions.add(phoenixDown);
        prescriptions.add(darkMatter);

        return prescriptions;
    }

    private static List<PrescriptionDTO> createPrescriptionDTOList() {
        PrescriptionDTO prescription1 = new PrescriptionDTO(potion);
        PrescriptionDTO prescription2 = new PrescriptionDTO(phoenixDown);
        PrescriptionDTO prescription3 = new PrescriptionDTO(darkMatter);

        List<PrescriptionDTO> prescriptionList = new ArrayList<>();

        prescriptionList.add(prescription1);
        prescriptionList.add(prescription2);
        prescriptionList.add(prescription3);

        return prescriptionList;
    }
}
