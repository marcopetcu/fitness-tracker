package com.example.fitness_tracker.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Verifies security public access rules: 
// 1) /login is publicly accessible (200 + login view) 
// 2) /css/style.css static asset served without authentication 
// 3) /journal (protected) redirects unauthenticated users to /login (3xx -> /login) 
// Ensures SecurityConfig permits public resources and enforces auth on protected endpoints.

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityPublicEndpointsTest {

    @Autowired MockMvc mockMvc;
    private static final Logger log = LoggerFactory.getLogger(SecurityPublicEndpointsTest.class);

    @Test
    @DisplayName("/login is publicly accessible")
    void loginPageAccessible() throws Exception {
    log.info("Testing public accessibility of /login");
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"));
    log.info("/login accessible OK");
    }

    @Test
    @DisplayName("/css/style.css served without auth")
    void cssServed() throws Exception {
    log.info("Requesting static resource /css/style.css");
        var res = mockMvc.perform(get("/css/style.css"))
            .andExpect(status().isOk())
            .andReturn();
        String contentType = res.getResponse().getContentType();
        assertThat(contentType).isNotNull();
        assertThat(contentType).startsWith("text/css");
        assertThat(res.getResponse().getContentAsString()).contains("body");
    log.info("/css/style.css served with contentType={} length={}", contentType, res.getResponse().getContentLength());
    }

    @Test
    @DisplayName("/journal redirects to /login when unauthenticated")
    void journalRedirectsWhenUnauthenticated() throws Exception {
    log.info("Verifying /journal redirects when not authenticated");
        mockMvc.perform(get("/journal"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/login"));
    log.info("/journal redirect to login confirmed");
    }
}
