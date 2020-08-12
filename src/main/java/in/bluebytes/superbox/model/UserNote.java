package in.bluebytes.superbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNote {

    private int noteId;

    private String noteTitle;

    private String noteDescription;

    private int userId;

}
