package work.rabbi.afis.services;

import com.digitalpersona.uareu.UareUException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.rabbi.afis.dtos.FingerprintFeatureSetDto;
import work.rabbi.afis.dtos.Member;
import work.rabbi.afis.entity.FingerprintTemplate;
import work.rabbi.afis.repositories.FingerprintMetaRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FingerprintIdentificationService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FingerprintMetaRepository fingerprintMetaRepository;

    public boolean identifyFingerprints(Member member) {
        logger.info("Starting fingerprint identification system! ID: {}", member.memberId);
        try {
            // todo: get fingerprint data from main database
            List<FingerprintTemplate> templates = fingerprintMetaRepository.findMemberFingerprints(member.getMemberId(), 1);
//            Optional<FingerprintTemplate> templates = fingerprintMetaRepository.findById(2021923L);
//            List<FingerprintTemplate> templates = fingerprintMetaRepository.findByMemberIdAndStatus(member.getMemberId(), 1);
//            List<FingerprintTemplate> templates = fingerprintMetaRepository.findByMemberId(member.getMemberId());

            Optional<FingerprintTemplate> temp = fingerprintMetaRepository.findById(2021923L);

            logger.info("Total members: " + temp.get().getMemberId());

            // todo: get fingerprint templates from branch from local database

            // todo: match fingerprint with all templates
//            Fmd inputFmd = FingerprintService.generateFmdFromBytes(fingerBmp);

            // todo: get all fingerprints from
            logger.info("Processed successfully!");
        }
//        catch (InterruptedException e) {
//            logger.error("Error while executing sample job", e);
//        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
