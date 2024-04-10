package com.example.final_project_client;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import com.example.final_project_client.Question;

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

    public void loginUser(String email, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "api/users/login",
                requestBody,
                listener,
                errorListener
        );

        requestQueue.add(request);
    }



    public void registerUser(String email, String password, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", email);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                BASE_URL + "api/users/register",
                requestBody,
                listener,
                errorListener
        );

        requestQueue.add(request);
    }

    public interface ResponseListener {
        void onResponse(JSONObject response);
    }

    public interface ErrorListener {
        void onError(VolleyError error);
    }

    // update user in the PUT: api/users/{id}
    public void updateUser(User user, ResponseListener listener, ErrorListener errorListener) {
        JSONObject requestBody = new JSONObject();
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("Id", user.getId());
            userJson.put("Username", user.getUsername());
            userJson.put("Password", user.getPassword());
            userJson.put("Email", user.getEmail());
            userJson.put("Role", user.getRole());
            userJson.put("Token", user.getToken());
            userJson.put("RefreshToken", user.getRefreshToken());
            userJson.put("TokenExpires", user.getTokenExpires());
            userJson.put("FullName", user.getFullName());
            userJson.put("Address", user.getAddress());
            userJson.put("EmailAddress", user.getEmail());
            userJson.put("PhoneNumber", user.getPhoneNumber());
            userJson.put("Image", user.getImage());
            userJson.put("Bio", user.getBio());

            // add userJson to requestBody
            requestBody.put("user", userJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                BASE_URL + "api/users/add",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onError(error);
                    }
                }
        );
        requestQueue.add(request);
    }

    public void getUser(int id, ResponseListener listener, ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                BASE_URL + "api/users/" + id,
                null,
                listener::onResponse,
                errorListener::onError
        );
        requestQueue.add(request);
    }

    public void getQuestions(String subject, String complexity, Response.Listener<List<Question>> listener, Response.ErrorListener errorListener) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL + "api/questions/search?subject=" + subject + "&complexity=" + complexity,
                null,
                response -> {
                    List<Question> questionList = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Question question = new Question();
                            question.setId(jsonObject.getInt("id"));
                            question.setQuestion(jsonObject.getString("questionText"));
                            question.setAnswer1(jsonObject.getString("answer1"));
                            question.setAnswer2(jsonObject.getString("answer2"));
                            question.setAnswer3(jsonObject.getString("answer3"));
                            question.setAnswer4(jsonObject.getString("answer4"));
                            question.setCorrectAnswer(jsonObject.getInt("correctAnswer"));
                            question.setSubject(jsonObject.getString("subject"));
                            question.setComplexity(String.valueOf(jsonObject.getInt("complexity")));
                            questionList.add(question);
                        }
                        listener.onResponse(questionList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorListener.onErrorResponse(null);
                    }
                },
                errorListener
        );
        requestQueue.add(request);
    }

    // get subjects /api/subjects
    public void getSubjects(Response.Listener<List<Subject>> listener, Response.ErrorListener errorListener) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL + "api/subjects",
                null,
                response -> {
                    List<Subject> subjectList = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Subject subject = new Subject();
                            subject.setId(jsonObject.getInt("id"));
                            subject.setSubjectName(jsonObject.getString("subjectName"));
                            subject.setSubjectDescription(jsonObject.getString("subjectDescription"));
                            subject.setSubjectImage(jsonObject.getString("subjectImage"));
                            subjectList.add(subject);
                        }
                        listener.onResponse(subjectList);
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

