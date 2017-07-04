package testVoice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class VoiceCodeTest {

	public static void main(String[] args) throws Exception {
		String urlPath = "http://voicode.baiwutong.com:8888/VoiceClient/voiceCode.do";
		
		String accountSid = "tpyzq1";
		String passwd = "tpy125";
		String operaType = "voiceCode";
		String destNumber = "18617128423";
		String voiceCode = "152932";
		String fetchDate = DateUtil.getDateStr(new Date());
		
		String hashPasswd = MD5Util.getLowCaseMD5(passwd);
		String signature = MD5Util.getLowCaseMD5(accountSid + hashPasswd + fetchDate);
		String serviceCode = "sw001";
		String callNumber = "01059780054";
		
		String param = "accountSid=" + accountSid +
				        "&signature=" + signature +
				        "&operaType=" + operaType +
				        "&destNumber=" + destNumber + 
				        "&voiceCode=" + voiceCode +
				        "&fetchDate=" + fetchDate +
				        "&serviceCode=" + serviceCode +
				        "&callNumber=" + callNumber;
		
		URL url = new URL(urlPath);
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		huc.setRequestMethod("POST");
		huc.setDoOutput(true);

		DataOutputStream out = new DataOutputStream(huc.getOutputStream());
		out.write(param.getBytes("UTF-8"));
		out.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
		String line,ret = "";
		while ((line = br.readLine()) != null) {
			System.out.println(line);
			ret += line;
		}
		huc.disconnect();
		br.close();

		/*JSONObject token_json = JSONObject.fromString(ret);*/
		System.out.println("json reuturn:" + ret);
	}
}
