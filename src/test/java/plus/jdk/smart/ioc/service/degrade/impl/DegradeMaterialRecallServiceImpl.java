package plus.jdk.smart.ioc.service.degrade.impl;

import plus.jdk.smart.ioc.annotations.ConditionOnRule;
import plus.jdk.smart.ioc.annotations.SmartService;
import plus.jdk.smart.ioc.service.degrade.MaterialRecallService;

import java.util.Collections;
import java.util.List;

@ConditionOnRule("global.qps > 1000")
@SmartService(group = MaterialRecallService.class)
public class DegradeMaterialRecallServiceImpl implements MaterialRecallService {

    @Override
    public List<String> recall(String feature) {
        return Collections.emptyList();
    }
}
