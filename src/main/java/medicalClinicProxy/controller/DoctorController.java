package medicalClinicProxy.controller;

import lombok.RequiredArgsConstructor;
import medicalClinicProxy.model.VisitDto;
import medicalClinicProxy.service.DoctorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/doctors/{id}/visits")
    public List<VisitDto> getDoctorAvailableVisits(@PathVariable Long id) {
        return doctorService.getDoctorAvailableVisits(id);
    }
}
