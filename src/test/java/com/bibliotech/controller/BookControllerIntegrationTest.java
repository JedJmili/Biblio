package com.bibliotech.controller;

import com.bibliotech.config.SecurityConfig;
import com.bibliotech.dto.BookCreateDTO;
import com.bibliotech.dto.BookDTO;
import com.bibliotech.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private com.bibliotech.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private com.bibliotech.security.JwtService jwtService;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        doAnswer(invocation -> {
            ServletRequest req = invocation.getArgument(0);
            ServletResponse res = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllBooks_ReturnsOk() throws Exception {
        BookDTO book = BookDTO.builder()
                .id(1L)
                .title("Test Book")
                .isbn("978-1234567890")
                .build();
        when(bookService.getAllBooks()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBookById_ReturnsOk() throws Exception {
        BookDTO book = BookDTO.builder()
                .id(1L)
                .title("Test Book")
                .isbn("978-1234567890")
                .build();
        when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "LIBRARIAN"})
    void createBook_WithAdminRole_ReturnsCreated() throws Exception {
        BookCreateDTO createDTO = BookCreateDTO.builder()
                .title("New Book")
                .isbn("978-0000000001")
                .totalCopies(3)
                .publicationYear(2024)
                .build();

        BookDTO savedBook = BookDTO.builder()
                .id(2L)
                .title("New Book")
                .isbn("978-0000000001")
                .build();

        when(bookService.createBook(any(BookCreateDTO.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createBook_WithUserRole_ReturnsForbidden() throws Exception {
        BookCreateDTO createDTO = BookCreateDTO.builder()
                .title("New Book")
                .isbn("978-0000000001")
                .totalCopies(3)
                .publicationYear(2024)
                .build();

        mockMvc.perform(post("/api/books")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isForbidden());
    }
}
