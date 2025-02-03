package dev.khanh.learnspring.dto.request;

import dev.khanh.learnspring.validator.DobConstraint;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
    @DobConstraint(min = 18, message = "INVALID_DOB")
    List<String> roles;
}
