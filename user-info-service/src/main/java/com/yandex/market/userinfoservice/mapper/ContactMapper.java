package com.yandex.market.userinfoservice.mapper;

import com.yandex.market.userinfoservice.dto.response.ContactDto;
import com.yandex.market.userinfoservice.model.Contact;
import com.yandex.market.userinfoservice.model.SocialNetwork;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {
    public Contact map(ContactDto contactDto) {
        return Contact.builder()
                .value(contactDto.value())
                .type(SocialNetwork.valueOf(contactDto.type()))
                .build();
    }

    public ContactDto mapToDto(Contact contact) {
        return new ContactDto(
                contact.getType().name(),
                contact.getValue()
        );
    }
}