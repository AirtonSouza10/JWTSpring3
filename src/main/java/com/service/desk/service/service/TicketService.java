package com.service.desk.service.service;

import com.service.desk.dto.TicketRequestDTO;
import com.service.desk.dto.TicketResponseDTO;

import java.util.List;

public interface TicketService {
    List<TicketResponseDTO> listarTickets();

    Long salvarTicket(TicketRequestDTO dto);

    void atualizarTicket(Long id,TicketRequestDTO dto);
}
