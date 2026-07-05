package com.service.desk.service;

import java.util.List;

public interface BrapiIntegrationService {
    void sincronizarAtivos();

    void sincronizarTodosOsTickers();

    List<String> descobrirTickersPorTipo(String tipo);

    List<String> descobrirTodosOsTickers();
}

