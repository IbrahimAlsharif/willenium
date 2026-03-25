/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configs.testRail;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HP
 */
public class TestRailManager {

    APIClient client;
    public static int
            PASSED = 1,
            FAILED = 5,
            Blocked = 2,
            UNTESTED = 3,
            RETEST = 4;


    public TestRailManager() {
        String base_url="https://wewilltech.testrail.io/";
        String api_url="index.php?/api/v2/";
        String userName="******";// add your username here
        String password="******";// add your password here
        client = new APIClient(base_url,api_url, userName, password);
    }

    public void getResults() throws IOException, APIException {
        JSONObject c = (JSONObject) client.sendGet("get_case/2372");
    }

    public void setResult(String testRunId, String testCaseID, int status, String shotPTH) throws IOException,APIException {
            Map data = new HashMap<>();
            data.put("status_id", status);
            data.put("comment", shotPTH);
            data.put("attachment", (shotPTH));
            JSONObject r = (JSONObject) client.sendPost("add_result_for_case/" + testRunId + "/" + testCaseID + "", data);
            // Add attachment in case of fail
            if (status == TestRailManager.FAILED) {
                String result_id = r.get("id").toString();
                client.sendPost("add_attachment_to_result/" + result_id, shotPTH);
            }
    }


    public String createTestRun(String projectName, int projectId) throws APIException, IOException {
        Map data = new HashMap<>();
        data.put("project_id", projectId);
        data.put("name", projectName + new Date().toString());
        data.put("include_all", true);
        JSONObject response = (JSONObject) client.sendPost("add_run/"+projectId, data);
        return response.get("id").toString();
    }
}


