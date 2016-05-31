package fi.vm.kapa.rova.client.model;

public class DecisionReason {
    private final String rule;
    private final String value;

    @SuppressWarnings("unused")
    private DecisionReason() {
        this.rule = null;
        this.value = null;
    }

    public DecisionReason(String rule, String value) {
        this.rule = rule;
        this.value = value;
    }

    public String getRule() {
        return rule;
    }

    public String getValue() {
        return value;
    }
}
