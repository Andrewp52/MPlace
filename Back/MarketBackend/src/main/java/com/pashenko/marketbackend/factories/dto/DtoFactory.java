package com.pashenko.marketbackend.factories.dto;

public interface DtoFactory <E, D>{
    D getDto(E entity);
}
