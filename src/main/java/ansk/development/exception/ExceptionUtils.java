package ansk.development.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExceptionUtils {

    public static boolean isTooManyRequestException(Exception e) {
        var splitMessage = commonExceptionParser(e);
        return splitMessage.size() == 2 && splitMessage.get(0) == 429;
    }

    public static long getTimeToRetry(Exception e) {
        return commonExceptionParser(e).get(1);
    }

    private static List<Long> commonExceptionParser(Exception e) {
        return Arrays
                .stream(e
                        .getMessage()
                        .replaceAll("[^0-9]+", " ")
                        .trim()
                        .split(" "))
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
