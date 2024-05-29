package work.rabbi.afis.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Member {
    public Long branchId;
    public Long memberId;
    public Boolean isRecapture;
    public UUID requestId;

}
