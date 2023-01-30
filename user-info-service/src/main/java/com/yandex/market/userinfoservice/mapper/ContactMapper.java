package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.userinfoservice.model.Contact;
import com.yandex.market.userinfoservice.model.SocialNetwork;
import org.openapitools.api.model.ContactDto;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {
    public Contact map(ContactDto contactDto) {
        return Contact.builder()
                .value(contactDto.getValue())
                .type(SocialNetwork.valueOf(contactDto.getType()))
                .build();
    }

    public ContactDto mapToDto(Contact contact) {
        ContactDto contactDto = new ContactDto();
        contactDto.setType(contact.getType().name());
        contactDto.setValue(contact.getValue());
        return contactDto;
    }
}