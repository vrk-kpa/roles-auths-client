package fi.vm.kapa.rova.client.xroad;

/**
 * Interface for creating clients.
 */
public interface XRoadClientFactory {
    HpaXRoadClient hpaClient();

    YpaXRoadClient ypaClient();
    
    YyaXRoadClient yyaClient();

    YhaXRoadClient yhaClient();
}
