package work.rabbi.afis.services;

import com.digitalpersona.uareu.Fmd;
import com.digitalpersona.uareu.UareUException;
import org.jobrunr.jobs.annotations.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BackgroundJobService {

    public static final long EXECUTION_TIME = 5000L;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicInteger count = new AtomicInteger();

    @Job(name = "The sample job with variable %0", retries = 2)
    public void executeSampleJob(String id, byte[] fingerBmp) {

        logger.info("The sample job has begun. The variable you passed is {}", id);
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

    public int getNumberOfInvocations() {
        return count.get();
    }
}
