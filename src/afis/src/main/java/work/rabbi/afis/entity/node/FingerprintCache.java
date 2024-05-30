package work.rabbi.afis.entity.node;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FingerprintCache")
@Getter
@Setter
public class FingerprintCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;
    @Column(name = "BranchId")
    private Long branchId;
    @Column(name = "P_MemberId")
    private Long memberId;
    @Column(name = "FingerIndex")
    private Integer fingerIndex;
    @Column(name = "Template")
    private byte[] template;
}
