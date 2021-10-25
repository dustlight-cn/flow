package plus.flow.zeebe.services.adapters;

/**
 * 适配器上下文
 */
public interface AdapterContext {

    String getClientId();

    String getOwner();

}
