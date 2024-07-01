package medicalClinicProxy.example.medical_clinic_proxy.controller;

import medicalClinicProxy.model.PatientDto;
import medicalClinicProxy.model.VisitDto;
import medicalClinicProxy.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @MockBean
    PatientService patientService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void getPatientVisits_CorrectData_ReturnVisitDtoList() throws Exception {
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
        when(patientService.getPatientVisits(1L)).thenReturn(visits);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/1/visits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(visitDto1.getId()))
                .andExpect(jsonPath("$[1].id").value(visitDto2.getId()))
                .andExpect(jsonPath("$[0].patientId").value(visitDto1.getPatientId()))
                .andExpect(jsonPath("$[1].patientId").value(visitDto2.getPatientId()));
    }

    @Test
    void assignPatientToVisit_CorrectData_ReturnPatientDto() throws Exception {
        // given
        PatientDto patientDto = new PatientDto(1L, "jk@wp.pl", "7654789", "Jan", "Koks",
                "556776543", LocalDate.of(2011, 11, 11));
        VisitDto visitDto = new VisitDto(
                1L,
                LocalDateTime.of(2025, 11, 10, 15, 0),
                LocalDateTime.of(2025, 11, 10, 16, 0),
                patientDto.getId(),
                1L);
        when(patientService.assignPatientToVisit(patientDto.getId(), visitDto.getId())).thenReturn(patientDto);
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/1/visits/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientDto.getId()))
                .andExpect(jsonPath("$.email").value(patientDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(patientDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(patientDto.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(patientDto.getPhoneNumber()))
                .andExpect(jsonPath("$.birthdate").value(patientDto.getBirthdate().toString()));
    }
}
