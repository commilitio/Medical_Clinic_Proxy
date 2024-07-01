package medicalClinicProxy.client;

import medicalClinicProxy.model.PatientDto;
import medicalClinicProxy.model.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "MedicalClinicClient", configuration = ClientConfiguration.class)
public interface MedicalClinicClient {

    @GetMapping("/patients/{id}/visits")
    List<VisitDto> getPatientVisits(@PathVariable Long id);

    @PatchMapping("/patients/{id}/visits/{visitId}")
    PatientDto assignPatientToVisit(@PathVariable Long id, @PathVariable Long visitId);

    @GetMapping("/doctors/{id}/visits")
    List<VisitDto> getDoctorAvailableVisits(@PathVariable Long id);

    @GetMapping("/visits/specialization")
    List<VisitDto> getVisitsBySpecialization(@RequestParam LocalDate visitDate, @RequestParam String specialization);

}
