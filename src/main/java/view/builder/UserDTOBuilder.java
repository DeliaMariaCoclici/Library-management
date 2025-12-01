package view.builder;

import view.model.BookDTO;
import view.model.UserDTO;

public class UserDTOBuilder {
    private UserDTO userDTO;

    public UserDTOBuilder() {userDTO = new UserDTO(); }

    public UserDTOBuilder setUsername(String username){
        userDTO.setUsername(username);
        return this;
    }

    public UserDTOBuilder setId(Long id) {
        userDTO.setId(id);
        return this;
    }

    public UserDTO build() {
        return userDTO;
    }
}
