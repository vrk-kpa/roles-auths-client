package fi.vm.kapa.rova.client.webapi;

public interface WebApiClientFactory {

    HpaWebApiClient hpaWebApiClient(String delegateId);

    YpaWebApiClient ypaWebApiClient(String delegateId);

}
