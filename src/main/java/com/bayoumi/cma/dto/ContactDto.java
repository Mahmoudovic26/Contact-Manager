package com.bayoumi.cma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ContactDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactRequest {

        @NotBlank(message = "First name is required")
        private String firstName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        @NotBlank(message = "Phone number is required")
        private String phoneNumber;

        @Email(message = "Email should be valid")
        private String email;

        @Past(message = "Birthdate must be in the past")
        private LocalDate birthdate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactResponse {

        private Long id;

        private String firstName;

        private String lastName;

        private String phoneNumber;

        private String email;

        private LocalDate birthdate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedContactResponse {

        private List<ContactResponse> contacts;

        private int currentPage;

        private int totalPages;

        private long totalElements;
    }
}