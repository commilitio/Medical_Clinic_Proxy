package medical_clinic_proxy.service;

import lombok.RequiredArgsConstructor;
import medical_clinic_proxy.client.MedicalClinicClient;
import medical_clinic_proxy.model.PatientDto;
import medical_clinic_proxy.model.VisitDto;
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
