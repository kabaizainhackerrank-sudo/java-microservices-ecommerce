package com.dennis.ecommerce.customerservice.mapper;

import com.dennis.ecommerce.customerservice.domain.entity.CustomerStatusHistory;
import com.dennis.ecommerce.customerservice.dto.response.CustomerStatusHistoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerStatusHistoryMapper {

    CustomerStatusHistoryResponse toResponse(CustomerStatusHistory history);
}
