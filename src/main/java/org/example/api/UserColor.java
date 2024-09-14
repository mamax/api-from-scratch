package org.example.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserColor {
    private Integer id;
    private String name;
    private Integer year;
    private String color;
    private String pantone_value;
}
