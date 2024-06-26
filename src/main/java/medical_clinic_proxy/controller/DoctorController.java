package medical_clinic_proxy.controller;

import lombok.RequiredArgsConstructor;
import medical_clinic_proxy.model.VisitDto;
import medical_clinic_proxy.service.DoctorService;
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
