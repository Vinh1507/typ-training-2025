package com.project.TYP.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.TYP.IntegrationTest;
import com.project.TYP.entity.User;
import com.project.TYP.repository.UserRepository;

import jakarta.transaction.Transactional;

@IntegrationTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser_shouldReturnUser_whenValid() throws Exception {
        User inputUser = new User(null, "ntg@gmail.com", "ntg29", "666666");

        String rs = mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(inputUser))
        ).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        System.out.println(rs);
        User outputUser = objectMapper.readValue(rs, User.class);

        assertEquals(inputUser.getEmail(), outputUser.getEmail());
    }

    @Test
    public void getAllUsers() throws Exception {
        User user1 = new User(null, "test1@gmail.com", null, null);
        User user2 = new User(null, "test2@gmail.com", null, null);
        User user3 = new User(null, "test3@gmail.com", null, null);
        List<User> users = List.of(user1, user2, user3);
        this.userRepository.saveAll(users);

        String rs = this.mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

        List<User> usersRs = this.objectMapper.readValue(rs, new TypeReference<List<User>>(){});

        assertEquals(3, usersRs.size());
        assertEquals(users.get(0).getEmail(), usersRs.get(0).getEmail());
        assertEquals(users.get(1).getEmail(), usersRs.get(1).getEmail());
        assertEquals(users.get(2).getEmail(), usersRs.get(2).getEmail());
    }

    @Test
    public void getUserById() throws Exception {
        User user1 = new User(null, "test1@gmail.com", null, null);
        User userInput = this.userRepository.saveAndFlush(user1);

        String rs = this.mockMvc.perform(get("/users/{id}", userInput.getId()))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

        User userOutput = this.objectMapper.readValue(rs, User.class);

        assertEquals(userInput.getId(), userOutput.getId());

    }

    // Tự làm với Delete và Update
}
