package ua.epam.spring.hometask.context;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

import ua.epam.spring.hometask.util.StringToLongSetConverter;

@Configuration
@Import(value = {AuditoriumContext.class, DaoContext.class, 
        DiscountContext.class, ServicesContext.class,
        AspectsContext.class})
@EnableAspectJAutoProxy
public class AppContext {
    
    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(getCustomConverters());
        return conversionServiceFactoryBean;
    }
    
    private Set<Converter<?, ?>> getCustomConverters() {
        StringToLongSetConverter stringToLongSetConverter = new StringToLongSetConverter();
        Set<Converter<?, ?>> converters = new HashSet<Converter<?,?>>();
        converters.add(stringToLongSetConverter);
        return converters;
    }
    
}
