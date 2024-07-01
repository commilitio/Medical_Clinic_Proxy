package medicalClinicProxy.service;

import lombok.RequiredArgsConstructor;
import medicalClinicProxy.client.MedicalClinicClient;
import medicalClinicProxy.model.VisitDto;
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
