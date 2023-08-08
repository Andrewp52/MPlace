package com.pashenko.marketbackend.factories.entities;

import com.pashenko.marketbackend.dto.AbstractDto;
import com.pashenko.marketbackend.entities.AbstractEntity;

public interface EntityFactory <E extends AbstractEntity, D extends AbstractDto>{
    E getEntity(D dto);
}
