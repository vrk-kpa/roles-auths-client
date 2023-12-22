package fi.vm.kapa.rova.client.model;

/**
 * Reason for disallowing operation.
 */
public class DecisionReason {

    private final String reasonRule;
    private final String reasonValue;
    private final String valueType;

    @SuppressWarnings("unused")
    private DecisionReason() {
        this(null, null, null);
    }

    public DecisionReason(String reasonRule, String reasonValue, String valueType) {
        this.reasonRule = reasonRule;
        this.reasonValue = reasonValue;
        this.valueType = valueType;
    }

    /**
     * @return id of denying rule.
     */
    public String getReasonRule() {
        return reasonRule;
    }

    /**
     * @return description of reason.
     */
    public String getReasonValue() {
        return reasonValue;
    }

    public String getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return "DecisionReason [reasonRule=" + reasonRule + ", reasonValue=" + reasonValue + ", valueType=" + valueType
                + "]";
    }

}
