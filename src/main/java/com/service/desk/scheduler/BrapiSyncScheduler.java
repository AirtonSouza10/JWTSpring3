package com.service.desk.scheduler;

import com.service.desk.service.BrapiIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BrapiSyncScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrapiSyncScheduler.class);

    private final BrapiIntegrationService brapiIntegrationService;

    @Value("${brapi.sync.tickers}")
    private String configuredTickers;

    @Value("${brapi.sync.enabled:true}")
    private boolean enabled;

    public BrapiSyncScheduler(BrapiIntegrationService brapiIntegrationService) {
        this.brapiIntegrationService = brapiIntegrationService;
    }

    // Executa diariamente às 01:00 da manhã por padrão
    @Scheduled(cron = "${brapi.sync.cron:0 0 1 * * ?}")
    public void runScheduledSync() {
        if (!enabled) {
            LOGGER.info("BRAPI sync scheduler is disabled (brapi.sync.enabled=false)");
            return;
        }
        try {
            brapiIntegrationService.sincronizarAtivos();
            LOGGER.info("BRAPI scheduled sync finished successfully");
        } catch (Exception ex) {
            LOGGER.error("Error during BRAPI scheduled sync: {}", ex.getMessage(), ex);
        }
    }
}

