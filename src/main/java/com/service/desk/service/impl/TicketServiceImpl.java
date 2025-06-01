package com.service.desk.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.exceptions.NegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.desk.dto.TicketRequestDTO;
import com.service.desk.dto.TicketResponseDTO;
import com.service.desk.entidade.Ticket;
import com.service.desk.repository.TicketRepository;
import com.service.desk.service.service.TicketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    @Autowired
    private final TicketRepository ticketRepository;

    public List<TicketResponseDTO> listarTickets() {
        log.info("Listando todos os tickets.");
        var tickets = ticketRepository.findAll();
        var dtos = tickets.stream()
        	    .map(ticket -> new TicketResponseDTO(
        	        ticket.getId(),
        	        ticket.getTitulo(),
        	        ticket.getDescricao(),
        	        ticket.getCategoria(),
        	        ticket.getSentimento()
        	    ))
        	    .collect(Collectors.toList());

        	return dtos;
    }

    public Long salvarTicket(TicketRequestDTO dto) {
        log.info("Criando ticket: {}", dto);
        validarCamposObrigatoriosTicket(dto);

        var ticket = new Ticket();
        ticket.setTitulo(dto.getTitulo());
        ticket.setDescricao(dto.getDescricao());
        ticket.setCategoria(dto.getCategoria());
        ticket.setSentimento(dto.getSentimento());

        var ticketSalvo = ticketRepository.save(ticket);

        log.info("Ticket salvo com sucesso: id={}, titulo={}", ticket.getId(), ticket.getTitulo());
        return ticketSalvo.getId();
    }

    public void atualizarTicket(Long id,TicketRequestDTO dto) {
        log.info("Atualizando ticket ID {}: {}", id, dto);
        var ticket = ticketRepository.findById(id).orElseThrow();

        validarCamposObrigatoriosTicket(dto);
        ticket.setTitulo(dto.getTitulo());
        ticket.setDescricao(dto.getDescricao());
        ticket.setCategoria(dto.getCategoria());
        ticket.setSentimento(dto.getSentimento());

        ticketRepository.save(ticket);

        log.info("Ticket atualizado com sucesso: id={}, titulo={}", id, ticket.getTitulo());

    }

    private static void validarCamposObrigatoriosTicket(TicketRequestDTO dto) {
        if (Objects.isNull(dto.getTitulo()) || dto.getTitulo().isBlank()) {
            throw new NegocioException(MensagemEnum.MSGE002.getKey(),"Título");
        }
        if (Objects.isNull(dto.getDescricao()) || dto.getDescricao().isBlank()) {
            throw new NegocioException(MensagemEnum.MSGE002.getKey(),"Descrição");
        }
    }
}
