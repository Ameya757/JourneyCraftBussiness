package gmail.bssushant2003.JourneyCraft.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class OtpService {

    private final ConcurrentHashMap<String, String> otpStore = new ConcurrentHashMap<>();

    @Autowired
    private EmailService emailService;

    public void generateAndSendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);

        String subject = "Thank You For Registering On JourneyCraft💕";
        String body = "Your OTP for registration is: " + otp;

        emailService.sendSimpleMail(email, subject, body);
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(email);
            return true;
        }
        return false;
    }
}

