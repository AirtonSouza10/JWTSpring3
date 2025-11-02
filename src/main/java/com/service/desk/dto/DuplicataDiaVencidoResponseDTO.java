package com.service.desk.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DuplicataDiaVencidoResponseDTO {
	private List<DuplicataDiaResponseDTO> dia;
	private List<DuplicataDiaResponseDTO> vencidos;
}
