package com.Spring.Boot.Junit.Mockito.Fixture.Assesment.MockitoFixtureAssesment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private int status;
    private String message;
}
