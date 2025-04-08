package org.borispopic.paymenttransfersystem.enums.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.borispopic.paymenttransfersystem.enums.EntryType;

@Converter(autoApply = true)
public class EntryTypeConverter implements AttributeConverter<EntryType, String> {
    @Override
    public String convertToDatabaseColumn(EntryType attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public EntryType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : EntryType.formValue(dbData);
    }
}
