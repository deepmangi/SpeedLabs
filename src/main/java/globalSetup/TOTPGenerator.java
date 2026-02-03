package globalSetup;

import de.taimos.totp.TOTP;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

public class TOTPGenerator {
    public static String getTOTPCode(String base32Secret) {
    	String secret = base32Secret;
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secret.toUpperCase().replace(" ", ""));
        String hexKey = Hex.encodeHexString(bytes);
       
        String totp = TOTP.getOTP(hexKey);
        return totp;
    }
}
