package com.service.desk.dto;

import com.service.desk.enumerator.UserRoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {
	private String login;
	private String senha;
	private UserRoleEnum role;
}
