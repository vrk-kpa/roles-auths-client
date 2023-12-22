package fi.vm.kapa.rova.client.webapi;

public interface WebApiJwtClientFactory {

    HpaWebApiJwtClient hpaWebApiJwtClient(String delegateId);

    YpaWebApiJwtClient ypaWebApiJwtClient(String delegateId);

}
