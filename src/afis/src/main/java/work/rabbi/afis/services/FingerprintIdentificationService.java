package work.rabbi.afis.services;

import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.UareUException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.rabbi.afis.dtos.FingerprintFeatureSetDto;
import work.rabbi.afis.dtos.Member;
import work.rabbi.afis.repositories.main.MainFingerprintRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
public class FingerprintIdentificationService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MainFingerprintRepository fingerprintMetaRepository;

    public boolean identifyFingerprints(Member member) {
        logger.info("Starting fingerprint identification system! ID: {}", member.memberId);
        try {
            // get fingerprint data from main database
            List<Object[]> results = fingerprintMetaRepository.findMemberFingerprintsAndFeatureSets(member.getMemberId(), 1);
            List<FingerprintFeatureSetDto> featureSets = results
                    .stream().map(this::mapToFingerprintFeatureSetDto)
                    .toList();
            logger.info("Total members: " + featureSets.size());

            // todo: get fingerprint templates from branch from local database
            List<byte[]> templates = new ArrayList<>();

            // todo: match fingerprint with all templates
//            Fmd inputFmd = FingerprintService.generateFmdFromBytes(fingerBmp);
            // Define the number of parallel threads
            int parallelism = 4;
            ForkJoinPool customThreadPool = new ForkJoinPool(parallelism);

            try {
                customThreadPool.submit(() -> {
                    List<CompletableFuture<Void>> futures = featureSets.stream()
                            .map(item -> CompletableFuture.runAsync(() -> processFingerprints(item.getFeatureSet(), templates), customThreadPool))
                            .toList();

                    CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
                    allOf.join();
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error when identify fingerprints for member: " + member.memberId);
            } finally {
                customThreadPool.shutdown();
            }

            // todo: get all fingerprints from
            logger.info("Processed successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private boolean processFingerprints(byte[] inputFmdData, List<byte[]> fmdList) {
        try {
            return FingerprintService.identifyFmdBytes(inputFmdData, fmdList);
        } catch (UareUException e) {
            logger.error("Error when processing fingerprint identification!", e);
        }
        return false;
    }

    private FingerprintFeatureSetDto mapToFingerprintFeatureSetDto(Object[] result) {
        return new FingerprintFeatureSetDto(
                ((Number) result[0]).longValue(),
                ((Number) result[1]).intValue(),
                (byte[]) result[2]
        );
    }
}
