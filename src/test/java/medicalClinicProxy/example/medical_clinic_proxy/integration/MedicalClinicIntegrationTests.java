package medicalClinicProxy.example.medical_clinic_proxy.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import feign.RetryableException;
import medicalClinicProxy.client.MedicalClinicClient;
import medicalClinicProxy.example.medical_clinic_proxy.config.RestTemplateConfig;
import medicalClinicProxy.model.PatientDto;
import medicalClinicProxy.model.VisitDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(RestTemplateConfig.class)
@AutoConfigureWireMock(port = 8838)
public class MedicalClinicIntegrationTests {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    MedicalClinicClient medicalClinicClient;
    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getPatientVisits_CorrectData_ReturnVisitDtoList() throws Exception {

        //given
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

        wireMockServer.stubFor(get(urlEqualTo("/patients/1/visits"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(objectMapper.writeValueAsString(visits))
                                .withFixedDelay(5000)
                )
        );
        // when
        String medicalClinicProxyUrl = "http://localhost:8083/patients/1/visits";
        ResponseEntity<VisitDto[]> response = restTemplate.getForEntity(medicalClinicProxyUrl, VisitDto[].class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<VisitDto> result = List.of(response.getBody());
        Assertions.assertEquals(visits, result);
        Assertions.assertEquals(1L, result.get(0).getPatientId());
        Assertions.assertEquals(1L, result.get(1).getPatientId());
        Assertions.assertEquals(1L, result.get(0).getDoctorId());
        Assertions.assertEquals(1L, result.get(1).getDoctorId());
        assertThat(visits.get(0).getVisitStartTime()).isEqualTo(LocalDateTime.of(2025, 11, 10, 15, 0));
        assertThat(visits.get(1).getVisitStartTime()).isEqualTo(LocalDateTime.of(2025, 11, 11, 15, 0));
        assertThat(visits.get(0).getVisitEndTime()).isEqualTo(LocalDateTime.of(2025, 11, 10, 16, 0));
        assertThat(visits.get(1).getVisitEndTime()).isEqualTo(LocalDateTime.of(2025, 11, 11, 16, 0));
    }

    @Test
    void testRetryer_Return503Response() throws Exception {
        //given
        wireMockServer.stubFor(get(urlEqualTo("/patients/1/visits"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/json")));

        // when
        RetryableException exception = Assertions.assertThrows(RetryableException.class, () -> {
            medicalClinicClient.getPatientVisits(1L);
        });

        // then
        assertThat(exception).isNotNull();
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

        wireMockServer.stubFor(patch(urlEqualTo("/patients/1/visits/1"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(objectMapper.writeValueAsString(patientDto))
                )
        );
        // when
        String medicalClinicProxyUrl = "http://localhost:8083/patients/1/visits/1";
        HttpEntity<VisitDto> requestUpdate = new HttpEntity<>(visitDto);
        ResponseEntity<PatientDto> response = restTemplate.exchange(medicalClinicProxyUrl, HttpMethod.PATCH, requestUpdate, PatientDto.class);


        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        PatientDto result = response.getBody();
        Assertions.assertEquals(patientDto.getId(), result.getId());
        Assertions.assertEquals(patientDto.getEmail(), result.getEmail());
        Assertions.assertEquals(patientDto.getFirstName(), result.getFirstName());
        Assertions.assertEquals(patientDto.getLastName(), result.getLastName());
        Assertions.assertEquals(patientDto.getPhoneNumber(), result.getPhoneNumber());
        Assertions.assertEquals(patientDto.getBirthdate(), result.getBirthdate());
    }

    @Test
    void getDoctorAvailableVisits_CorrectData_ReturnVisitDtoList() throws Exception {
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

        wireMockServer.stubFor(get(urlEqualTo("/doctors/1/visits"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(objectMapper.writeValueAsString(visits))
                )
        );

        // when
        String medicalClinicProxyUrl = "http://localhost:8083/doctors/1/visits";
        ResponseEntity<VisitDto[]> response = restTemplate.getForEntity(medicalClinicProxyUrl, VisitDto[].class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<VisitDto> result = List.of(response.getBody());
        Assertions.assertEquals(visits, result);
        Assertions.assertEquals(1L, result.get(0).getDoctorId());
        Assertions.assertEquals(1L, result.get(1).getDoctorId());
        Assertions.assertEquals(1L, result.get(0).getPatientId());
        Assertions.assertEquals(1L, result.get(1).getPatientId());
        assertThat(visits.get(0).getVisitStartTime()).isEqualTo(LocalDateTime.of(2025, 11, 10, 15, 0));
        assertThat(visits.get(1).getVisitStartTime()).isEqualTo(LocalDateTime.of(2025, 11, 11, 15, 0));
        assertThat(visits.get(0).getVisitEndTime()).isEqualTo(LocalDateTime.of(2025, 11, 10, 16, 0));
        assertThat(visits.get(1).getVisitEndTime()).isEqualTo(LocalDateTime.of(2025, 11, 11, 16, 0));
    }

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

        wireMockServer.stubFor(get(urlEqualTo("/visits/specialization?visitDate=29.05.2025&specialization=Cardiology"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(objectMapper.writeValueAsString(visits))
                )
        );
        // when
        String medicalClinicProxyUrl = "http://localhost:8083/visits/specialization?visitDate=2025-05-29&specialization=Cardiology";
        ResponseEntity<VisitDto[]> response = restTemplate.getForEntity(medicalClinicProxyUrl, VisitDto[].class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<VisitDto> result = List.of(response.getBody());
        Assertions.assertEquals(visits, result);
        Assertions.assertEquals(1L, result.get(0).getDoctorId());
        Assertions.assertEquals(1L, result.get(1).getDoctorId());
        Assertions.assertEquals(1L, result.get(0).getPatientId());
        Assertions.assertEquals(1L, result.get(1).getPatientId());
        assertThat(visits.get(0).getVisitStartTime()).isEqualTo(LocalDateTime.of(2025, 11, 10, 15, 0));
        assertThat(visits.get(1).getVisitStartTime()).isEqualTo(LocalDateTime.of(2025, 11, 11, 15, 0));
        assertThat(visits.get(0).getVisitEndTime()).isEqualTo(LocalDateTime.of(2025, 11, 10, 16, 0));
        assertThat(visits.get(1).getVisitEndTime()).isEqualTo(LocalDateTime.of(2025, 11, 11, 16, 0));
    }
}
