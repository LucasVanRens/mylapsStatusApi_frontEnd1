package com.mylaps.statusAPI.controller;

import com.mylaps.statusAPI.Model.Maintenance;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApplicationController {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String systems(Model model) throws IOException, JSONException {
        final DefaultHttpClient httpclient = new DefaultHttpClient();
        final String url = "http://localhost:8080/systems";
        final HttpGet httpGet = new HttpGet(url);
        final HttpResponse response = httpclient.execute(httpGet);

        final List<Maintenance> systems = new ArrayList<>();
        Maintenance systemObject = new Maintenance();
        final JSONArray myJson = new JSONArray(EntityUtils.toString(response.getEntity()));

        for (int i = 0; i < myJson.length(); i++) {
            JSONObject jsonobject = myJson.getJSONObject(i);
            systemObject.setSystemStatus(jsonobject.getString("systemStatus"));
            systemObject.setSystemName(jsonobject.getString("systemName"));
            systemObject.setBeginDate(jsonobject.getString("beginDate"));
            systemObject.setEndDate(jsonobject.getString("endDate"));
            systems.add(systemObject);
        }
        model.addAttribute("systems", systems);

        return "index.html";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginScreen() {
        return "loginScreen";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestHeader(value = "username") final String username,
                            @RequestHeader(value = "password") final String password, final HttpServletResponse servletResponse) throws JSONException, IOException {
        final DefaultHttpClient httpclient = new DefaultHttpClient();
        final String url = "http://stag-turntable.mylaps.lan:30090/api/v1/authenticate";
        final HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("X-Auth-Username", username);
        httpPost.addHeader("X-Auth-Password", password);

        final HttpResponse response = httpclient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == 200) {
            final JSONObject myJson = new JSONObject(EntityUtils.toString(response.getEntity()));
            final String usernameJSON = myJson.getString("username");
            final String tokenJSON = myJson.getString("token");

            final Cookie cookieToken = new Cookie("access_token", URLEncoder.encode(tokenJSON, "UTF-8"));
            final Cookie cookieName = new Cookie("current_user", URLEncoder.encode(usernameJSON, "UTF-8"));
            cookieToken.setMaxAge(34560);
            cookieName.setMaxAge(34560);
            servletResponse.addCookie(cookieName);
            servletResponse.addCookie(cookieToken);
        }
        return "loginLoadScreen";
    }

}
