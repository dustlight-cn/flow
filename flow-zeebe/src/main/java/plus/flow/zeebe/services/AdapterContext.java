package plus.flow.zeebe.services;

/**
 * 适配器上下文
 */
public interface AdapterContext {

    String getClientId();

    String getOwner();

}
