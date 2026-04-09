package cy.jdkdigital.utilitarian.logger;

import cy.jdkdigital.utilitarian.Config;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegexMessageFilter extends AbstractFilter
{
    @Override
    public Result filter(LogEvent event) {
        var message = event.getMessage();
        if (message.getFormat() != null && !Config.LOG_SUPPRESSOR_MESSAGES_REGEX_STRINGS.get().isEmpty()) {
            for (String s : Config.LOG_SUPPRESSOR_MESSAGES_REGEX_STRINGS.get()) {
                Pattern p = getPattern(s);
                if (p != null && p.matcher(message.getFormattedMessage()).matches()) {
                    return Result.DENY;
                }
            }
        }
        return Result.NEUTRAL;
    }

    static Map<String, Pattern> patterns = new HashMap<>();
    private static Pattern getPattern(String s) {
        if (!patterns.containsKey(s)) {
            patterns.put(s, Pattern.compile(s));
        }
        return patterns.getOrDefault(s, null);
    }
}
