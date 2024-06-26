package medical_clinic_proxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class VisitDto {

    private Long id;
    private LocalDateTime visitStartTime;
    private LocalDateTime visitEndTime;
    private Long patientId;
    private Long doctorId;
}
