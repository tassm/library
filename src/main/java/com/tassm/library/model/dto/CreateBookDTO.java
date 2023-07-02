package com.tassm.library.model.dto;

import com.tassm.library.validation.ValidPublicationYear;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.ISBN;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateBookDTO {

    @NotBlank(message = "ISBN is required")
    @ISBN
    private String isbn;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Author information is required")
    private Set<String> authorNames;

    @NotNull(message = "Publication year is required")
    @ValidPublicationYear
    private Integer publicationYear;
}
