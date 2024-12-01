package plus.jdk.smart.ioc.service.degrade;

import java.util.List;

/**
 * Define a standard interface for material recall services
 * The main function of this interface is to provide a standard method to access the entrance to recall relevant material information in the system.
 */
public interface MaterialRecallService {

    /**
     * List of recalled materials
     * This method is used to recall a list of identifiers for a set of materials. It does not receive any parameters, which means that the list of recalled materials is based on the logic or configuration within the service, rather than the specific conditions provided externally.
     * The recalled materials can be materials in the form of strings. ID, name or other identifier
     * @return List of material identifiers, each identifier is a string
     */
    List<String> recall(String feature);
}
