package work.rabbi.afis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "P_MemberFingerTemplate")
@Getter
@Setter
public class FingerprintTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "P_MemberId")
    private Long memberId;

    @Column(name = "Template")
    private byte[] template;

    @Column(name = "Finger")
    private Integer finger;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "FeatureSet")
    private byte[] featureSet;

//    @Column(name = "IsVerified")
//    private Boolean isVerified;

    @Column(name = "VerificationStatus")
    private Integer verificationStatus;

//    @Column(name = "AuthorizeStatus")
//    private String authorizeStatus;
}
