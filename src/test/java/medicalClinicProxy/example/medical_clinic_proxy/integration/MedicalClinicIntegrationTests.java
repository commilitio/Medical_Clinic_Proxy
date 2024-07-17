package medicalClinicProxy.example.medical_clinic_proxy.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import feign.RetryableException;
import medicalClinicProxy.client.MedicalClinicClient;
import medicalClinicProxy.model.VisitDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWireMock(port = 8838)
public class MedicalClinicIntegrationTests {

    @Autowired
    MedicalClinicClient medicalClinicClient;
    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getPatientVisits_CorrectData_ReturnVisitDtoList() throws IOException {

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
                                .withFixedDelay(10000)
                )
        );
        // when
        List<VisitDto> result = medicalClinicClient.getPatientVisits(1L);

        // then
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
    void testRetryer_Return503Response() throws IOException {
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
}
