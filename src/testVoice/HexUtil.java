package testVoice;



import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class HexUtil {
	static String digital = "0123456789ABCDEF"; 
	public static String byteToHex(byte[] data){ 
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (int i = 0; i < data.length; i++) {
            int    v = data[i] & 0xff;

            bos.write(digital.charAt(v >>> 4));
            bos.write(digital.charAt(v & 0xf));
        }
        return bos.toString();
	}
	
	public static byte[] hexToByte(String hex) throws UnsupportedEncodingException{
		final byte[] encodingTable =
            {
                (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5', (byte)'6', (byte)'7',
                (byte)'8', (byte)'9', (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f'
            };
        
        byte[] decodingTable = new byte[128];

     
        for (int i = 0; i < encodingTable.length; i++) {
                decodingTable[encodingTable[i]] = (byte)i;
        }
            
        decodingTable['A'] = decodingTable['a'];
        decodingTable['B'] = decodingTable['b'];
        decodingTable['C'] = decodingTable['c'];
        decodingTable['D'] = decodingTable['d'];
        decodingTable['E'] = decodingTable['e'];
        decodingTable['F'] = decodingTable['f'];
    
        int len = hex.length();
        byte[] hex_byte = hex.getBytes("UTF-8");
      
        int retLen = len / 2;
        byte[] ret = new byte[retLen];
        int j = 0;
        for(int i = 0;i < len;i += 2){
            ret[j] =  decodingTable[hex_byte[i]];
            ret[j] =  (byte) ((ret[j] << 4) | decodingTable[hex_byte[i + 1]]);
            j++;
        }
        return ret;
	}
	
	public static void main(String[] args) throws Exception{
		
	}
}
