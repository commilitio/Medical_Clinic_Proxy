package medicalClinicProxy.service;

import lombok.RequiredArgsConstructor;
import medicalClinicProxy.client.MedicalClinicClient;
import medicalClinicProxy.model.VisitDto;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VisitService {

    private final MedicalClinicClient medicalClinicClient;

    public List<VisitDto> getVisitsBySpecialization(LocalDate visitDate, String specialization) {
        return medicalClinicClient.getVisitsBySpecialization(visitDate, specialization);
    }
}
