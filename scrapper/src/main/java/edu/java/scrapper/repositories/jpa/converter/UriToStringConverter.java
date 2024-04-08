package edu.java.scrapper.repositories.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.URI;
import org.springframework.util.StringUtils;

@Converter(autoApply = true)
public class UriToStringConverter implements AttributeConverter<URI, String> {
    @Override
    public String convertToDatabaseColumn(URI entityValue) {
        return (entityValue == null) ? null : entityValue.toString();
    }

    @Override
    public URI convertToEntityAttribute(String databaseValue) {
        return (StringUtils.hasLength(databaseValue) ? URI.create(databaseValue) : null);
    }
}
