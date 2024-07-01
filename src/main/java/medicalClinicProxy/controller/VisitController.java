package medicalClinicProxy.controller;

import lombok.RequiredArgsConstructor;
import medicalClinicProxy.model.VisitDto;
import medicalClinicProxy.service.VisitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class VisitController {

    private final VisitService visitService;

    @GetMapping("/visits/specialization")
    public List<VisitDto> getVisitsBySpecialization(@RequestParam LocalDate visitDate, @RequestParam String specialization) {
        return visitService.getVisitsBySpecialization(visitDate, specialization);
    }
}
