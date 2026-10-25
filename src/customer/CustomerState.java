package customer;

public enum CustomerState {
    IDLE,
    AWAITING_ITEM_TASK,
    SHOPPING,
    READY_FOR_CHECKOUT,
    QUEUED_FOR_CHECKOUT,
    CHECKING_OUT,
    EXITING_STORE,
}
