package in.bluebytes.superbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Integer userId;

    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    private String salt;

}
