package work.rabbi.afis.services;

import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.UareUException;
import org.jobrunr.jobs.annotations.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import work.rabbi.afis.dtos.Member;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BackgroundJobService {

    public static final long EXECUTION_TIME = 5000L;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicInteger count = new AtomicInteger();

    @Autowired
    private FingerprintIdentificationService fingerprintIdentificationService;

    @Job(name = "The sample job with variable %0", retries = 3)
    public void executeSampleJob(String id, byte[] fingerBmp) {

        logger.info("Starting fingerprint identification system! ID: {}", id);
        try {
//            Thread.sleep(EXECUTION_TIME);
            Fmd inputFmd = FingerprintService.generateFmdFromBytes(fingerBmp);

            // todo: get all fingerprints from
            logger.info("Processed successfully!");
        }
//        catch (InterruptedException e) {
//            logger.error("Error while executing sample job", e);
//        }
        catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UareUException e) {
            logger.error("Error while processing fingerprint", e);
        } finally {
            count.incrementAndGet();
            logger.info("Sample job has finished...");
        }
    }


    @Job(name = "Fingerprint Identification Job with %0", retries = 3)
    public void executeFpIdentificationJob(Member member) {
        logger.info("Starting fingerprint identification task...");
        boolean status = fingerprintIdentificationService.identifyFingerprints(member);
        logger.info("Identification status: " + status);
    }

    public int getNumberOfInvocations() {
        return count.get();
    }
}
