package work.rabbi.afis.repositories.node;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import work.rabbi.afis.entity.node.FingerprintCache;

import java.util.List;

public interface NodeFingerprintRepository extends JpaRepository<FingerprintCache, Long> {
    @Query(value = "SELECT P_MemberId, FingerIndex, Template " +
            "FROM FingerprintCache " +
            "WHERE [BranchId] = :branchId",
            nativeQuery = true, name = "FingerprintTemplateMapping")
    List<Object[]> findFingerprintsByBranch(@Param("branchId") Long branchId);
}
