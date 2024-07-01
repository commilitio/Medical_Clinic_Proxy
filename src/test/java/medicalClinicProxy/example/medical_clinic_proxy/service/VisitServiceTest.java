package medicalClinicProxy.example.medical_clinic_proxy.service;

import medicalClinicProxy.client.MedicalClinicClient;
import medicalClinicProxy.model.VisitDto;
import medicalClinicProxy.service.VisitService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

public class VisitServiceTest {

    private VisitService visitService;
    private MedicalClinicClient medicalClinicClient;

    @BeforeEach
    void setUp() {
        this.medicalClinicClient = Mockito.mock(MedicalClinicClient.class);

        this.visitService = new VisitService(medicalClinicClient);
    }

    @Test
    void getVisitsBySpecialization_VisitsAreAvailable_ReturnVisitDtoList() {
        // given
        LocalDate visitDate = LocalDate.of(2025, 11, 10);
        String specialization = "Cardiology";
        VisitDto visitDto1 = new VisitDto(
                1L,
                LocalDateTime.of(2025, 11, 10, 15, 0),
                LocalDateTime.of(2025, 11, 10, 16, 0),
                1L,
                1L);
        VisitDto visitDto2 = new VisitDto(
                1L,
                LocalDateTime.of(2025, 11, 11, 15, 0),
                LocalDateTime.of(2025, 11, 11, 16, 0),
                1L,
                1L);
        List<VisitDto> visits = List.of(visitDto1, visitDto2);
        when(medicalClinicClient.getVisitsBySpecialization(visitDate, specialization))
                .thenReturn(visits);
        // when
        List<VisitDto> result = visitService.getVisitsBySpecialization(LocalDate.of(2025, 11, 10), "Cardiology");
        // then
        Assertions.assertEquals(visits, result);
        Assertions.assertEquals(1L, result.get(0).getPatientId());
        Assertions.assertEquals(1L, result.get(1).getPatientId());
        Assertions.assertEquals(1L, result.get(0).getDoctorId());
        Assertions.assertEquals(1L, result.get(1).getDoctorId());
    }
}
