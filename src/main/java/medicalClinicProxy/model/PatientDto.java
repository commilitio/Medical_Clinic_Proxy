package medicalClinicProxy.model;

import lombok.*;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PatientDto {

    private final Long id;
    private final String email;
    private final String idCardNo;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final LocalDate birthdate;
}
