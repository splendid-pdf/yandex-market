package com.marketplace.sellerinfoservice;

import com.marketplace.sellerinfoservice.dto.SellerRequestDto;
import com.marketplace.sellerinfoservice.dto.SellerResponseDto;
import com.marketplace.sellerinfoservice.mapper.SellerMapper;
import com.marketplace.sellerinfoservice.mapper.SellerMapperImpl;
import com.marketplace.sellerinfoservice.model.BusinessModel;
import com.marketplace.sellerinfoservice.model.Seller;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SellerMapperImpl.class)
@ContextConfiguration(classes = SellerMapperImpl.class)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
class SellerMapperTest {

    private final SellerMapper mapper;

    private final Seller SELLER = Seller.builder()
            .id(100L)
            .externalId(UUID.randomUUID())
            .firstName("Александр")
            .lastName("Бородач")
            .email("alexsandrooo@example.com")
            .legalAddress("г. Саратов, ул. Московская, д123, к13")
            .companyName("Alex Corp")
            .imageUrl("https://example.com/image.jpg")
            .businessModel(BusinessModel.OOO)
            .ITN("123456789012")
            .PSRN("123456789012345")
            .BIC("123456789")
            .paymentAccount("12345678901234567890")
            .corporateAccount("12345678901234567890")
            .build();

    private final SellerRequestDto SELLER_REQUEST_DTO = new SellerRequestDto(
            "Александр",
            "Бородач",
            "alexsandrooo@example.com",
            "г. Саратов, ул. Московская, д123, к13",
            "Alex Corp",
            "https://example.com/image.jpg",
            BusinessModel.OOO,
            "123456789012",
            "123456789012345",
            "123456789",
            "12345678901234567890",
            "12345678901234567890");

    @Test
    void toSellerResponseDto() {
        SellerResponseDto sellerResponseDto = mapper.toSellerResponseDto(SELLER);

        Assertions.assertNotNull(sellerResponseDto);
        assertThat(sellerResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(SELLER);
    }

    @Test
    void toSellerModelFromRequestDto() {
        Seller seller = mapper.toSellerModel(SELLER_REQUEST_DTO);

        Assertions.assertNotNull(seller);
        assertThat(SELLER_REQUEST_DTO)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(UUID.class)
                .isEqualTo(seller);
    }

    @Test
    void updateSellerModel() {
        SellerRequestDto sellerRequestDto = new SellerRequestDto(
                "Игорь",
                null,
                "alexsandrooo@gmail.com",
                "г. Саратов, ул. Московская, д123, к14",
                null,
                null,
                null,
                "123456789999",
                null,
                null,
                "00000078901234567890",
                null);

        Seller sellerAfterUpdate = mapper.updateSellerModel(sellerRequestDto, SELLER);

        Assertions.assertAll(
                () -> Assertions.assertEquals(sellerRequestDto.firstName(), sellerAfterUpdate.getFirstName()),
                () -> Assertions.assertEquals(SELLER.getLastName(), sellerAfterUpdate.getLastName()),
                () -> Assertions.assertEquals(sellerRequestDto.email(), sellerAfterUpdate.getEmail()),
                () -> Assertions.assertEquals(sellerRequestDto.legalAddress(), sellerAfterUpdate.getLegalAddress()),
                () -> Assertions.assertEquals(SELLER.getCompanyName(), sellerAfterUpdate.getCompanyName()),
                () -> Assertions.assertEquals(SELLER.getBusinessModel(), sellerAfterUpdate.getBusinessModel()),
                () -> Assertions.assertEquals(sellerRequestDto.ITN(), sellerAfterUpdate.getITN()),
                () -> Assertions.assertEquals(SELLER.getPSRN(), sellerAfterUpdate.getPSRN()),
                () -> Assertions.assertEquals(SELLER.getBIC(), sellerAfterUpdate.getBIC()),
                () -> Assertions.assertEquals(sellerRequestDto.paymentAccount(), sellerAfterUpdate.getPaymentAccount()),
                () -> Assertions.assertEquals(SELLER.getCorporateAccount(), sellerAfterUpdate.getCorporateAccount())
        );
    }

}
