package plus.jdk.smart.ioc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecallResult {

    /**
     * Order receipt number。
     */
    private String receipt;
}
