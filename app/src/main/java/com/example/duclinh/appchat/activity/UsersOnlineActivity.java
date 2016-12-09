package com.example.duclinh.appchat.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.adapter.AdapterUsersOnline;
import com.example.duclinh.appchat.data.Account;
import com.example.duclinh.appchat.data.FormMessage;
import com.example.duclinh.appchat.designnotifi.receivermessage.CenterManager;
import com.example.duclinh.appchat.fragment.ChangePasswordFragment;
import com.example.duclinh.appchat.fragment.ScreenChatFragment;
import com.example.duclinh.appchat.fragment.SignupFragment;
import com.example.duclinh.appchat.orther.DividerItemDecoration;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.emitter.Emitter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersOnlineActivity extends AppCompatActivity {
    private CenterManager centerManagerMessage;
    private Toolbar toolbar;
    private RecyclerView listUsers;
    private AdapterUsersOnline adapterUsersOnline;
    private ArrayList<Account> listAccount;
    private SwipeRefreshLayout swipeRefresh;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CircleImageView avatar;
    private TextView nickname;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_online);
        controlView();
        handleLogic();
        controlEvent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyApplication.socket.disconnect();
        finish();
    }

    private void handleLogic() {
        // init center notifi message
        centerManagerMessage = new CenterManager();
        listAccount = new ArrayList<Account>();

        setSupportActionBar(toolbar);
        adapterUsersOnline = new AdapterUsersOnline(listAccount);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listUsers.setLayoutManager(layoutManager);
        listUsers.setItemAnimator(new DefaultItemAnimator());
        listUsers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        listUsers.setAdapter(adapterUsersOnline);
        //  adapterUsersOnline.notifyDataSetChanged();
        Log.d("code", 1 + "");
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        Picasso.with(this).load(MyApplication.HOST + getIntent().getStringExtra("avatar")).into(avatar);
        nickname.setText(getIntent().getStringExtra("nickname"));
        username.setText(getIntent().getStringExtra("username"));


        MyApplication.socket.off("resultListUser");
        MyApplication.socket.on("resultListUser", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                try {
                    Log.d("code", 2 + "");
                    JSONArray listUsersOnline = object.getJSONArray("listUser");
                    for (int i = 0; i < listUsersOnline.length(); i++) {
                        object = listUsersOnline.getJSONObject(i);
                        Account account = new Account(object.getString("username"), object.getString("nickname"), object.getString("avatar"), object.getString("status"));
                        listAccount.add(account);
                        centerManagerMessage.addClient(account);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
               /* UsersOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UsersOnlineActivity.this, "2", Toast.LENGTH_SHORT).show();
                        adapterUsersOnline.notifyDataSetChanged();
                    }
                });*/


            }
        });
        MyApplication.socket.emit("listUser");
        MyApplication.socket.off("addUser");
        MyApplication.socket.on("addUser", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                JSONObject object = (JSONObject) args[0];
                try {
                    Account account = new Account(object.getString("username"), object.getString("nickname"), object.getString("avatar"), object.getString("status"));
                    listAccount.add(account);
                    centerManagerMessage.addClient(account);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                UsersOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UsersOnlineActivity.this, args[0].toString(), Toast.LENGTH_SHORT).show();
                        adapterUsersOnline.notifyDataSetChanged();
                    }
                });

            }
        });

        MyApplication.socket.off("removeUser");
        MyApplication.socket.on("removeUser", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                String username = null;
                try {
                    username = object.getString("username");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int i;
                int length = listAccount.size();
                for (i = 0; i < length; i++) {
                    if (listAccount.get(i).getUsername().equals(username)) {
                        break;
                    }
                }
                centerManagerMessage.removeClient(listAccount.get(i));
                listAccount.remove(i);
                UsersOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterUsersOnline.notifyDataSetChanged();
                    }
                });
            }
        });

        MyApplication.socket.off("receiveMessage");
        MyApplication.socket.on("receiveMessage", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                UsersOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject object = (JSONObject) args[0];
                        String username = null;
                        String message = null;
                        try {
                            username = object.getString("username");
                            message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        centerManagerMessage.notifiAllClient(username, message, 2);
                        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(UsersOnlineActivity.this)
                                .setSmallIcon(R.drawable.message)
                                .setContentTitle(username)
                                .setContentText(message);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(1, builder.build());
                    }
                });
            }
        });

        MyApplication.socket.off("resultSendMessage");
        MyApplication.socket.on("resultSendMessage", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                UsersOnlineActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject object = (JSONObject) args[0];
                        String username = null;
                        String message = null;
                        try {
                            username = object.getString("username");
                            message = object.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        centerManagerMessage.notifiAllClient(username, message, 1);
                    }
                });
            }
        });

        MyApplication.socket.off("connect");
        MyApplication.socket.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // re login when disconnect
            }
        });

    }

    private void controlEvent() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                swipeRefresh.setRefreshing(false);
                Toast.makeText(UsersOnlineActivity.this, "Updated !", Toast.LENGTH_SHORT).show();
            }
        });
        listUsers.addOnItemTouchListener(new RecyclerTouchListener(this, listUsers, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(UsersOnlineActivity.this, position + "", Toast.LENGTH_SHORT).show();
                String account = listAccount.get(position).getNickname();
                String username = listAccount.get(position).getUsername();
                FragmentManager fragmentManager = getSupportFragmentManager();
                ScreenChatFragment screenChatFragment = ScreenChatFragment.newInstance(account, username, centerManagerMessage, listAccount.get(position).getList());
                screenChatFragment.show(fragmentManager, account);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(UsersOnlineActivity.this, item.getItemId() + "", Toast.LENGTH_SHORT).show();
                item.setChecked(false);
                switch (item.getItemId()) {
                    case R.id.drawer_home:
                        break;
                    case R.id.drawer_feedback:
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        ChangePasswordFragment fragment = ChangePasswordFragment.newInstance("Change password");
                        fragment.show(fragmentManager, "fragment");
                        break;
                    case R.id.drawer_logout:
                        MyApplication.socket.disconnect();
                        Intent intent = new Intent(UsersOnlineActivity.this, MainActivity.class);
                        UsersOnlineActivity.this.startActivity(intent);
                        UsersOnlineActivity.this.finish();
                        break;
                    case R.id.drawer_exit:
                        System.exit(1);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    private void controlView() {

        listUsers = (RecyclerView) findViewById(R.id.activity_users_online_listusers);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.activity_users_online_swiperefresh);
        toolbar = (Toolbar) findViewById(R.id.activity_users_online_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_users_online_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.activity_users_online_navigation);
        avatar = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.header_avatar);
        nickname = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_nickname);
        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_username);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private UsersOnlineActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final UsersOnlineActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
