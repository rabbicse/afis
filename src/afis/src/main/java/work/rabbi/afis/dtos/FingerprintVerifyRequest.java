package work.rabbi.afis.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FingerprintVerifyRequest {
    private Member member;
    private Integer mode;
}
