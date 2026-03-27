package com.dennis.ecommerce.customerservice.mapper;

import com.dennis.ecommerce.customerservice.domain.entity.CustomerPreferences;
import com.dennis.ecommerce.customerservice.dto.request.CustomerPreferencesRequest;
import com.dennis.ecommerce.customerservice.dto.response.CustomerPreferencesResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerPreferencesMapper {

    CustomerPreferencesResponse toResponse(CustomerPreferences preferences);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CustomerPreferencesRequest request, @MappingTarget CustomerPreferences preferences);
}
