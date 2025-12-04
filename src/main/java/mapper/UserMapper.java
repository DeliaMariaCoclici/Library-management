package mapper;

import model.User;
import model.builder.UserBuilder;
import view.builder.UserDTOBuilder;
import view.model.UserDTO;

public class UserMapper {

    public static UserDTO convertUserToDTO(User user) {
        return new UserDTOBuilder()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .build();
    }

    public static User convertDTOToUser(UserDTO userDTO) {
        UserBuilder builder = new UserBuilder()
                .setUsername(userDTO.getUsername());

        if (userDTO.getId() != null) {
            builder.setId(userDTO.getId());
        }
        return builder.build();
    }
}
