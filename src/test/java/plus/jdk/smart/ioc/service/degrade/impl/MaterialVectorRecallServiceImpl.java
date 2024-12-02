package plus.jdk.smart.ioc.service.degrade.impl;

import plus.jdk.smart.ioc.annotations.ConditionOnRule;
import plus.jdk.smart.ioc.annotations.SmartService;
import plus.jdk.smart.ioc.model.RecallContext;
import plus.jdk.smart.ioc.model.RecallResult;
import plus.jdk.smart.ioc.service.degrade.MaterialRecallService;

@ConditionOnRule("global.qps <= 1000")
@SmartService(group = MaterialRecallService.class)
public class MaterialVectorRecallServiceImpl implements MaterialRecallService {

    @Override
    public RecallResult recall(RecallContext context) {
        return RecallResult.builder().receipt("normal, qps <= 1000").build();
    }
}
