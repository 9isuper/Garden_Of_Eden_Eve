package com.isuper.eden.eve.boot.autoconfigure.condition;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isuper.eden.eve.common.constant.SystemConstant;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.List;
import java.util.Map;


@Order(Ordered.HIGHEST_PRECEDENCE + 44)
class OnProfilesActiveCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(ConditionalOnProfilesActive.class.getName());
        if (MapUtils.isEmpty(annotationAttributes)) {
            annotationAttributes = Maps.newHashMap();
        }
        //当前生效的环境
        List<String> profiles = Lists.newArrayList(context.getEnvironment().getActiveProfiles());
        // 获取参数
        String[] profilesActives = (String[]) annotationAttributes.get("profilesActives");
        Object property = annotationAttributes.get("property");
        boolean enabled = (boolean) annotationAttributes.get("enabled");

        boolean isMatched = false;
        //先看配置文件
        if (property != null && StringUtils.isNotBlank(String.valueOf(property))) {
            //获取配置文件中的配置值。多个则逗号分隔
            String value = context.getEnvironment().getProperty(property.toString());
            if (StringUtils.isNotBlank(value)) {
                List<String> values = Splitter.on(SystemConstant.SEPARATOR_COMMA).splitToList(value);
                isMatched = profiles.stream().filter(values::contains).count() > 0;
            }
        } else if (profilesActives != null && profilesActives.length > 0) {
            isMatched = Lists.newArrayList(profilesActives).stream().filter(profiles::contains).count() > 0;
        } else {
            isMatched = true;
        }
        return new ConditionOutcome(enabled ? isMatched : !isMatched, "ok");
    }

}
