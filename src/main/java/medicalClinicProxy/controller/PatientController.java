package medicalClinicProxy.controller;

import lombok.RequiredArgsConstructor;
import medicalClinicProxy.model.PatientDto;
import medicalClinicProxy.model.VisitDto;
import medicalClinicProxy.service.PatientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/patients/{id}/visits")
    public List<VisitDto> getPatientVisits(@PathVariable Long id) {
        return patientService.getPatientVisits(id);
    }

    @PatchMapping("/patients/{id}/visits/{visitId}")
    public PatientDto assignPatientToVisit(@PathVariable Long id, @PathVariable Long visitId) {
        return patientService.assignPatientToVisit(id, visitId);
    }
}
