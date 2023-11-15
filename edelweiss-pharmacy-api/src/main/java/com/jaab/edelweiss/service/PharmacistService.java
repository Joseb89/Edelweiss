package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PharmacistRepository;
import com.jaab.edelweiss.exception.PharmacistNotFoundException;
import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * This is a service class for creating and maintaining pharmacist data
 *
 * @author Joseph Barr
 */

@Service
public class PharmacistService {

    private final PharmacistRepository pharmacistRepository;

    public PharmacistService(PharmacistRepository pharmacistRepository) {
        this.pharmacistRepository = pharmacistRepository;
    }

    /**
     * Saves a new pharmacist to the pharmacist database
     *
     * @param pharmacist - the Pharmacist object
     * @return - the new pharmacist
     */
    public Pharmacist createPharmacist(Pharmacist pharmacist) {
        pharmacist.setRole(Role.PHARMACIST);
        pharmacistRepository.save(pharmacist);

        return pharmacist;
    }

    /**
     * Updates the pharmacist's information and merges it to the pharmacist database
     *
     * @param pharmacistId - the ID of the pharmacist
     * @param fields       - the object containing the updated information
     * @return - the updated pharmacist
     */
    public Pharmacist updatePharmacistInfo(Long pharmacistId, Map<String, Object> fields) {
        Pharmacist pharmacist = getPharmacistById(pharmacistId);

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Pharmacist.class, key);

            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, pharmacist, value);
            }
        });

        pharmacistRepository.save(pharmacist);

        return pharmacist;
    }

    /**
     * Deletes a pharmacist from the pharmacist database based on their ID
     *
     * @param pharmacistId - the ID of the pharmacist
     * @throws PharmacistNotFoundException if the pharmacist with the specified ID is not found
     */
    public void deletePharmacist(Long pharmacistId) throws PharmacistNotFoundException {
        Pharmacist pharmacist = getPharmacistById(pharmacistId);

        pharmacistRepository.deleteById(pharmacist.getId());
    }

    /**
     * Retrieves a pharmacist from the pharmacist database based on their ID
     *
     * @param pharmacistId - the ID of the pharmacist
     * @return - the pharmacist if available
     * @throws PharmacistNotFoundException if the pharmacist with the specified ID is not found
     */
    private Pharmacist getPharmacistById(Long pharmacistId) throws PharmacistNotFoundException {
        return pharmacistRepository.findById(pharmacistId)
                .orElseThrow(()-> new PharmacistNotFoundException("No pharmacist with the specified ID found."));
    }
}
