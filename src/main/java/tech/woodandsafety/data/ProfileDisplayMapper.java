package tech.woodandsafety.data;

import jakarta.inject.Singleton;
import tech.woodandsafety.dto.ProfileDisplayDTO;
import tech.woodandsafety.mapper.DisplayMapper;

@Singleton
public class ProfileDisplayMapper implements DisplayMapper<CustomUser, ProfileDisplayDTO> {
    @Override
    public ProfileDisplayDTO toDisplayDTO(CustomUser customUser) {
        return new ProfileDisplayDTO(customUser.getName(), customUser.getEmail(), customUser.getTelephone());
    }
}
