package com.tassm.library.model.dto;

import com.tassm.library.validation.ValidPublicationYear;
import jakarta.validation.constraints.Size;
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
public class BookDTO {

    @ISBN private String isbn;

    private String title;

    @Size(min = 1)
    private Set<String> authorNames;

    @ValidPublicationYear private Integer publicationYear;
}
