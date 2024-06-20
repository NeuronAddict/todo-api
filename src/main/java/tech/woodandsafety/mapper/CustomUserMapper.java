package tech.woodandsafety.mapper;

import tech.woodandsafety.data.CustomUser;
import tech.woodandsafety.dto.CustomUserDTO;

public class CustomUserMapper implements Mapper<CustomUserDTO, CustomUser>{
    @Override
    public CustomUser toEntity(CustomUserDTO customUserDTO) {
        return null;
    }

    @Override
    public CustomUserDTO toDTO(CustomUser customUser) {
        return null;
    }
}
