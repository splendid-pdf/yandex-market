package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.userinfoservice.model.Contact;
import com.yandex.market.userinfoservice.model.SocialNetwork;
import org.openapitools.api.model.ContactDto;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper implements Mapper <ContactDto, Contact> {
    @Override
    public Contact map(ContactDto contactDto) {
        return Contact.builder()
                .value(contactDto.getValue())
                .type(SocialNetwork.valueOf(contactDto.getType().name()))
                .build();
    }

    @Override
    public ContactDto mapToDto(Contact contact) {
        ContactDto contactDto = new ContactDto();
        contactDto.setType(ContactDto.TypeEnum.valueOf(contact.getType().name()));
        contactDto.setValue(contact.getValue());
        return contactDto;
    }
}
