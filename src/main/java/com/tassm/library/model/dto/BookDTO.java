package com.tassm.library.model.dto;

import com.tassm.library.validation.ValidPublicationYear;
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

    @ISBN public String isbn;

    public String title;

    public String authorName;

    @ValidPublicationYear public int publicationYear;
}
