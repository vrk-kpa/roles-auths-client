package fi.vm.kapa.rova.client;

/**
 * Interface for creating clients.
 */
public interface ClientFactory {
    HpaClient hpaClient();
    YpaClient ypaClient();
}
