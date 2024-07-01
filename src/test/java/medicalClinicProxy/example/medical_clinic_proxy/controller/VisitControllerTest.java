package medicalClinicProxy.example.medical_clinic_proxy.controller;

import medicalClinicProxy.model.VisitDto;
import medicalClinicProxy.service.VisitService;
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
public class VisitControllerTest {

    @MockBean
    VisitService visitService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void getVisitsBySpecialization_CorrectData_ReturnVisitDtoList() throws Exception {
        // given
        VisitDto visitDto1 = new VisitDto(
                1L,
                LocalDateTime.of(2025, 11, 10, 15, 0),
                LocalDateTime.of(2025, 11, 10, 16, 0),
                1L,
                1L);
        VisitDto visitDto2 = new VisitDto(
                2L,
                LocalDateTime.of(2025, 11, 11, 15, 0),
                LocalDateTime.of(2025, 11, 11, 16, 0),
                1L,
                1L);
        List<VisitDto> visits = List.of(visitDto1, visitDto2);

        LocalDate visitDate = LocalDate.of(2025, 11, 10);
        String specialization = "Cardiology";

        when(visitService.getVisitsBySpecialization(visitDate, specialization)).thenReturn(visits);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/visits/specialization")
                        .param("visitDate", visitDate.toString())
                        .param("specialization", specialization)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(visitDto1.getId()))
                .andExpect(jsonPath("$[1].id").value(visitDto2.getId()))
                .andExpect(jsonPath("$[0].doctorId").value(visitDto1.getDoctorId()))
                .andExpect(jsonPath("$[1].doctorId").value(visitDto2.getDoctorId()))
                .andExpect(jsonPath("$[0].patientId").value(visitDto1.getPatientId()))
                .andExpect(jsonPath("$[1].patientId").value(visitDto2.getPatientId()));
    }
}
