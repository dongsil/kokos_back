package com.base.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class UserDTO {

    private final long id;
    private final String email;
    @NonNull
    private final String name;

}
