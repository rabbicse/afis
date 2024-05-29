package work.rabbi.afis.repositories;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import work.rabbi.afis.dtos.FingerprintFeatureSetDto;
import work.rabbi.afis.entity.FingerprintTemplate;

import java.util.List;

public interface FingerprintMetaRepository extends JpaRepository<FingerprintTemplate, Long> {
    @Query(value = "SELECT [Id], [P_MemberId], [Finger], [Template], [FeatureSet], [Status] " +
            "FROM [AMBSKE].[dbo].[P_MemberFingerTemplate] " +
            "WHERE [P_MemberId] = :memberId AND [Status] = :status",
            nativeQuery = true, name = "FingerprintTemplateMapping")
    List<FingerprintTemplate> findMemberFingerprints(@Param("memberId") Long memberId,
                                                          @Param("status") Integer status);

    List<FingerprintTemplate> findByMemberId(Long memberId);
    List<FingerprintTemplate> findByMemberIdAndStatus(Long memberId, Integer status);
}
