package com.tassm.library.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookDTO {
    @NotBlank public String isbn;
    @NotBlank public String title;
    @NotBlank public String authorName;
    // TODO: validate year
    @NotNull public int publicationYear;
}
