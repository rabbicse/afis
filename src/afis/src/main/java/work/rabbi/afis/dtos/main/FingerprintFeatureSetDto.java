package work.rabbi.afis.dtos.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FingerprintFeatureSetDto {
    private Long memberId;

    private Integer finger;

    private byte[] featureSet;
}
