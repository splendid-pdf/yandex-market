package com.yandex.market.userinfoservice.specification;

import com.yandex.market.userinfoservice.model.Sex;
import com.yandex.market.userinfoservice.model.User;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.api.model.UserFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

import static java.util.Optional.empty;

public class UserSpecification {



    public static Specification<User> filter(UserFilter userFilter) {
        return (root, query, criteriaBuilder) -> {
            Optional<Predicate> predicate = empty();
            if(!StringUtils.isBlank(userFilter.getFirstName())) {
                predicate = Optional.of(criteriaBuilder.equal(root.get("firstName"), userFilter.getFirstName()));
            }
            if(!StringUtils.isBlank(userFilter.getLastName())) {
                predicate = predicate
                        .map(value -> criteriaBuilder.and(criteriaBuilder.equal(root.get("lastName"), userFilter.getLastName()), value))
                        .or(() -> Optional.of(criteriaBuilder.equal(root.get("lastName"), userFilter.getLastName())));
            }
            if(!StringUtils.isBlank(userFilter.getSex())) {
                predicate = predicate
                        .map(value -> criteriaBuilder.and(criteriaBuilder.equal(root.get("sex"), Sex.valueOf(userFilter.getSex())), value))
                        .or(() -> Optional.of(criteriaBuilder.equal(root.get("sex"), Sex.valueOf(userFilter.getSex()))));
            }
            if(userFilter.getNotificationSettings() != null) {
//               predicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("notificationSetting"), userFilter.getNotificationSettings()),predicate);
            }
            if(userFilter.getLocation() != null) {
               // predicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("location"), userFilter.getLocation()), predicate);
            }
            return predicate.get();
        };
    }
//        return (root, query, criteriaBuilder) -> criteriaBuilder
//                                                    .and(criteriaBuilder.equal(root.get("firstName"), userFilter.getFirstName())
//                                                            ,criteriaBuilder.equal(root.get("lastName"), userFilter.getLastName())
//                                                            ,criteriaBuilder.equal(root.get("sex"), userFilter.getSex()));


}
