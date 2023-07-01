package com.tassm.library.model.dto;

import com.tassm.library.validation.ValidPublicationYear;
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

    private Set<AuthorDTO> authors;

    @ValidPublicationYear private int publicationYear;
}
