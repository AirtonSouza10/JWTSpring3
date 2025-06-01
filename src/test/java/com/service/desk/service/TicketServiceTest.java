package com.service.desk.service;

import com.service.desk.dto.TicketRequestDTO;
import com.service.desk.exceptions.NegocioException;
import com.service.desk.repository.TicketRepository;
import com.service.desk.service.service.TicketService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TicketServiceTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @Test
    @Order(1)
    void testSalvarTicket() {
        var dto = new TicketRequestDTO();
        dto.setTitulo("Novo ticket");
        dto.setDescricao("Descrição do ticket");
        dto.setCategoria("Categoria A");
        var ticketSalvo = ticketService.salvarTicket(dto);

        var ticket = ticketRepository.findById(ticketSalvo);
        assertThat(ticket).isPresent();
        assertThat(ticket.get().getTitulo()).isEqualTo("Novo ticket");
        assertThat(ticket.get().getDescricao()).isEqualTo("Descrição do ticket");
        assertThat(ticket.get().getCategoria()).isEqualTo("Categoria A");
    }

    @Test
    @Order(2)
    @Sql(scripts = "classpath:script_bd/script_ticket.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testAtualizarTicket() {
        TicketRequestDTO dto = new TicketRequestDTO();
        dto.setTitulo("Ticket atualizado");
        dto.setDescricao("Descrição atualizada");
        dto.setCategoria("Categoria B");
        ticketService.atualizarTicket(20000l,dto);
        var ticket = ticketRepository.findById(20000l).get();
        assertThat(ticket.getTitulo()).isEqualTo("Ticket atualizado");
        assertThat(ticket.getDescricao()).isEqualTo("Descrição atualizada");
        assertThat(ticket.getCategoria()).isEqualTo("Categoria B");
    }

    @Test
    @Order(3)
    void testListaTicket() {
        var listaTickets = ticketService.listarTickets();
        assertThat(listaTickets).isNotEmpty();
    }

    @Test
    @Order(4)
    void testSalvarTicketException() {
        var dto = new TicketRequestDTO();
        dto.setTitulo("");

        NegocioException exception = assertThrows(NegocioException.class, () -> {
            ticketService.salvarTicket(dto);
        });

        assertThat(exception.getMessage()).isEqualTo("MSGE002");
    }
}
