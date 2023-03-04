package com.yandex.market.userservice.mapper;

import com.yandex.market.userservice.dto.response.ContactDto;
import com.yandex.market.userservice.model.Contact;
import com.yandex.market.userservice.model.SocialNetwork;
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