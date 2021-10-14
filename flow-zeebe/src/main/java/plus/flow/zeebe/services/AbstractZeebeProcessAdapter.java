package plus.flow.zeebe.services;

public abstract class AbstractZeebeProcessAdapter implements ZeebeProcessAdapter {

    private int order = 0;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
