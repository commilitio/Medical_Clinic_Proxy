package medical_clinic_proxy.service;

import lombok.RequiredArgsConstructor;
import medical_clinic_proxy.client.MedicalClinicClient;
import medical_clinic_proxy.model.VisitDto;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DoctorService {

    private final MedicalClinicClient medicalClinicClient;

    public List<VisitDto> getDoctorAvailableVisits(Long doctorId) {
        return medicalClinicClient.getDoctorAvailableVisits(doctorId);
    }
}
