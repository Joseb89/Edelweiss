package com.jaab.edelweiss.utils;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.model.Role;
import com.jaab.edelweiss.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class TestUtils {

    public static Pharmacist createPharmacist() {
        return new Pharmacist(1L, "Alistair", "Theirin",
                "kingofferelden@yahoo.com", "sonofmaric", Role.PHARMACIST);
    }

    public static List<PrescriptionDTO> pendingPrescriptions() {
        List<PrescriptionDTO> prescriptions = prescriptionList();

        return prescriptions.stream()
                .filter(p -> Objects.equals(p.prescriptionStatus(), Status.PENDING))
                .toList();
    }

    private static List<PrescriptionDTO> prescriptionList() {
        PrescriptionDTO potion = new PrescriptionDTO(1L, "Wynne", "Langrene",
                "Potion", (byte) 50, Status.PENDING);

        PrescriptionDTO elfroot = new PrescriptionDTO(2L, "Varric", "Tethras",
                "Elfroot", (byte) 75, Status.PENDING);

        PrescriptionDTO lyrium = new PrescriptionDTO(3L, "Solas", "Wolffe",
                "Lyrium", (byte) 100, Status.APPROVED);

        List<PrescriptionDTO> prescriptions = new ArrayList<>();

        prescriptions.add(potion);
        prescriptions.add(elfroot);
        prescriptions.add(lyrium);

        return prescriptions;
    }
}
