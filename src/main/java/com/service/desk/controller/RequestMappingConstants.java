package com.service.desk.controller;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestMappingConstants {
    public static final String VERSAO = "/api";
    public static final String TICKET_END_POINT = VERSAO + "/tickets";
    public static final String HEALTH_ENO_POINT = VERSAO + "/health";
    public static final String AUTH = VERSAO + "/auth";
    public static final String FORNECEDOR_END_POINT = VERSAO + "/fornecedores";
    public static final String TELEFONE_END_POINT = VERSAO + "/telefones";
    public static final String TELEFONE_TIPOS_END_POINT = VERSAO + "/telefone-tipos";
    public static final String FORMA_PAGAMENTO_END_POINT = VERSAO + "/forma-pagamento";
    public static final String TIPO_PAGAMENTO_END_POINT = VERSAO + "/tipo-pagamento";
    public static final String TIPO_NOTA_END_POINT = VERSAO + "/tipo-nota";
}
