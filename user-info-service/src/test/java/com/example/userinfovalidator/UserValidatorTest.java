package com.example.userinfovalidator;

import com.yandex.market.userinfoservice.UserInfoServiceApplication;
import com.yandex.market.userinfoservice.validator.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.api.model.ContactDto;
import org.openapitools.api.model.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = UserInfoServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yaml")
@ActiveProfiles("test")
class UserValidatorTest {

    @Autowired
    private UserValidator userValidator;

//    @Test
//    void when_SexIsNull_then_OK(){
//        UserRequestDto userRequestDto = new UserRequestDto();
//        userRequestDto.setSex(null);
//        ArrayList<String> errorMessages = new ArrayList<>();
//
//        userValidator.validateSex(userRequestDto, errorMessages);
//
//    }


    @Test
    void when_SocialNetworkValueAndTypeIsNull_then_OK() {
        UserRequestDto userRequestDto = new UserRequestDto();
        ContactDto contactDto = new ContactDto();
        ArrayList<String> errorMessages = new ArrayList<>();
        userRequestDto.setContacts(List.of(contactDto));

        userValidator.validateContact(userRequestDto, errorMessages);

        Assertions.assertTrue(errorMessages.isEmpty());
    }

    @Test
    void when_SocialNetworkValueAndTypeIsValid_then_OK() {
        UserRequestDto userRequestDto = new UserRequestDto();
        ContactDto contactDto = new ContactDto();
        contactDto.setValue("a");
        contactDto.setType("VK");
        ArrayList<String> errorMessages = new ArrayList<>();
        userRequestDto.setContacts(List.of(contactDto));

        userValidator.validateContact(userRequestDto, errorMessages);

        Assertions.assertTrue(errorMessages.isEmpty());
    }

    @Test
    void when_SocialNetworkValueIsNullAndTypeIsValid_then_NotOK() {
        UserRequestDto userRequestDto = new UserRequestDto();
        ContactDto contactDto = new ContactDto();
        contactDto.setValue(null);
        contactDto.setType("VK");
        ArrayList<String> errorMessages = new ArrayList<>();
        userRequestDto.setContacts(List.of(contactDto));

        userValidator.validateContact(userRequestDto, errorMessages);

        Assertions.assertFalse(errorMessages.isEmpty());
    }

    @Test
    void when_SocialNetworkValueIsNullAndTypeisNotValid_then_NotOK() {
        UserRequestDto userRequestDto = new UserRequestDto();
        ContactDto contactDto = new ContactDto();
        contactDto.setValue(null);
        contactDto.setType("K");
        ArrayList<String> errorMessages = new ArrayList<>();
        userRequestDto.setContacts(List.of(contactDto));

        userValidator.validateContact(userRequestDto, errorMessages);

        Assertions.assertFalse(errorMessages.isEmpty());
    }

    @Test
    void when_SocialNetworkValueIsEmptyAndTypeisValid_then_NotOK() {
        UserRequestDto userRequestDto = new UserRequestDto();
        ContactDto contactDto = new ContactDto();
        contactDto.setValue("");
        contactDto.setType("VK");
        ArrayList<String> errorMessages = new ArrayList<>();
        userRequestDto.setContacts(List.of(contactDto));

        userValidator.validateContact(userRequestDto, errorMessages);

        Assertions.assertFalse(errorMessages.isEmpty());
    }

    @Test
    void when_SocialNetworkValueIsValidAndTypeisNull_then_NotOK() {
        UserRequestDto userRequestDto = new UserRequestDto();
        ContactDto contactDto = new ContactDto();
        contactDto.setValue("abc");
        contactDto.setType(null);
        ArrayList<String> errorMessages = new ArrayList<>();
        userRequestDto.setContacts(List.of(contactDto));

        userValidator.validateContact(userRequestDto, errorMessages);

        Assertions.assertFalse(errorMessages.isEmpty());
    }

    @Test
    void when_SocialNetworkValueIsValidAndTypeisNotValid_then_NotOK() {
        UserRequestDto userRequestDto = new UserRequestDto();
        ContactDto contactDto = new ContactDto();
        contactDto.setValue("abc");
        contactDto.setType("K");
        ArrayList<String> errorMessages = new ArrayList<>();
        userRequestDto.setContacts(List.of(contactDto));

        userValidator.validateContact(userRequestDto, errorMessages);

        Assertions.assertFalse(errorMessages.isEmpty());
    }

    @Test
    void when_SocialNetworkValueIsValidAndTypeisEmpty_then_NotOK() {
        UserRequestDto userRequestDto = new UserRequestDto();
        ContactDto contactDto = new ContactDto();
        contactDto.setValue("abc");
        contactDto.setType("");
        ArrayList<String> errorMessages = new ArrayList<>();
        userRequestDto.setContacts(List.of(contactDto));

        userValidator.validateContact(userRequestDto, errorMessages);

        Assertions.assertFalse(errorMessages.isEmpty());
    }

}
