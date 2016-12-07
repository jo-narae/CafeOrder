package com.narae.cafeorder.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.narae.cafeorder.R;
import com.narae.cafeorder.activity.OrderActivity;
import com.narae.cafeorder.database.DBManager;
import com.narae.cafeorder.menu.MenuListViewAdapter;
import com.narae.cafeorder.menu.MenuListViewItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FrappuccinoFragment extends Fragment{

    MenuListViewAdapter adapter;
    ListView listview ;

    private View seletedItem;

    String engName;
    String korName;
    String priceStr;
    String keyName;

    int tallPrice;

    DBManager manager;

    public FrappuccinoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        String url = getString(R.string.server_url) + "/coffeeinfo";

        // Adapter 생성
        adapter = new MenuListViewAdapter() ;
        listview = (ListView) view.findViewById(R.id.menulistview);

        sendRequest(url);
        settingItemClick();

        manager = new DBManager(getContext());

        return view;
    }

    private void sendRequest(String url) {
        JsonArrayRequest socRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject coffee = response.getJSONObject(i);
                        String eng_name = coffee.getString("engName");
                        String kor_name = coffee.getString("korName");
                        String coffee_type = coffee.getString("coffeeType");
                        String price = coffee.getString("price") + "원";
                        String img_name = coffee.getString("keyName");

                        String uri = "@drawable/" + img_name;
                        String packName = getContext().getPackageName(); // 패키지명
                        int imageResource = getResources().getIdentifier(uri, null, packName);

                        if(coffee_type.equals("FRAPPUCCINO")) {
                            adapter.addItem(ContextCompat.getDrawable(getContext(), imageResource), img_name, eng_name, kor_name, price);
                        }

                        // 리스트뷰 참조 및 Adapter달기
                        listview.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ERROR" + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(socRequest);

    }

    /**
     * 아이템 선택
     */
    private void settingItemClick() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                MenuListViewItem item = (MenuListViewItem) parent.getItemAtPosition(position) ;
                engName = item.getEngName() ;
                korName = item.getKorName() ;
                priceStr = item.getPrice();
                priceStr = priceStr.substring(0, priceStr.length()-1);
                keyName = item.getKeyName() ;
                //Drawable iconDrawable = item.getIcon();

                if(seletedItem!=null) {
                    nonSelectItemInit(seletedItem);
                }

                if(seletedItem!=v) {
                    seletedItem = v;
                    selectItemInit(v);
                } else {
                    seletedItem = null;
                }
                // TODO : use item data.
            }
        });
    }

    /**
     * 아이템 초기화(미선택영역)
     */
    private void nonSelectItemInit(View v) {
        v.findViewById(R.id.hiddenCount).setVisibility(View.GONE);
        v.findViewById(R.id.hiddenMenuLayout).setVisibility(View.GONE);
        //해당 아이템 초기화
        TextView countText = (TextView)v.findViewById(R.id.countText);
        countText.setText("1");
        TextView priceText = (TextView) v.findViewById(R.id.priceText);
        priceText.setText(String.valueOf(tallPrice) + "원");
    }

    /**
     * 아이템 초기화(선택영역)
     */
    private void selectItemInit(View v) {
        Button redButton = (Button) seletedItem.findViewById(R.id.coffeeHOT);
        Button blueButton = (Button) seletedItem.findViewById(R.id.coffeeICED);
        redButton.setVisibility(View.GONE); //프라푸치노는 기본적으로 아이스이기 때문에 버튼을 비활성화 한다
        blueButton.setVisibility(View.GONE); //프라푸치노는 기본적으로 아이스이기 때문에 버튼을 비활성화 한다
        v.findViewById(R.id.hiddenCount).setVisibility(View.VISIBLE);
        v.findViewById(R.id.hiddenMenuLayout).setVisibility(View.VISIBLE);
        v.findViewById(R.id.countAdd).setOnClickListener(myListener);
        v.findViewById(R.id.countDelete).setOnClickListener(myListener);
        v.findViewById(R.id.cartAdd).setOnClickListener(myListener);
        v.findViewById(R.id.btnOrder).setOnClickListener(myListener);

        String[] sizeType = {"TALL", "GRANDE", "VENTI"};
        tallPrice = Integer.parseInt(priceStr);

        Spinner s = (Spinner) v.findViewById(R.id.spinnerSize);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getContext(),              // 액티비티 클래스 내에 어댑터를 정의할 경우 this는 액티비티 자신을 의미합니다.
                R.layout.spinner_item,     // 현재 선택된 항목을 보여주는 레이아웃의 ID
                sizeType                   // 위에 정의한 문자열의 배열 객체를 대입합니다.
        );

        adapter.setDropDownViewResource(R.layout.dropdown_item);

        final TextView priceText = (TextView) v.findViewById(R.id.priceText);

        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(parent.getItemAtPosition(position).equals("TALL")) {
                    priceText.setText(String.valueOf(tallPrice * Integer.parseInt(((TextView) seletedItem.findViewById(R.id.countText)).getText().toString())) + "원");
                    priceStr = String.valueOf(tallPrice);
                } else if(parent.getItemAtPosition(position).equals("GRANDE")) {
                    priceText.setText(String.valueOf((tallPrice + 500) * Integer.parseInt(((TextView) seletedItem.findViewById(R.id.countText)).getText().toString())) + "원");
                    priceStr = String.valueOf(tallPrice + 500);
                } else if(parent.getItemAtPosition(position).equals("VENTI")) {
                    priceText.setText(String.valueOf((tallPrice + 1000) * Integer.parseInt(((TextView) seletedItem.findViewById(R.id.countText)).getText().toString())) + "원");
                    priceStr = String.valueOf(tallPrice + 1000);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * 선택된 아이템에 해당하는 버튼 로직
     */
    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView countText = (TextView)seletedItem.findViewById(R.id.countText);
            TextView priceText = (TextView)seletedItem.findViewById(R.id.priceText);
            int count = Integer.parseInt(countText.getText().toString());
            int totalPrice = 0;

            switch (view.getId()) {
                case R.id.countAdd:
                    count++; // 개수 증가
                    countText.setText(String.valueOf(count));
                    priceText.setText(String.valueOf(Integer.parseInt(priceStr)*count)+"원");
                    break;
                case R.id.countDelete:
                    if(count>1) { //최소 1 이하로는 떨어지지 않도록 한다
                        count--; // 개수 감소
                    }
                    countText.setText(String.valueOf(count));
                    priceText.setText(String.valueOf(Integer.parseInt(priceStr)*count)+"원");
                    break;
                case R.id.cartAdd :
                    totalPrice = Integer.parseInt(priceStr) * Integer.parseInt(((TextView) seletedItem.findViewById(R.id.countText)).getText().toString());
                    //담기 sqlite insert문 실행
                    if(manager.insertCartList(keyName, engName, korName, ((TextView) seletedItem.findViewById(R.id.countText)).getText().toString(), String.valueOf(totalPrice),
                            ((Spinner) seletedItem.findViewById(R.id.spinnerSize)).getSelectedItem().toString(), "ICED")) {
                        //sql 쿼리 실행 후 안내
                        Toast toast = Toast.makeText(getContext(),
                                "선택한 상품을 장바구니에 담았습니다.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 200);
                        toast.show();

                        //장바구니 메뉴 개수 표시 시작
                        ((OrderActivity)getActivity()).mCartCount = ((OrderActivity)getActivity()).mCartCount + 1; //장바구니 개수 동기화
                        onClickIncrementCartCount(((OrderActivity)getActivity()).mCartCount);
                        //장바구니 메뉴 개수 표시 끝
                    }
                    break;
                case R.id.btnOrder :
                    totalPrice = Integer.parseInt(priceStr) * Integer.parseInt(((TextView) seletedItem.findViewById(R.id.countText)).getText().toString());

                    String JSONString = "{ \"userId\" : \"" + manager.selectUserId() + "\", \"coffees\" : [ ";
                    JSONString += "{\"keyName\" : \"" + keyName + "\", \"count\" : " + ((TextView) seletedItem.findViewById(R.id.countText)).getText();
                    JSONString += ", \"size\" : \"" + ((Spinner) seletedItem.findViewById(R.id.spinnerSize)).getSelectedItem().toString();
                    JSONString += "\", \"temperature\" : \"ICED\", \"totalPrice\" : " + totalPrice + "}";
                    JSONString += " ] }";
                    orderRequest(JSONString);
                    break;
            }
        }
    };

    /**
     * 장바구니 메뉴 개수 표시
     * @param count
     */
    public void onClickIncrementCartCount(int count) {
        ((OrderActivity)getActivity()).setBadgeCount(getContext(), ((OrderActivity)getActivity()).mCartMenuIcon, String.valueOf(count));
    }

    /**
     * 서버 통신 후 주문
     */
    private void orderRequest(String JSONString) {
        String url = getString(R.string.server_url) + "/orders";
        JSONObject json = null;
        try {
            json = new JSONObject(JSONString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest socRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                alert_confirm.setMessage("주문이 신청되었습니다.").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish(); //이전 화면으로 넘기기
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error!!", error);
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                alert_confirm.setMessage("주문이 실패했습니다. 다시 주문해주세요.").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return; // 확인 후 제자리에 멈추기
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(socRequest);
    }

}
