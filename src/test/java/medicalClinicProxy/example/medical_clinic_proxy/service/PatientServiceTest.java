package medicalClinicProxy.example.medical_clinic_proxy.service;

import medicalClinicProxy.client.MedicalClinicClient;
import medicalClinicProxy.model.PatientDto;
import medicalClinicProxy.model.VisitDto;
import medicalClinicProxy.service.PatientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

public class PatientServiceTest {

    private PatientService patientService;
    private MedicalClinicClient medicalClinicClient;

    @BeforeEach
    void setUp() {
        this.medicalClinicClient = Mockito.mock(MedicalClinicClient.class);

        this.patientService = new PatientService(medicalClinicClient);
    }

    @Test
    void getPatientVisits_VisitsAreAvailable_ReturnVisitDtoList() {
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
        when(medicalClinicClient.getPatientVisits(1L)).thenReturn(visits);
        // when
        List<VisitDto> result = patientService.getPatientVisits(1L);
        // then
        Assertions.assertEquals(visits, result);
        Assertions.assertEquals(1L, result.get(0).getPatientId());
        Assertions.assertEquals(1L, result.get(1).getPatientId());
        Assertions.assertEquals(1L, result.get(0).getDoctorId());
        Assertions.assertEquals(1L, result.get(1).getDoctorId());
    }

    @Test
    void assignPatientToVisit_CorrectData_ReturnPatient() {
        // given
        VisitDto visitDto = new VisitDto(
                1L,
                LocalDateTime.of(2025, 11, 10, 15, 0),
                LocalDateTime.of(2025, 11, 10, 16, 0),
                1L,
                1L);
        PatientDto patientDto = new PatientDto(1L, "jk@wp.pl", "7654789", "Jan", "Koks",
                "556776543", LocalDate.of(2011, 11, 11));

        when(medicalClinicClient.assignPatientToVisit(patientDto.getId(), visitDto.getId())).thenReturn(patientDto);
        // when
        PatientDto result = patientService.assignPatientToVisit(patientDto.getId(), visitDto.getId());
        // then
        Assertions.assertEquals(patientDto, result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("jk@wp.pl", result.getEmail());
        Assertions.assertEquals("7654789", result.getIdCardNo());
        Assertions.assertEquals("Jan", result.getFirstName());
        Assertions.assertEquals("Koks", result.getLastName());
        Assertions.assertEquals("556776543", result.getPhoneNumber());
    }
}
