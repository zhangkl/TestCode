package testJson;

import net.sf.json.JSONObject;

/**
 * Created by zhang on 2016/10/20.
 */
public class JsonToBean {

    public static void main(String[] args) {
        String jsonStr = "{\"name\":\"zhang\",\"age\":123,\"sex\":\"男\"}";

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);

        System.out.println(jsonObject.toString());

        PersonBean personBean = (PersonBean) JSONObject.toBean(jsonObject, PersonBean.class);

        System.out.println(personBean.toString());

    }
}
