package com.dennis.ecommerce.customerservice.mapper;

import com.dennis.ecommerce.customerservice.domain.entity.Customer;
import com.dennis.ecommerce.customerservice.dto.request.CreateCustomerRequest;
import com.dennis.ecommerce.customerservice.dto.request.UpdateCustomerRequest;
import com.dennis.ecommerce.customerservice.dto.response.CustomerResponse;
import org.mapstruct.*;

///  @Mapper(componentModel = "spring"):
///  MapStruct lee esta interfaz en compilación y genera automáticamente
///  la clase CustomerMapperImpl con todo el código de mapeo entre
///  entidades y DTOs. componentModel = "spring" la registra como
///  Bean para poder inyectarla con @Autowired en los servicios.
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    /// el Mapper funciona asi: toma lo que viene en el parametro y conviertelo en el tipo del metodo
    /// los nombres de abajo "toResponse" y toEntity" son una convencion para que el codigo sea legible y predecible

    // aqui convierto la entidad Customer en el dto de la carpeta "response" "CustomerResponse" por eso se pone "toResponse"
    // to pondria mejor "toResponseDto"
    CustomerResponse toResponse(Customer customer);//  "toma un Customer y conviértelo en un CustomerResponse"

    // se usa en los POST(crear entidad a partir del dto)
    // en este caso "CreateCustomerRequest" es un dto de la carpeta request que se convierte en la entidad "Customer" por eso el nombre "toEntity"
    Customer toEntity(CreateCustomerRequest request);// "toma un CreateCustomerRequest y conviértelo en un Customer"

    // se usa para los PUT/PATCH(actuializar entidad)
    // Solo actualiza campos que no son null
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)// para updates parciales. Si el cliente solo manda el teléfono, MapStruct no sobreescribe el nombre con null.
    void updateEntityFromRequest(UpdateCustomerRequest request, @MappingTarget Customer customer);
}
