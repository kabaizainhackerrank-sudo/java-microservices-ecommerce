package com.dennis.ecommerce.customerservice.mapper;

import com.dennis.ecommerce.customerservice.domain.entity.Address;
import com.dennis.ecommerce.customerservice.dto.request.AddressRequest;
import com.dennis.ecommerce.customerservice.dto.response.AddressResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponse toResponse(Address address);

    Address toEntity(AddressRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(AddressRequest request, @MappingTarget Address address);
}
