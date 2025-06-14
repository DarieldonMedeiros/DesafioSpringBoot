package com.zipdin.avaliacao.services;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String>{

    private static final Logger logger = LoggerFactory.getLogger(StringListConverter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute){
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        
        try{
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.error("Erro ao converter lista de strings para JSON", e);
            throw new IllegalArgumentException("Erro ao converter lista de strings para JSON", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData){
        if (dbData == null || dbData.isEmpty()) {
            return Collections.emptyList();
        }
        
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            logger.error("Erro ao converter JSON para lista", e);
            throw new IllegalArgumentException("Erro ao converter JSON para lista", e);
        }
    }
    
}
