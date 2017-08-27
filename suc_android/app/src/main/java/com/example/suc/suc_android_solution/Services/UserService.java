package com.example.suc.suc_android_solution.Services;

/**
 * Created by efridman on 27/8/17.
 */

import android.util.JsonReader;

import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Utils.Network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Servicio utilizado para obtener informacion de usuarios, como por ejemplo, colaboradores.
 */
public class UserService {

    private static final String USERS_API_BASE_URL = "/api/users";

    public List<User> getAllUsers(){
        List<User> results = new ArrayList<User>();

        URL userRequestUrl = Network.buildUrl(USERS_API_BASE_URL, new HashMap<String,Object>());

        try {
            String jsonUsersResponse = Network
                    .getResponseFromHttpUrl(userRequestUrl);

            JSONObject jsonResult = new JSONObject(jsonUsersResponse);
            JSONArray jsonUsersResult = jsonResult.getJSONArray("users");

            for(int i=0; i < jsonUsersResult.length(); i++){
                JSONObject jsonUser = jsonUsersResult.getJSONObject(i);

                User.Builder userBuilder = new User.Builder();
                userBuilder
                        .setName(jsonUser.getString("name"))
                        .setUserRole(jsonUser.getInt("role"))
                        .setDoor(jsonUser.getString("door"))
                        .setStreet(jsonUser.getString("street"))
                .setFloor(jsonUser.getString("floor"))
                .setPhone(jsonUser.getString("phone"))
                .setPass(jsonUser.getString("pass"))
                .setAlias(jsonUser.getString("alias"))
                .setBornDate(new SimpleDateFormat("yyyy-MM-dd").parse(jsonUser.getString("bornDate").split("T")[0]))
                .setDocNumber(jsonUser.opt("docNum") != null ? jsonUser.getString("docNum") : "")
                .setStreetNumber(jsonUser.opt("streetNumber").toString().matches("-?\\d+(\\.\\d+)?") ? jsonUser.getInt("streetNumber") : null)
                .setMail(jsonUser.getString("mail"))
                .setSurname(jsonUser.getString("surname"));

                results.add(userBuilder.build());

            }

            return results;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
