package com.service.desk.enumerator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusContaEnum {

    EM_ABERTO(1L, "EM_ABERTO", "Em Aberto"),
    PAGO(2L, "PAGO", "Pago"),
    CANCELADO(3L, "CANCELADO", "Cancelado"),
    NEGOCIADO(4L, "NEGOCIADO", "Negociado"),
    PROTESTADO(5L, "PROTESTADO", "Protestado");

    private final Long id;
    private final String codigo;
    private final String descricao;
}
