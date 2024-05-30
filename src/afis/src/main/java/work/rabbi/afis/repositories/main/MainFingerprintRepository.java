package work.rabbi.afis.repositories.main;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import work.rabbi.afis.entity.main.FingerprintTemplate;

import java.util.List;

@Repository
public interface MainFingerprintRepository extends JpaRepository<FingerprintTemplate, Long> {
    @Query(value = "SELECT [P_MemberId], [Finger], [FeatureSet]" +
            "FROM [AMBSKE].[dbo].[P_MemberFingerTemplate] " +
            "WHERE [P_MemberId] = :memberId AND [Status] = :status",
            nativeQuery = true, name = "FingerprintTemplateMapping")
    List<Object[]> findMemberFingerprintsAndFeatureSets(@Param("memberId") Long memberId,
                                                     @Param("status") Integer status);

    @Query(value = "SELECT [P_MemberId], [Finger], [Template]" +
            "FROM [AMBSKE].[dbo].[P_MemberFingerTemplate] " +
            "WHERE [P_MemberId] = :memberId AND [Status] = :status",
            nativeQuery = true, name = "FingerprintTemplateMapping")
    List<Object[]> findMemberFingerprintsAndTemplates(@Param("memberId") Long memberId,
                                          @Param("status") Integer status);

    List<FingerprintTemplate> findByMemberId(Long memberId);
    List<FingerprintTemplate> findByMemberIdAndStatus(Long memberId, Integer status);
}
