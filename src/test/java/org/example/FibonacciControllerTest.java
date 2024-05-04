package org.example;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FibonacciControllerTest extends PostgresTestContainerInitializer {
    @Autowired
    private FibonacciRepository repository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void fillingDatabase() {
        repository.save(new FibonacciNumber(4, 3));
    }

    @AfterEach
    public void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Get request where index > 1")
    public void givenIndexOnePlus_whenRequestToDatabase_thenGetResponseOk() throws Exception {
        mockMvc.perform(get("/org/example/{index}", 4)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.index").value(4))
                .andDo(print());
    }

    @Test
    @DisplayName("Get request where index < 1")
    public void givenIndexOneMinus_whenRequestToDatabase_thenGetResponseBad() throws Exception {
        mockMvc.perform(get("/org/example/{index}", 0))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
