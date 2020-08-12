package in.bluebytes.superbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credential {

    private int credentialId;

    private String url;

    private String userName;

    private String password;

    private String key;

    private int userId;

    private String plainPassword;

}
