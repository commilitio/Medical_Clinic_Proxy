package medicalClinicProxy.example.medical_clinic_proxy.service;

import medicalClinicProxy.client.MedicalClinicClient;
import medicalClinicProxy.model.VisitDto;
import medicalClinicProxy.service.DoctorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

public class DoctorVisitTest {

    private DoctorService doctorService;
    private MedicalClinicClient medicalClinicClient;

    @BeforeEach
    void setUp() {
        this.medicalClinicClient = Mockito.mock(MedicalClinicClient.class);

        this.doctorService = new DoctorService(medicalClinicClient);
    }

    @Test
    void getDoctorAvailableVisits_VisitsAreAvailable_ReturnVisitDtoList() {
        // given
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
        when(medicalClinicClient.getDoctorAvailableVisits(1L)).thenReturn(visits);
        // when
        List<VisitDto> result = doctorService.getDoctorAvailableVisits(1L);
        // then
        Assertions.assertEquals(visits, result);
        Assertions.assertEquals(1L, result.get(0).getPatientId());
        Assertions.assertEquals(1L, result.get(1).getPatientId());
        Assertions.assertEquals(1L, result.get(0).getDoctorId());
        Assertions.assertEquals(1L, result.get(1).getDoctorId());
    }
}
