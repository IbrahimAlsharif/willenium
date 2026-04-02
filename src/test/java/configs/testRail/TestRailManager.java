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

    private final APIClient client;
    private final TestRailConfig config;
    public static int
            PASSED = 1,
            FAILED = 5,
            Blocked = 2,
            UNTESTED = 3,
            RETEST = 4;


    public TestRailManager() {
        config = new TestRailConfig();
        if (!config.isConfigured()) {
            client = null;
            return;
        }
        client = new APIClient(config.getBaseUrl(), config.getApiUrl(), config.getUsername(), config.getPassword());
    }

    public APIClient getClient() {
        config.requireConfigured("TestRail API usage");
        return client;
    }

    public void getResults() throws IOException, APIException {
        JSONObject c = (JSONObject) getClient().sendGet("get_case/2372");
    }

    public void setResult(String testRunId, String testCaseID, int status, String shotPTH) throws IOException,APIException {
            config.requireConfigured("TestRail result publishing");
            Map data = new HashMap<>();
            data.put("status_id", status);
            if (shotPTH != null && !shotPTH.isBlank()) {
                data.put("comment", shotPTH);
                data.put("attachment", shotPTH);
            }
            JSONObject r = (JSONObject) getClient().sendPost("add_result_for_case/" + testRunId + "/" + testCaseID + "", data);
            // Add attachment in case of fail
            if (status == TestRailManager.FAILED && shotPTH != null && !shotPTH.isBlank()) {
                String result_id = r.get("id").toString();
                getClient().sendPost("add_attachment_to_result/" + result_id, shotPTH);
            }
    }


    public String createTestRun() throws APIException, IOException {
        config.requireConfigured("TestRail run creation");
        Map data = new HashMap<>();
        data.put("project_id", config.getProjectId());
        data.put("name", config.getRunName() + " " + new Date());
        data.put("include_all", true);
        if (config.getSuiteId() != null) {
            data.put("suite_id", config.getSuiteId());
        }
        JSONObject response = (JSONObject) getClient().sendPost("add_run/" + config.getProjectId(), data);
        return response.get("id").toString();
    }
}

