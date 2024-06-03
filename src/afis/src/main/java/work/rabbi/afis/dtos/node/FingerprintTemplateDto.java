package work.rabbi.afis.dtos.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FingerprintTemplateDto {
    private Long branchId;
    private Long memberId;
    private Integer fingerIndex;
    private byte[] template;
}
