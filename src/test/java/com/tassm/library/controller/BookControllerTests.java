package com.tassm.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tassm.library.model.dto.BookDTO;
import com.tassm.library.model.dto.CreateBookDTO;
import com.tassm.library.model.dto.ErrorDTO;
import com.tassm.library.model.mapping.BookMapper;
import com.tassm.library.service.BookService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest({BookController.class, BookMapper.class})
public class BookControllerTests {

    @Autowired MockMvc mockMvc;
    @Autowired BookMapper bookMapper;
    @MockBean BookService bookService;

    private BookDTO bookDTO;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        reset(bookService);
        bookDTO = new BookDTO("978-3-16-148410-0", "some book", Set.of("Gordon Ramsey"), 2009);
    }

    /*
     * CREATE TESTS
     */

    @Test
    @DisplayName("Create should succeed with with 201 created")
    public void testCreate_succeedsWith201() throws Exception {
        when(bookService.saveBookAndAuthors(any())).thenReturn(bookDTO);
        var createBookDTO = bookMapper.bookDTOToCreateBookDTO(bookDTO);
        String bodyAsString = objectMapper.writeValueAsString(createBookDTO);
        this.mockMvc
                .perform(
                        post("/book").contentType(MediaType.APPLICATION_JSON).content(bodyAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(bodyAsString));
    }

    @Test
    @DisplayName("Create should fail with with 400 for invalid isbn")
    public void testCreate_succeedsFail400_badIsbn() throws Exception {
        var createBookDTO = bookMapper.bookDTOToCreateBookDTO(bookDTO);
        createBookDTO.setIsbn("aninvalid-isbn-212");
        String bodyAsString = objectMapper.writeValueAsString(createBookDTO);
        String expectedError =
                objectMapper.writeValueAsString(
                        new ErrorDTO(400, "Invalid request body or parameter - try again"));
        this.mockMvc
                .perform(
                        post("/book").contentType(MediaType.APPLICATION_JSON).content(bodyAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedError));
    }

    // these should probably be a @ParameterizedTest somehow
    @Test
    @DisplayName("Create should fail with with 400 for missing mandatory isbn")
    public void testCreate_fails400_missingIsbn() throws Exception {
        var createBookDTO = bookMapper.bookDTOToCreateBookDTO(bookDTO);
        createBookDTO.setIsbn(null);
        missingMandatoryFieldTest(createBookDTO);
    }

    @Test
    @DisplayName("Create should fail with with 400 for missing mandatory title")
    public void testCreate_fails400_missingTitle() throws Exception {
        var createBookDTO = bookMapper.bookDTOToCreateBookDTO(bookDTO);
        createBookDTO.setTitle(null);
        missingMandatoryFieldTest(createBookDTO);
    }

    @Test
    @DisplayName("Create should fail with with 400 for missing mandatory publicationDate")
    public void testCreate_fails400_missingPubDate() throws Exception {
        var createBookDTO = bookMapper.bookDTOToCreateBookDTO(bookDTO);
        createBookDTO.setPublicationYear(null);
        missingMandatoryFieldTest(createBookDTO);
    }

    @Test
    @DisplayName("Create should fail with with 400 for missing mandatory authorNames")
    public void testCreate_fails400_missingAuthorNames() throws Exception {
        var createBookDTO = bookMapper.bookDTOToCreateBookDTO(bookDTO);
        createBookDTO.setAuthorNames(null);
        missingMandatoryFieldTest(createBookDTO);
    }

    @Test
    @DisplayName("Create should fail with with 400 for empty authorNames")
    public void testCreate_fails400_emptyAuthorNames() throws Exception {
        var createBookDTO = bookMapper.bookDTOToCreateBookDTO(bookDTO);
        createBookDTO.setAuthorNames(new HashSet<>());
        missingMandatoryFieldTest(createBookDTO);
    }

    /*
     * GET TESTS
     */
    @Test
    @DisplayName("Get one by ISBN should succeed with with 200 ok")
    public void testGet_succeedsWith200() throws Exception {
        String isbn = bookDTO.getIsbn();
        when(bookService.findBookByIsbn(eq(isbn))).thenReturn(bookDTO);
        String bodyAsString = objectMapper.writeValueAsString(bookDTO);
        this.mockMvc
                .perform(get("/book/" + isbn).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(bodyAsString));
    }

    @Test
    @DisplayName("Get many books should succeed with with 200 ok")
    public void testGetMany_succeedsWith200() throws Exception {
        when(bookService.findBooks(eq(null), eq(null), eq(null))).thenReturn(List.of(bookDTO));
        String bodyAsString = objectMapper.writeValueAsString(List.of(bookDTO));
        this.mockMvc
                .perform(get("/book").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(bodyAsString));
        verify(bookService, times(1)).findBooks(eq(null), eq(null), eq(null));
    }

    @Test
    @DisplayName("Get many books filtered on authorName should succeed with with 200 ok")
    public void testGetMany_filterAuthors_succeedsWith200() throws Exception {
        String dummyAuthor = "authorA";
        this.mockMvc
                .perform(
                        get("/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .queryParam("authorName", dummyAuthor))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
        verify(bookService, times(1)).findBooks(eq(dummyAuthor), eq(null), eq(null));
    }

    @Test
    @DisplayName("Get many books filtered on min and max pubYear should succeed with with 200 ok")
    public void testGetMany_filterYears_succeedsWith200() throws Exception {
        this.mockMvc
                .perform(
                        get("/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .queryParam("rangeStart", "1972")
                                .queryParam("rangeEnd", "1979"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"));
        verify(bookService, times(1)).findBooks(null, 1972, 1979);
    }

    /*
     * UPDATE TESTS
     */
    @Test
    @DisplayName("Update book by ISBN should succeed with with 200 ok")
    public void testUpdate_succeedsWith200() throws Exception {
        String isbn = bookDTO.getIsbn();
        when(bookService.updateBookAndAuthors(eq(isbn), any())).thenReturn(bookDTO);
        String bodyAsString = objectMapper.writeValueAsString(bookDTO);
        this.mockMvc
                .perform(
                        patch("/book/" + isbn)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyAsString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(bodyAsString));
        verify(bookService, times(1)).updateBookAndAuthors(isbn, bookDTO);
    }

    /*
     * DELETE TESTS
     */
    @Test
    @DisplayName("Delete by ISBN should succeed with with 200 ok")
    public void testDelete_succeedsWith200() throws Exception {
        String isbn = bookDTO.getIsbn();
        this.mockMvc
                .perform(delete("/book/" + isbn).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    private void missingMandatoryFieldTest(CreateBookDTO bookDTO) throws Exception {
        String bodyAsString = objectMapper.writeValueAsString(bookDTO);
        String expectedError =
                objectMapper.writeValueAsString(
                        new ErrorDTO(400, "Invalid request body or parameter - try again"));
        this.mockMvc
                .perform(
                        post("/book").contentType(MediaType.APPLICATION_JSON).content(bodyAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedError));
    }
}
