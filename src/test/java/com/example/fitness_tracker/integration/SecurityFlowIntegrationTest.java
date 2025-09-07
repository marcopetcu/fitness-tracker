package com.example.fitness_tracker.integration;

import com.example.fitness_tracker.domain.Role;
import com.example.fitness_tracker.repo.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// Creates roles, registers a normal user (auto-logged in), 
// confirms that user gets 403 on /admin/muscle-groups, 
// then logs in as seeded admin and confirms access (200, view mg/list). 
// Verifies end-to-end: registration, session auth, and role-based access control. Suggested comment:

// Integration test: register USER -> forbidden on admin -> login as ADMIN -> access allowed (mg/list)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityFlowIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired RoleRepository roleRepository;
    private static final Logger log = LoggerFactory.getLogger(SecurityFlowIntegrationTest.class);

    private void ensureRoles() {
        if (roleRepository.findByName("USER").isEmpty()) {
            Role r = new Role(); r.setName("USER"); roleRepository.save(r);
        }
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role r = new Role(); r.setName("ADMIN"); roleRepository.save(r);
        }
    }

    @Test
    @DisplayName("Registration -> user forbidden on admin page -> admin allowed")
    void registrationAndAccessControl() throws Exception {
    log.info("Ensuring roles exist");
    ensureRoles();

        // 1. Register a new normal user (role assigned by controller logic)
    log.info("Registering new user 'regular@example.com'");
        MvcResult reg = mockMvc.perform(post("/register")
                .param("username", "regular")
                .param("email", "regular@example.com")
                .param("password", "password123")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andReturn();

        MockHttpSession userSession = (MockHttpSession) reg.getRequest().getSession(false);
        assertThat(userSession).isNotNull(); // authenticated via request.login
    log.info("User session established: {}", userSession.getId());

        // 2. Attempt to access admin area -> 403
    log.info("Attempting admin access with normal user (expect 403)");
        mockMvc.perform(get("/admin/muscle-groups").session(userSession))
            .andExpect(status().isForbidden());
    log.info("Forbidden confirmed for normal user");

        // 3. Login as seeded / or existing admin
    log.info("Logging in as admin@example.com");
        MvcResult adminLogin = mockMvc.perform(post("/login")
                .param("username", "admin@example.com") // email used as username principal
                .param("password", "admin")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andReturn();

        MockHttpSession adminSession = (MockHttpSession) adminLogin.getRequest().getSession(false);
        assertThat(adminSession).isNotNull();
    log.info("Admin session established: {}", adminSession.getId());

        // 4. Access admin area -> 200 OK
    log.info("Accessing admin muscle groups with admin session (expect 200)");
        mockMvc.perform(get("/admin/muscle-groups").session(adminSession))
            .andExpect(status().isOk())
            .andExpect(view().name("mg/list"));
    log.info("Admin access successful");
    }
}
