package com.service.desk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.desk.dto.TicketRequestDTO;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketControllerTest {
    private final String path = RequestMappingConstants.TICKET_END_POINT;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void testCriarTicketEnd() throws Exception {
        var dto = new TicketRequestDTO();
        dto.setTitulo("Teste integração");
        dto.setDescricao("Descrição teste integração");
        dto.setCategoria("Categoria Teste");

        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @Sql(scripts = "classpath:script_bd/script_ticket.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testAtualizarTicket() throws Exception {
        var dto = new TicketRequestDTO();
        dto.setTitulo("Teste integração");
        dto.setDescricao("Descrição teste integração");
        dto.setCategoria("Categoria Teste");

        mockMvc.perform(put(path + "/{id}" , 20000L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(23)
    void testCListarTickets() throws Exception {
        mockMvc.perform(get(path)).andExpect(status().isOk());
    }

    @SneakyThrows
    private String convertToJson(final Object object) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(object);
    }
}
