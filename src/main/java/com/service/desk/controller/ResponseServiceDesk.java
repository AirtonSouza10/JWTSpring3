package com.service.desk.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseServiceDesk implements Serializable {

	private static final long serialVersionUID = 1L;

	private Object resposta;
	private List<String> msgSucesso = new ArrayList<>();
	private List<String> msgErro = new ArrayList<>();
	private List<String> msgAlerta = new ArrayList<>();

	public ResponseServiceDesk(Object resposta) {
		this.resposta = resposta;
	}

}
