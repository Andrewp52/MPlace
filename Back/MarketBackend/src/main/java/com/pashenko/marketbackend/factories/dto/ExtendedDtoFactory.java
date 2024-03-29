package com.pashenko.marketbackend.factories.dto;

public interface ExtendedDtoFactory <E, D> extends DtoFactory<E, D>{
    D getExtendedDto(E entity);
}
