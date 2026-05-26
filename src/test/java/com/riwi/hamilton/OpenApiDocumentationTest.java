package com.riwi.hamilton;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void openApiDocsShouldDocumentOnlyApiPaths() throws Exception {
        mockMvc.perform(get("/v3/api-docs/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/events/']").exists())
                .andExpect(jsonPath("$.paths['/api/venues/']").exists())
                .andExpect(jsonPath("$.paths['/admin/events']").doesNotExist())
                .andExpect(jsonPath("$.paths['/admin/venues']").doesNotExist());
    }
}
