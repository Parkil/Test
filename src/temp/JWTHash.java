package temp;

import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;

/*
 * jwt hash 생성 예제
 */
public class JWTHash {
	public static void main(String[] args) {
		String secureKey = "1d9b87e47123f4b4";
		
		JWTSigner signer = new JWTSigner(secureKey);
		
		Map<String, Object> claimMap = new HashMap<>();
		
		claimMap.put("version", "1.6");
		claimMap.put("media", "SMR_MEMBERS");
		claimMap.put("site", "SMRINJTBC");
		claimMap.put("requesttime", "20171019134300");
		claimMap.put("tid", "4e1409a0904d8825c1f4bc752bafbc11");
		claimMap.put("uuid", "4e1409a0904d8825c1f4bc752bafbc11");
		claimMap.put("ip", "192.168.10.1");
		claimMap.put("gender", "3");
		claimMap.put("age", "99");
		claimMap.put("platform", "PCWEB");
		claimMap.put("cpid", "C1");
		claimMap.put("channelid", "S01");
		claimMap.put("category", "01");
		claimMap.put("section", "01");
		claimMap.put("programid", "S01_V0000359936");
		claimMap.put("clipid", "S01_ClipID");
		claimMap.put("contentnumber", "16");
		claimMap.put("adtype", "PRE");
		
		System.out.println(signer.sign(claimMap));
	}
}
