package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.userinfoservice.model.Contact;
import com.yandex.market.userinfoservice.model.SocialNetwork;
import org.apache.commons.lang3.ObjectUtils;
import org.openapitools.api.model.ContactDto;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {
    public Contact map(ContactDto contactDto) {

        Contact contact = Contact.builder()
                .value(contactDto.getValue())
                .build();

        if (ObjectUtils.isNotEmpty(contactDto.getType())){
            contact.setType(SocialNetwork.valueOf(contact.getType().name()));
        }

        return contact;
    }

    public ContactDto mapToDto(Contact contact) {
        ContactDto contactDto = new ContactDto();
        contactDto.setType(ContactDto.TypeEnum.valueOf(contact.getType().name()));
        contactDto.setValue(contact.getValue());
        return contactDto;
    }
}