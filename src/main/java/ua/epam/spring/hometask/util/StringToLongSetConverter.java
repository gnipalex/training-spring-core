package ua.epam.spring.hometask.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

public class StringToLongSetConverter implements Converter<String, Set<Long>> {

    private static final char[] SUPPORTED_DELIMITERS = {',', ';', ' '};
    
    @Override
    public Set<Long> convert(String source) {
        String[] numbers = getSplittedNumbers(source);
        try {
            return parseNumbers(numbers);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("source string " + source + " has incorrect format", e);
        }
    }
    
    private Set<Long> parseNumbers(String[] numbers) {
        return Arrays.stream(numbers).map(Long::valueOf)
                .collect(Collectors.toSet());
    }
    
    private String[] getSplittedNumbers(String source) {
        for (char delimiter : SUPPORTED_DELIMITERS) {
            String[] splittedNumbers = source.split(String.valueOf(delimiter));
            if (splittedNumbers.length > 1) {
                return splittedNumbers;
            }
        }
        return new String[] {source};
    }

}
