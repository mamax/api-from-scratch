package org.example.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class UserRequest {

    private String email;
    private String password;
}
