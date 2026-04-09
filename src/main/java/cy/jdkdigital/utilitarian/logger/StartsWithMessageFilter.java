package cy.jdkdigital.utilitarian.logger;

import cy.jdkdigital.utilitarian.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class StartsWithMessageFilter extends AbstractFilter
{
    @Override
    public Result filter(LogEvent event) {
        var message = event.getMessage();
        if (message.getFormat() != null && !Config.LOG_SUPPRESSOR_MESSAGES_STARTS_WITH_STRINGS.get().isEmpty()) {
            for (String s : Config.LOG_SUPPRESSOR_MESSAGES_STARTS_WITH_STRINGS.get()) {
                if (StringUtils.startsWith(message.getFormattedMessage(), s)) {
                    return Result.DENY;
                }
            }
        }
        return Result.NEUTRAL;
    }
}
