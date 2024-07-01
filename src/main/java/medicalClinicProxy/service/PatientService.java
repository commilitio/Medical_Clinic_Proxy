package medicalClinicProxy.service;

import lombok.RequiredArgsConstructor;
import medicalClinicProxy.client.MedicalClinicClient;
import medicalClinicProxy.model.PatientDto;
import medicalClinicProxy.model.VisitDto;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final MedicalClinicClient medicalClinicClient;

    public List<VisitDto> getPatientVisits(Long patientId) {
        return medicalClinicClient.getPatientVisits(patientId);
    }

    public PatientDto assignPatientToVisit(Long patientId, Long visitId) {
        return medicalClinicClient.assignPatientToVisit(patientId, visitId);
    }
}
