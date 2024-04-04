package com.example.final_project_client;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ApiHelper {

    private static final String BASE_URL = "https://finalprojectserver20240404121625.azurewebsites.net/";

    private final RequestQueue requestQueue;

    public ApiHelper(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getItems(Response.Listener<List<Item>> listener, Response.ErrorListener errorListener) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL + "api/items",
                null,
                response -> {
                    List<Item> itemList = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Item item = new Item();
                            item.setName(jsonObject.getString("name"));
                            item.setPrice(jsonObject.getDouble("price"));
                            itemList.add(item);
                        }
                        listener.onResponse(itemList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(null);
                    }
                },
                errorListener
        );
        requestQueue.add(request);
    }
}

