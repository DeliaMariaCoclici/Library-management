package view.model;

import javafx.beans.property.*;

public class UserDTO {
    private StringProperty username;
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public void setUsername(String username) { usernameProperty().set(username); }
    public String getUsername() { return usernameProperty().get(); }

    public StringProperty usernameProperty() {
        if (username == null) {
            username = new SimpleStringProperty(this, "username");
        }
        return username;
    }
}