package com.service.desk.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.service.desk.dto.TicketRequestDTO;
import com.service.desk.enumerator.MensagemEnum;
import com.service.desk.service.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(RequestMappingConstants.TICKET_END_POINT)
@Tag(name = "Ticket", description = "End points de Ticket")
public class TicketController extends ControllerServiceDesk{

	@Autowired
    private TicketService ticketService;

	@Operation(summary = "Retorna a lista de tickets")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk listarTickets() {
		return new ResponseServiceDesk(ticketService.listarTickets());
    }

	@Operation(summary = "Salvar novo ticket")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseServiceDesk criarTicket(@Valid @RequestBody TicketRequestDTO dto) {
        ticketService.salvarTicket(dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }

	@Operation(summary = "Atualiza dados do ticket")
	@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseServiceDesk atualizarTicket(@PathVariable Long id , @Valid @RequestBody TicketRequestDTO dto) {
    	ticketService.atualizarTicket(id,dto);
        return new ResponseServiceDesk(responseSucesso(MensagemEnum.MSGS001));
    }
}
