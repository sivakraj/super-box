package in.bluebytes.superbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFile {

    private int fileId;

    private String fileName;

    private String contentType;

    private String fileSize;

    private int userId;

    private byte[] fileData;

}
