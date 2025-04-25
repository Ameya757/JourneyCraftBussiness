package gmail.bssushant2003.JourneyCraft.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String email;
    private String name;
    private String phoneNo;
    private String extraInfo;

}
